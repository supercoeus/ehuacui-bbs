package org.ehuacui.bbs.interceptor;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 基础拦截器
 * Created by jianwei.zhou on 2016/9/13.
 */
public class BasicHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            BeforeAdviceController authPassport = ((HandlerMethod) handler).getMethodAnnotation(BeforeAdviceController.class);
            Class<? extends Interceptor>[] interceptors = authPassport.value();
            for (int i = 0; i < interceptors.length; i++) {
                Class<? extends Interceptor> interceptor = interceptors[i];
                ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext(), "org.springframework.web.servlet.FrameworkServlet.CONTEXT.SpringMVC");
                Object object = context.getBean(interceptor);
                Method method = interceptor.getDeclaredMethod("invoke", HttpServletRequest.class, HttpServletResponse.class);
                method.invoke(object, request, response);
            }
        }
        return true;
    }

}
