package it.polimi.ingsw.galaxytruckers.server.controller.utils;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.controller.lobby.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Utility class for converting ship board objects to player-related string identifiers.
 * Provides methods to convert ShipBoard objects to player nicknames in various collection types.
 */
public class ConversionUtils {

    /**
     * Converts a ShipBoard object to its associated player's nickname.
     *
     * @param shipBoard the ShipBoard to convert
     * @return the nickname of the player associated with the ShipBoard
     */
    public static String convert(ShipBoard shipBoard) {
        return Player.getPlayer(shipBoard).getNickname();
    }

    /**
     * Converts a collection of ShipBoard objects to a collection of player nicknames.
     *
     * @param <T> the type of the source collection containing ShipBoard objects
     * @param <R> the type of the target collection to contain player nicknames
     * @param collection the collection of ShipBoard objects to convert
     * @param supplier a supplier function to create the target collection
     * @return a collection of player nicknames corresponding to the ShipBoard objects
     */
    public static <T extends Collection<ShipBoard>, R extends Collection<String>> R convertCollection(T collection, Supplier<R> supplier) {
        return collection.stream()
                .map(ConversionUtils::convert)
                .collect(supplier,R::add,R::addAll);
    }

    /**
     * Converts a map with ShipBoard keys to a map with player nickname keys.
     *
     * @param <V> the type of values in the map
     * @param map the map with ShipBoard keys to convert
     * @return a new map with the same values but keys converted to player nicknames
     */
    public static <V> Map<String,V> convertMap(Map<ShipBoard,V> map) {
        return map.entrySet().stream().collect(Collectors.toMap(
                entry -> convert(entry.getKey()),
                Map.Entry::getValue));
    }

    /**
     * Converts an array of ShipBoard objects to an array of player nicknames.
     *
     * @param array the array of ShipBoard objects to convert
     * @return an array of player nicknames corresponding to the ShipBoard objects
     */
    public static String[] convertArray(ShipBoard[] array) {
        return Arrays.stream(array)
                .map(ConversionUtils::convert)
                .toArray(String[]::new);
    }
}
