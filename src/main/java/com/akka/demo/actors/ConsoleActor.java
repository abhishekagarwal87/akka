package com.akka.demo.actors;
import akka.actor.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * User: abhishek.agarwal
 * Date: 24/11/13
 * Time: 8:22 PM
 */
public class ConsoleActor extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Exception {
        Messages.Event event = (Messages.Event) o;
        if (event.getAction() == Messages.ChatAction.EnableConsole) {
            //System.out.println("accepting user input now");
            acceptUserInput();
        }
    }

    private void acceptUserInput() throws IOException {
        System.out.println("Please type something for your buddies and press enter!\n" +
        "Or, you can type:\n" +
        "stop, to disable chat\n" +
        "start, to restart it\n" +
        "or done, to exit this program!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = reader.readLine();
            getContext().parent().tell(Messages.createEvent(Messages.ChatAction.MsgFromConsole, line), getSelf());
            if (line.equalsIgnoreCase("done"))
                break;
        }

    }
}
