package controllers;

import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;
import play.mvc.Results;

import models.Education;
import models.Experience;
import models.Funding;
import models.Scholarship;
import models.School;
import models.User;
import models.Work;
import models.WorkCursus;
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
			Education e = fe.get().generate(connectedUser, s);

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

	public class InteractiveScholarship implements InteractivePanelObject<Funding> {

		@Override
		public Funding getFromRequest() throws AskForNewRequestResultException {
			Form<Funding.Builder> fun = ff.form(Funding.Builder.class).bindFromRequest();
			Form<Scholarship.Builder> sch = ff.form(Scholarship.Builder.class).bindFromRequest();
			this.checkFormFromRequest(fun);
			this.checkFormFromRequest(sch);

			Scholarship s = sch.get().generate();
			Funding f = fun.get().generate(connectedUser, s);
			return f;
		}

		@Override
		public Funding findById(Long objectId) {
			return Funding.find.byId(objectId);
		}

		@Override
		public Result objectAJAXReturn() {
			return Results.ok(views.html.inc.profile.scholarshipPanel.render(connectedUser.myFunding, true));
		}

	}

	public class InteractiveExperience implements InteractivePanelObject<Experience> {

		@Override
		public Experience getFromRequest() throws AskForNewRequestResultException {
			Form<Experience.Builder> exp = ff.form(Experience.Builder.class).bindFromRequest();
			this.checkFormFromRequest(exp);

			return exp.get().generate(connectedUser);
		}

		@Override
		public Experience findById(Long objectId) {
			return Experience.find.byId(objectId);
		}

		@Override
		public Result objectAJAXReturn() {
			return Results.ok(views.html.inc.profile.experiencePanel.render(connectedUser.myExperience, true));
		}

	}

	public class InteractiveWork implements InteractivePanelObject<WorkCursus> {

		@Override
		public WorkCursus getFromRequest() throws AskForNewRequestResultException {
			Form<WorkCursus.Builder> fwc = ff.form(WorkCursus.Builder.class).bindFromRequest();
			Form<Work.Builder> fw = ff.form(Work.Builder.class).bindFromRequest();
			this.checkFormFromRequest(fw);
			this.checkFormFromRequest(fwc);

			Work w = fw.get().generate();
			WorkCursus wc = fwc.get().generate(connectedUser, w);
			Logger.warn(w.toString());
			Logger.warn(wc.toString());
			return wc;
		}

		@Override
		public WorkCursus findById(Long objectId) {
			return WorkCursus.find.byId(objectId);
		}

		@Override
		public Result objectAJAXReturn() {
			return Results.ok(views.html.inc.profile.workPanel.render(connectedUser.myWorkcursus, true));
		}

	}

}
