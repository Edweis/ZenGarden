package models.tools;

import java.util.HashMap;
import java.util.Map;

import models.User;
import models.tools.Search.SearchField;

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
