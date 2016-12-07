package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlWriter;

import models.Appointment;
import models.Chat;
import models.Contact;
import models.Country;
import models.Education;
import models.Experience;
import models.Funding;
import models.Message;
import models.Room;
import models.Scholarship;
import models.School;
import models.User;
import models.Work;
import models.WorkCursus;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class MainTools extends Controller {

	public static final String YAML_FILE_NAM = "public/ydata.yml";

	/**
	 * Render the Home page
	 * 
	 * @return
	 */
	public Result home() {
		return ok(views.html.pages.home.render(User.find.all()));
	}

	public Result resetDB() {
		// Country c = new Country();
		// c.Code2 = "FR";
		// c.Code3 = "FRA";
		// c.Name = "France";
		// c.insert();

		createYAMLfile();

		return ok("done !").as("text/html");

	}

	private void createYAMLfile() {

		List<Object> toWrite = new ArrayList<Object>();

		toWrite.add(Appointment.find.all());
		toWrite.add(Chat.find.all());
		toWrite.add(Contact.find.all());
		toWrite.add(Country.find.all());
		toWrite.add(Education.find.all());
		toWrite.add(Experience.find.all());
		toWrite.add(Funding.find.all());
		toWrite.add(Message.find.all());
		toWrite.add(Room.find.all());
		toWrite.add(Scholarship.find.all());
		toWrite.add(School.find.all());
		toWrite.add(User.find.all());
		toWrite.add(Work.find.all());
		toWrite.add(WorkCursus.find.all());

		YamlWriter writer = null;
		try {
			writer = new YamlWriter(new FileWriter(YAML_FILE_NAM));

			for (Object o : toWrite) {
				writer.write(o);
			}

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
