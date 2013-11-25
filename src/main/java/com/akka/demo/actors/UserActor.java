package com.akka.demo.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import lombok.extern.log4j.Log4j;

import static com.akka.demo.actors.Messages.ChatAction.*;

/**
 * User: abhishek.agarwal
 * Date: 24/11/13
 * Time: 8:28 PM
 */

public class UserActor extends UntypedActor {

    private ActorRef consoleActor = getContext().actorOf(Props.create(ConsoleActor.class), "console");

    @Override
    public void onReceive(Object o) throws Exception {
        Messages.Event event = (Messages.Event) o;
        switch (event.getAction()) {
            case Begin:
                //System.out.println("enabling console");
                consoleActor.tell(Messages.createEvent(Messages.ChatAction.EnableConsole, null), getSelf());
                break;
            case MsgFromConsole:
                String message = event.getMessage();
                if (message.equalsIgnoreCase("done")) {
                    //System.out.println("kill received");
                    getContext().parent().tell(Messages.createEvent(KillChat, null), getSelf());
                } else if (message.equalsIgnoreCase("stop")) {
                    //System.out.println("stop received");
                    getContext().parent().tell(Messages.createEvent(StopChat, null), getSelf());
                } else if (message.equalsIgnoreCase("start")) {
                    //System.out.println("start received");
                    getContext().parent().tell(Messages.createEvent(StartChat, null), getSelf());
                } else {
                    //System.out.println("message received from console");
                    getContext().parent().tell(Messages.createEvent(Speak, message), getSelf());
                }
                break;
            case Speak:
                String labeledText = sender().path().name() + ":" + event.getMessage();
                System.out.println(labeledText);
                break;


        }
    }
}
