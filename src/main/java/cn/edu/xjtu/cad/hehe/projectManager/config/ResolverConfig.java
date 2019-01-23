package cn.edu.xjtu.cad.hehe.projectManager.config;

import cn.edu.xjtu.cad.hehe.projectManager.resolver.CurUserIDResolver;
import cn.edu.xjtu.cad.hehe.projectManager.resolver.ProjectResolver;
import cn.edu.xjtu.cad.hehe.projectManager.resolver.TableNameResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ResolverConfig implements WebMvcConfigurer {


    @Autowired
    CurUserIDResolver curUserIDResolver;


    @Autowired
    ProjectResolver projectResolver;

    @Autowired
    TableNameResolver tableNameResolver;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(curUserIDResolver);
        resolvers.add(tableNameResolver);
        resolvers.add(projectResolver);
    }

}
