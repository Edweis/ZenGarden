import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import play.Logger;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.Helpers;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import controllers.routes;
import models.User;

public class SignTests extends PrepareTests {

	@Test
	public void loginSucess() {
		// User.create("bob@example.com", "secret", "Bob").save();

		RequestBuilder req = new RequestBuilder().method(Helpers.POST)
				.bodyForm(ImmutableMap.of("email", "bob@example.com", "password", "secret"))
				.uri(routes.SignTools.signUp().url());

		Result result = play.test.Helpers.route(req);

		assertEquals(200, result.status());

	}

	@Test
	public void loginFail() {
		RequestBuilder req = new RequestBuilder().method(Helpers.POST)
				.bodyForm(ImmutableMap.of("email", "bob@example.com", "password", "badone"))
				.uri(routes.SignTools.signUp().url());

		Result result = play.test.Helpers.route(req);

		assertEquals(303, result.status());
	}

	@Test
	public void signInWithoutSchoolSucess() {

		Map<String, String> form = new HashMap<String, String>();
		form.put("email", "boby@example.com");
		form.put("password", "secret");
		form.put("confirmation", "secret");
		form.put("firstName", "Boby");

		RequestBuilder req = new RequestBuilder().method(Helpers.POST).bodyForm(form)
				.uri(routes.SignTools.signIn().url());

		Result result = play.test.Helpers.route(req);

		assertEquals(200, result.status());
		assertNotNull(User.find.where().eq("email", "boby@example").findUnique());
		Logger.info("Success : " + result.header("Location").get());
	}

	@Test
	public void signInWithoutSchoolFail_passwordMismatch() {

		Map<String, String> form = new HashMap<String, String>();
		form.put("email", "boby@example.com");
		form.put("password", "secret");
		form.put("confirmation", "sicrit");
		form.put("firstName", "Boby");

		RequestBuilder req = new RequestBuilder().method(Helpers.POST).bodyForm(form)
				.uri(routes.SignTools.signIn().url());

		Result result = play.test.Helpers.route(req);

		assertEquals(303, result.status());
		assertNull(User.find.where().eq("email", "boby@example").findUnique());
		Logger.info("Fail : " + result.header("Location").get());
	}
}
