package com.duyi.hrb.filter;

import com.duyi.hrb.domain.Admin;
import com.duyi.hrb.domain.Student;
import com.duyi.hrb.service.AdminService;
import com.duyi.hrb.service.StudentService;
import com.duyi.hrb.util.RSAEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminFilter implements Filter {

    private static ApplicationContext ac;

    private static AdminService adminService;

    static {
        ac = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");

        adminService = (AdminService) ac.getBean("adminService");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fil) throws IOException, ServletException {
        System.out.println("filter execute");
//
//        HttpServletRequest request = (HttpServletRequest) req;
//
//        HttpServletResponse response = (HttpServletResponse) res;
//
//        StringBuffer path = ((HttpServletRequest) req).getRequestURL();

        Cookie[] cookies = ((HttpServletRequest) req).getCookies();

        System.out.println(cookies);

//        System.out.println(cookies.length);
        boolean b = false;

        for (Cookie cookie : cookies) {

            String name = cookie.getName();//sNo

//            System.out.println(cookie);

            System.out.println(name);

            String account = cookie.getValue();

//            System.out.println(account);

            if (account != null || !account.equals("")) {
                try {
                    String encodeSno = RSAEncrypt.decrypt(account);

                    Admin admin = adminService.findByAccount(encodeSno);

                    if (admin != null) {

                        b = true;

                        System.out.println("filter放行");

                        fil.doFilter(req, res);
                    }
                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
        if (!b) {

            System.out.println("跳转回adminLogin.html");

            req.getRequestDispatcher("/adminLogin").forward(req, res);

        }
    }

    @Override
    public void destroy() {

    }
}
