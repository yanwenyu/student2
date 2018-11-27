package com.duyi.hrb.dao;

import com.duyi.hrb.domain.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentDao {
    void add(Student student);

    void del(String sno);

    void update(Student student);

    List<Student> findByAll(@Param(value = "uId") String uId);

    List<Student> findByPage(@Param(value = "uId") String uId,@Param(value = "offset") int offset,@Param(value = "size") int size);

    Student findBySno(@Param(value = "sNo") String sNo);

    int getPageSum();
}
