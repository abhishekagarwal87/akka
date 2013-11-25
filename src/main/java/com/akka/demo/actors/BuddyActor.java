package com.akka.demo.actors;

import akka.actor.UntypedActor;

import java.util.Random;

import static com.akka.demo.actors.Messages.ChatAction.Speak;

/**
 * User: abhishek.agarwal
 * Date: 24/11/13
 * Time: 8:28 PM
 */
public class BuddyActor extends UntypedActor {
    Random rand = new Random(System.currentTimeMillis());

    @Override
    public void onReceive(Object o) throws Exception {
        Messages.Event event = (Messages.Event) o;
        String message = event.getMessage();
        switch (event.getAction()) {
            case Speak:
                if (!sender().path().name().equalsIgnoreCase("user")) {
                    return;
                }
                switch (rand.nextInt(3)) {
                    case 0:
                        getContext().parent().tell(Messages.createEvent(Speak, message + " sounds good"), getSelf());
                        break;
                    case 1:
                        getContext().parent().tell(Messages.createEvent(Speak, message + " sounds bad"), getSelf());
                        break;
                    case 2:
                        getContext().parent().tell(Messages.createEvent(Speak, "I don't care about " + message ), getSelf());
                        break;
                }
                break;
            case Begin:
                // Ignoring the begin message
                break;
        }
    }
}
