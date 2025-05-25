package it.polimi.ingsw.galaxytruckers.network.messages;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;

public final class RegisterNickname extends Request {
    private final String nickname;

    public RegisterNickname(String nickname) {
        super();
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void execute(VirtualServer server) {
        server.registerNickname(nickname);
    }
}
