package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;

public class ClientMain {
    public static void main(String[] args) {
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
        rmiClient.registerNickname("pinco-pallo68");
        System.out.println("You have registered as pinco-pallo68");
    }
}
