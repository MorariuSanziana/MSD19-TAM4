package org.app.service.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import org.app.service.entities.Member;
import org.app.service.entities.Task;


@Remote
public interface MemberDataService {

	// CREATE or UPDATE
			Member addMember(Member memberToAdd);

			// DELETE
			String removeMember(Member memberToDelete);

			// READ
			Member getMemberByID(Integer idMembru);
			Collection<Member> getMembers();
			
			// Custom READ: custom query
			Member getMemberByNume(String numeMembru);
			
			// Others
			String getMessage();
			Collection<Member> toCollection();
			
			
			Member createNewMember(Integer id);

			Collection<Member> removeFromCollection(Member member);
}
