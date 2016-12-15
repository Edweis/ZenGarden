package controllers;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.util.ArrayList;
import java.util.List;

import models.School;

public class Autocomplete extends Controller {

	public Result school(String input) {
		ArrayList<String> suggestions = new ArrayList<String>();
		Logger.info("AC input : " + input);

		List<School> ls = School.find.where().like("Name", "%" + input + "%").findList();
		for (School s : ls) {
			suggestions.add(s.getName() + " (" + s.getCountry().getName() + ")");
		}
		Logger.info(Json.toJson(suggestions).toString());

		return Results.ok(Json.toJson(suggestions)).as("application/json");
	}
}
