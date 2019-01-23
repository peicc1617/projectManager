package cn.edu.xjtu.cad.hehe.projectManager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan({"cn.edu.xjtu.cad.hehe.projectManager.controller",
        "cn.edu.xjtu.cad.hehe.projectManager.service",
        "cn.edu.xjtu.cad.hehe.projectManager.config",
        "cn.edu.xjtu.cad.hehe.projectManager.resolver"})
@MapperScan("cn.edu.xjtu.cad.hehe.projectManager.dao")
public class ProjectManagerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ProjectManagerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerApplication.class, args);
    }

}

