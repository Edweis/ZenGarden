package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

import controllers.tools.Search;
import controllers.tools.Search.SearchResult;

public class SearchTools extends Controller {

	public Result display(String queries, String filters) {

		Search mySearch = new Search();
		mySearch.setQueries(queries.split(";"));
		mySearch.setFilters(filters.split(";"));
		List<SearchResult> res = mySearch.process();

		return ok(views.html.pages.search.render(res));

	}
}
