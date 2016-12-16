package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

import models.User;

public class Secured extends Security.Authenticator {

	/**
	 * TODO : I don't need to fetch the user every time, this might take some
	 * unnecessary computing time.
	 */
	@Override
	public String getUsername(Context ctx) {
		User u = connectedUser(ctx);
		if (u == null) {
			return null;
		} else {
			return u.toString();
		}
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		// TODO : How to put unauthorized instead of redirect?
		return redirect(routes.SignTools.renderSignUpError("Oups you should be connected to access this page"));
	}

	public static User connectedUser(Context ctx) {
		Long id = getUserIdLong(ctx);
		if (id != null) {
			return User.find.byId(id);
		} else {
			return null;
		}
	}

	private static String getUserIdString(Context ctx) {
		return ctx.session().get(SignTools.USER_S);
	}

	public static Long getUserIdLong(Context ctx) {
		String id = getUserIdString(ctx);
		if (id != null) {
			return Long.parseLong(id);
		} else {
			return null;
		}
	}

}