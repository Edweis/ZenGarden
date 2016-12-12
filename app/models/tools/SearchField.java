package models.tools;

import java.lang.reflect.Field;
import java.util.Set;

import controllers.tools.WrongConstructionException;
import models.User;

public class SearchField {
	private Field field;

	public SearchField(String fieldInString) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * String that will be displayed in the get request. TODO : improve and
	 * check.
	 */
	@Override
	public String toString() {
		return formatFetchPathForEbean();
	}

	public SearchField(Field field) {
		this.field = field;
	}

	public Set<User> search(String query) {
		String path = formatFetchPathForEbean();
		Set<User> res = User.find.where().like(path, "%" + query + "%").findSet();
		return res;
	}

	/**
	 * Format the {@link SearcheableField#userFetchPath()} to be used in
	 * {@link com.avaje.ebean.ExpressionList#eq(String, Object)}.<br/>
	 * <p>
	 * For example the field <tt>Name</tt> in <tt>School</tt> annotated with :
	 * <br/>
	 * <tt>User.myEducation.School</tt><br/>
	 * will be format into : <br/>
	 * <tt>myEducation.School.Name</tt>
	 * </p>
	 * 
	 * TODO : Check annotation at compile time and not at very single search ...
	 * 
	 * @return
	 */
	private String formatFetchPathForEbean() {

		// construction debug
		String className = field.getDeclaringClass().getSimpleName();
		String fieldName = field.getName();
		String path = field.getAnnotation(SearcheableField.class).userFetchPath();

		if (!path.startsWith("User") || !path.endsWith(className)) {
			throw new WrongConstructionException(
					"The path '" + path + "' from the field '" + fieldName + "' in the class '" + className
							+ "' doesn't starts with 'User' or doesn't ends with '" + className + "'");
		}

		// Remove the "User[.]" at the beginning
		path = path.replace("User.", "");
		path = path.replace("User", "");

		// Add the field name
		path = path + "." + field.getName();

		return path;
	}

	/**
	 * return the value of the field for a specific user
	 * 
	 * @param user
	 * @return
	 */
	public String value(User user) {
		try {
			return field.get(user).toString();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Generate the JPQL request from the
	 * {@link SearcheableField#userFetchPath()}. For example : </br>
	 * for <tt>fetchPath</tt> = <tt>user.myEducation.School.Country</tt></br>
	 * it will return </br>
	 * <tt>SELECT x FROM User x JOIN x.myEducation a JOIN a.School b JOIN b.Country c WHERE c.?1</tt>
	 * Don't forget to do .addParameter(1, "something") on the query.
	 * 
	 * USE {@link SearchField#formatFetchPathForEbean()} with Ebean instead.
	 * JPQL not needed anymore.
	 * 
	 * @param fld
	 * @return
	 */
	@Deprecated
	private String generateJPQLQueryFromField(Field fld) {
		String res = "SELECT x FROM User x ";

		String className = fld.getDeclaringClass().getSimpleName();
		String userFetchPath = fld.getAnnotation(SearcheableField.class).userFetchPath();
		String fieldName = fld.getName();

		if (!userFetchPath.startsWith("User") || !userFetchPath.endsWith(className)) {
			throw new WrongConstructionException(
					"The path '" + userFetchPath + "' from the field '" + fieldName + "' in the class '" + className
							+ "' doesn't starts with 'User' or doesn't ends with '" + className + "'");
		}

		String[] stones = userFetchPath.split("\\.");
		String[] argsName = { "x", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j" };
		if (stones.length > 10) {
			throw new WrongConstructionException(
					"The path '" + userFetchPath + "' from the field '" + fieldName + "' in the class '" + className
							+ "' should have a path shorter than 10 table, come look t the code bro.");
		}
		for (int i = 1; i < stones.length; i++) {
			res = res + "JOIN " + argsName[i - 1] + "." + stones[i] + " AS " + argsName[i] + " ";
		}

		res = res + "WHERE " + argsName[stones.length - 1] + "." + fieldName + " LIKE CONCAT('%', ?1, '%')";

		return res;
	}

}