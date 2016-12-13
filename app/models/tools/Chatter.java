package models.tools;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Chatter extends UntypedActor {

	public static Props props(ActorRef out) {
		return Props.create(Chatter.class, out);
	}

	private final ActorRef out;

	public Chatter(ActorRef out) {
		this.out = out;
	}

	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			out.tell("I received your message: " + message, self());
		}
	}
}
