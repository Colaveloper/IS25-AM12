package it.polimi.ingsw.galaxytruckers.network;

import it.polimi.ingsw.galaxytruckers.network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SafeSocket {

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public SafeSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public synchronized void write (Message message) throws IOException {
        out.writeObject(message);
        out.flush();
    }

    public Message read() throws IOException, ClassNotFoundException {
        return (Message) in.readObject();
    }

    public synchronized void close() throws IOException {
        if (!socket.isClosed()) {
            out.close();
            in.close();
            socket.close();
        }
    }
}