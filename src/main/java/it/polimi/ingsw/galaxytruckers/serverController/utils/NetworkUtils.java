package it.polimi.ingsw.galaxytruckers.serverController.utils;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class NetworkUtils {
    public static String convert(ShipBoard shipBoard) {
        return Player.getPlayer(shipBoard).getNickname();
    }

    public static <T extends Collection<ShipBoard>, R extends Collection<String>> R convertCollection(T collection, Supplier<R> supplier) {
        return collection.stream()
                .map(NetworkUtils::convert)
                .collect(supplier,R::add,R::addAll);
    }

    public static <V> Map<String,V> convertMap(Map<ShipBoard,V> map) {
        return map.entrySet().stream().collect(Collectors.toMap(
                entry -> convert(entry.getKey()),
                Map.Entry::getValue));
    }
}
