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

import org.app.service.entities.Student;
import org.app.service.entities.Task;
import org.scrum.client.services.StudentsRESTServiceConsumer;
import org.scrum.client.services.TasksRESTServiceConsumer;

@Named @SessionScoped
public class TasksViewController implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(TasksViewController.class.getName());
	// Data Model
	private List<Task> tasks = new ArrayList<Task>();
	//
	@Inject
	private TasksRESTServiceConsumer restService; 
	//
	public List<Task> getTasks() {
		return tasks;
	}

	@PostConstruct
	private void init() {
		this.tasks = restService.getTasks();
		if (this.getTasks().size() >0)
			this.selectedTask = this.getTasks().get(0);
	}
	
	//
	private Task selectedTask;

	public Task getSelectedTask() {
		return selectedTask;
	}

	// Data Grid support
	public void setSelectedTask(Task selectedTask) {
		this.selectedTask = selectedTask;
		System.out.println(">>> >>>> selectedProject: " + this.selectedTask);
	}
	
	// Filtering Support
	private List<Task> filteredTasks = new ArrayList<Task>();

	public List<Task> getFilteredTasks() {
		return filteredTasks;
	}

	public void setFilteredTasks(List<Task> filteredTasks) {
		logger.info("filteredProjects ::: ");
		if (filteredTasks != null)
			filteredTasks.stream().forEach(p -> System.out.println(p.getDenumireTask()));
		else
			logger.info(">>> NULL ....");
		
		this.filteredTasks = filteredTasks;
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
		System.out.println(">>> >>>> onRowSelect ==> selectedProject is: " + this.selectedTask);
	}
	
	public void saveSelectedTask(ActionEvent actionEvent) {
        addMessage("Saved selected Task => " + this.selectedTask.getDenumireTask());
        this.tasks = this.restService.addTask(this.selectedTask);
    }	
	
	public void addNewTask(ActionEvent actionEvent) {
		this.selectedTask = this.restService.createTask();
		this.tasks = this.restService.getTasks();
		addMessage("NEW Task => " + this.selectedTask.getDenumireTask());
	}
	
	public void deleteTask(ActionEvent actionEvent) {
		addMessage("Deleted Task => " + this.selectedTask.getDenumireTask());
		if (this.selectedTask != null)
				this.restService.deleteTask(this.selectedTask);
		this.tasks = this.restService.getTasks();
		if (!this.tasks.isEmpty())
			this.selectedTask = this.tasks.get(0);
		
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


