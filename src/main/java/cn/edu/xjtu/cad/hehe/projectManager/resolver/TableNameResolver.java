package cn.edu.xjtu.cad.hehe.projectManager.resolver;

import cn.edu.xjtu.cad.hehe.projectManager.annotation.TableName;
import cn.edu.xjtu.cad.hehe.projectManager.service.DBService;
import cn.edu.xjtu.cad.hehe.projectManager.util.Lang;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.logging.Logger;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

@Service
public class TableNameResolver implements HandlerMethodArgumentResolver {

    @Autowired
    DBService dbService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.hasParameterAnnotation(TableName.class))
            return true;
        else
            return false;
    }

    @Override
    public String resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String toolName = nativeWebRequest.getParameter("toolName");
        if(!validateToolNameParams(toolName)) {
            toolName = getToolNameFromReferer(nativeWebRequest.getHeader("referer"));
        }
        if(toolName!=null){
            toolName = getTableName(toolName);
            dbService.onRequest(toolName);
            nativeWebRequest.setAttribute("tableName",toolName, WebRequest.SCOPE_REQUEST);
        }
        return toolName;
    }

    /**
     * 验证toolName参数
     * @param toolName
     * @return
     */
    private boolean validateToolNameParams(String toolName){
        return toolName!=null&&toolName.trim().length()!=0;
    }

    /**
     * 从Header referer参数中获取解析toolName
     * @param referer
     * @return
     */
    private String getToolNameFromReferer(String referer){
        if(referer!=null||referer.trim().length()>0){
            String[] strings = referer.split("/");
            if(strings.length>3&&strings[3]!=null&&strings[3].trim().length()>0){
                return strings[3];
            }
        }
        LOGGER.warning("无法从referer中解析toolName");
        return null;
    }

    private String getTableName(String toolName){
        return Lang.toUnderScore(toolName).concat("_project");
    }
}
