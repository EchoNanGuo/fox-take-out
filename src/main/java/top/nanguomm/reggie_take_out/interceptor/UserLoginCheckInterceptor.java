package top.nanguomm.reggie_take_out.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.nanguomm.reggie_take_out.domain.Result;
import top.nanguomm.reggie_take_out.utils.BaseContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UserLoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long userId = (Long) request.getSession().getAttribute("user");
        if ( userId != null) {
            //给当前用户Servlet线程设置值
            BaseContext.setCurrentId(userId);
            return true;
        }

        log.info("host:" + request.getRemoteAddr() + "-----未登录，拦截-------------------");
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        return false;
    }
}
