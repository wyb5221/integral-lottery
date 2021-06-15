package com.dachen;

import com.dachen.common.interceptor.LoggerInterceptor;
import com.dachen.util.PropertiesUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringCloudApplication
@EnableSwagger2
@EnableAsync
@ComponentScan("com.dachen")
public class App extends WebMvcConfigurerAdapter implements EnvironmentAware {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
	/**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(new LoggerInterceptor()).addPathPatterns("/**");
      super.addInterceptors(registry);
    }
    
    @Override
    public void setEnvironment(Environment environment) {
        PropertiesUtil.env = environment;
    }
}
