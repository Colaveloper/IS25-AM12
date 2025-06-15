package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.HashSet;
import java.util.Set;

public class EnabledSurrenderPolicy implements SurrenderPolicy {
    private final Set<ShipBoard> requests = new HashSet<>();
    private final Set<ShipBoard> surrenderedShips = new HashSet<>();
    private GameEventListener listener;

    @Override
    public void setEventListener(GameEventListener gameEventListener) {
        this.listener = gameEventListener;
    }

    @Override
    public boolean isSurrenderEnabled() {
        return true;
    }

    @Override
    public boolean requestSurrender(ShipBoard shipBoard, SurrenderCause cause) {
        if (surrenderedShips.contains(shipBoard)) return false;
        boolean res = requests.add(shipBoard);
        if (res && listener != null) listener.notifySurrenderRequestEvent(shipBoard, cause);
        return res;
    }

    @Override
    public Set<ShipBoard> confirmSurrender(FlightBoard flightBoard) {

        Set<ShipBoard> allShips = flightBoard.getShipToPlace().keySet();
        allShips.stream()
                .filter(s -> s.getCrewSize() == 0)
                .forEach(s -> requestSurrender(s, SurrenderCause.NOCREW));
        flightBoard.getLappedShips().forEach(s -> requestSurrender(s,SurrenderCause.LAPPED));

        this.surrenderedShips.addAll(requests);
        Set<ShipBoard> newSurrenderedShips = new HashSet<>(this.requests);
        this.requests.clear();
        flightBoard.removeShips(newSurrenderedShips);
        if (listener !=  null && !newSurrenderedShips.isEmpty()) listener.notifySurrenderEvent(newSurrenderedShips.stream().toList());
        return newSurrenderedShips;
    }

    @Override
    public Set<ShipBoard> getSurrenderedShips() {
        return new HashSet<>(surrenderedShips);
    }

    @VisibleForTesting
    public Set<ShipBoard> getRequests() {
        return new HashSet<>(requests);
    }

    @VisibleForTesting
    protected GameEventListener getListener() {
        return listener;
    }
}
