package entity;

import annotations.Column;
import annotations.Entity;

@Entity(table="subject")
public class Subject {
	
	@Column(name="id", 			sqlType="INTEGER not NULL AUTO_INCREMENT", id=true)
	private Integer id;

	@Column(name="name", 	sqlType="VARCHAR(255)", id = false)
	private String name;

	@Column(name="num_students", 		 sqlType="INTEGER", id = false)
	private Integer numStudents;

	@Override
	public String toString() {
		return "Subject [id=" + id + ", name=" + name + ", numStudents=" + numStudents + "]";
	}

	
	// make all the getter/setter/toString
	
}
