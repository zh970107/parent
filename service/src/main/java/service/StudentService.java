package service;

import entity.Student;
import mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
public class StudentService {

    @Autowired
    StudentMapper sm;

    public Student getInfo() {
       // return sm.getInfo();//就孩不用数据库没什么意义
        return  new Student("张三",20,"男");
    }

    @RequestMapping(value="/insert",produces = "application/json;charset=UTF-8")
    public int insert(Student student) {
        student = new Student(1,"张三",18,"男");
        return sm.insert(student);
    }
}
