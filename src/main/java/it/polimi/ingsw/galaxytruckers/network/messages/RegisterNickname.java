package it.polimi.ingsw.galaxytruckers.network.messages;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;

/**
 * Represents a request to register a nickname for a player.
 */
public final class RegisterNickname extends Request {
    private final String nickname;

    /**
     * Creates a new RegisterNickname request with the specified nickname.
     *
     * @param nickname the nickname to register
     */
    public RegisterNickname(String nickname) {
        super();
        this.nickname = nickname;
    }

    @Override
    public void execute(VirtualServer server) {
        server.registerNickname(nickname);
    }
}
