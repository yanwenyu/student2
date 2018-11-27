package com.duyi.hrb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.duyi.hrb.domain.RespModel;
import com.duyi.hrb.domain.Student;
import com.duyi.hrb.enums.RespStatusEnum;
import com.duyi.hrb.service.StudentService;
import com.duyi.hrb.util.RSAEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;


@Controller
public class StudentController extends BaseController {

    @Autowired
    StudentService studentService;

    @RequestMapping(value = "/adm/findAll", method = RequestMethod.GET)


    public void findAll(@RequestParam(name = "callback") String callback, String uId,HttpServletResponse resp) throws Exception {
//        resp.setHeader("content-type", "application:json;charset=utf8");
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        resp.setHeader("Access-Control-Allow-Methods", "POST");
//        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
//        setHeader(resp);

//        String encodeUid = RSAEncrypt.decrypt(uId);

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
        String resultString = "";
        int count = 0;
        if(uId == null || "".equals(uId)) {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The uId can't get",
                            null

                             ));
            writeJsonp(resp,callback,resultString);
            return;
        }

    //问题1：用不用先判断一下该管理团存在？

        List<Student> findAll = studentService.findAll(uId);
        
//        for(Student s : findAll) {
//
//            System.out.println(s);
//
//        }



        resultString = JSON.toJSONString(new RespModel(RespStatusEnum.SUCCESS.getValue(),null, findAll));

        writeJsonp(resp,callback,resultString);
    }



    @RequestMapping(value = "/adm/findByPage", method = RequestMethod.GET)

    public void findByPage(@RequestParam(name = "callback") String callback, @RequestParam(name = "page") int page,@RequestParam(name = "size")int size,@RequestParam(name="uId") String uId, HttpServletRequest res, HttpServletResponse resp) throws Exception {

        JSONObject result = new JSONObject();
//
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        resp.setHeader("content-type", "application:json;charset=utf8");
//        resp.setHeader("Access-Control-Allow-Methods", "POST");
//        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

//        setHeader(resp);

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

        //问题2：这个的失败结果

        String encodeUid = RSAEncrypt.decrypt(uId);
        List<Student> findByPage = studentService.findByPage(encodeUid, page , size);
        int count = studentService.count();
        result.put("count",count);
        result.put("findBypageList", findByPage);
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

    @RequestMapping(value = "/adm/delBySno", method = RequestMethod.GET)

    public void delBySno(@RequestParam(name = "callback") String callback,@RequestParam(name = "sNo") String sNo, HttpServletResponse resp) throws IOException {
        
        System.out.println("sNo：" + sNo);
        
        JSONObject result = new JSONObject();
        String resultString = "";
        boolean isTrue = studentService.delBySno(sNo);

        if (isTrue) {

            resultString = JSON.toJSONString(new RespModel(RespStatusEnum.SUCCESS.getValue(),null,null));

        } else {

            resultString = JSON.toJSONString(new RespModel(RespStatusEnum.FAIL.getValue(),
                                                "not find this student",
                                                null));

        }
            writeJsonp(resp,callback,resultString);
    }

    @RequestMapping(value = "/adm/addStudent", method = RequestMethod.GET)

    public void addStudent(@RequestParam(name = "callback") String callback,@RequestParam(name = "sNo") String sNo, @RequestParam(name = "name")String name,
                           @RequestParam(name = "email") String email, @RequestParam(name = "sex") Integer sex, @RequestParam(name = "birth") Integer birth,@RequestParam(name = "phone") String phone,
                           String address, String uId, HttpServletResponse resp) throws Exception {

        String resultString = "";

        if (sNo == null || sNo.equals("")) {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The sNo is null",
                            null));
            writeJsonp(resp,callback,resultString);
            return;
        }

        if (email == null || "".equals(email)) {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The email is null",
                            null));
            writeJsonp(resp,callback,resultString);
        }
        if (birth == 0) {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The birth greater than zero",
                            null));
            writeJsonp(resp,callback,resultString);

            return;
        }

        if (phone == null || "".equals(phone)) {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The phone is null",
                            null));
            writeJsonp(resp,callback,resultString);

            return;
        }

        if (address == null || "".equals(address)) {
            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The address is null",
                            null));
            writeJsonp(resp,callback,resultString);

            return;
        }

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
            resultString = JSON.toJSONString(new RespModel(RespStatusEnum.SUCCESS.getValue(),null,null));

//            resp.getWriter().write(result.toJSONString());
//            resp.getWriter().write(callback + "(" + result.toJSONString() + ")");
        } else {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "this number is exist",
                            null));
        }
            writeJsonp(resp, callback, resultString);

    }

    @RequestMapping(value = "/adm/updateStudent", method = RequestMethod.GET)

    public void updateStudent(@RequestParam(name = "callback") String callback,@RequestParam(name = "sNo") String sNo, String name, String email, Integer sex, Integer birth, String phone, String address, HttpServletRequest res, HttpServletResponse resp) throws IOException {

        String resultString = "";

        if (sNo == null || sNo.equals("")) {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The sNo is null",
                            null));
            writeJsonp(resp,callback,resultString);
            return;
        }

        if (email == null || "".equals(email)) {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The email is null",
                            null));
            writeJsonp(resp,callback,resultString);
        }
        if (birth == 0) {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The birth greater than zero",
                            null));
            writeJsonp(resp,callback,resultString);

            return;
        }

        if (phone == null || "".equals(phone)) {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The phone is null",
                            null));
            writeJsonp(resp,callback,resultString);

            return;
        }

        if (address == null || "".equals(address)) {
            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The address is null",
                            null));
            writeJsonp(resp,callback,resultString);

            return;
        }

        JSONObject result = new JSONObject();


        boolean isTrue = studentService.update(sNo, name, email, sex, birth, phone, address);

        if (isTrue) {

            resultString = JSON.toJSONString(new RespModel(RespStatusEnum.SUCCESS.getValue(),null,null));

        } else {

            resultString = JSON.toJSONString(new RespModel(
                    RespStatusEnum.FAIL.getValue(),
                    "Not find this student",
                    null));
        }

        writeJsonp(resp,callback,resultString);
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
