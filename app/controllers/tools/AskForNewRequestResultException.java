package controllers.tools;

import play.mvc.Result;

/**
 * Exception that occur when an error occur in an process and needs to redirect
 * the user. <br/>
 * For instance the user tries to edit an Education that is not yours via a fake
 * form or an injection, the action has to stop to redirect the guys.
 * 
 * @author piou
 *
 */
public class AskForNewRequestResultException extends Exception {

	private static final long serialVersionUID = 1L;

	private Result result;

	/**
	 * Set the result to be return by the calling action.
	 * 
	 * @param result
	 */
	public AskForNewRequestResultException(Result result) {
		this.result = result;
	}

	/**
	 * Return the result to be returned by the action.
	 * 
	 * @return
	 */
	public Result getResult() {
		return result;
	}

}
