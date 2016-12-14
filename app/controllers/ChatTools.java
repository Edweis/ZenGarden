package controllers;

import play.Logger;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.sql.Timestamp;
import java.util.List;

import com.google.inject.Inject;

import controllers.ChatTools.MessageContent;
import controllers.tools.AskForNewRequestResultException;
import models.Chat;
import models.Message;
import models.Room;
import models.User;

public class ChatTools extends Controller {

	private FormFactory ff;

	@Inject
	public ChatTools(FormFactory ff) {
		this.ff = ff;
	}

	/**
	 * Display the room between the connectedUser and the user referenced by the
	 * userId.
	 * 
	 * @param userId
	 * @return
	 */
	public Result withYou(Long userId) {
		User userToChatWith = User.find.byId(userId);
		if (userToChatWith == null) {
			return Results.notFound("The user hasn't been found");
		}

		Room room = Room.getRoomBetween(userToChatWith, Secured.connectedUser(ctx()));
		if (room == null) {
			Logger.info("creating a new room ....");
			User cu = Secured.connectedUser(ctx());
			room = new Room(userToChatWith, cu);
			room.setChat(new Chat(cu.getFirstName() + " ~ " + userToChatWith.getFirstName(), room));
			room.save();
		}
		return ok(views.html.pages.chat.render(room));
	}

	/**
	 * Get the latest message in the room send after untilThisTimestamp
	 * 
	 * @param roomId
	 * @param untilThisTimestamp
	 * @return the list of messages.
	 */
	public Result lastMessages(Long roomId, Long untilThisTimestamp) {
		Room room = Room.find.byId(roomId);
		if (room == null) {
			return notFound("room not found");
		}
		Timestamp ts = new Timestamp(untilThisTimestamp);

		List<Message> lm = Message.find.where().eq("Chat.Room", room).and().gt("Date", ts).findList();

		return ok(views.html.pages.messageItem.render(lm, Secured.connectedUser(ctx())));
	}

	/**
	 * Send a message in the room referenced by roomId. The message will be
	 * persisted only if the connectedUser belongs to the room.
	 * 
	 * @param roomId
	 * @return
	 */
	public Result send(Long roomId) {
		// check if connected is allowed to post
		User cu = Secured.connectedUser(ctx());
		Room room = Room.find.byId(roomId);
		if (room == null) {
			return notFound("room not found");
		}

		// gather information
		String content = ff.form(MessageContent.class).bindFromRequest().get().getContentMsg();
		Message message = new Message(room.getChat(), cu, content);

		// send the message
		try {
			room.getChat().send(message);
		} catch (AskForNewRequestResultException e) {
			return e.getResult();
		}

		return ok();
	}

	/**
	 * Sub class to fetch value send via the send bar in the chat. I am sure it
	 * exists a less verbose way to get ONLY ONE field.
	 * 
	 * @author piou
	 *
	 */
	public static class MessageContent {
		@Constraints.Required
		private String contentMsg;

		public String getContentMsg() {
			return contentMsg;
		}

		public void setContentMsg(String contentMsg) {
			this.contentMsg = contentMsg;
		}

	}

}
