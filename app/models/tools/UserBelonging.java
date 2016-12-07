package models.tools;

import models.User;

/**
 * Interface that implements methods that are used to protect the class from
 * external CRUD actions. It means that some classes will need your class to
 * implements this one.
 * 
 * @author piou
 *
 */
public interface UserBelonging {
	/**
	 * return true if the user user has the right to alter the object.
	 * 
	 * @param user
	 * @return
	 */
	public abstract boolean hasRight(User user);
}
