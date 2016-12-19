package controllers.tools;

import play.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import models.Country;
import models.Education;
import models.Experience;
import models.Scholarship;
import models.School;
import models.User;
import models.Work;
import models.tools.Search;
import models.tools.SearchField;
import models.tools.SearcheableField;
import net.sf.ehcache.config.Searchable;

/**
 * Contains global values that needs to be initialized on startup.
 * 
 * @author piou
 *
 */
@Singleton
public class Global {

	/**
	 * Classes in witch we will make the search. Theses classes should contain
	 * field annotated with {@link Searchable}.
	 */
	private final static Class<?>[] ALL_ENTITY_CLASS = { User.class, Education.class, Scholarship.class, Work.class,
			Country.class, Experience.class, School.class };

	/**
	 * Represents all searcheable field. Initiated in constructor. TODO: How can
	 * I run this ONLY ONCE and not every time someone instantiate a Search
	 * object ?
	 */
	public static List<SearchField> ALL_SEARCHEABLE_FIELDS;

	public Global() {
		ALL_SEARCHEABLE_FIELDS = initSearchableFields();
		Logger.info("Searcheble fields loaded. Size : " + ALL_SEARCHEABLE_FIELDS.size());
	}

	/**
	 * Finds from {@link SEARCH_CLASS} all search fields, with are the fields
	 * annotated with {@link SearchField}. It looks in all classes manually
	 * input in {@link Search#ALL_ENTITY_CLASS}.
	 * 
	 * @return
	 */
	private List<SearchField> initSearchableFields() {
		ArrayList<SearchField> res = new ArrayList<SearchField>();

		// Generate filter for all these class
		for (Class<?> c : ALL_ENTITY_CLASS) {

			for (Field f : c.getDeclaredFields()) {
				if (f.isAnnotationPresent(SearcheableField.class)) {
					res.add(new SearchField(f));
				}
			}
		}
		return res;
	}
}
