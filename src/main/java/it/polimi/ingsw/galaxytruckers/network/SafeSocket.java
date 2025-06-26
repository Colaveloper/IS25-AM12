package it.polimi.ingsw.galaxytruckers.network;

import it.polimi.ingsw.galaxytruckers.network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A thread-safe wrapper around a Socket that provides methods to read and write messages.
 */
public class SafeSocket {

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    /**
     * Creates a SafeSocket instance with the given Socket.
     *
     * @param socket the Socket to wrap
     * @throws IOException if an I/O error occurs when creating the input or output streams
     */
    public SafeSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Writes a Message to the socket's output stream.
     *
     * @param message the message to write
     * @throws IOException if an I/O error occurs while writing the message
     */
    public synchronized void write (Message message) throws IOException {
        out.writeObject(message);
        out.flush();
    }

    /**
     * Reads a Message from the socket's input stream.
     *
     * @return the read Message
     * @throws IOException if an I/O error occurs while reading the message
     * @throws ClassNotFoundException if the class of the serialized object cannot be found
     */
    public Message read() throws IOException, ClassNotFoundException {
        return (Message) in.readObject();
    }

    /**
     * Closes the socket and its associated streams.
     *
     * @throws IOException if an I/O error occurs while closing the streams or socket
     */
    public synchronized void close() throws IOException {
        if (!socket.isClosed()) {
            out.close();
            in.close();
            socket.close();
        }
    }
}