package orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import annotations.Column;
import annotations.CreateTable;
import annotations.Delete;
import annotations.MappedClass;
import annotations.Save;
import annotations.Select;
import realdb.GhettoJdbcBlackBox;

public class DaoInvocationHandler implements InvocationHandler {

	static GhettoJdbcBlackBox jdbc;
	
	public DaoInvocationHandler() {
		// TODO Auto-generated constructor stub
		
		if (jdbc==null)
		{
			jdbc = new GhettoJdbcBlackBox();
			jdbc.init("com.mysql.cj.jdbc.Driver", 				// DO NOT CHANGE
					  "jdbc:mysql://localhost/jdbcblackbox",    // change jdbcblackbox to the DB name you wish to use
					  "czarina", 									// USER NAME
					  "sherbet@310");										// PASSWORD
		}
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
	{
		System.out.println("    DaoInvocationHandler.invoke()");
		System.out.println("    " + method.getName());
		Annotation[] methAnns = method.getDeclaredAnnotations();
		System.out.println("    Annotations: ");
		/*for (Annotation a : methAnns) 
		{
			System.out.println("    " + a);
		}*/
		
		// determine method annotation type and call the appropriate method		
		Annotation a = methAnns[0];
		// @CreateTable
		if (a instanceof CreateTable) 
		{
			createTable(method);
		}
		// @Save
		else if (a instanceof Save)
		{
			save(method, proxy);
		}
		// @Delete
		else if (a instanceof Delete)
		{
			delete(method, proxy);
		}
		// @Select
		else if (a instanceof Select)
		{
			select(method, args);
		}
	
		return null;
	}
	
	
	// HELPER METHOD: when putting in field values into SQL, strings are in quotes otherwise they go in as is
	private String getValueAsSql(Object o) throws Exception
	{
		if (o.getClass()==String.class)
		{
			return "\""+o+"\"";
		}
		else
		{
			return String.valueOf(o);
		}		
	}
	
	
	// handles @CreateTable
	private void createTable(Method method)
	{
		System.out.println("        DaoInvocationHandler.createTable()");
		System.out.println("        " + method);
// 		SAMPLE SQL 		
//	    CREATE TABLE REGISTRATION (id INTEGER not NULL AUTO_INCREMENT,
//												first VARCHAR(255), 
//												last VARCHAR(255), age INTEGER, PRIMARY KEY ( id ))
		
// 		Using the @MappedClass annotation from method
		// get the required class 		
		Class c = method.getDeclaringClass();
		Annotation an = c.getAnnotation(MappedClass.class);
		MappedClass mc = (MappedClass)an;	
		Class reqClass = mc.clazz();
		
		// use reflection to check all the fields for @Column
		
		Field fields[] = reqClass.getDeclaredFields();
		String sqlString = "CREATE TABLE REGISTRATION (";
		for (Field f : fields)
		{
			Annotation[] fieldAnns = f.getDeclaredAnnotations();
			for (Annotation a : fieldAnns)
			{
				//System.out.println(a);
				// use the @Column attributed to generate the required sql statment
				if (a instanceof Column)
				{
					String fieldName = f.getName();
					//System.out.println(fieldName);
					Annotation fieldAnn = f.getAnnotation(Column.class);
					Column col = (Column)fieldAnn;
					sqlString = sqlString + fieldName;
					
					
					String fieldSqlType = col.sqlType(); 
					//System.out.println(fieldSqlType);
					sqlString = sqlString + " " + fieldSqlType + ", ";
				}
			}
		}
		sqlString = sqlString + "PRIMARY KEY (id))";
		System.out.println("        SQL STATEMENT: " + sqlString);
		
// 		Run the sql
		try 
		{
			jdbc.runSQL(sqlString);	
		}
		catch(Exception ex) 
		{
			   System.out.println("Table already exists.");
		}
	}
	
	// handles @Delete
	private void delete(Method method, Object o) throws Exception
	{
// 		SAMPLE SQL		
//  	DELETE FROM REGISTRATION WHERE ID=1
		
		
// 		Using the @MappedClass annotation from method
		// get the required class 		
		// use reflection to check all the fields for @Column
		// find which field is the primary key
		// for the Object o parameter, get the value of the field and use this as the primary value 
		// for the WHERE clause
				// if the primary key field value is null, throw a RuntimeException("no pk value")
		
		
		// run the sql
//		jdbc.runSQL(SQL STRING);
		
		System.out.println("        DaoInvocationHandler.delete()");
	}
	
	// handles @Save
	private void save(Method method, Object o) throws Exception
	{
// 		Using the @MappedClass annotation from method
		// get the required class 		
		// use reflection to check all the fields for @Column
		// find which field is the primary key
		// for the Object o parameter, get the value of the field
			// if the field is null run the insert(Object o, Class entityClass, String tableName) method
			// if the field is not null run the update(Object o, Class entityClass, String tableName) method
		
		System.out.println("        DaoInvocationHandler.save()");
	}

	private void insert(Object o, Class entityClass, String tableName) throws Exception 
	{
		
		
// 		SAMPLE SQL		
//		INSERT INTO table_name (column1, column2, column3, ...)
//		VALUES (value1, value2, value3, ...)	


//		HINT: columnX comes from the entityClass, valueX comes from o 
		
		
// 		run sql		
//		jdbc.runSQL(SQL STRING);
	}

	private void update(Object o, Class entityClass, String tableName) throws IllegalAccessException, Exception {

//		SAMPLE SQL		
//		UPDATE table_name
//		SET column1 = value1, column2 = value2, ...
//		WHERE condition;
		
//		HINT: columnX comes from the entityClass, valueX comes from o 		
		
//		run sql
//		jdbc.runSQL(SQL STRING);
	}

		
	// handles @Select
	private Object select(Method method, Object[] args) throws Exception
	{
		System.out.println("        DaoInvocationHandler.select()");
		
		// same style as lab
		
// PART I		
// 		Using the @MappedClass annotation from method
//		get the required class
//		Use this class to extra all the column information (this is the replacement for @Results/@Result)		
//		generate the SELECT QUERY		

// PART II
		
//		this will pull actual values from the DB		
//		List<HashMap<String, Object>> results = jdbc.runSQLQuery(SQL QUERY);

		
		// process list based on getReturnType
		if (method.getReturnType()==List.class)
		{
			List returnValue = new ArrayList();
			
			// create an instance for each entry in results based on mapped class
			// map the values to the corresponding fields in the object
			// DO NOT HARD CODE THE TYPE and FIELDS USE REFLECTION
			
			return returnValue;
		}
		else
		{
			// if not a list return type
			
			// if the results.size() == 0 return null
			// if the results.size() >1 throw new RuntimeException("More than one object matches")
			// if the results.size() == 1
				// create one instance based on mapped class
				// map the values to the corresponding fields in the object
				// DO NOT HARD CODE THE TYPE and FIELDS USE REFLECTION
						
			return null;
		}
	}
	
}
