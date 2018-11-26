package com.duyi.hrb.dao;

import com.duyi.hrb.domain.Student;

import java.util.List;

public interface StudentDao {
    void add(Student student);

    void del(String sno);

    void update(Student student);

    List<Student> findByAll(String uId);

    List<Student> findByPage(String uId,int offset,int size);

    Student findBySno(String sno);

    int count();
}
