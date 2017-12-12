package org.app.service.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import org.app.service.entities.Student;


@Remote
public interface StudentDataService {

	   // CREATE or UPDATE
    Student addStudent(Student studentToAdd);

	// DELETE
	String removeStudent(Student studentToDelete);

	// READ
	Student getStudentByID(Integer idStudent);
	Collection<Student> getStudents();
	
	// Custom READ: custom query
	Student getStudentByName(String numeStudent);
	
	// Others
	String getMessage();

	Student getStudentByNume(String numeStudent);
}
