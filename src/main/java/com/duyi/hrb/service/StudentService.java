package com.duyi.hrb.service;

import com.duyi.hrb.dao.StudentDao;
import com.duyi.hrb.domain.Student;
import com.duyi.hrb.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentDao studentDao;

    public boolean addStudent(String sNo, String name, String email, int sex, int birth, String phone, String address , String uId) {

        Student student1 = studentDao.findBySno(sNo);

        if (student1 != null) {

            return false;

        } else {

            Student student = new Student();

            student.setsNo(sNo);

            student.setName(name);

            student.setEmail(email);

            student.setSex(sex);

            student.setBirth(birth);

            student.setPhone(phone);

            student.setAddress(address);

            student.setuId(uId);

            studentDao.add(student);

            return true;
        }
    }

    public boolean delBySno(String sNo) {
        Student student1 = studentDao.findBySno(sNo);

        if (student1 == null) {

            return false;

        } else {

            studentDao.del(sNo);

            return true;
        }

    }

    public boolean update(String sNo, String name, String email, int sex, int birth, String phone, String address) {

        Student student1 = studentDao.findBySno(sNo);

        if (student1 == null) {

            return false;

        } else {

            Student student = new Student();

            student.setsNo(sNo);

            student.setName(name);

            student.setSex(sex);

            student.setEmail(email);

            student.setBirth(birth);

            student.setPhone(phone);

            student.setAddress(address);

            studentDao.update(student);

            return true;
        }

    }

    public List<Student> findAll(String uId) {

        return studentDao.findByAll(uId);
    }

    public Student findBySno(String sNo) {
        Student student = studentDao.findBySno(sNo);

        if (student == null) {

            return null;

        } else {

            return student;
        }
    }

//    public Student login(String sNo, String password) {
//
//        Student student = studentDao.findBySno(sNo);
//
//        String md5Password = MD5Util.MD5Encode(password,"utf8");
//
//        if (student != null && student.getPassword().equals(md5Password)) {
//
//            return student;
//        } else {
//
//            return null;
//
//        }
//
//    }

}
