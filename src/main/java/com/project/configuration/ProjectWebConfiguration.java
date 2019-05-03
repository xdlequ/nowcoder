package com.project.configuration;

import com.project.Interceptor.LoginRequiredInterceptor;
import com.project.Interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by ql on 2019/5/3.
 */
public class ProjectWebConfiguration implements WebMvcConfigurer {
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;
    @Autowired
    PassportInterceptor passportInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
    }
}
