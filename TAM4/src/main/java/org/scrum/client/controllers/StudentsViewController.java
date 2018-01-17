package org.scrum.client.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.app.service.entities.Student;
import org.scrum.client.services.StudentsRESTServiceConsumer;

//projectsViewController
@Named @SessionScoped
public class StudentsViewController implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(StudentsViewController.class.getName());
	// Data Model
	private List<Student> students = new ArrayList<Student>();
	//
	@Inject
	private StudentsRESTServiceConsumer restService; 
	//
	public List<Student> getProjects() {
		return students;
	}

	@PostConstruct
	private void init() {
		this.students = restService.getStudents();
		if (this.getStudents().size() >0)
			this.selectedStudent = this.getStudents().get(0);
	}
	private List<Student> getStudents() {
		// TODO Auto-generated method stub
		return null;
	}
	//
	private Student selectedStudent;

	public Student getSelectedStudent() {
		return selectedStudent;
	}

	// Data Grid support
	public void setSelectedStudent(Student selectedStudent) {
		this.selectedStudent = selectedStudent;
		System.out.println(">>> >>>> selectedProject: " + this.selectedStudent);
	}
	
	// Filtering Support
	private List<Student> filteredStudents = new ArrayList<Student>();

	public List<Student> getFilteredStudents() {
		return filteredStudents;
	}

	public void setFilteredStudents(List<Student> filteredStudents) {
		logger.info("filteredProjects ::: ");
		if (filteredStudents != null)
			filteredStudents.stream().forEach(p -> System.out.println(p.getNumeStudent()));
		else
			logger.info(">>> NULL ....");
		
		this.filteredStudents = filteredStudents;
	}
	
	public boolean filterByDate(Object value, Object filter, Locale locale) {

        if( filter == null ) {
            return true;
        }

        if( value == null ) {
            return false;
        }

       Date dateValue = (Date) value;
       Date dateFilter = (Date) filter;
       
       SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
       return dateFormat.format(dateFilter).equals(dateFormat.format(dateValue));
    }	
	
	public void onRowSelect(SelectEvent event) {
		System.out.println(">>> >>>> onRowSelect ==> selectedProject is: " + this.selectedStudent);
	}
	
	public void saveSelectedStudent(ActionEvent actionEvent) {
        addMessage("Saved selected project => " + this.selectedStudent.getNumeStudent());
        this.students = this.restService.addStudent(this.selectedStudent);
    }	
	
	public void addNewStudent(ActionEvent actionEvent) {
		this.selectedStudent = this.restService.createStudent();
		this.students = this.restService.getStudents();
		addMessage("NEW project => " + this.selectedStudent.getNumeStudent());
	}
	
	public void deleteStudent(ActionEvent actionEvent) {
		addMessage("Deleted project => " + this.selectedStudent.getNumeStudent());
		if (this.selectedStudent != null)
				this.restService.deleteStudent(this.selectedStudent);
		this.students = this.restService.getStudents();
		if (!this.students.isEmpty())
			this.selectedStudent = this.students.get(0);
		
	}
	//
	public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }	
	// Data Table Editable
	public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        addMessage("Cell Changed: " + "Old: " + oldValue + ", New:" + newValue);
    }
}
// https://api.jqueryui.com/theming/icons/
// http://www.logicbig.com/tutorials/java-ee-tutorial/jsf/custom-component-basic/
/*
	<f:facet name="first">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
	</f:facet>
*/

