package controllers;

import play.Logger;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security.Authenticated;

import com.google.inject.Inject;

import models.Country;
import models.School;
import models.User;
import models.tools.InteractivePanelObject;

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
	 * Action on a panel object. It should look like : <br/>
	 * <tt>action/clazz[/id]</tt><br/>
	 * for instance : <br/>
	 * <tt>edit/education/4523</tt>
	 * 
	 * @param path
	 * @return
	 */
	public Result actionOnPanelObject(String action, String clazz, Long id) {

		User connectedUser = Secured.connectedUser(ctx());
		InteractivePanelObject<?> ipo;
		switch (clazz) {
		case "education":
			ipo = new InteractivePanel(connectedUser, ff).new InteractiveEducation();
			break;
		case "scholarship":
			ipo = new InteractivePanel(connectedUser, ff).new InteractiveScholarship();
			break;
		case "experience":
			ipo = new InteractivePanel(connectedUser, ff).new InteractiveExperience();
			break;
		case "work":
			ipo = new InteractivePanel(connectedUser, ff).new InteractiveWork();
			break;

		default:
			return badRequest("class not recognized");
		}
		Logger.warn("in remove section, good");

		switch (action) {
		case "add":
			return ipo.addObject();
		case "remove":
			if (id == 0) {
				return badRequest("bad id");
			}
			return ipo.removeObject(id, connectedUser);
		case "edit":
			if (id == 0) {
				return badRequest("bad id");
			}
			return ipo.editObject(id, connectedUser);

		default:
			return badRequest("action not recognized");
		}

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
