package cn.edu.xjtu.cad.hehe.projectManager.resolver;

import cn.edu.xjtu.cad.hehe.projectManager.annotation.ProjectObject;
import cn.edu.xjtu.cad.hehe.projectManager.model.AppProject;
import cn.edu.xjtu.cad.hehe.projectManager.model.Column;
import cn.edu.xjtu.cad.hehe.projectManager.service.DBService;
import cn.edu.xjtu.cad.hehe.projectManager.util.Lang;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import javax.jws.WebResult;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;


@Service
public class ProjectResolver extends  ModelAttributeMethodProcessor {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    DBService dbService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ProjectObject.class);

    }
    public ProjectResolver() {
        super(false);
    }

    /**
     * Class constructor.
     *
     * @param annotationNotRequired if "true", non-simple method arguments and
     *                              return values are considered model attributes with or without a
     *                              {@code @ModelAttribute} annotation
     */
    public ProjectResolver(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }


    @Override
    protected Object createAttribute(String attributeName, MethodParameter parameter,
                                     WebDataBinderFactory binderFactory, NativeWebRequest webRequest) throws Exception {
        String tableName = (String) webRequest.getAttribute("tableName",WebRequest.SCOPE_REQUEST);
        if(tableName==null){
            return null;
        }
        Class<?> clazz = dbService.getClassFromMap(tableName);
        Constructor<?> ctor = BeanUtils.findPrimaryConstructor(clazz);
        if (ctor == null) {
            Constructor<?>[] ctors = clazz.getConstructors();
            if (ctors.length == 1) {
                ctor = ctors[0];
            }
            else {
                try {
                    ctor = clazz.getDeclaredConstructor();
                }
                catch (NoSuchMethodException ex) {
                    throw new IllegalStateException("No primary or default constructor found for " + clazz, ex);
                }
            }
        }

        Object attribute = constructAttribute(ctor, attributeName, parameter, binderFactory, webRequest);
        return attribute;
    }

    /**
     * This implementation downcasts {@link WebDataBinder} to
     * {@link ServletRequestDataBinder} before binding.
     * @see ServletRequestDataBinderFactory
     */
    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        Assert.state(servletRequest != null, "No ServletRequest");
        ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
        servletBinder.bind(servletRequest);
    }

}
