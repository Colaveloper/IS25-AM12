//package it.polimi.ingsw.galaxytruckers.network.server.socket;
//
//import it.polimi.ingsw.galaxytruckers.network.shared.ClientRequest;
//import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
//
//import java.awt.*;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.net.Socket;
//
//class ClientHandler implements Runnable {
//    private final Socket socket;
//    private final ServerControllerInterface controller;
//
//
//    public ClientHandler(Socket socket, ServerControllerInterface controller) {
//        this.socket = socket;
//        this.controller = controller;
//    }
//
//    @Override
//    public void run() {
//        try {
//            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
//            while (true) {
//                Object obj = in.readObject();
//                if (obj instanceof ClientRequest request) {
//                    handleRequest(request);
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            System.out.println("Client disconnected "+ e.getMessage());
//        }
//    }
//
//    private void handleRequest(ClientRequest request) {
//        switch (request.methodName()) {
//            case "registerNickname" -> {
//                controller.registerNickname((String) request.args()[0]);
//            }
////            case "drawCard" -> {
////                controller.drawCard((String) request.nickname());
////            }
//            default -> System.out.println("Unknown methodName: " + request.methodName());
//        }
//    }
//}
