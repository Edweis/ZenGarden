package controllers;

import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security.Authenticated;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;

import com.google.inject.Inject;

import models.User;
import models.tools.InteractivePanelObject;

@Authenticated(Secured.class)
public class ProfileTools extends Controller {

	private FormFactory ff;

	@Inject
	public ProfileTools(FormFactory ff) {
		this.ff = ff;
	}

	public Result index() {
		Long connectedUserId = Secured.getUserIdLong(ctx());
		return display(connectedUserId);
	}

	/**
	 * Action on a panel object. It should look like : <br/>
	 * <tt>action/clazz[/id]</tt><br/>
	 * for instance : <br/>
	 * <tt>edit/education/4523</tt>
	 * 
	 * @param path
	 * @return
	 */
	public Result actionOnPanelObject(String action, String clazz, Long id) {

		User connectedUser = Secured.connectedUser(ctx());
		InteractivePanelObject<?> ipo;
		switch (clazz) {
		case "education":
			ipo = new InteractivePanel(connectedUser, ff).new InteractiveEducation();
			break;
		case "scholarship":
			ipo = new InteractivePanel(connectedUser, ff).new InteractiveScholarship();
			break;
		case "experience":
			ipo = new InteractivePanel(connectedUser, ff).new InteractiveExperience();
			break;
		case "work":
			ipo = new InteractivePanel(connectedUser, ff).new InteractiveWork();
			break;
		case "user":
			Logger.debug(request().contentType().get());
			if (request().contentType().get().contains("multipart/form-data")) {
				// if it's his profile picture
				MultipartFormData<File> body = request().body().asMultipartFormData();
				FilePart<File> picture = body.getFile("picture");

				if (picture != null) {
					String fileName = picture.getFilename();
					String extension = FilenameUtils.getExtension(fileName);
					Logger.debug(fileName);

					Path path = (new File(User.generateAbsolutePicturePath(connectedUser, extension))).toPath();
					Logger.debug(path.toString());

					File file = picture.getFile();
					try (InputStream is = new FileInputStream(file)) {
						java.nio.file.Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
						connectedUser.setPictureExtension(User.generateRelativePicturePath(connectedUser, extension));
						connectedUser.update();

						Logger.debug("DONE !");
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

<<<<<<< HEAD
					return redirect(routes.ProfileTools.index());
				} else {
					return badRequest("Missing file");
				}

			} else {
				// it is something else
=======
					return ok("File uploaded");
				} else {
					flash("error", "Missing file");
					return badRequest();
				}

			} else {
				// else
>>>>>>> c9a13fabf198f71892f2b4e874aeef971e795873
				Form<User.Updater> fuu = ff.form(User.Updater.class).bindFromRequest();
				if (fuu.hasErrors()) {
					return badRequest(fuu.globalError().message());
				} else {
					fuu.get().update(connectedUser).update();
<<<<<<< HEAD
					return ok(fuu.get().displayResult()).as("html");
=======
					return ok(connectedUser.getIntroductionText());
>>>>>>> c9a13fabf198f71892f2b4e874aeef971e795873
				}
			}

		default:
			return badRequest("class not recognized");
		}

		switch (action) {
		case "add":
			return ipo.addObject();
		case "remove":
			if (id == 0) {
				return badRequest("bad id");
			}
			return ipo.removeObject(id, connectedUser);
		case "edit":
			if (id == 0) {
				return badRequest("bad id");
			}
			return ipo.editObject(id, connectedUser);

		default:
			return badRequest("action not recognized");
		}

	}

	public Result display(Long userId) {
		User u = User.find.byId(userId);

		if (u == null) {
			return Results.notFound("User not found");
		} else {
			// Is it your profile ?
			if (u.getId() == Secured.getUserIdLong(ctx())) {
				return ok(views.html.pages.profile.render(u, true));
			} else {
				return ok(views.html.pages.profile.render(u, false));
			}
		}

	}

}
