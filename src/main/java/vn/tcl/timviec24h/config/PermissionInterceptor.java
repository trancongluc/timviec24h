package vn.tcl.timviec24h.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import vn.tcl.timviec24h.domain.Permission;
import vn.tcl.timviec24h.domain.Role;
import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.service.UserService;
import vn.tcl.timviec24h.util.SecurityUtil;
import vn.tcl.timviec24h.util.error.IdInvalidException;
import vn.tcl.timviec24h.util.error.PermissionException;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);
        //Check permission
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email != null || !email.isEmpty()) {
            User user = userService.getUserByUsername(email);
            if(user != null){
                Role role = user.getRole();
                if(role != null){
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow =permissions.stream().anyMatch(item -> item.getApiPath().equals(path) ||
                            item.getMethod().equals(httpMethod));
                    if (!isAllow){
                        throw new PermissionException("Bạn k có quyền truy cập endpoint này");
                    }
                }else {
                    throw new PermissionException("Bạn k có quyền truy cập endpoint này");
                }
            }
        }
        return true;
    }

}
