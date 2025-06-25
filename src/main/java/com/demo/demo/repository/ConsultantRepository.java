package com.demo.demo.repository;

import com.demo.demo.entity.Consultant;
import com.demo.demo.entity.Course;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultantRepository extends JpaRepository<Consultant, Long> {
}
