package cn.edu.xjtu.cad.hehe.projectManager.service;

import cn.edu.xjtu.cad.hehe.projectManager.dao.ProjectDao;
import cn.edu.xjtu.cad.hehe.projectManager.model.AppProject;
import cn.edu.xjtu.cad.hehe.projectManager.model.Result;
import cn.edu.xjtu.cad.hehe.projectManager.util.ErrorCons;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    ProjectDao projectDao;

    @Autowired
    DBService dbService;

    /**
     * @param userID
     * @param id
     * @param tableName
     * @return
     */
    public Result getAppProjectByID(long userID, long id, String tableName) {
        AppProject appProject = this.getAppProjectByID(id,tableName);
        Result result = this.auth(appProject,userID,tableName);
        if(result!=null)
            return result;
        return  Result.success(appProject);
    }

    /**
     * @param userID
     * @param tableName
     * @return
     */
    public Result getAppProjectListByUser(long userID, String tableName) {
        List<AppProject> appProjectList = projectDao.getProjectListByUserID(tableName,userID).stream()
                .map(map -> this.map2Project(map,tableName))
                .collect(Collectors.toList());
        return Result.success(appProjectList);
    }

    /**
     *
     * @param userID
     * @param project
     * @param tableName
     * @return
     */
    public Result updateAppProject(long userID, AppProject project, String tableName){
        project.setUserID(userID);
        AppProject appProject = this.getAppProjectByID(project.getId(),tableName);
        Result result = this.auth(appProject,userID,tableName);

        if(result!=null)
            return result;
        projectDao.updateProject(tableName,project,getToUpdateColumnNameList(project,tableName));
        return Result.success(projectDao.getProjectByID(tableName,project.getId()));
    }



    /**
     * @param userID
     * @param project
     * @param tableName
     * @return
     */
    public Result addAppProject(long userID, AppProject project, String tableName) {
        project.setUserID(userID);
        projectDao.addProjectTest(tableName,project,getToUpdateColumnNameList(project,tableName));
        return Result.success(project);

    }

    /**
     * @param userID
     * @param tableName
     * @param id
     * @return
     */
    public Result deleteAppProject(long userID, String tableName, long id) {
        AppProject appProject = this.getAppProjectByID(id,tableName);
        Result result = this.auth(appProject,userID,tableName);
        if(result!=null)
            return result;
        projectDao.deleteProject(tableName,id);
        return Result.success("删除成功");
    }

    public Result getProjectRecordWithBinding(String tableName,long projectID, String[] resultKeys) {
        Result result = new Result();
        Map<Integer,String> id2KeyMap = new HashMap<Integer, String>();
        for (String key : resultKeys) {
            //解码
            long res = -1;
            res = Long.valueOf(new String((Base64.decodeBase64(key))));
            //验证
            int id = -1;
            if(res>=0&&projectID == (int)(res>>>32)){
                id = (int)res;
            }
            if(id<0){
                result.setError(ErrorCons.PERMISSION_ERROR);
                return result;
            }
            id2KeyMap.put(id,key);
        }
        List<AppProject> appProjectList = projectDao.getProjectByKey(tableName,new ArrayList<>(id2KeyMap.keySet()));
        if(appProjectList==null||appProjectList.size()==0){
            //无结果
            return Result.failure(ErrorCons.NORESULT_ERROR);
        }else {
            for (AppProject appProject : appProjectList) {
                appProject.setResultKey(id2KeyMap.get(appProject.getId()));
            }
            return Result.success(appProjectList);
        }
    }

    public Result validateProjectRecord4User(String tableName,long projectID, long id, long userID) {
        Result result = auth(getAppProjectByID(id,tableName),userID,tableName);
        if(result==null){
            return Result.success(encrypt(projectID,id));
        }
        return result;
    }


    private AppProject getAppProjectByID(long projectID,String tableName){
        Map<String,Object> map = projectDao.getProjectByID(tableName,projectID);
        return map2Project(map,tableName);
    }


    /**
     * 验证用户的权限
     * @param appProject
     * @param userID
     * @param tableName
     * @return
     * ErrorCons.NORESULT_ERROR 结果不存在
     * ErrorCons.PERMISSION_ERROR 用户权限错误
     */
    private Result auth(AppProject appProject,long userID,String tableName){
        if(appProject==null){
            return Result.failure(ErrorCons.NORESULT_ERROR);
        }
        if(appProject.getUserID()!=userID){
            return Result.failure(ErrorCons.PERMISSION_ERROR);
        }
        return null;
    }

    private String encrypt(long projectID,long ID){
        String data = projectID+""+ID;
        return  Base64.encodeBase64String(data.getBytes());
    }

    /**
     * map转化为AppProject
     * @param map
     * @param tableName
     * @return
     */
    private AppProject map2Project(Map<String,Object> map,String tableName){
        if(map==null||map.size()==0) return null;
        AppProject appProject = dbService.generateAppProjectBeanObject(tableName);
        BeanWrapper beanWrapper = new BeanWrapperImpl(appProject);
        beanWrapper.setPropertyValues(map);
        return appProject;
    }
    private List<String> getToUpdateColumnNameList(AppProject appProject,String tableName){
        Set<String> columnNameSet = new HashSet<>(dbService.getUpdateColumnNameList(tableName));
        List<String> toUpdateNameList  = new ArrayList<>();
        BeanMap.create(appProject).forEach((k,v)->{
            if(columnNameSet.contains(k.toString())&&v!=null){
                toUpdateNameList.add(k.toString());
            }
        });
        return toUpdateNameList;
    }
}
