package com.akka.demo.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.core.ImmutableList;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

/**
 * User: abhishek.agarwal
 * Date: 24/11/13
 * Time: 8:28 PM
 */
public class ChatManager extends UntypedActor {
    private static final int NUM_BUDDIES = 3;
    public enum State {
        ChatOffline, ChatOnline
    }

    private State state = State.ChatOffline;
    private Set<ActorRef> chatters = null;
    private List<String> msgSoFar = null;


    @Override
    public void onReceive(Object msg) throws Exception {
        Messages.Event event = (Messages.Event) msg;
        if (state == State.ChatOffline) {
            whenOffline(event);
        } else if (state == State.ChatOnline) {
            whenOnline(event);
        } else {
            whenUnhandled(event);
        }
    }

    private void whenOffline(Messages.Event event) {
        switch (event.getAction()) {
            case CreateChat:
                if (chatters != null) return;
                //System.out.println("Inside CreateChat while in offline state");
                Set<ActorRef> actorRefList = new HashSet<ActorRef>();
                actorRefList.add(context().actorOf(Props.create(UserActor.class), "user"));
                for (int i = 1; i <= NUM_BUDDIES; i++)
                    actorRefList.add(context().actorOf(Props.create(BuddyActor.class), "buddy"+i));
                changeState(State.ChatOnline, actorRefList, new LinkedList<String>());
                break;
            case StartChat:
                //System.out.println("Inside StartChat while in offline state");
                changeState(State.ChatOnline, this.chatters, new LinkedList<String>());
                break;
            default:
                whenUnhandled(event);
                break;
        }
    }

    private void whenOnline(Messages.Event event) {
        switch (event.getAction()) {
            case Speak:
                //System.out.println(format("Message event: {%s} received in online state: ", event.getMessage()));
                for (ActorRef actorRef : Sets.difference(chatters, ImmutableSet.of(sender()))) {
                    actorRef.forward(event, getContext());
                }
                String labeledText = sender().path().name() + ":"  + event.getMessage();
                msgSoFar.add(labeledText);
                changeState(this.state, chatters, msgSoFar);
                break;
            case StopChat:
                //System.out.println("StopChat event received in online state");
                changeState(State.ChatOffline, this.chatters, null);
                break;
            default:
                whenUnhandled(event);
                break;

        }
    }

    private void whenUnhandled(Messages.Event event) {
        switch (event.getAction()) {
            case KillChat:
                System.out.println("shutting down\n\n" +
                        "-- Begin Server chat log--");
                for (String msg : msgSoFar) {
                    System.out.println(msg);
                }
                System.out.println("-- End Server chat log");
                getContext().system().shutdown();
                break;
        }
    }

    private void changeState(State state, Set<ActorRef> data, List<String> msgSoFar)  {
        State oldState = this.state;
        Set<ActorRef> oldData = this.chatters;
        this.state = state;
        this.chatters = data;
        this.msgSoFar = msgSoFar;
        onTransition(oldState, state, oldData, data);
    }

    private void onTransition(State oldState, State newState, Set<ActorRef> oldData, Set<ActorRef> newData) {
        if (oldState == State.ChatOffline && newState == State.ChatOnline) {
            if (oldData == null) {
                for (ActorRef actorRef : newData) {
                    actorRef.tell(Messages.createEvent(Messages.ChatAction.Begin, null), getSelf());
                }
            }
        }
    }



}
