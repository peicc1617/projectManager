package cn.edu.xjtu.cad.hehe.projectManager.resolver;

import cn.edu.xjtu.cad.hehe.projectManager.annotation.CurUserID;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

@Service
public class CurUserIDResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.hasParameterAnnotation(CurUserID.class))
            return true;
        else
            return false;
    }

    @Override
    public Long resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return Long.valueOf(((Map<String,String>)nativeWebRequest.getAttribute("userInfo",RequestAttributes.SCOPE_SESSION)).get("id"));
    }
}
