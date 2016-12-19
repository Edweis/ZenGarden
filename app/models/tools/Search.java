package models.tools;

import play.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controllers.tools.Global;
import models.User;

public class Search {

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
	 * Set the filters of the search. If null, filter is just will be an empty
	 * array.
	 */
	public void setFilters(String[] filters) {
		this.filters = new ArrayList<SearchField>();
		if (filters != null) {
			// there are some fields input
			for (int i = 0; i < filters.length; i++) {
				// for each of them
				if (!"".equals(filters[i])) {
					// if they are not empty
					boolean found = false;
					for (SearchField f : getFieldsFromLabel(filters[i])) {
						// we add all filters that are labeled with that name
						this.filters.add(f);
						found = true;
					}
					if (!found) {
						Logger.warn("field : " + filters[i] + " hasn't been found");
					}
				}
			}
		}
	}

	/**
	 * Return the list of SearchFields with the label <tt>fieldLabel</tt>
	 * 
	 * @param fieldLabel
	 * @return
	 */
	private List<SearchField> getFieldsFromLabel(String fieldLabel) {
		ArrayList<SearchField> res = new ArrayList<SearchField>();
		for (SearchField sf : Global.ALL_SEARCHEABLE_FIELDS) {
			if (sf.label.equals(fieldLabel)) {
				res.add(sf);
			}
		}
		return res;
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
			fieldToProcess = Global.ALL_SEARCHEABLE_FIELDS;
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
