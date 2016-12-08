package controllers;

import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import com.google.inject.Inject;

import controllers.SignTools.LoginUser;
import models.User;

public class SignTools extends Controller {

	/**
	 * Session variable that corresponds to the connected user id.
	 */
	public static final String USER_S = "connectedUser";
	private FormFactory ff;

	@Inject
	public SignTools(FormFactory ff) {
		this.ff = ff;
	}

	/**
	 * Render the Sign In page
	 * 
	 * @return
	 */
	public Result renderSignIn() {
		return ok(views.html.pages.signin.render(ff.form(User.Builder.class)));
	}

	/**
	 * Return the Sign In page with an error message
	 * 
	 * @param errorMessage
	 * @return
	 */
	public Result renderSignUpError(String errorMessage) {
		return ok(views.html.pages.signup.render(ff.form(LoginUser.class), errorMessage));
	}

	/**
	 * Render the Sign Up page
	 * 
	 * @return
	 */
	public Result renderSignUp() {
		return ok(views.html.pages.signup.render(ff.form(LoginUser.class), null));
	}

	/**
	 * Authenticate from an email and password. Store the User.Id in session.
	 * Return true if succeeded, false instead.
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	private static boolean authenticate(String email, String password) {
		User u = User.authenticate(email, password);
		if (u != null) {
			Controller.session().clear();
			Controller.session(USER_S, u.Id.toString());
			return true;
		} else {
			return false;
		}
	}

	public Result logout() {
		session().clear();
		Logger.debug("Logged out");
		return redirect(routes.MainTools.home());
	}

	/**
	 * Sign In action
	 * 
	 * @return
	 */
	public Result signIn() {
		Form<User.Builder> siForm = ff.form(User.Builder.class).bindFromRequest();

		if (siForm.hasErrors()) {
			return Results.badRequest(views.html.pages.signin.render(siForm));
		} else {
			User.Builder nu = siForm.get();
			nu.generate();

			SignTools.authenticate(nu.email, nu.password);
			return redirect(routes.MainTools.home());
		}

	}

	/**
	 * Sign Up action
	 * 
	 * @return
	 */
	public Result signUp() {
		Form<LoginUser> suForm = ff.form(LoginUser.class).bindFromRequest();

		if (suForm.hasErrors()) {
			return Results.badRequest(views.html.pages.signup.render(suForm, null));
		} else {
			authenticate(suForm.get().email, suForm.get().password);
			return redirect(routes.MainTools.home());
		}

	}

	public static class LoginUser {
		@Constraints.Required
		public String email;
		@Constraints.Required
		public String password;

		public String validate() {
			if (User.authenticate(email, password) != null) {
				return null;
			} else {
				return "Email or password incorrect";
			}
		}
	}

}
