package com.duyi.hrb.controller;

import com.alibaba.fastjson.JSONObject;
import com.duyi.hrb.domain.Student;
import com.duyi.hrb.service.StudentService;
import com.duyi.hrb.util.RSAEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
public class StudentController {

    @Autowired
    StudentService studentService;

    @RequestMapping(value = "/adm/findAll", method = RequestMethod.POST)

    public void findAll(String callback, String uId, HttpServletRequest res, HttpServletResponse resp) throws Exception {

        JSONObject result = new JSONObject();

//        resp.setHeader("content-type", "application:json;charset=utf8");
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        resp.setHeader("Access-Control-Allow-Methods", "POST");
//        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        setHeader(resp);

        String encodeUid = RSAEncrypt.decrypt(uId);

//        String uId = "";
//
//        Cookie[] cookies = res.getCookies();
//
//        ok:
//        for (Cookie cookie : cookies) {
//
//            String accountName = cookie.getName();
//
//            if ("account".equals(accountName)) {
//
//                String accountValue = cookie.getValue();
//
//                uId = RSAEncrypt.decrypt(accountValue);
//
//                break ok;
//            }
//        }

        List<Student> findAll = studentService.findAll(encodeUid);
        result.put("findAll", findAll.toString());
        resp.getWriter().write(callback + "(" + result.toJSONString() + ")");
    }




    @RequestMapping(value = "/adm/findByPage", method = RequestMethod.POST)

    public void findByPage(String callback, int page, String uId, HttpServletRequest res, HttpServletResponse resp) throws Exception {

        JSONObject result = new JSONObject();
//
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        resp.setHeader("content-type", "application:json;charset=utf8");
//        resp.setHeader("Access-Control-Allow-Methods", "POST");
//        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

        setHeader(resp);

//        String uId = "";
//
//        Cookie[] cookies = res.getCookies();
//
//        ok:
//        for (Cookie cookie : cookies) {
//
//            String accountName = cookie.getName();
//
//            if ("account".equals(accountName)) {
//
//                String accountValue = cookie.getValue();
//
//                uId = RSAEncrypt.decrypt(accountValue);
//
//                break ok;
//            }
//        }

        String encodeUid = RSAEncrypt.decrypt(uId);
        List<Student> findByPage = studentService.findByPage(encodeUid, page);
        result.put("findAll", findByPage.toString());
        resp.getWriter().write(callback + "(" + result.toJSONString() + ")");
    }

//    @RequestMapping("/page/findBySno")
//
//    public void findBySno(String sNo, HttpServletResponse resp) throws IOException {
//
//        Student stu = studentService.findBySno(sNo);
//
//        JSONObject result = new JSONObject();
//
//        if (stu == null) {
//
//            result.put("status", "fail");
//
//            result.put("message", "not find this student");
//
//            resp.getWriter().write(result.toJSONString());
//
//        } else {
//
//            resp.getWriter().write(stu.toString());
//        }
//    }

    @RequestMapping(value = "/adm/delBySno", method = RequestMethod.POST)

    public void delBySno(String sNo, HttpServletResponse resp) throws IOException {

        setHeader(resp);

        JSONObject result = new JSONObject();

        boolean isTrue = studentService.delBySno(sNo);

        if (isTrue) {

            result.put("status", "success");

            resp.getWriter().write(result.toJSONString());

        } else {

            result.put("status", "fail");

            result.put("message", "not find this student");

            resp.getWriter().write(result.toJSONString());
        }
    }

    @RequestMapping(value = "/adm/addStudent", method = RequestMethod.POST)

    public void addStudent(String sNo, String name, String email, Integer sex, Integer birth, String phone, String address, String uId, HttpServletRequest res, HttpServletResponse resp) throws Exception {

        check(sNo, name, email, sex, birth, phone, address, res, resp);

        JSONObject result = new JSONObject();

        setHeader(resp);
//        if (sNo == null || sNo.equals("")) {
//            result.put("status", "fail");
//
//            result.put("message", "this sNo is not exist");
//
//            resp.getWriter().write(result.toJSONString());
//
//            return;
//        }
//        if (email == null || email.equals("")) {
//            result.put("status", "fail");
//
//            result.put("message", "this password is not exist");
//
//            resp.getWriter().write(result.toJSONString());
//            return;
//        }
//        if (birth == 0) {
//            result.put("status", "fail");
//
//            result.put("message", "this birth is error");
//
//            resp.getWriter().write(result.toJSONString());
//
//            return;
//        }
//
//        if (phone == null || phone.equals("")) {
//            result.put("status", "fail");
//
//            result.put("message", "this phone is not exist");
//
//            resp.getWriter().write(result.toJSONString());
//
//            return;
//        }
//
//        if (address == null || address.equals("")) {
//            result.put("status", "fail");
//
//            result.put("message", "this address is not exist");
//
//            resp.getWriter().write(result.toJSONString());
//
//            return;
//        }

//        String uId = "";
//
//        Cookie[] cookies = res.getCookies();
//
//        ok:
//        for (Cookie cookie : cookies) {
//
//            String accountName = cookie.getName();
//
//            if ("account".equals(accountName)) {
//
//                String accountValue = cookie.getValue();
//
//                uId = RSAEncrypt.decrypt(accountValue);
//
////                System.out.println(uId);
//
//                break ok;
//            }
//        }


        String encodeUid = RSAEncrypt.decrypt(uId);

        boolean isTrue = studentService.addStudent(sNo, name, email, sex, birth, phone, address, encodeUid);

        if (isTrue) {

            result.put("status", "success");

            resp.getWriter().write(result.toJSONString());

        } else {

            result.put("status", "fail");

            result.put("message", "this number is exist");

            resp.getWriter().write(result.toJSONString());

        }

    }

    @RequestMapping(value = "/adm/updateStudent", method = RequestMethod.POST)

    public void updateStudent(String sNo, String name, String email, Integer sex, Integer birth, String phone, String address, HttpServletRequest res, HttpServletResponse resp) throws IOException {

        check(sNo, name, email, sex, birth, phone, address, res, resp);

        JSONObject result = new JSONObject();

        setHeader(resp);

        boolean isTrue = studentService.update(sNo, name, email, sex, birth, phone, address);

        if (isTrue) {

            result.put("status", "success");

            resp.getWriter().write(result.toJSONString());
        } else {

            result.put("status", "fail");

            result.put("message", "not found this student");

            resp.getWriter().write(result.toJSONString());
        }

    }

    private void check(String sNo, String name, String email, Integer sex, Integer birth, String phone, String address, HttpServletRequest res, HttpServletResponse resp) throws IOException {

        JSONObject result = new JSONObject();

        setHeader(resp);

        if (sNo == null || sNo.equals("")) {
            result.put("status", "fail");

            result.put("message", "this sNo is not exist");

            resp.getWriter().write(result.toJSONString());

            return;
        }

        if (name == null || name.equals("")) {
            result.put("status", "fail");

            result.put("message", "this name is not exist");

            resp.getWriter().write(result.toJSONString());

            return;
        }

        if (email == null || email.equals("")) {
            result.put("status", "fail");

            result.put("message", "this password is not exist");

            resp.getWriter().write(result.toJSONString());
            return;
        }
        if (birth == 0) {
            result.put("status", "fail");

            result.put("message", "this birth is error");

            resp.getWriter().write(result.toJSONString());

            return;
        }

        if (phone == null || phone.equals("")) {
            result.put("status", "fail");

            result.put("message", "this phone is not exist");

            resp.getWriter().write(result.toJSONString());

            return;
        }

        if (address == null || address.equals("")) {
            result.put("status", "fail");

            result.put("message", "this address is not exist");

            resp.getWriter().write(result.toJSONString());

            return;
        }
    }
    private void setHeader(HttpServletResponse resp) {

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("content-type", "application:json;charset=utf8");
        resp.setHeader("Access-Control-Allow-Methods", "POST");
        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

    }
//    @RequestMapping("/login")
//
//    public void login(String sNo, String password, HttpServletResponse resp, HttpServletRequest res) throws Exception {
//
//        JSONObject result = new JSONObject();
//
//        if (sNo == null || sNo.equals("")) {
//
//            result.put("status", "fail");
//
//            result.put("message", "username is null");
//
//            resp.getWriter().write(result.toJSONString());
//
//            return;
//        }
//
//        if (password == null || password.equals("")) {
//
//            result.put("status", "fail");
//
//            result.put("message", "password is null");
//
//            resp.getWriter().write(result.toJSONString());
//
//            return;
//        }
//
//        Student student = studentService.login(sNo, password);
//
//        if (student == null) {
//
//            result.put("status", "fail");
//
//            result.put("message", "username or password error");
//
//            resp.getWriter().write(result.toJSONString());
//        } else {
//            String str = RSAEncrypt.encrypt(sNo);
//            Cookie cookie = new Cookie("sNO", str);
//            resp.addCookie(cookie);
////            res.getRequestDispatcher("/page/findBySno").forward(res, resp);
//            result.put("status", "success");
//
//            resp.getWriter().write(result.toJSONString());
//        }
//
//    }

}
