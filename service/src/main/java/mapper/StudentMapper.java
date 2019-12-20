package mapper;


import entity.Student;

public interface StudentMapper {

    Student getInfo();

    int insert(Student record);

}
