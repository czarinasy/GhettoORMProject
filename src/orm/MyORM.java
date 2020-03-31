package orm;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import annotations.Column;
import annotations.Entity;
import annotations.MappedClass;
import annotations.Save;
import dao.BasicMapper;
import dao.StudentMapper;
import dao.SubjectMapper;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner; 

public class MyORM 
{	
	
	HashMap<Class, Class> entityToMapperMap = new HashMap<Class, Class>();
	
	
	public void init() throws Exception
	{
		System.out.println("init");
		// scan all mappers -- @MappedClass
		scanMappers();		
		
		// scan all the entities -- @Entity
		scanEntities();
		
		// create all entity tables
		createTables();

	}


	private void scanMappers() throws ClassNotFoundException 
	{
		System.out.println("\nMyORM.scanMappers()");

		// use FastClasspathScanner to scan the dao package for @MappedClass
		ScanResult daoPack = new FastClasspathScanner("dao").scan();		
		List<String> annStrings = daoPack.getNamesOfClassesWithAnnotation(MappedClass.class);
		System.out.println(annStrings);
		for (String s : annStrings)
		{
			Class c = Class.forName(s);
			Annotation an = c.getAnnotation(MappedClass.class);
			MappedClass mc = (MappedClass)an;				
			System.out.println(mc.clazz());			
						
			// check if the clazz has the @Entity annotation
			if (!mc.clazz().isAnnotationPresent(Entity.class)) 
			{
				// if not throw new RuntimeException("No @Entity")  
				throw new RuntimeException("No @Entity");
			}
			
			// map the clazz to the mapper class
			entityToMapperMap.put(mc.clazz(), c);
			System.out.println(entityToMapperMap);
		}
	}
	

	private void scanEntities() throws ClassNotFoundException 
	{
		// use FastClasspathScanner to scan the entity package for @Entity
		System.out.println("\nMyORM.scanEntities()");
		
		ScanResult entPack = new FastClasspathScanner("entity").scan();		
		List<String> annStrings = entPack.getNamesOfClassesWithAnnotation(Entity.class);
		System.out.println(annStrings);
		for (String s : annStrings)
		{
			// go through each of the fields 
			Class c = Class.forName(s);
			Field[] fields = c.getDeclaredFields();
			int idNum = 0;
			for (Field f : fields)
			{
				f.setAccessible(true);
				Annotation an = f.getAnnotation(Column.class);
				Column col = (Column) an;
				System.out.println(col.id());

				if (col.id())
				{
					idNum++;
				}
			}
			
			// check if there is only 1 field with a Column id attribute
			if (idNum != 1)
			{
				// if more than one field has id throw new RuntimeException("duplicate id=true")
				throw new RuntimeException("duplicate id=true");
			}
		}
	}
	
	
	public Object getMapper(Class clazz)
	{
		System.out.println("MyORM.getMapper()");
		
		// create the proxy object for the mapper class supplied in clazz parameter
		// all proxies will use the supplied DaoInvocationHandler as the InvocationHandler
		InvocationHandler handler = new DaoInvocationHandler();
		Object object =  Proxy.newProxyInstance(
				ClassLoader.getSystemClassLoader(), 					
				new Class[] { clazz }, 
				handler									
				);
		return object;
	}
	

	private void createTables()
	{

		System.out.println("\nMyORM.createTables()");
		// go through all the Mapper classes in the map
		for (Entry<Class, Class> entry : entityToMapperMap.entrySet())
		{
			String mapperName = entry.getValue().toString();
			System.out.println(mapperName);
	
			// create a proxy instance for each
			// all these proxies can be casted to BasicMapper
			BasicMapper proxy = (BasicMapper) getMapper(entry.getValue());

			// run the createTable() method on each of the proxies
			proxy.createTable();
			
			/*if (mapperName.equals("interface dao.StudentMapper"))
			{
				System.out.println("\nstudent mapper");
				StudentMapper proxy2 = (StudentMapper) getMapper(entry.getValue());
				proxy2.createTable();
			}
			/*else if (mapperName.equals("interface dao.SubjectMapper"))
			{
				System.out.println("    subject mapper");
				SubjectMapper proxy = (SubjectMapper) getMapper(entry.getValue());
			}*/
		}
	}
}
