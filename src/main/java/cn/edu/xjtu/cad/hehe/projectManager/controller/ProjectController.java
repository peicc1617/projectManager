package cn.edu.xjtu.cad.hehe.projectManager.controller;

import cn.edu.xjtu.cad.hehe.projectManager.annotation.CurUserID;
import cn.edu.xjtu.cad.hehe.projectManager.annotation.ProjectObject;
import cn.edu.xjtu.cad.hehe.projectManager.annotation.TableName;
import cn.edu.xjtu.cad.hehe.projectManager.model.AppProject;
import cn.edu.xjtu.cad.hehe.projectManager.model.Result;
import cn.edu.xjtu.cad.hehe.projectManager.service.ProjectService;
import cn.edu.xjtu.cad.hehe.projectManager.util.ErrorCons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    /**
     * 根据项目ID获取项目
     * @param userID
     * @param tableName
     * @param id
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET,params = "id")
    public Result getProjectByID(@CurUserID long userID, @TableName String tableName, long id){
        if(StringUtils.isEmpty(tableName)){
            return Result.failure(ErrorCons.APPNAME_DEFICIAENCY);
        }
        if(id<1){
            return Result.failure(ErrorCons.PARAMS_ERROR);
        }
        return projectService.getAppProjectByID(userID,id,tableName);
    }

    /**
     * 获取当前用户的所有项目
     * @param userID
     * @param tableName
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public Result getProjectListByUser(@CurUserID long userID,@TableName String tableName){
        if(tableName==null)
            return Result.failure(ErrorCons.APPNAME_DEFICIAENCY);
        return projectService.getAppProjectListByUser(userID,tableName);
    }

    /**
     * 更新项目
     * @param userID
     * @param tableName
     * @param project
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.PUT)
    public Result updateProject(@CurUserID long userID,@TableName String tableName,@ProjectObject AppProject project){
        if(StringUtils.isEmpty(tableName)){
            return Result.failure(ErrorCons.APPNAME_DEFICIAENCY);
        }
        return projectService.updateAppProject(userID,project,tableName);
    }

    /**
     * 添加项目
     * @param userID
     * @param tableName
     * @param project
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.POST)
    public Result addProject(@CurUserID long userID,@TableName String tableName,@ProjectObject AppProject project){
        if(StringUtils.isEmpty(tableName)){
            return Result.failure(ErrorCons.APPNAME_DEFICIAENCY);
        }
        return projectService.addAppProject(userID,project,tableName);
    }

    /**
     * 删除项目
     * @param userID
     * @param tableName
     * @param id
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.DELETE)
    public Result deleteProject(@CurUserID long userID,@TableName String tableName,long id){
        if(StringUtils.isEmpty(tableName)){
            return Result.failure(ErrorCons.APPNAME_DEFICIAENCY);
        }
        if(id<1)
            return Result.failure(ErrorCons.PARAMS_ERROR);
        return projectService.deleteAppProject(userID,tableName,id);
    }

    @RequestMapping(value = "/binding",method = RequestMethod.GET)
    public Result getBindingProject(@TableName String tableName,long id,String[] resultKeys){
        if(StringUtils.isEmpty(tableName)){
            return Result.failure(ErrorCons.APPNAME_DEFICIAENCY);
        }
        if(id<1||resultKeys==null||resultKeys.length==0)
            return Result.failure(ErrorCons.PARAMS_ERROR);
        return projectService.getProjectRecordWithBinding(tableName,id,resultKeys);
    }

    @RequestMapping(value = "/binding",method = RequestMethod.POST)
    public Result getBindingProject(@CurUserID long userID,@TableName String tableName,long id,long projectID){
        if(StringUtils.isEmpty(tableName)){
            return Result.failure(ErrorCons.APPNAME_DEFICIAENCY);
        }
        if(id<1||projectID<1){
            return Result.failure(ErrorCons.PARAMS_ERROR);
        }

        return projectService.validateProjectRecord4User(tableName,projectID,id,userID);
    }


}
