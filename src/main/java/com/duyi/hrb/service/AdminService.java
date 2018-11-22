package com.duyi.hrb.service;

import com.duyi.hrb.dao.AdminDao;
import com.duyi.hrb.domain.Admin;
import com.duyi.hrb.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

import java.lang.reflect.AccessibleObject;
import java.security.MessageDigest;
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

            String md5Password = MD5Util.MD5Encode(password,"utf8");

            System.out.println(md5Password);

            admin.setPassword(md5Password);

            admin.setEmail(email);

            adminDao.add(admin);

            return true;
        }
    }

    public Admin login(String account,String password) {

        Admin a = findByAccount(account);

        String md5Password = MD5Util.MD5Encode(password,"utf8");

        if (a != null && a.getPassword().equals(md5Password)) {

            return a;

        } else {

            return null;

        }
    }

    public Admin findByAccount(String account) {

        return adminDao.findByAccount(account);

    }


}
