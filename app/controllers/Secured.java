package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

import models.User;

public class Secured extends Security.Authenticator {

	/**
	 * Session variable that corresponds to the connected user id.
	 */
	public static final String USER_S = "connectedUser";

	/**
	 * Return the Id of the user as a string.</br>
	 * I also check in the database that the user actually exists. It is not
	 * supposed to happened in production but in development after a database
	 * clean up it can happen.
	 */
	@Override
	public String getUsername(Context ctx) {
		User u = User.find.select("Id").where().idEq(getUserIdLong(ctx)).findUnique();
		if (u == null) {
			return null;
		} else {
			return u.getId().toString();
		}
	}

	/**
	 * Redirect the user to the home page if he is not conected.
	 */
	@Override
	public Result onUnauthorized(Context ctx) {
		// TODO : How to put unauthorized instead of redirect?
		return redirect(routes.SignTools.renderSignUpError("Oups you should be connected to access this page"));
	}

	/**
	 * Get the user connected. His Id is specified in a cookie with name :
	 * {@linkplain Secured#USER_S}. </br>
	 * 
	 * @param ctx
	 * @return
	 */
	public static User connectedUser(Context ctx) {
		Long id = getUserIdLong(ctx);
		if (id != null) {
			return User.find.byId(id);
		} else {
			return null;
		}
	}

	/**
	 * Return the connected user Id as a String.
	 * 
	 * @param ctx
	 * @return
	 */
	private static String getUserIdString(Context ctx) {
		return ctx.session().get(Secured.USER_S);
	}

	/**
	 * Return the connected user Id as a Long.
	 * 
	 * @param ctx
	 * @return
	 */
	public static Long getUserIdLong(Context ctx) {
		String id = getUserIdString(ctx);
		if (id != null) {
			return Long.parseLong(id);
		} else {
			return null;
		}
	}

}