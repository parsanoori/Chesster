package Main;

import Communication.Server;

public class Main {
    public static void main(String[] args) {
        Thread server = new Thread(new Server());
        server.start();
        System.out.println("Server started ... ");
    }

}
