package com.akka.demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.akka.demo.actors.ChatManager;
import com.akka.demo.actors.Messages;

import static com.akka.demo.actors.Messages.ChatAction.CreateChat;

/**
 * User: abhishek.agarwal
 * Date: 18/11/13
 * Time: 10:52 PM
 */
public class Service {
    public static void main(String... args) {
        ActorSystem system = ActorSystem.create();
        ActorRef manager = system.actorOf(Props.create(ChatManager.class), "manager");
        manager.tell(Messages.createEvent(CreateChat, null), null);

    }
}
