package controllers.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.avaje.ebean.Ebean;

import models.Country;
import models.Education;
import models.Experience;
import models.Scholarship;
import models.School;
import models.User;
import models.Work;
import models.tools.SearcheableField;
import net.sf.ehcache.config.Searchable;

public class Search {

	/**
	 * Classes in witch we will make the search. Theses classes should contain
	 * field annotated with {@link Searchable}.
	 */
	private final static Class<?>[] ALL_ENTITY_CLASS = { User.class, Education.class, Scholarship.class, Work.class,
			Country.class, Experience.class, School.class };

	private List<String> queries;
	private List<SearchField> filters;

	/**
	 * Set the queries of the search
	 * 
	 * @param queries
	 */
	public void setQueries(String[] queries) {
		this.queries = Arrays.asList(queries);
	}

	/*
	 * Set the filters of the search
	 */
	public void setFilters(String[] filters) {
		this.filters = new ArrayList<SearchField>();
		for (int i = 0; i < filters.length; i++) {
			this.filters.add(new SearchField(filters[i]));
		}
	}

	/**
	 * Process the research with queries ad filters input. If queries hasn't
	 * been defined all User will be displayed. If filters hasn't bn defined
	 * every SearchField will be interpreted.
	 * 
	 * @return
	 */
	public List<SearchResult> process() {

		List<SearchField> fieldToProcess;

		// initiate fieldToProcess
		if (filters == null || filters.isEmpty()) {
			fieldToProcess = initSearchableFields();
		} else {
			fieldToProcess = filters;
		}

		// search in every field user
		SearchResultFactory factory = new SearchResultFactory();

		for (String q : queries) {
			for (SearchField f : fieldToProcess) {
				factory.process(q, f);
			}
		}

		// extract results
		return factory.extractResults();
	}

	/**
	 * Finds from {@link SEARCH_CLASS} all search fields, with are the fields
	 * annotated with {@link SearchField}.
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
			return field.toString();
		}

		public SearchField(Field field) {
			this.field = field;
		}

		public Set<User> search(String query) {
			String jpqlQuery = generateQueryFromSearchableField(field);
			return Ebean.createQuery(User.class, jpqlQuery).setParameter(1, query).findSet();
		}

	}

	/**
	 * Factory that creates {@link SearchResult}s. It can be seen as a Set of
	 * {@link SearchResult}s that is updated after each
	 * {@link SearchResultFactory#process(String, SearchField)}. All
	 * SearchResults have a unique USer associated.
	 * 
	 * @author piou
	 *
	 */
	private class SearchResultFactory {

		/**
		 * Store SearchResult, I used a Map so it is "kinda" easier to know if
		 * the user has already been found earlier by using containsKey() on it.
		 */
		private Map<User, SearchResult> storage;

		public SearchResultFactory() {
			storage = new HashMap<User, SearchResult>();
		}

		/**
		 * Extract the results from after all processes.
		 * 
		 * @return
		 */
		public List<SearchResult> extractResults() {
			return (List<SearchResult>) storage.values();
		}

		/**
		 * Research the query in the SearchField for all user in the database
		 * and store results in the factory.
		 * 
		 * @param query
		 * @param place
		 */
		public void process(String query, SearchField place) {
			// render all user that has query in their place
			Set<User> found = place.search(query);

			// update the storage
			for (User u : found) {
				if (storage.containsKey(u)) {
					storage.get(u).addMatching(query, place);
				} else {
					storage.put(u, new SearchResult(u, query, place));
				}
			}

		}

	}

	/**
	 * Represents a result that has :
	 * <ul>
	 * <li>a {@link User} user that should be displayed.</li>
	 * <li>a <tt> Map<String, SearchField><//t> matching that associate one
	 * query to one specific search field.</li>
	 * <li>a Matching Rank that corresponds to the number of matching the User
	 * had (the size of the matching map actually). The higher the rank the
	 * better the result.</li>
	 * </ul>
	 * 
	 * @author piou
	 *
	 */
	public class SearchResult {
		private User user;
		private Map<String, SearchField> matching;

		public SearchResult(User user, String query, SearchField place) {
			this.user = user;
			matching = new HashMap<String, SearchField>();
			addMatching(query, place);
		}

		public void addMatching(String query, SearchField place) {
			matching.put(query, place);
		}

		public User getUser() {
			return user;
		}

		public int getMathingRank() {
			return matching.size();
		}

		public Map<String, SearchField> getMatching() {
			return matching;
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
	 * @param fetchPath
	 * @return
	 */
	private static String generateQueryFromSearchableField(Field field) {
		String res = "SELECT x FROM User x ";

		String className = field.getDeclaringClass().getSimpleName();
		String userFetchPath = field.getAnnotation(SearcheableField.class).userFetchPath();
		String fieldName = field.getName();

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
