package cn.senlin.jiaoyi.util.interceptor;

import cn.senlin.jiaoyi.enums.SystemConstantEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 拦截器配置
 *
 * @author swu
 * @date 2020-03-31
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new SessionHandlerInterceptor());

        registration.addPathPatterns("/**");

        //不拦截接口配置
        registration.excludePathPatterns("/user/login");
        registration.excludePathPatterns("/user/regist");
        registration.excludePathPatterns("/registCheck/checkUserAccount");
        registration.excludePathPatterns("/sell/kafka");
        registration.excludePathPatterns("/jsp/**");
        registration.excludePathPatterns("/js/**");
        registration.excludePathPatterns("/css/**");
        registration.excludePathPatterns("/assets/**");
    }

    static class SessionHandlerInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            //创建session
            HttpSession session = request.getSession();

            if (session.getAttribute(SystemConstantEnum.SESSION_USER_KEY.getCode()) == null) {
                try {
                    response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");  //重定向到登录界面
                } catch (IOException e) {
                    log.error("拦截器报错：", e);
                }
                return false;
            }

            return true;
        }
    }

}
