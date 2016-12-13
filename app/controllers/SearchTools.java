package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import java.util.Arrays;
import java.util.List;

import models.tools.Search;
import models.tools.SearchResult;

public class SearchTools extends Controller {

	public Result display(String queries, String filters) {

		Search mySearch = new Search();
		mySearch.setQueries(queries == null ? null : queries.split(";"));
		mySearch.setFilters(filters == null ? null : filters.split(";"));
		List<SearchResult> res = mySearch.process();

		return ok(views.html.pages.search.render(res, queries == null ? null : Arrays.asList(queries.split(";"))));

	}
}
