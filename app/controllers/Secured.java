package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

import models.User;

public class Secured extends Security.Authenticator {

	@Override
	public String getUsername(Context ctx) {
		return getUserIdString(ctx);
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.SignTools.renderSignUpError("Oups you should be connected to access this page"));
	}

	public static User connectedUser(Context ctx) {
		return User.find.byId(Long.parseLong(getUserIdString(ctx)));
	}

	private static String getUserIdString(Context ctx) {
		return ctx.session().get(SignTools.USER_S);
	}

	public static Long getUserIdLong(Context ctx) {
		return Long.parseLong(getUserIdString(ctx));
	}

}