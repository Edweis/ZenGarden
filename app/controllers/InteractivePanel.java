package controllers;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;
import play.mvc.Results;

import models.Education;
import models.School;
import models.User;
import models.tools.InteractivePanelObject;

public class InteractivePanel {

	private User connectedUser;
	private FormFactory ff;

	public InteractivePanel(User connectedUser, FormFactory ff) {
		this.connectedUser = connectedUser;
		this.ff = ff;
	}

	public class InteractiveEducation implements InteractivePanelObject<Education> {

		@Override
		public Education getFromRequest() throws AskForNewRequestResultException {
			Form<Education.Builder> fe = ff.form(Education.Builder.class).bindFromRequest();
			Form<School.Builder> fs = ff.form(School.Builder.class).bindFromRequest();
			this.checkFormFromRequest(fe);
			this.checkFormFromRequest(fs);

			School s = fs.get().generate();
			User u = connectedUser;
			Education e = fe.get().generate(u, s);

			return e;
		}

		@Override
		public Result objectAJAXReturn() {
			return Results.ok(views.html.inc.profile.educationPanel.render(connectedUser.myEducation, true));
		}

		@Override
		public Education findById(Long objectId) {
			return Education.find.byId(objectId);
		}

	}
}
