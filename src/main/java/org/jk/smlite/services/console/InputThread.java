package org.jk.smlite.services.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputThread implements Runnable {
    public void run() {
        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = null;
            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(input);
        }
    }
}
