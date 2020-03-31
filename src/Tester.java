import dao.StudentMapper;
import dao.SubjectMapper;
import entity.Student;
import entity.Subject;
import orm.MyORM;

public class Tester {

	public static void main(String[] args) throws Exception {
		
		/*SETUP:
		 *  start MySQL on local machine
		 *  verify that SQL is running on phpmyadmin
		 *  CREATE DATABASE jdbcblackbox;
		 *  double check that database got added to phpmyadmin
		 *  replace login credentials in DaoInvocationHandler.java (in constructor) and GhettoJdbcBlackbox.java (in main method)
		 *  run Tester.java
		*/ 
		
		MyORM orm = new MyORM();
		orm.init();
		
		StudentMapper sm = (StudentMapper) orm.getMapper(StudentMapper.class);
		
//		for (int i = 0; i<10; i++)
//		{
//			Student s = new Student();
//			s.setAge(10+i);
//			s.setFirst("Test"+i);
//			s.setLast("Test"+i);
//			
//			sm.save(s);
//		}
//		
//		System.out.println(sm.getById(4));
//		
//		System.out.println(sm.getAll());
//		
//		System.out.println(sm.getByFirstNameAndLastName("Test1", "Test1"));
//		
//		
//		SubjectMapper sbm = (SubjectMapper) orm.getMapper(SubjectMapper.class);
//		
//		Subject sb = new Subject();
//		sb.setName("cs124");
//		sb.setNumStudents(20);
//		
//		sbm.save(sb);

	}

}
