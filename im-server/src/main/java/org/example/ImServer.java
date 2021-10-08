package org.example;

import org.example.config.ImServerHttpStart;
import org.example.config.ImServerWebSocketStart;

public class ImServer {

    public static void main(String[] args) throws Exception {
        ImServerWebSocketStart.start();
        ImServerHttpStart.start();
    }
}
