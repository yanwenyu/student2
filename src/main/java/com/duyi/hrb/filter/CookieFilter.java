package com.duyi.hrb.filter;

import com.duyi.hrb.dao.StudentDao;
import com.duyi.hrb.domain.Student;
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

public class CookieFilter implements Filter {

    private static ApplicationContext ac;

    private static StudentService studentService;

    static {
        ac = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");

        studentService = (StudentService) ac.getBean("studentService");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fil) throws IOException, ServletException {
        System.out.println("filter execute");

        HttpServletRequest request = (HttpServletRequest) req;

        HttpServletResponse response = (HttpServletResponse) res;

        StringBuffer path = ((HttpServletRequest) req).getRequestURL();

        Cookie[] cookies = ((HttpServletRequest) req).getCookies();

        System.out.println(cookies);

//        System.out.println(cookies.length);
        boolean b = false;

        for (Cookie cookie : cookies) {

            String name = cookie.getName();//sNo

//            System.out.println(cookie);

            System.out.println(name);

            String userSno = cookie.getValue();

            System.out.println(userSno);

            if (userSno != null || !userSno.equals("")) {
                try {
                    String encodeSno = RSAEncrypt.decrypt(userSno);

                    Student student = studentService.findBySno(encodeSno);

                    if (student != null) {

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

            System.out.println("跳转回login.html");

            req.getRequestDispatcher("/login").forward(req, res);

        }
    }

    @Override
    public void destroy() {

    }
}
