package org.scrum.client.controllers;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class MenuViewController {
	private static final long serialVersionUID = 1L;

	public void showSprints() {
        addMessage("Show sprints feature: unde construction!", "TO DO!");
    }
	
	public void showFilterableProjects() {
		addMessage("Show filterable projects to edit feature: unde construction!", "TO DO!");
	}
	
	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}

// http://localhost:8080/MSD.WebClient
// http://localhost:8080/MSD.WebClient/welcome.xhtml
// http://localhost:8080/MSD.WebClient/forms/ProjectListEditForm.xhtml
// http://localhost:8080/MSD.WebClient/forms/ProjectEditForm.xhtml
// http://localhost:8080/MSD.WebClient/forms/ProjectsListForm.xhtml

// https://www.primefaces.org/showcase/ui/data/datatable/summaryRow.xhtml
// https://erdemgunayjavaworks.wordpress.com/2012/08/10/a-sample-template-driven-web-page-with-primefaces/
// https://www.mkyong.com/jsf2/jsf-2-templating-with-facelets-example/
// http://www.theserverside.com/tutorial/Creating-pages-based-on-a-JSF-template-Using-the-Facelets-uidefine-tag

// PrimeFaces Blueprint: Template tags: page 63:
// https://github.com/sudheerj/primefaces-blueprints
// https://api.jqueryui.com/resources/icons-list.html

