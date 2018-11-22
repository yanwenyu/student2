package com.duyi.hrb.controller;

import com.alibaba.fastjson.JSONObject;
import com.duyi.hrb.domain.Admin;
import com.duyi.hrb.domain.Student;
import com.duyi.hrb.service.AdminService;
import com.duyi.hrb.service.StudentService;
import com.duyi.hrb.util.RSAEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Controller
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    StudentService studentService;

    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    @ResponseBody
    public void login(String account, String password, HttpServletRequest res, HttpServletResponse resp) throws Exception {

        JSONObject result = new JSONObject();

        if (account == null || account.equals("")) {

            result.put("status","fail");

            result.put("message","The account is null");

            resp.getWriter().write(result.toJSONString());

            return;
        }
        if (password == null || password.equals("")) {

            result.put("status","fail");

            result.put("message","The password is null");

            resp.getWriter().write(result.toJSONString());

            return;
        }

        Admin admin = adminService.login(account,password);

        if (admin == null) {

            result.put("status","fail");

            result.put("message","account or password error");

            resp.getWriter().write(result.toJSONString());
        } else {

            String str = RSAEncrypt.encrypt(account);

            Cookie cookie = new Cookie("account",str);

            resp.addCookie(cookie);

//            res.setAttribute("account",account);

//            res.getRequestDispatcher("/adm/findAll").forward(res, resp);

            result.put("status", "success");

            resp.getWriter().write(result.toJSONString());
        }

    }

    //添加一个update修改状态








    @RequestMapping(value = "/register" , method = RequestMethod.POST)
    public void registe(String account, String password, String email, HttpServletRequest res, HttpServletResponse resp) throws IOException, NoSuchAlgorithmException {

        JSONObject result = new JSONObject();

        if (account == null || account.equals("")) {

            result.put("status","fail");

            result.put("message","The account count null");

            resp.getWriter().write(result.toJSONString());

            return;
        }
        if (password == null || password.equals("")) {

            result.put("status","fail");

            result.put("message","The password count null");

            resp.getWriter().write(result.toJSONString());

            return;
        }

        if (email == null || email.equals("")) {

            result.put("status","fail");

            result.put("message","The email count null");

            resp.getWriter().write(result.toJSONString());

            return;
        }

        boolean isTrue = adminService.addAdmin(account,password,email);

        if (isTrue) {

            result.put("status","success");

            resp.getWriter().write(result.toJSONString());

        } else {

            result.put("status","fail");

            result.put("message","The admin is exist");

            resp.getWriter().write(result.toJSONString());

        }
    }


    @RequestMapping(value = "/adm/findBySno",method = RequestMethod.POST)
    @ResponseBody

    public void findBySno(String sNo, HttpServletResponse resp) throws IOException {

        Student stu = studentService.findBySno(sNo);

        JSONObject result = new JSONObject();

        if (stu == null) {

            result.put("status", "fail");

            result.put("message", "not find this student");

            resp.getWriter().write(result.toJSONString());

        } else {

            resp.getWriter().write(stu.toString());

        }
    }
}
