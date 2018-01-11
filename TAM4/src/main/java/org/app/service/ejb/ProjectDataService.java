package org.app.service.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import org.app.service.entities.Project;
import org.app.service.entities.Task;


@Remote
public interface ProjectDataService {

	            // CREATE or UPDATE
	            Project addProject(Project projectToAdd);

				// DELETE
				String removeProject(Project projectToDelete);

				// READ
				Project getProjectByID(Integer idProiect);
				Collection<Project> getProjects();
				
				// Custom READ: custom query
				Project getProjectByNume(String numeProiect);
				
				// Others
				String getMessage();
				
				Collection<Project> toCollection();
				
				
				Project createNewProject(Integer id);

				Collection<Project> removeFromCollection(Project project);
					
}
