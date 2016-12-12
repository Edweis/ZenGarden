package models.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Country;
import models.Education;
import models.Experience;
import models.Scholarship;
import models.School;
import models.User;
import models.Work;
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
	 * Set the queries of the search. If null queries is just an empty array.
	 * 
	 * @param queries
	 */
	public void setQueries(String[] queries) {
		this.queries = queries == null ? new ArrayList<String>() : Arrays.asList(queries);
	}

	/*
	 * Set the filters of the search. If null filter is just an empty array.
	 */
	public void setFilters(String[] filters) {
		this.filters = new ArrayList<SearchField>();
		if (filters != null) {
			for (int i = 0; i < filters.length; i++) {
				if (!"".equals(filters[i])) {
					this.filters.add(new SearchField(filters[i]));
				}
			}
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
		if (filters.isEmpty()) {
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

			return new ArrayList<SearchResult>(storage.values());
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

			// update the storage, I use this shitty code and not
			// .containsKey(u) because containsKey doesn't work.
			// TODO : find out why
			User contains;
			for (User u : found) {

				contains = null;
				for (Iterator<User> i = storage.keySet().iterator(); i.hasNext();) {
					User s = (User) i.next();
					if (s.equals(u)) {
						contains = s;
						break;
					}
				}

				if (contains != null) {// storage.containsKey(u)
					storage.get(contains).addMatching(query, place);
				} else {
					storage.put(u, new SearchResult(u, query, place));
				}
			}

		}

	}

}
