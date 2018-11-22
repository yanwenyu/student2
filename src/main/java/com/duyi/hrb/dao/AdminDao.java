package com.duyi.hrb.dao;

import com.duyi.hrb.domain.Admin;

import java.util.List;

public interface AdminDao {

    void add(Admin admin);

    void update(Admin admin);

    Admin findByAccount(String account);

    List<Admin> findAll();

}
