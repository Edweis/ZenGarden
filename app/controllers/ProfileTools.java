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
	 * Edit a field of the connected user. Field should be parse like this :
	 * <br/>
	 * TODO doc and method
	 * 
	 * @param field
	 * @return
	 */
	@Deprecated
	public Result edit(String path) {
		String[] s = path.split("/");

		// Split the path - can be done in routing
		String clazz;
		Long id;
		String field;
		try {
			clazz = s[0];
			id = Long.parseLong(s[1]);
			field = s[2];
		} catch (ArrayIndexOutOfBoundsException e) {
			return badRequest("The request should have 3 parameters");
		} catch (NumberFormatException e) {
			return badRequest("The second parameter should be a Long");
		}

		// cases
		User cu = Secured.connectedUser(ctx());
		return null;
	}

	/**
	 * AJAX action that add an education.
	 * 
	 * @return the education panel to re-render via javascript.
	 */
	public Result addEducation() {
		Form<Education.Builder> ef = ff.form(Education.Builder.class).bindFromRequest();
		Form<School.Builder> es = ff.form(School.Builder.class).bindFromRequest();

		if (ef.hasErrors()) {
			Logger.error(ef.errors().keySet().toString());
			Logger.error(ef.errors().values().toString());
			return Results.badRequest(ef.globalError().message());
		}
		if (es.hasErrors()) {
			Logger.error(es.errors().keySet().toString());
			Logger.error(es.errors().values().toString());
			return Results.badRequest(es.globalError().message());
		}

		// submit
		User u = Secured.connectedUser(ctx());
		School s = es.get().generate();
		Education e = ef.get().generate(u, s);
		s.save();
		e.save();

		Logger.debug("Education added !", e);
		u.refresh();
		return ok(views.html.inc.profile.educationPanel.render(u.myEducation, true));
	}

	/**
	 * AJAX action that edit an education.
	 * 
	 * @return
	 */
	public Result editEducation(Long educationId) {
		// check if its his
		Education ee = Education.find.byId(educationId);
		if (ee == null) {
			return Results.notFound("education id: " + educationId);
		}
		User cu = Secured.connectedUser(ctx());
		if (!ee.getUser().equals(cu)) {
			return Results.unauthorized();
		}

		// Check the form
		Form<Education.Builder> ef = ff.form(Education.Builder.class).bindFromRequest();
		Form<School.Builder> es = ff.form(School.Builder.class).bindFromRequest();

		if (ef.hasErrors()) {
			return Results.badRequest(ef.globalError().message());
		}
		if (es.hasErrors()) {
			return Results.badRequest(es.globalError().message());
		}

		// Submit
		es.get().replace(ee.getSchool());
		ef.get().replace(ee).update();

		// display
		cu.refresh();
		return ok(views.html.inc.profile.educationPanel.render(cu.myEducation, true));

	}

	public Result deleteEducation(Long educationId) {
		// check if its his
		Education ee = Education.find.byId(educationId);
		Logger.info("deleting education " + educationId + " ...");
		if (ee == null) {
			return Results.notFound("education id: " + educationId);
		}
		User cu = Secured.connectedUser(ctx());
		if (!ee.getUser().equals(cu)) {
			return Results.unauthorized();
		}
		// process
		ee.delete();
		cu.refresh();
		return ok(views.html.inc.profile.educationPanel.render(cu.myEducation, true));

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
