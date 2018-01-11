package org.app.service.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import org.app.service.entities.Task;

@Remote
public interface TaskDataService {

	// CREATE or UPDATE
		Task addTask(Task taskToAdd);

		// DELETE
		String removeTask(Task taskToDelete);

		// READ
		Task getTaskByID(Integer idTask);
		Collection<Task> getTasks();
		
		// Custom READ: custom query
		Task getTaskByDenumire(String denumireTask);
		
		// Others
		String getMessage();

		Collection<Task> toCollection();
		
	
	Task createNewTask(Integer id);

	Collection<Task> removeFromCollection(Task task);
		
	
}
