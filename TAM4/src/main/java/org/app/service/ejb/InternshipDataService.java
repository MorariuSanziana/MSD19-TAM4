package org.app.service.ejb;

import java.util.Collection;

import javax.ejb.Remote;



import org.app.service.entities.Internship;

@Remote
public interface InternshipDataService {

	// CREATE or UPDATE
		Internship addInternship(Internship internshipToAdd);

		// DELETE
		String removeInternship(Internship internshipToDelete);

		// READ
		Internship getInternshipByID(Integer IdInternship);
		Collection<Internship> getInternships();
		
		// Custom READ: custom query
		Internship getInternshipByDenumire(String denumireInternship);
		
		// Others
		String getMessage();
}
