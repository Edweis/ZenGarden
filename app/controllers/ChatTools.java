package controllers;

import play.Logger;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security.Authenticated;

import java.sql.Timestamp;
import java.util.List;

import com.google.inject.Inject;

import controllers.ChatTools.MessageContent;
import controllers.tools.AskForNewRequestResultException;
import models.Chat;
import models.Message;
import models.Relationship;
import models.Room;
import models.User;

@Authenticated(Secured.class)
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

		User connectedUser = Secured.connectedUser(ctx());
		if (userId == connectedUser.getId()) {
			// If he is trying to chat with himself, we redirect him to the home
			// page.
			return redirect(routes.MainTools.home());
		}

		Room room = Room.getRoomBetween(userToChatWith, Secured.connectedUser(ctx()));
		if (room == null) {
			Logger.info("creating a new room ....");
			room = new Room(userToChatWith, connectedUser);
			room.setChat(new Chat(connectedUser.getFirstName() + " ~ " + userToChatWith.getFirstName(), room));
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
	 * Give the connectedUser contact to the person with who he is in the room
	 * with.
	 * 
	 * @param roomId
	 * @return
	 */
	public Result giveContact(Long roomId) {
		User connectedUser, interlocutor;
		try {
			connectedUser = getConnectedUserInTheRoom(roomId);
			interlocutor = getInterlocutor(roomId);
		} catch (AskForNewRequestResultException e) {
			return e.getResult();
		}

		// check existing relationships
		Relationship rel = Relationship.hasHeSharedWith(connectedUser, interlocutor);
		if (rel != null) {
			return unauthorized("You already have shared your contacts with " + interlocutor.getFirstName());
		}

		// create new relationship
		Relationship nr = new Relationship(connectedUser, interlocutor);
		nr.insert();
		return ok();

	}

	/**
	 * Get the contact of the guy with connectedUser in the Room
	 * 
	 * @param roomId
	 * @return
	 */
	public Result getContact(Long roomId) {
		User connectedUser, interlocutor;
		try {
			connectedUser = getConnectedUserInTheRoom(roomId);
			interlocutor = getInterlocutor(roomId);
		} catch (AskForNewRequestResultException e) {
			return e.getResult();
		}

		// check relationship
		Relationship rel = Relationship.hasHeSharedWith(connectedUser, interlocutor);
		if (rel == null) {
			return unauthorized(interlocutor.getFirstName() + " hasn't shared contacts with you yet");
		}

		// return contact
		return ok(Json.toJson(interlocutor.getMyContactsInfo()));
	}

	/**
	 * return the connectedUser IF he belongs to the room.
	 * 
	 * @param roomId
	 * @return
	 * @throws AskForNewRequestResultException
	 *             if the connectedUser doesn't belongs in the room.
	 */
	private User getConnectedUserInTheRoom(Long roomId) throws AskForNewRequestResultException {
		User res = Secured.connectedUser(ctx());
		if (!Room.find.byId(roomId).getParticipants().contains(res)) {
			throw new AskForNewRequestResultException(unauthorized("You don't belong to this room !"));
		}
		return res;
	}

	/**
	 * Get the person with who the connectedUser is dealing with in the room. NB
	 * : THIS ONLY WORKS WITH TWO PEOPLE IN THE ROOM.
	 * 
	 * @param room
	 * @return
	 */
	private User getInterlocutor(Long roomId) throws AskForNewRequestResultException {
		User connectedUser = Secured.connectedUser(ctx());

		// getRoom
		Room room = Room.find.byId(roomId);
		if (room == null) {
			throw new AskForNewRequestResultException(notFound("chat unknown"));
		}

		// getInterlocutor
		List<User> lp = room.getParticipants();
		if (!lp.remove(connectedUser)) {
			throw new AskForNewRequestResultException(unauthorized("you don't belong to this chat"));
		}

		// Check if there is indeed only one choice left
		if (lp.size() != 1) {
			throw new AskForNewRequestResultException(
					Results.internalServerError("oops it seems that there aren't two people in the room ... Oo"));
		}

		return lp.get(0);
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
