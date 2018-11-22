package com.duyi.hrb.service;

import com.duyi.hrb.dao.AdminDao;
import com.duyi.hrb.domain.Admin;
import com.duyi.hrb.util.MD5Util;
import com.duyi.hrb.util.MailOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class AdminService {

    @Autowired
    AdminDao adminDao;

    public boolean addAdmin(String account, String password, String email) throws NoSuchAlgorithmException {

        Admin a = findByAccount(account);

        if (a != null) {

            return false;

        } else {

            Admin admin = new Admin();

            admin.setAccount(account);

            String md5Password = MD5Util.MD5Encode(password, "utf8");

            System.out.println(md5Password);

            admin.setPassword(md5Password);

            admin.setEmail(email);

            adminDao.add(admin);

            return true;
        }
    }

    public Admin login(String account, String password) {

        Admin a = findByAccount(account);

        String md5Password = MD5Util.MD5Encode(password, "utf8");

        if (a != null && a.getPassword().equals(md5Password)) {

            return a;

        } else {

            return null;

        }
    }

    public Admin findByAccount(String account) {

        return adminDao.findByAccount(account);

    }

    public Admin findByEmail(String email) {

        return adminDao.findByEmail(email);

    }

    public void senEmail(String encryptionAccount, String user, String password, String host, String from, String to, String subject) throws Exception {

        //邮箱内容
        StringBuffer sb = new StringBuffer();


        MailOperation operation = new MailOperation();

        String yzm = "http://127.0.0.1:8080/adminActivate?encryptionAccount=" + encryptionAccount;

        sb.append("<!DOCTYPE>" + "<div bgcolor='#f1fcfa'   style='border:1px solid #d9f4ee; font-size:14px; line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;   padding-bottom:5px;'><span style='font-weight:bold;'>温馨提示：</span>"
                + "<div style='width:950px;font-family:arial;'>欢迎使用渡一教育平台，您的激活链接为：<br/><h2 style='color:green'><a href=" + yzm + ">" + yzm + "</a></h2><br/>本邮件由系统自动发出，请勿回复。<br/>感谢您的使用。<br/>XXXX有限公司</div>"
                + "</div>");


        String res = operation.sendMail(user, password, host, from, to,
                subject, sb.toString());

        System.out.println(res);


    }

    public void senForgetEmail(String encryptionAccount, String user, String password, String host, String from, String to, String subject) throws Exception {

        //邮箱内容
        StringBuffer sb = new StringBuffer();


        MailOperation operation = new MailOperation();

        String yzm = "http://127.0.0.1:8080/resetPasswords.html?encryptionAccount=" + encryptionAccount;
        System.out.println("ras:" + encryptionAccount);

        sb.append("<!DOCTYPE>" + "<div bgcolor='#f1fcfa'   style='border:1px solid #d9f4ee; font-size:14px; line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;   padding-bottom:5px;'><span style='font-weight:bold;'>温馨提示：</span>"
                + "<div style='width:950px;font-family:arial;'>欢迎使用渡一教育平台，您的修改密码连接为：<br/><h2 style='color:green'><a href=" + yzm + ">" + yzm + "</a></h2><br/>本邮件由系统自动发出，请勿回复。<br/>感谢您的使用。<br/>XXXX有限公司</div>"
                + "</div>");


        String res = operation.sendMail(user, password, host, from, to,
                subject, sb.toString());

        System.out.println(res);


    }

    public boolean updateStatus(String account) {

        Admin admin = adminDao.findByAccount(account);

        if (admin == null) {

            return false;
        } else {

            admin.setStatus(1);
            adminDao.update(admin);

            return true;

        }

    }

    public boolean updatePassword(String account, String newPassword) {

        Admin admin = adminDao.findByAccount(account);

        if (admin == null) {

            return false;
        } else {

            admin.setPassword(newPassword);

            adminDao.updatePassword(admin);

            return true;

        }

    }

}
