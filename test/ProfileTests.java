import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import play.Logger;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.Helpers;

import java.util.HashMap;

import org.junit.Test;

import controllers.SignTools;
import controllers.routes;
import models.Country;
import models.Education;
import models.User;

public class ProfileTests extends PrepareTests {

	public User Connect() {
		User u;

		if (User.find.all().size() == 0) {
			u = User.create("bob@example.com", "secret", "Bob");
			u.save();
		} else {
			u = User.find.all().get(0);
		}

		Logger.info("connected with : " + u);

		return u;
	}

	@Test
	public void addAndDeleteEducation() {
		User u = Connect();

		Country france = new Country();
		france.Name = "France";
		france.Code2 = "FR";
		france.Code3 = "FRA";
		france.save();

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("DurationMonth", "13");
		map.put("IsCurrentEducation", "on");
		map.put("Major", "maj");
		map.put("Promotion", "promo");
		map.put("StartYear", "2016");
		map.put("countryCode3", "FRA");
		map.put("schoolName", "mySchool");

		/**
		 * ADD NEW EDUCATION
		 */
		RequestBuilder req = new RequestBuilder().method(Helpers.POST).session(SignTools.USER_S, u.Id.toString())
				.bodyForm(map).uri(routes.ProfileTools.actionOnPanelObject("add", "education", 0).url());
		Result result = play.test.Helpers.route(req);

		assertEquals(200, result.status());

		boolean hasThisEd = false;
		Long idOfThisEd = null;
		u.refresh();
		for (Education e : u.myEducation) {
			if (e.getDurationMonth() == 13 && e.getSchool().getName().equals("mySchool")) {
				hasThisEd = true;
				idOfThisEd = e.getId();
			}
		}

		assertTrue(hasThisEd);
		assertNotNull(Education.find.byId(idOfThisEd).getSchool());
		Logger.debug(routes.ProfileTools.actionOnPanelObject("remove", "education", idOfThisEd).url());

		/**
		 * DELETE WITHOUT RIGHTS
		 */
		// create new user
		User.Builder tu2 = new User.Builder();
		tu2.email = "new@mail.fr";
		tu2.password = "azeryuiop";
		tu2.confirmation = tu2.password;
		tu2.firstName = "newGuy";
		User u2 = tu2.generate();
		u2.save();

		req = new RequestBuilder().session(SignTools.USER_S, u2.Id.toString()).method(Helpers.GET)
				.uri(routes.ProfileTools.actionOnPanelObject("remove", "education", idOfThisEd).url());
		result = play.test.Helpers.route(req);

		assertEquals(401, result.status());
		assertNotNull(Education.find.byId(idOfThisEd));

		/**
		 * DELETE WITH RIGHTS
		 */

		req = new RequestBuilder().session(SignTools.USER_S, u.Id.toString()).method(Helpers.GET)
				.uri(routes.ProfileTools.actionOnPanelObject("remove", "education", idOfThisEd).url());
		result = play.test.Helpers.route(req);

		assertEquals(200, result.status());
		assertNull(Education.find.byId(idOfThisEd));

	}

}
