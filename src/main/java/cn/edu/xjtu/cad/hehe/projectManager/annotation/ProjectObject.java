package cn.edu.xjtu.cad.hehe.projectManager.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProjectObject {
}
