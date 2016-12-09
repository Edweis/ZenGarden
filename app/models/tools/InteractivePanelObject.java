package models.tools;

import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Results;

import com.avaje.ebean.Model;

import controllers.tools.AskForNewRequestResultException;
import models.User;

public interface InteractivePanelObject<T extends Model & UserBelonging> {

	/**
	 * Action that edit the object with the one sent with the request. </br>
	 * 
	 * TODO think about : should delete happen before or after insert ?
	 *
	 * TODO the object is deleted and a new on is created, it would be better in
	 * term of request savings to only update the current object.
	 * 
	 * @param objectId
	 * @return
	 */
	public default Result editObject(Long objectId, User user) {
		try {
			T object = checkId(objectId);
			T currentObject = hasRight(object, user);
			T newObject = getFromRequest();

			currentObject.delete();
			newObject.insert();

			Logger.debug("InteractiveUserObject updated :\n\tFrom : " + currentObject + "\n\tTo : " + newObject);
			return objectAJAXReturn();
		} catch (AskForNewRequestResultException re) {
			return re.getResult();
		}
	}

	/**
	 * Action that delete the object.
	 * 
	 * @param objectId
	 * @return
	 */
	public default Result removeObject(Long objectId, User user) {
		try {
			T object = checkId(objectId);
			T currentObject = hasRight(object, user);
			currentObject.delete();

			Logger.debug("InteractiveUserObject deleted :\n\t" + currentObject);
			return objectAJAXReturn();
		} catch (AskForNewRequestResultException re) {
			return re.getResult();
		}
	}

	/**
	 * Action that add the object send in the request.
	 * 
	 * @return
	 */
	public default Result addObject() {
		try {
			T currentObject = getFromRequest();
			currentObject.insert();

			Logger.debug("InteractiveUserObject added :\n\t" + currentObject);
			return objectAJAXReturn();
		} catch (AskForNewRequestResultException re) {
			return re.getResult();
		}
	}

	/**
	 * Get the object created send by the user in the POST request. </br>
	 * You can use {@link InteractivePanelObject#checkFormFromRequest(Form)} to
	 * check your form(s).
	 * 
	 * @return
	 * @throws AskForNewRequestResultException
	 */
	abstract T getFromRequest() throws AskForNewRequestResultException;

	/**
	 * Return the object thanks to its id.
	 * 
	 * @param objectId
	 * @return
	 * @throws AskForNewRequestResultException
	 *             if not found.
	 */
	default T checkId(Long objectId) throws AskForNewRequestResultException {
		T res = findById(objectId);
		if (res == null) {
			throw new AskForNewRequestResultException(Results.notFound("id not found"));
		} else {
			return res;
		}
	}

	/**
	 * Return an object thanks to its id. Return null if not found.
	 * 
	 * @param objectId
	 * @return the object
	 */
	abstract T findById(Long objectId);

	/**
	 * Throws exception on error in the form.
	 * 
	 * @param form
	 * @throws AskForNewRequestResultException
	 *             on error
	 */
	default void checkFormFromRequest(Form<?> form) throws AskForNewRequestResultException {
		if (form.hasErrors()) {
			throw new AskForNewRequestResultException(Results.badRequest(form.globalError().message()));
		}
	}

	/**
	 * Return an HTTP 200 result with what should be handle by the front end.
	 * 
	 * @return
	 */
	public Result objectAJAXReturn();

	/**
	 * Check if the user has the right to alter this object.
	 * 
	 * @param object
	 * @param user
	 * @return
	 * @throws AskForNewRequestResultException
	 */
	default T hasRight(T object, User user) throws AskForNewRequestResultException {
		if (object.hasRight(user)) {
			return object;
		} else {
			throw new AskForNewRequestResultException(Results.unauthorized());
		}
	}

}
