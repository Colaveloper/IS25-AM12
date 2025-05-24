package it.polimi.ingsw.galaxytruckers.view.adventureClient;


import it.polimi.ingsw.galaxytruckers.view.enums.GoodsType;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliElement;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty.Penalty;

import static it.polimi.ingsw.galaxytruckers.model.Deck.parsePlanets;
import static it.polimi.ingsw.galaxytruckers.model.Deck.parseGoods;

public class CliAdventureCard{

    public String renderCard(AdventureCard card){
        StringBuilder result = new StringBuilder();
        switch(card){
            case AbandonedShipCard abandonedShipCard -> {
                result.append("[ABANDONED SHIP]\n");
                result.append("Flight days lost: ").append(abandonedShipCard.getFlightDaysLoss()).append("\n");
                result.append("Credits prize: ").append(abandonedShipCard.getCreditPrize()).append("\n");
                result.append("Required crew sacrifice: ").append(abandonedShipCard.getRequiredCrew()).append("\n");
            }
            case AbandonedStationCard abandonedStationCard -> {
                result.append("[ABANDONED STATION]\n");
                result.append(describeGoods(abandonedStationCard.getGoodsPrize()));
                result.append("Flight days lost: ").append(abandonedStationCard.getFlightDaysLoss()).append("\n");
                result.append("Required crew sacrifice: ").append(abandonedStationCard.getRequiredCrew()).append("\n");
            }
            case CombatZoneCard combatZoneCard -> {
                result.append("[COMBAT ZONE]\n");
                List<CombatZoneCheck> checks = combatZoneCard.getChecks();
                List<Penalty> penalties = combatZoneCard.getPenalties();
                for (int i = 0; i < checks.size(); i++) {
                    result.append("Whoever has ").append(checks.get(i)).append(penalties.get(i)).append("\n");
                }
            }
            case EpidemicCard epidemicCard -> {
            }
            case MeteorSwarmCard meteorSwarmCard -> {
                result = new StringBuilder(describeProjectiles(meteorSwarmCard.getProjectiles()));
            }
            case OpenSpaceCard openSpaceCard -> {
            }
            case PiratesCard piratesCard -> {
            }
            case PlanetsCard planetsCard -> {
                result = new StringBuilder(describePlanets(planetsCard.getPlanets()));
            }
            case SabotageCard sabotageCard -> {
            }
            case SlaversCard slaversCard -> {
            }
            case SmugglersCard smugglersCard -> {
                result = new StringBuilder(describeGoods(smugglersCard.getGoodsPrize()));
            }
            case StarDustCard starDustCard -> {
            }
        }
        return result.toString();
    }

    private String describePlanets(List<Map<GoodsType, Integer>> planets) {
        /*
        * entrySet().stream() — loop over all good types per planet
        * Collections.nCopies() — repeat goods names based on count
        * flatMap() — flatten the repeated names into one stream
        * joining(", ") — turn them into the nice comma-separated string
        * */

        if (planets.isEmpty()) return "No planets.";

        StringBuilder sb = new StringBuilder();

        List<GoodsType> orderedGoods = List.of(GoodsType.BLUE, GoodsType.GREEN, GoodsType.YELLOW, GoodsType.RED);

        for (int i = 0; i < planets.size(); i++) {
            sb.append("planet ").append(i + 1).append(": ");

            int finalI = i;
            String goods = orderedGoods.stream()
                    .flatMap(type -> {
                        int count = planets.get(finalI).getOrDefault(type, 0);
                        return Collections.nCopies(count, type.name().toLowerCase()).stream();
                    })
                    .collect(Collectors.joining(", "));

            sb.append(goods).append("\n");
        }

        return sb.toString().trim();
    }

    private String describeGoods(Map<GoodsType, Integer> goods) {
        if (goods.isEmpty()) return "No goods.";

        List<GoodsType> orderedGoods = List.of(GoodsType.BLUE, GoodsType.GREEN, GoodsType.YELLOW, GoodsType.RED);

        return orderedGoods.stream()
                .flatMap(type -> Collections.nCopies(goods.getOrDefault(type, 0), type.name().toLowerCase()).stream())
                .collect(Collectors.joining(", "));
    }

    public String describeProjectiles(List<Projectile> projectiles) {
        Map<Integer, String> directionMap = Map.of(
                0, "front",
                1, "left",
                2, "back",
                3, "right"
        );

        return projectiles.stream()
                .map(p -> p.type() + " coming from the " +
                        directionMap.getOrDefault(p.direction(), "unknown"))
                .collect(Collectors.joining("\n"));
    }
}