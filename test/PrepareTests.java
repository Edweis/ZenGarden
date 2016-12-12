import play.Application;
import play.Logger;
import play.libs.Yaml;
import play.mvc.Result;
import play.test.Helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.avaje.ebean.Ebean;

public abstract class PrepareTests {
	private static final boolean RESET_DB = true;

	List<String> ymalFiles = Arrays.asList("public/ydata.yml", "public/ycountries.yml");
	protected static Application app;
	private static String createDdl;
	private static String dropDdl;

	/**
	 * Create the fake application and create queries to wipe up database
	 * (evolution)
	 */
	@BeforeClass
	public static void startApp() {

		List<Object> lo = copyActualDB();

		app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
		Helpers.start(app);

		// Reading the evolution file
		if (RESET_DB) {
			readYmlFile();
		}
	}

	private static List<Object> copyActualDB() {
		// TODO Auto-generated method stub
		return null;
	}

	@AfterClass
	public static void stopApp() {
		Helpers.stop(app);
	}

	/**
	 * Wipe up database and reinsert content from the yml file
	 */
	@Before
	public void createCleanDb() {
		if (RESET_DB) {
			Ebean.execute(Ebean.createCallableSql(dropDdl));
			Ebean.execute(Ebean.createCallableSql(createDdl));

			ArrayList<Object> l = new ArrayList<Object>();
			for (String s : ymalFiles) {
				l.addAll((List<?>) Yaml.load(s));
			}

			for (Object i : l) {
				Ebean.save(i);
			}

		}

	}

	private static void readYmlFile() {
		String evolutionContent = null;
		try {
			evolutionContent = FileUtils
					.readFileToString(app.getWrappedApplication().getFile("conf/evolutions/default/1.sql"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Splitting the String to get Create & Drop DDL
		String[] splittedEvolutionContent = evolutionContent.split("# --- !Ups");
		String[] upsDowns = splittedEvolutionContent[1].split("# --- !Downs");
		createDdl = upsDowns[0];
		dropDdl = upsDowns[1];
	}

	protected void displayHeader(Result result) {
		Logger.info(result.headers().keySet().toString());
		Logger.info(result.headers().values().toString());
	}
}
