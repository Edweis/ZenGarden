package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import models.User;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class MainTools extends Controller {

	/**
	 * Render the Home page
	 * 
	 * @return
	 */
	public Result home() {
		return ok(views.html.pages.home.render(User.find.all()));
	}

}
