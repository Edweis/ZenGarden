import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import play.Logger;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.Helpers;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import controllers.SignTools;
<<<<<<< HEAD
import controllers.routes;
=======
import models.Country;
>>>>>>> c9a13fabf198f71892f2b4e874aeef971e795873
import models.Education;
import models.User;

public class ProfileTests extends PrepareTests {

	Map<String, String> connect = new HashMap<String, String>();
	Map<String, String> wrongConnect = new HashMap<String, String>();
<<<<<<< HEAD
=======

	private User connectedUser;

	public void Connect() {
		User u;
>>>>>>> c9a13fabf198f71892f2b4e874aeef971e795873

	private User connectedUser;

<<<<<<< HEAD
	public void Connect() {
		connect.put(SignTools.USER_S, "1");
	}

	@Test
	public void addAndRemoveEducation() {
=======
		connectedUser = u;
		connect.clear();
		connect.put(SignTools.USER_S, u.getId().toString());
	}

	@Test
	public void addAndDeleteEducation() {
>>>>>>> c9a13fabf198f71892f2b4e874aeef971e795873

		/**
		 * ADD NEW EDUCATION
		 */

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("DurationMonth", "13");
		map.put("IsCurrentEducation", "on");
		map.put("Major", "maj");
		map.put("Promotion", "promo");
		map.put("StartYear", "2016");
		map.put("countryCode3", "FRA");
		map.put("schoolName", "mySchool");

<<<<<<< HEAD
=======
		/**
		 * ADD NEW EDUCATION
		 */
>>>>>>> c9a13fabf198f71892f2b4e874aeef971e795873
		RequestBuilder req = new RequestBuilder().method(Helpers.POST).session(connect).bodyForm(map)
				.uri(routes.ProfileTools.actionOnPanelObject("add", "education", 0).url());
		Result result = play.test.Helpers.route(req);

		// tests
		assertEquals(200, result.status());

		boolean hasThisEd = false;
		Long idOfThisEd = null;

		for (Education e : connectedUser.getMyEducation()) {
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
<<<<<<< HEAD
=======
		// create new user
		User.Builder tu2 = new User.Builder();
		tu2.email = "new@mail.fr";
		tu2.password = "azeryuiop";
		tu2.confirmation = tu2.password;
		tu2.firstName = "newGuy";
		User u2 = tu2.generate();
		u2.save();

		wrongConnect.put(SignTools.USER_S, u2.getId().toString());
>>>>>>> c9a13fabf198f71892f2b4e874aeef971e795873

		req = new RequestBuilder().session(wrongConnect).method(Helpers.GET)
				.uri(routes.ProfileTools.actionOnPanelObject("remove", "education", idOfThisEd).url());
		result = play.test.Helpers.route(req);

		assertEquals(401, result.status());
		assertNotNull(Education.find.byId(idOfThisEd));

		/**
		 * DELETE WITH RIGHTS
		 */

		req = new RequestBuilder().session(connect).method(Helpers.GET)
				.uri(routes.ProfileTools.actionOnPanelObject("remove", "education", idOfThisEd).url());
		result = play.test.Helpers.route(req);

		assertEquals(200, result.status());
		assertNull(Education.find.byId(idOfThisEd));

	}

	@Test
	public void changeIntrodutionText() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("IntroductionText", "fils de pute");

		RequestBuilder req = new RequestBuilder().method(Helpers.POST).session(connect).bodyForm(map)
				.uri(routes.ProfileTools.actionOnPanelObject("edit", "user", 0).url());
		Result result = play.test.Helpers.route(req);

		assertEquals(200, result.status());
		assertEquals("fils de pute", User.find.where().eq("id", connect.get(SignTools.USER_S)));
	}

}
