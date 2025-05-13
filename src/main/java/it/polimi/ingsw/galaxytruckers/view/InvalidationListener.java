package it.polimi.ingsw.galaxytruckers.view;

import java.io.IOException;

public interface InvalidationListener {
    void onInvalidate() throws IOException;
}