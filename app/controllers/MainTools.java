package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.text.csv.CsvReader;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlWriter;

import models.Country;
import models.User;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class MainTools extends Controller {

	public static final String COUNTRIES_FILE_NAME = "public/ycountries.yml";
	public static final String ENTITIES_FILE_NAME = "public/ydata.yml";
	public static final String COUNTRY_CSV = "public/countries.csv";

	/**
	 * Render the Home page
	 * 
	 * @return
	 */
	public Result home() {
		return ok(views.html.pages.home.render(User.find.all()));
	}

	public Result resetDB() {

		importCountries();
		// createYAMLfile((List<?>) User.find.all(), ENTITIES_FILE_NAME);
		// createYAMLfile((List<?>) Country.find.all(), COUNTRIES_FILE_NAME);
		// importYAMLfile();

		return ok("done !").as("text/html");

	}

	private void importYAMLfile() {
		// TODO Auto-generated method stub
	}

	private void importCountries() {
		try {
			File f = new File(COUNTRY_CSV);

			FileReader reader = new FileReader(f);

			CsvReader<Country> csvReader = Ebean.createCsvReader(Country.class);

			csvReader.setPersistBatchSize(20);

			csvReader.addProperty("Name");
			csvReader.addProperty("Code2");
			csvReader.addProperty("Code3");

			csvReader.process(reader);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void createYAMLfile(List<?> lo, String fileName) {

		YamlWriter writer = null;
		try {
			new File(fileName).delete();

			writer = new YamlWriter(new FileWriter(fileName));

			writer.write(lo);

			Logger.warn(lo.size() + " entities inserted.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (YamlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
