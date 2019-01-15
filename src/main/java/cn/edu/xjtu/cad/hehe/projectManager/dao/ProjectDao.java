package cn.edu.xjtu.cad.hehe.projectManager.dao;

import cn.edu.xjtu.cad.hehe.projectManager.model.AppProject;
import cn.edu.xjtu.cad.hehe.projectManager.model.Column;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public interface ProjectDao {

    /**
     * 从表中查询出列的信息
     * @param tableName
     * @return
     */
    List<Column> getColumnListFromTable(@Param("tableName")String tableName)  throws DataAccessException;

    void deleteTable(String tableName);

    void addTable(@Param("tableName") String tableName,@Param("list") List<String> columnStringList,@Param("pk") String pk);

    /**
     * 向表中添加字段
     * @param tableName 表名
     * @param columnString 待添加的字段
     */
    void addColumnsToTable(@Param("tableName") String tableName,@Param("columnString") String columnString);

    /**
     * 修改表中的字段
     * @param tableName 表名
     * @param columnString 待修改的字段
     */
    void editTableColumns(@Param("tableName") String tableName,@Param("columnString") String columnString);

    Map<String,Object> getProjectByID(@Param("tableName") String tableName, @Param("id")long id);


    List<Map<String,Object>> getProjectListByUserID(@Param("tableName") String tableName,@Param("userID")long userID);

    /**
     * 删除项目
     * @param id
     * @return
     */
    long deleteProject(@Param("tableName") String tableName,@Param("id")long id);

    /**
     * 更新项目
     * @param appProject
     * @return
     */
    long updateProject(@Param("tableName") String tableName,@Param("project") AppProject appProject,@Param("list") List<String> columnNames);

    /**
     * 添加项目
     * @param appProject
     * @return
     */
    long addProject(@Param("tableName") String tableName,@Param("project")AppProject appProject,@Param("list") List<String> columnNames);

    /**
     * 验证用户权限
     * @param tableName
     * @param userID
     * @param projectID
     * @return
     */
    long auth(@Param("tableName") String tableName,@Param("userID") long userID,@Param("id") long projectID);

    List<AppProject> getProjectByKey(@Param("tableName")String tableName,@Param("list") ArrayList<Integer> integers);
}
