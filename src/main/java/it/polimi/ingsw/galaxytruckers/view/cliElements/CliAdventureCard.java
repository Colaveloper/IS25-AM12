package it.polimi.ingsw.galaxytruckers.view.cliElements;


import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty.Penalty;

public class CliAdventureCard extends CliElement{
    AdventureCard card;

    public CliAdventureCard(AdventureCard card) {
        this.card = card;
    }

    @Override
    protected List<String> getNewDescription(){
        StringBuilder result = new StringBuilder();
        result.append("[NO CARD]"); // default value
        switch(card){
            case AbandonedShipCard abandonedShipCard -> {
                result.setLength(0);
                result.append("[ABANDONED SHIP]\n");
                result.append("Flight days lost: ").append(abandonedShipCard.getFlightDaysLoss()).append("\n");
                result.append("Credits prize: ").append(abandonedShipCard.getCreditPrize()).append("\n");
                result.append("Required crew sacrifice: ").append(abandonedShipCard.getRequiredCrew()).append("\n");
            }
            case AbandonedStationCard abandonedStationCard -> {
                result.setLength(0);
                result.append("[ABANDONED STATION]\n");
                result.append(describeGoods(abandonedStationCard.getGoodsPrize()));
                result.append("Flight days lost: ").append(abandonedStationCard.getFlightDaysLoss()).append("\n");
                result.append("Required crew sacrifice: ").append(abandonedStationCard.getRequiredCrew()).append("\n");
            }
            case CombatZoneCard combatZoneCard -> {
                result.setLength(0);
                result.append("[COMBAT ZONE]\n");
                List<CombatZoneCheck> checks = combatZoneCard.getChecks();
                List<Penalty> penalties = combatZoneCard.getPenalties();
                for (int i = 0; i < checks.size(); i++) {
                    result.append("Whoever has ").append(checks.get(i))
                            .append(" ")
                            .append(penalties.get(i)).append("\n");
                }
            }
            case EpidemicCard epidemicCard -> {
                result.setLength(0);
                result.append("[EPIDEMIC]\n");
                result.append("An epidemic strikes!\n");
            }
            case MeteorSwarmCard meteorSwarmCard -> {
                result.setLength(0);
                result.append("[METEOR SWARM]\n");
                result.append(describeProjectiles(meteorSwarmCard.getProjectiles()));
            }
            case OpenSpaceCard openSpaceCard -> {
                result.setLength(0);
                result.append("[OPEN SPACE]\n");
                result.append("Warm up those engines!\n");
            }
            case PiratesCard piratesCard -> {
                result.setLength(0);
                result.append("[PIRATES]\n");
                result.append(describeProjectiles(piratesCard.getProjectiles()));
                result.append("Needed firepower to defeat pirates: ")
                        .append(piratesCard.getFirePowerThreshold())
                        .append("\n");
                result.append("Credits reward for defeating: ")
                        .append(piratesCard.getCreditPrize())
                        .append("\n");
                result.append("Flight days lost: ").append(piratesCard.getFlightDaysLoss());
            }
            case PlanetsCard planetsCard -> {
                result.setLength(0);
                result.append("[PLANETS]\n")
                        .append("Flight days lost for landing: ")
                        .append(planetsCard.getFlightDaysLoss())
                        .append("\n")
                        .append(describePlanets(planetsCard.getPlanets()));
            }
            case SabotageCard sabotageCard -> {
                result.setLength(0);
                result.append("[SABOTAGE]\n")
                        .append("Sabotage! Tough luck to whoever has the least crew.\n");
            }
            case SlaversCard slaversCard -> {
                result.setLength(0);
                result.append("[SLAVERS]\n")
                        .append("Firepower needed to defeat slavers: ").append(slaversCard.getFirePowerThreshold())
                        .append("\n")
                        .append("Crew lost if not defeated: ").append(slaversCard.getCrewLoss())
                        .append("\n")
                        .append("Credits reward for defeating: ").append(slaversCard.getCreditPrize())
                        .append("\n")
                        .append("Flight days lost for defeating: ").append(slaversCard.getFlightDaysLoss())
                        .append("\n");
            }
            case SmugglersCard smugglersCard -> {
                result.setLength(0);
                result.append("[SMUGGLERS]\n")
                        .append("Firepower needed to defeat smugglers: ")
                        .append(smugglersCard.getFirePowerThreshold())
                        .append("\n")
                        .append("Goods received for defeating smugglers:\n")
                        .append(describeGoods(smugglersCard.getGoodsPrize()))
                        .append("\n")
                        .append("Flight days lost for defeating smugglers: ")
                        .append(smugglersCard.getFlightDaysLoss())
                        .append("\n")
                        .append("Goods lost if not defeated: ")
                        .append(smugglersCard.getGoodsLoss())
                        .append("\n");
            }
            case StarDustCard starDustCard -> {
                result.setLength(0);
                result.append("[STAR DUST]\n")
                        .append("Hope you have those connectors sealed!\n");
            }
        }
        return List.of(result.toString());
    }

    private static String describePlanets(List<Map<GoodsType, Integer>> planets) {
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
            sb.append("planet ").append(i).append(": ");

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

    private static String describeGoods(Map<GoodsType, Integer> goods) {
        if (goods.isEmpty()) return "No goods.";

        List<GoodsType> orderedGoods = List.of(GoodsType.BLUE, GoodsType.GREEN, GoodsType.YELLOW, GoodsType.RED);

        return orderedGoods.stream()
                .flatMap(type -> Collections.nCopies(goods.getOrDefault(type, 0), type.name().toLowerCase()).stream())
                .collect(Collectors.joining(", "));
    }

    private static String describeProjectiles(List<Projectile> projectiles) {
        if(projectiles.isEmpty()) return "No projectiles.";

        StringBuilder result = new StringBuilder();
        for(Projectile projectile : projectiles) {
            result.append(projectile.type())
                    .append(" coming from ")
                    .append(projectile.direction())
                    .append("\n");
        }
        return result.toString();
    }
}