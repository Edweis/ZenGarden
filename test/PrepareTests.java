import play.Application;
import play.Logger;
import play.libs.Yaml;
import play.mvc.Result;
import play.test.Helpers;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.avaje.ebean.Ebean;

import models.User;

public abstract class PrepareTests {
	private static final String YML_FILE = "/public/ydata.yml";
	private static final boolean RESET_DB = false;

	protected static Application app;
	private static String createDdl;
	private static String dropDdl;

	/**
	 * Create the fake application and create queries to wipe up database
	 * (evolution)
	 */
	@BeforeClass
	public static void startApp() {
		app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
		Helpers.start(app);

		// Reading the evolution file
		if (RESET_DB) {
			readYmlFile();
		}
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

			if (User.find.findRowCount() == 0) {
				List<?> l = (List<?>) Yaml.load(YML_FILE);
				for (Object i : l) {
					Ebean.save(i);
				}
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
