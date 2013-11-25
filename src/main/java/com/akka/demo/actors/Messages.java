package com.akka.demo.actors;

import akka.actor.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * User: abhishek.agarwal
 * Date: 25/11/13
 * Time: 4:52 PM
 */
public class Messages {

    public enum ChatAction {
        CreateChat,
        StartChat,
        StopChat,
        Speak,
        KillChat,
        Begin,
        MsgFromConsole,
        EnableConsole
    }

    public static Event createEvent(ChatAction action, String message) {
        return new Event(action, message);
    }

    @AllArgsConstructor
    public static class Event {
        @Getter private ChatAction action;
        @Getter private String message;
    }
}
