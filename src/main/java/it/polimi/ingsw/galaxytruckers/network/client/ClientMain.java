package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;

public class ClientMain {
    public static void main(String[] args) {
        VirtualServer server;
        RmiClient rmiClient;
        try {
            rmiClient = new RmiClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            rmiClient.start("Galaxy-Truckers-Server", "127.0.0.1", 1234);
        } catch (Exception e) {
            System.err.println("Could not start RMI server because of " + e.getMessage());
        }
        server = rmiClient;
        for (int i = 0; i < 2; i++) {
            try {
                server.registerNickname("pinco-pallo68");
                System.out.println("You have successfully registered as pinco-pallo68");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
