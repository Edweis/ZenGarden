package models.tools;

import java.util.ArrayList;
import java.util.List;

import models.User;

/**
 * Represents a result that has :
 * <ul>
 * <li>a {@link User} user that should be displayed.</li>
 * <li>a <tt> Map<String, SearchField><//t> matching that associate one query to
 * one specific search field.</li>
 * <li>a Matching Rank that corresponds to the number of matching the User had
 * (the size of the matching map actually). The higher the rank the better the
 * result.</li>
 * </ul>
 * 
 * @author piou
 *
 */
public class SearchResult {

	private User user;
	private List<String> keys;
	private List<SearchField> values;

	public SearchResult(User user, String query, SearchField place) {
		this.user = user;
		keys = new ArrayList<String>();
		values = new ArrayList<SearchField>();
		addMatching(query, place);
	}

	public void addMatching(String query, SearchField place) {
		keys.add(query);
		values.add(place);
	}

	public User getUser() {
		return user;
	}

	public int getMatchingRank() {
		return keys.size();
	}

	public List<String> getKeys() {
		return keys;
	}

	public List<SearchField> getValues() {
		return values;
	}

}
