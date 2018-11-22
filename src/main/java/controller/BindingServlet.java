package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import model.Result;
import service.ProjectService;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.naming.Binding;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

@WebServlet(name = "binding", urlPatterns = "/api/v1/project/binding")
public class BindingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json,charset=UTF-8");
        Result result = new Result();
        int projectID = Integer.valueOf(req.getParameter("projectID"));
        String resultKeyJSONArr = req.getParameter("resultKeys");
        String toolName = req.getParameter("toolName");
        ProjectService projectService = new ProjectService(toolName);
        result =  projectService.getProjectRecordWithBinding(projectID,resultKeyJSONArr);
        resp.getWriter().println(JSON.toJSONString(result));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json,charset=UTF-8");
        Result result = new Result();
        String toolName = req.getParameter("toolName");
        int projectID = Integer.valueOf(req.getParameter("projectID"));
        int id = Integer.valueOf(req.getParameter("id"));
        String username = ((Map<String,String>) req.getSession().getAttribute("userInfo")).get("username");
        ProjectService projectService = new ProjectService(toolName);
        result = projectService.validateProjectRecord4User(projectID,id,username);
        resp.getWriter().println(JSON.toJSONString(result));

//        String oldKey = req.getParameter("oldID");
//        String newIDStr = req.getParameter("newID");
//        int newID = -1;
//        if (newIDStr != null) newID = Integer.parseInt(newIDStr);
//        String toolName = req.getParameter("toolName");
//        if (toolName == null || toolName.length() == 0) {
//            String[] strings = req.getHeader("referer").split("/");
//            toolName = strings[3];
//        }
//        resp.setCharacterEncoding("utf-8");
//        resp.setContentType("application/json,charset=UTF-8");
//        String username = ((Map<String, String>) req.getSession().getAttribute("userInfo")).get("username");
//        String resultKey = req.getParameter("resultKey");
//        ProjectService projectService = new ProjectService(toolName);
//        Result result = projectService.updateProjectRecord(oldKey, newID, resultKey, username);
//        resp.getWriter().println(JSON.toJSONString(result));
    }



}
