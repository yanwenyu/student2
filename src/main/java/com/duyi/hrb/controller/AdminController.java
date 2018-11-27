package com.duyi.hrb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.duyi.hrb.domain.Admin;
import com.duyi.hrb.domain.RespModel;
import com.duyi.hrb.domain.Student;
import com.duyi.hrb.enums.RespStatusEnum;
import com.duyi.hrb.service.AdminService;
import com.duyi.hrb.service.StudentService;
import com.duyi.hrb.util.MD5Util;
import com.duyi.hrb.util.MailOperation;
import com.duyi.hrb.util.RSAEncrypt;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AdminController extends BaseController {

    @Autowired
    AdminService adminService;

    @Autowired
    StudentService studentService;

    private void setHeader(HttpServletResponse resp) {

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("content-type", "application:json;charset=utf8");
        resp.setHeader("Access-Control-Allow-Methods", "POST");
        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

    }

    @RequestMapping(value = "/adminLogin",method = RequestMethod.POST)//不能是post~~~~？？？？？
    //@RequestParam(name = "callback") String callback,
    @ResponseBody
    public void login( String account, String password, HttpServletResponse resp) throws Exception {

        setHeader(resp);

//        resp.setHeader("content-type", "application/json;charset=utf8");
//        resp.setHeader("Access-Control-Allow-Origin", "*");//跨域
//        resp.setHeader("Access-Control-Allow-Methods", "POST");
//        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type,Set-Cookie");
//      resp.setHeader("Access-Control-Allow-Credentials", "true");

        System.out.println("账号：" + account);

        System.out.println("密码：" + password);

//        System.out.println(callback);

        JSONObject result = new JSONObject();

        if (account == null || account.equals("")) {

            result.put("status", "fail");

            result.put("message", "The account is null");

            resp.getWriter().write( result.toJSONString() );

//            resp.getWriter().write(callback + "(" + result.toJSONString() + ")");

            return;
        }
        if (password == null || password.equals("")) {

            result.put("status", "fail");

            result.put("message", "The password is null");

            resp.getWriter().write( result.toJSONString() );

            return;
        }

        Admin admin = adminService.login(account, password);

        if (admin == null) {

            result.put("status", "fail");

            result.put("message", "account or password error");

            resp.getWriter().write( result.toJSONString() );
        } else {

            String str = RSAEncrypt.encrypt(account);

            Cookie cookie = new Cookie("account", str);

            resp.addCookie(cookie);

//            res.setAttribute("account",account);

//            res.getRequestDispatcher("/adm/findAll").forward(res, resp);

            result.put("status", "success");

            resp.getWriter().write( result.toJSONString() );
        }
        resp.getWriter().close();

    }

    @RequestMapping(value = "/adminActivate", method = RequestMethod.GET)

    //@RequestParam(name = "callback") String callback,
    //添加一个update修改状态
    public void adminActivate( String encryptionAccount, HttpServletRequest res, HttpServletResponse resp) throws Exception {

        setHeader(resp);

        System.out.println(encryptionAccount);

        String encodeAccount = RSAEncrypt.decrypt(encryptionAccount);

        System.out.println(encodeAccount);

        JSONObject result = new JSONObject();

        boolean isTrue = adminService.updateStatus(encodeAccount);


        if (isTrue) {

            result.put("status", "success");

            resp.getWriter().write( result.toJSONString() );

//            resp.getWriter().write(callback + "(" + result.toJSONString() + ")");


        } else {

            result.put("message", "not find this user");

            result.put("status", "fail");

            resp.getWriter().write( result.toJSONString() );

//            resp.getWriter().write(callback + "(" + result.toJSONString() + ")");

        }

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)

    //@RequestParam(name = "callback") String callback,

    public void registe(String account, String password, String rePassword, String email, HttpServletResponse resp) throws Exception {

//        resp.setHeader("content-type", "application:json;charset=utf8");
//        resp.setHeader("Access-Control-Allow-Origin", "*");
//        resp.setHeader("Access-Control-Allow-Methods", "POST");
//        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");


        System.out.println(account + password + rePassword + email);

        JSONObject result = new JSONObject();

        if (account == null || account.equals("")) {

            result.put("status", "fail");

            result.put("message", "The account count null");

            resp.getWriter().write( result.toJSONString() );

            return;
        }

        if (password == null || password.equals("")) {

            result.put("status", "fail");

            result.put("message", "The password count null");

            resp.getWriter().write( result.toJSONString() );

            return;
        }

        if (rePassword == null || rePassword.equals("")) {

            result.put("status", "fail");

            result.put("message", "The rePassword count null");

            resp.getWriter().write( result.toJSONString() );

            return;
        }

        if (!password.equals(rePassword)) {

            result.put("status", "fail");

            result.put("message", "The two input passwords do not match");

            resp.getWriter().write( result.toJSONString() );

            return;

        }

        if (email == null || email.equals("")) {

            result.put("status", "fail");

            result.put("message", "The email count null");

            resp.getWriter().write( result.toJSONString() );

            return;
        }

        // 邮箱验证规则
        String regEx = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,5}$";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);

        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {

            result.put("status", "fail");

            result.put("message", "The Email format error");

            resp.getWriter().write( result.toJSONString() );

            return;

        }

        boolean isTrue = adminService.addAdmin(account, password, email);

        if (isTrue) {

            String user = "18846411586@163.com";

            String password1 = "ywywenyu22";

            String host = "smtp.163.com";

            String from = "18846411586@163.com";

            String to = email;// 收件人

            String subject = "输入邮件主题";

            String encryptionAccount = URLEncoder.encode(RSAEncrypt.encrypt(account));

            adminService.senEmail(encryptionAccount, user, password1, host, from, to, subject);

            result.put("status", "success");

            result.put("message", "Please open your registered email for activation!");

            resp.getWriter().write( result.toJSONString() );

        } else {

            result.put("status", "fail");

            result.put("message", "The admin is exist");

            resp.getWriter().write( result.toJSONString() );

        }
    }


    @RequestMapping(value = "/adm/findBySno", method = RequestMethod.GET)
    @ResponseBody

    public void findBySno(@RequestParam(name = "callback") String callback,@RequestParam(name = "sNo") String sNo, HttpServletResponse resp) throws IOException {

        System.out.println("sNo:" + sNo);

        String resultString = "";

        Student stu = studentService.findBySno(sNo);

        setHeader(resp);

        if (stu == null) {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.FAIL.getValue(),
                            "The student not exit",
                            null));

        } else {

            resultString = JSON.toJSONString(
                    new RespModel(
                            RespStatusEnum.SUCCESS.getValue(),
                            null,
                            stu));

        }
            writeJsonp(resp,callback,resultString);
    }

    @RequestMapping(value = "/act", method = RequestMethod.POST)
    @ResponseBody
    public void activateHref(@RequestParam(name = "email") String email, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        JSONObject result = new JSONObject();

        setHeader(resp);

//        String email = req.getParameter("email");

        // 邮箱验证规则
        String regEx = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,5}$";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);

        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(email);

        // 字符串是否与正则表达式相匹配
        if (matcher.matches()) {

            System.out.println("格式正确");

            //发送邮件

            String user = "18846411586@163.com";

            String password = "ywywenyu22";

            String host = "smtp.163.com";

            String from = "18846411586@163.com";

            String to = email;// 收件人

            String subject = "输入邮件主题";


            Admin ad = adminService.findByEmail(email);

//            判断是否存在这个人

            if (ad == null) {

                result.put("status", "fail");

                result.put("message", "not find this user");

                resp.getWriter().write(result.toJSONString());

                return;

            }

            String encryptionAccount = RSAEncrypt.encrypt(ad.getAccount());

            String urlEncryptionAccount = URLEncoder.encode(encryptionAccount, "utf-8");

            adminService.senEmail(urlEncryptionAccount, user, password, host, from, to, subject);

        } else {

            result.put("status", "fail");

            result.put("message", "Mail format error");

            resp.getWriter().write(result.toJSONString());

        }

    }

    @RequestMapping(value = "/sendForgetEmail", method = RequestMethod.POST)
    @ResponseBody
    public void sendForgetEmail(@RequestParam(name = "email") String email, HttpServletResponse resp) throws Exception {

        JSONObject result = new JSONObject();

        setHeader(resp);

        // 邮箱验证规则
        String regEx = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,5}$";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);

        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(email);

        // 字符串是否与正则表达式相匹配
        if (matcher.matches()) {

            System.out.println("格式正确");

            //发送邮件

            String user = "18846411586@163.com";

            String password = "ywywenyu22";

            String host = "smtp.163.com";

            String from = "18846411586@163.com";

            String to = email;// 收件人

            String subject = "输入邮件主题";


            Admin ad = adminService.findByEmail(email);

//            判断是否存在这个人

            if (ad == null) {

                result.put("message", "not find this user");

                result.put("status", "fail");

                resp.getWriter().write(result.toJSONString());

                return;

            }

            String encryptionAccount = RSAEncrypt.encrypt(ad.getAccount());

            String urlEncryptionAccount = URLEncoder.encode(encryptionAccount, "utf-8");

            adminService.senForgetEmail(urlEncryptionAccount, user, password, host, from, to, subject);

        } else {

            result.put("status", "fail");

            result.put("message", "Mail format error");

            resp.getWriter().write(result.toJSONString());

        }

    }


    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    public void forgotPassword(@RequestParam(name = "newPassword") String newPassword,
                               @RequestParam(name = "cofPassword") String cofPassword,
                               @RequestParam(name = "urlEncryptionAccount") String urlEncryptionAccount, HttpServletResponse resp) throws Exception {

        JSONObject result = new JSONObject();

        setHeader(resp);

        if (newPassword == null || newPassword.equals("")) {

            result.put("status", "fail");

            result.put("message", "The newPassword is null");

            resp.getWriter().write(result.toJSONString());

            return;

        }

        if (cofPassword == null || cofPassword.equals("")) {

            result.put("message", "The cofPassword is null");

            result.put("status", "fail");

            resp.getWriter().write(result.toJSONString());

            return;

        }

        if (!newPassword.equals(cofPassword)) {

            result.put("message", "The two input passwords do not match");

            result.put("status", "fail");

            resp.getWriter().write(result.toJSONString());

            return;

        }

        System.out.println("urlEncryptionAccount:" + urlEncryptionAccount);

//        String encryptionAccount = URLDecoder.decode(urlEncryptionAccount,"utf-8");

//        System.out.println("encryptionAccount:" + encryptionAccount);

        String encodeAccount = RSAEncrypt.decrypt(urlEncryptionAccount);

        System.out.println("encodeAccount" + encodeAccount);

        //修改密码语句

        boolean isTrue = adminService.updatePassword(encodeAccount, newPassword);

        if (isTrue) {

            result.put("status", "success");

            resp.getWriter().write(result.toJSONString());


        } else {

            result.put("status", "fail");

            result.put("message", "not find this user");

            resp.getWriter().write(result.toJSONString());

        }


    }


}
