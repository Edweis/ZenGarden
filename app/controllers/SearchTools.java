package controllers;

import play.mvc.Controller;
import play.mvc.Result;

<<<<<<< HEAD
import java.util.Arrays;
import java.util.List;

import models.tools.Search;
import models.tools.SearchResult;
=======
import java.util.List;

import controllers.tools.Search;
import controllers.tools.Search.SearchResult;
>>>>>>> c9a13fabf198f71892f2b4e874aeef971e795873

public class SearchTools extends Controller {

	public Result display(String queries, String filters) {

		Search mySearch = new Search();
<<<<<<< HEAD
		mySearch.setQueries(queries == null ? null : queries.split(";"));
		mySearch.setFilters(filters == null ? null : filters.split(";"));
		List<SearchResult> res = mySearch.process();

		return ok(views.html.pages.search.render(res, queries == null ? null : Arrays.asList(queries.split(";"))));
=======
		mySearch.setQueries(queries.split(";"));
		mySearch.setFilters(filters.split(";"));
		List<SearchResult> res = mySearch.process();

		return ok(views.html.pages.search.render(res));
>>>>>>> c9a13fabf198f71892f2b4e874aeef971e795873

	}
}
