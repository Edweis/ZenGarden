package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security.Authenticated;

import models.User;

@Authenticated(Secured.class)
public class ProfileTools extends Controller {

	public Result index() {
		Long connectedUserId = Secured.getUserIdLong(ctx());
		return display(connectedUserId);
	}

	public Result display(Long userId) {
		User u = User.find.byId(userId);

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
