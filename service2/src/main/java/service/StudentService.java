package service;

import entity.Student;
import mapper.StudentMapper;
import org.springframework.stereotype.Service;

@Service
public class StudentService implements StudentMapper {

  /*  @Autowired
    StudentMapper sm;*/

    @Override
    public Student getInfo() {
       // return sm.getInfo();//就孩不用数据库没什么意义
        return  new Student("李四",25,"女");

    }
}
