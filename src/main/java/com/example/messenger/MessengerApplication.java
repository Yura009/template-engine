package com.example.messenger;

import com.example.messenger.service.Messenger;
import com.example.messenger.service.TemplateEngine;

import java.util.Scanner;

public class MessengerApplication {
    public static void main(String[] args) {
        Messenger messenger = new Messenger(new TemplateEngine());

        if (args.length == 2) {
            messenger.runFileMode(args[0], args[1]);
        } else {
            messenger.runConsoleMode(new Scanner(System.in), System.out, System.err);
        }
    }
}
