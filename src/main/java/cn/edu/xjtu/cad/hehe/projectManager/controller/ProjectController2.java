package cn.edu.xjtu.cad.hehe.projectManager.controller;

import cn.edu.xjtu.cad.hehe.projectManager.annotation.CurUserID;
import cn.edu.xjtu.cad.hehe.projectManager.model.Result;
import cn.edu.xjtu.cad.hehe.projectManager.service.DBService;
import cn.edu.xjtu.cad.hehe.projectManager.service.ProjectService;
import cn.edu.xjtu.cad.hehe.projectManager.util.ErrorCons;
import cn.edu.xjtu.cad.hehe.projectManager.util.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v2/project")
public class ProjectController2 {
    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectController projectController;

    @Autowired
    DBService dbService;

    @ModelAttribute("tableName")
    public String getTableName(@PathVariable(required = false) String toolName, HttpServletRequest request){
        String tableName = Lang.getTableName(toolName,request.getHeader("referer"));
        if(StringUtils.isEmpty(tableName)){

        }
        dbService.validateDB(tableName);
        return tableName;

    }
    /**
     * 根据项目ID获取项目
     * @param id
     * @return
     */
    @RequestMapping(value = "/{toolName}/{id}",method = RequestMethod.GET)
    public ModelAndView getProjectByID(@PathVariable("toolName") String toolName, @PathVariable long id){
        ModelAndView model = new ModelAndView("forward:/api/v1/project?toolName="+toolName+"&id="+id);//默认forward，可以不用写
        return model;
    }


    /**
     * 获取当前用户的所有项目
     * @param toolName
     * @return
     */
    @RequestMapping(value = "/{toolName}",method = RequestMethod.GET)
    public ModelAndView getProjectListByUser(@PathVariable("toolName")  String toolName){
        ModelAndView model = new ModelAndView("forward:/api/v1/project?toolName="+toolName);//默认forward，可以不用写
        return model;
    }

    /**
     * 根据key获取项目
     * @param tableName 表明
     * @param tProjectID 模板项目ID
     * @param resultKeys key数组
     * @return
     */
    @RequestMapping(value = "/{toolName}",method = RequestMethod.GET,params = {"resultKeys","tProjectID"})
    public Result getBindingProject(@ModelAttribute String tableName, long tProjectID, String[] resultKeys){
        if(StringUtils.isEmpty(tableName)){
            return Result.failure(ErrorCons.APPNAME_DEFICIAENCY);
        }
        if(tProjectID<1||resultKeys==null||resultKeys.length==0)
            return Result.failure(ErrorCons.PARAMS_ERROR);
        return projectService.getProjectRecordWithBinding(tableName,tProjectID,resultKeys);
    }

    /**
     * 获取Project的共享key
     * @param userID 当前用户ID
     * @param tableName 表名
     * @param projectID 项目ID
     * @param tProjectID 模板项目ID
     * @return
     */
    @RequestMapping(value = "/{toolName}/{projectID}/key",method = RequestMethod.GET,params = "tProjectID")
    public Result getProjectKey(@CurUserID long userID, @ModelAttribute String tableName, @PathVariable long projectID, long tProjectID){
        if(StringUtils.isEmpty(tableName)){
            return Result.failure(ErrorCons.APPNAME_DEFICIAENCY);
        }
        if(tProjectID<1||projectID<1){
            return Result.failure(ErrorCons.PARAMS_ERROR);
        }
        return projectService.validateProjectRecord4User(tableName,tProjectID,projectID,userID);
    }


}
