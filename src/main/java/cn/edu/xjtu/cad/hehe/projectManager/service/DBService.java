package cn.edu.xjtu.cad.hehe.projectManager.service;

import cn.edu.xjtu.cad.hehe.projectManager.dao.ProjectDao;
import cn.edu.xjtu.cad.hehe.projectManager.model.AppProject;
import cn.edu.xjtu.cad.hehe.projectManager.model.Column;
import cn.edu.xjtu.cad.hehe.projectManager.util.AppProjectBean;
import cn.edu.xjtu.cad.hehe.projectManager.util.MYSQLType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

@Service
public class DBService {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    ProjectDao projectDao;

    @Value("${my.service.db.model:ON_CHANGE}")
    private DBServiceModel dbServiceModel;

    @Value("${my.service.db.retryTime:1}")
    private int retryTime;

    private  final static String pk = "id";

    private final static String NO_UPDATE = "-NO_UPDATE-";
    private Map<String, Map<String, Column>> toolDBMap = new ConcurrentHashMap<>();
    private Map<String, Class> toolClassMap = new ConcurrentHashMap<>();

    /**
     * 采用linkedHashMap来保证字段的顺序，只是为了好看
     */
    private static Map<String, Column> objectMap = new LinkedHashMap<String, Column>() {{
        put("id", new Column("id", "int(11)", "NO", "PRI", null, "auto_increment", "当前项目ID-NO_UPDATE-"));
        put("projectName", new Column("projectName", "varchar(255)", "YES", "", null, "", "项目名"));
        put("createTime", new Column("createTime", "datetime", "YES", "", "CURRENT_TIMESTAMP", "", "项目创建时间-NO_UPDATE-"));
        put("editTime", new Column("editTime", "datetime", "YES", "", "CURRENT_TIMESTAMP", "on update CURRENT_TIMESTAMP", "项目更新时间-NO_UPDATE-"));
        put("userID", new Column("userID", "int(11)", "NO", "", null, "", "用户ID-NO_UPDATE-"));
        put("memo", new Column("memo", "varchar(255)", "YES", "", null, "", "项目备注"));
        put("appResult", new Column("appResult", "text", "YES", "", null, "", "项目报告结果"));
        put("appContent", new Column("appContent", "text", "YES", "", null, "", "项目数据"));
        put("reservation", new Column("reservation", "text", "YES", "", null, "", "预留字段"));
        put("resultKey", new Column("resultKey", "varchar(255)", "YES", "", null, "", "记录密钥，用来获取共享数据"));
    }};

    /**
     * 验证数据库的类别
     *
     * @param tableName
     */
    public void validateDB(String tableName) {
        getDBtoMap(tableName);
        //从数据库中查询出了tableName对应的表结构信息
        //如果数据库中的字段不匹配公共字段，那么修改数据库中的字段
        int i = 0;
        while (true) {
            //验证表的结构与基本结构是否匹配
            if (validateCommonColumns(tableName)) {
                //如果与基本结构匹配的话，那么跳出循环
                break;
            } else {
                i++;
                //如果不匹配的话，需要修改表的结构
                logger.debug("第" + i + "次修改表的结构");
                updateTableBasicColumns(tableName);
            }
            if (i > retryTime) {
                logger.error("更新" + tableName + "表结构失败");
                return;
            }
        }
        getClassMapFromDBMap(tableName);
    }

    private void getClassMapFromDBMap(String tableName) {
        AppProject appProject = generateAppProjectBeanObject(tableName);
        logger.info("新构建的代理对象的成员变量" + Arrays.stream(appProject.getClass().getDeclaredFields()).map(b -> b.getName()).collect(Collectors.joining(";")));
        logger.info("新构建的代理对象的成员函数" + Arrays.stream(appProject.getClass().getDeclaredMethods()).map(b -> b.getName()).collect(Collectors.joining(";")));
        toolClassMap.put(tableName, appProject.getClass());
    }


    private Class type2JavaClass(String typeString) {
        int index = typeString.indexOf('(');
        if (index == -1) index = typeString.length();
        MYSQLType type = MYSQLType.valueOf(typeString.substring(0, index).toUpperCase());
        return type.getJavaClass();
    }

    /**
     * 查询表的结构
     *
     * @param tableName
     * @return
     */
    private List<Column> getColumnListFromTable(String tableName) {
        List<Column> columnList = new ArrayList<>();
        boolean flag = true;
        for (int i = 0; i < retryTime && flag; i++) {
            try {
                columnList = projectDao.getColumnListFromTable(tableName);
                flag = true;
            } catch (DataAccessException e) {
                LOGGER.info(tableName + "不存在");
                addTable(tableName);
            }
        }
        return columnList;
    }

    /**
     * 修改表的公共结构
     *
     * @param tableName
     */
    private void updateTableBasicColumns(String tableName) {
        LOGGER.info("修改" + tableName + "的基本结构");
        //获取表的结构
        Map<String, Column> map = toolDBMap.get(tableName);
        //对比，去掉不需要修改的字段
        List<String> toAddColumns = new ArrayList<>();
        List<String> toEditColumns = new ArrayList<>();
        objectMap.forEach((k, v) -> {
            if (!map.containsKey(k)) {
                toAddColumns.add(v.getSQLString());
            } else if (!map.get(k).equals(v)) {
                toEditColumns.add(v.getSQLString());
            }
        });
        logger.info("待添加的字段为" + toAddColumns.toString());
        logger.info("待修改的字段名称" + toEditColumns.toString());
        toAddColumns.forEach(c -> projectDao.addColumnsToTable(tableName, c));
        toEditColumns.forEach(c -> projectDao.editTableColumns(tableName, c));
        //修改完成以后更新Map
        getDBtoMap(tableName);
    }

    /**
     * 新建表
     *
     * @param tableName
     */
    private void addTable(String tableName) {
        LOGGER.info("新建表" + tableName);
        projectDao.addTable(tableName, objectMap.values().stream().map(Column::toString).collect(Collectors.toList()), pk);
    }

    /**
     * 删除表
     *
     * @param tableName
     */
    private void deleteTable(String tableName) {
        LOGGER.info("删除表" + tableName);
        projectDao.deleteTable(tableName);
    }

    /**
     * 验证表的结构与默认结构是否一致
     *
     * @param tableName 当前表的名称
     */
    private boolean validateCommonColumns(String tableName) {
        Map<String, Column> map = toolDBMap.get(tableName);
        return objectMap.entrySet().stream().filter(e -> !(map.containsKey(e.getKey()) && map.get(e.getKey()).equals(e.getValue()))).count() == 0;
    }

    /**
     * 从数据库中获取当前表对应的结构Map
     *
     * @param tableName
     * @return
     */
    private void getDBtoMap(String tableName) {
        toolDBMap.put(tableName, getColumnListFromTable(tableName).stream().collect(Collectors.toMap(Column::getField, Function.identity())));
    }

    private Map<String, Column> getExtraColumnsMap(String tableName) {
        return toolDBMap.get(tableName)
                .entrySet()
                .stream()
                .filter(e -> !objectMap.containsKey(e.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    private Map<String, Class> getExtraFieldByColumnsMap(String tableName) {
        return getExtraColumnsMap(tableName).entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> type2JavaClass(e.getValue().getType())));
    }

    public AppProject generateAppProjectBeanObject(String tableName) {
        AppProjectBean bean = new AppProjectBean(getExtraFieldByColumnsMap(tableName));
        return bean.object;
    }

    public Map<String, Column> getColumnsMap(String tableName) {
        return toolDBMap.get(tableName);
    }

    public List<String> getUpdateColumnNameList(String tableName){
        return toolDBMap.get(tableName)
                .entrySet()
                .stream()
                .filter(e->e.getValue().getComment().contains(NO_UPDATE))
                .map(e->e.getKey())
                .collect(Collectors.toList());
    }

    /**
     * 每次请求触发的函数，
     * 主要是确定map中是否保存了tableName对应的表结构记录
     *
     * @param tableName
     */
    public void onRequest(String tableName) {
        switch (dbServiceModel) {
            case ON_ERROR:
                updateOnSQLError(tableName);
                return;
            case ON_CHANGE:
                updateOnRequest(tableName);
                return;
            default:
                LOGGER.warning("更新模式配置错误");
        }
    }


    /**
     * 当发生错误时才进行更新
     *
     * @param tableName
     */
    private void updateOnSQLError(String tableName) {
        if (!toolDBMap.containsKey(tableName)) {
            validateDB(tableName);
        }
    }

    /**
     * 每次请求都会进行更新
     *
     * @param tableName
     */
    private void updateOnRequest(String tableName) {
        validateDB(tableName);
    }



    public Class getClassFromMap(String tableName) {
        return toolClassMap.get(tableName);
    }
}
