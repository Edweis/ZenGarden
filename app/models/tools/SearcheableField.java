package models.tools;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This tag should only be put on a class that : </br>
 * <ul>
 * <li>is a String</li>
 * <li>is linked to a database column</li>
 * <li>has getter and setter</li>
 * </ul>
 * All field tagged with @{@link SearcheableField} will be searchable in
 * <tt>search.xhtml</tt>.
 * 
 * @author François Rullière
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface SearcheableField {
	/**
	 * Label that represent the entity searched. It will be used as a filter.
	 * <p>
	 * For instance all fields labeled with <tt>Education</tt> will all be used
	 * as {@link SearchField}s in a {@link Search} instance when performing the
	 * search.
	 * </p>
	 * Several fields can ths have the same label.
	 * 
	 * @return
	 */
	String label() default "";

	/**
	 * Link the user to the class, it has to start with <tt>User</tt> and end
	 * with a Field with a type of the current class. For instance for the
	 * <tt>Country</tt> class : </br>
	 * <tt>User.myEducation.School.Country</tt> or
	 * <tt>User.Nationality</tt></br>
	 * It seams that all @{@link SearcheableField} fields from the same class
	 * will have the same value of <tt>userFetchPath</tt>.
	 * 
	 * @return
	 */
	String userFetchPath();
}
