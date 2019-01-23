package cn.edu.xjtu.cad.hehe.projectManager.resolver;

import cn.edu.xjtu.cad.hehe.projectManager.annotation.CurUserID;
import cn.edu.xjtu.cad.hehe.projectManager.annotation.TableName;
import cn.edu.xjtu.cad.hehe.projectManager.service.DBService;
import cn.edu.xjtu.cad.hehe.projectManager.util.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Service
public class TableNameResolver   implements HandlerMethodArgumentResolver {
    @Autowired
    DBService dbService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TableName.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String toolName = webRequest.getParameter("toolName");
        String tableName = Lang.getTableName(toolName,webRequest.getHeader("referer"));
        if(StringUtils.isEmpty(tableName)){

        }
        dbService.validateDB(tableName);
        webRequest.setAttribute("tableName",tableName, RequestAttributes.SCOPE_REQUEST);
        return tableName;
    }
}
