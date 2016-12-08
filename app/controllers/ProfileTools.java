package controllers;

import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security.Authenticated;

import com.google.inject.Inject;

import models.Country;
import models.Education;
import models.School;
import models.User;

@Authenticated(Secured.class)
public class ProfileTools extends Controller {

	private FormFactory ff;

	@Inject
	public ProfileTools(FormFactory ff) {
		this.ff = ff;
	}

	public Result index() {
		Long connectedUserId = Secured.getUserIdLong(ctx());
		return display(connectedUserId);
	}

	/**
	 * AJAX action that add an education.
	 * 
	 * @return the education panel to re-render via javascript.
	 */
	public Result addEducation() {
		Education e;
		try {
			e = getEducationFromRequest();
			e.getSchool().save();
			e.save();
			Logger.debug("Education added :\n\t" + e);
			return educationAJAXReturn();
		} catch (AskForNewRequestResultException re) {
			return re.getResult();
		}
	}

	/**
	 * AJAX action that edit an education.
	 * 
	 * @return
	 */
	public Result editEducation(Long educationId) {
		try {
			// check if its his
			Education ee = hasRightOnEducation(educationId);

			Education e = getEducationFromRequest();

			// replace
			// TODO to be tested
			e.setId(ee.getId());
			e.update();

			Logger.debug("Education updated :\n\tFrom : " + ee + "\n\tTo : " + e);
			return educationAJAXReturn();
		} catch (AskForNewRequestResultException re) {
			return re.getResult();
		}
	}

	public Result deleteEducation(Long educationId) {
		try {
			Education ee = hasRightOnEducation(educationId);
			ee.delete();
			Logger.debug("Education deleted :\n\t" + ee);
			return educationAJAXReturn();
		} catch (AskForNewRequestResultException e) {
			return e.getResult();
		}
	}

	private Education getEducationFromRequest() throws AskForNewRequestResultException {
		Form<Education.Builder> ef = ff.form(Education.Builder.class).bindFromRequest();
		Form<School.Builder> es = ff.form(School.Builder.class).bindFromRequest();

		if (ef.hasErrors()) {
			throw new AskForNewRequestResultException(Results.badRequest(ef.globalError().message()));
		}
		if (es.hasErrors()) {
			throw new AskForNewRequestResultException(Results.badRequest(es.globalError().message()));
		}

		// submit
		User u = Secured.connectedUser(ctx());
		School s = es.get().generate();
		Education e = ef.get().generate(u, s);

		return e;

	}

	private Education hasRightOnEducation(Long educationId) throws AskForNewRequestResultException {
		Education e = Education.find.byId(educationId);
		if (e == null) {
			throw new AskForNewRequestResultException(Results.notFound("education id: " + educationId));
		}
		User cu = Secured.connectedUser(ctx());
		if (!e.getUser().equals(cu)) {
			throw new AskForNewRequestResultException(Results.unauthorized());
		}

		return e;
	}

	private Result educationAJAXReturn() {
		User u = Secured.connectedUser(ctx());

		return ok(views.html.inc.profile.educationPanel.render(u.myEducation, true));
	}

	public Result display(Long userId) {
		User u = User.find.byId(userId);

		School s = School.find.byId((long) 1);
		Country c = Country.find.byId(1L);

		if (u == null) {
			return Results.notFound("User not found");
		} else {
			// Is it your profile ?
			if (u.Id == Secured.getUserIdLong(ctx())) {
				return ok(views.html.pages.profile.render(u, true));
			} else {
				return ok(views.html.pages.profile.render(u, false));
			}
		}

	}

}
