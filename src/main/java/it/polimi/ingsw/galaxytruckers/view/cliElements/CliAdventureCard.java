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
        List<String> description = new ArrayList<>();
//        description.add("[NO CARD]"); // default value
        switch(card){
            case AbandonedShipCard abandonedShipCard -> {
                description.add("[ABANDONED SHIP]");
                description.add("Flight days lost: " + abandonedShipCard.getFlightDaysLoss());
                description.add("Credits prize: " + abandonedShipCard.getCreditPrize());
                description.add("Required crew sacrifice: " + abandonedShipCard.getRequiredCrew());
            }
            case AbandonedStationCard abandonedStationCard -> {
                description.add("[ABANDONED STATION]");
                description.addAll(describeGoods(abandonedStationCard.getGoodsPrize()));
                description.add("Flight days lost: " + abandonedStationCard.getFlightDaysLoss());
                description.add("Required crew sacrifice: " + abandonedStationCard.getRequiredCrew());
            }
            case CombatZoneCard combatZoneCard -> {
                description.add("[COMBAT ZONE]");
                List<CombatZoneCheck> checks = combatZoneCard.getChecks();
                List<Penalty> penalties = combatZoneCard.getPenalties();
                for (int i = 0; i < checks.size(); i++) {
                    description.add("Whoever has " + checks.get(i) + " " + penalties.get(i));
                }
            }
            case EpidemicCard epidemicCard -> {
                description.add("[EPIDEMIC]");
                description.add("An epidemic strikes!");
            }
            case MeteorSwarmCard meteorSwarmCard -> {
                description.add("[METEOR SWARM]");
                description.add(describeProjectiles(meteorSwarmCard.getProjectiles()));
            }
            case OpenSpaceCard openSpaceCard -> {
                description.add("[OPEN SPACE]");
                description.add("Warm up those engines!");
            }
            case PiratesCard piratesCard -> {
                description.add("[PIRATES]");
                description.add(describeProjectiles(piratesCard.getProjectiles()));
                description.add("Needed firepower to defeat pirates: " + piratesCard.getFirePowerThreshold());
                description.add("Credits reward for defeating: " + piratesCard.getCreditPrize());
                description.add("Flight days lost: " + piratesCard.getFlightDaysLoss());
            }
            case PlanetsCard planetsCard -> {
                description.add("[PLANETS]");
                description.add("Flight days lost for landing: " + planetsCard.getFlightDaysLoss());
                description.addAll(describePlanets(planetsCard.getPlanets()));
            }
            case SabotageCard sabotageCard -> {
                description.add("[SABOTAGE]");
                description.add("Sabotage! Tough luck to whoever has the least crew.");
            }
            case SlaversCard slaversCard -> {
                description.add("[SLAVERS]");
                description.add("Firepower needed to defeat slavers: " + slaversCard.getFirePowerThreshold());
                description.add("Crew lost if not defeated: " + slaversCard.getCrewLoss());
                description.add("Credits reward for defeating: " + slaversCard.getCreditPrize());
                description.add("Flight days lost for defeating: " + slaversCard.getFlightDaysLoss())
                        ;
            }
            case SmugglersCard smugglersCard -> {
                description.add("[SMUGGLERS]");
                description.add("Firepower needed to defeat smugglers: " + smugglersCard.getFirePowerThreshold());
                description.add("Goods received for defeating smugglers:" + describeGoods(smugglersCard.getGoodsPrize()));
                description.add("Flight days lost for defeating smugglers: " + smugglersCard.getFlightDaysLoss());
                description.add("Goods lost if not defeated: " + smugglersCard.getGoodsLoss())
                        ;
            }
            case StarDustCard starDustCard -> {
                description.add("[STAR DUST]");
                description.add("Hope you have those connectors sealed!");
            }
        }
        return description;
    }

    private static List<String> describePlanets(List<Map<GoodsType, Integer>> planets) {
        /*
        * entrySet().stream() — loop over all good types per planet
        * Collections.nCopies() — repeat goods names based on count
        * flatMap() — flatten the repeated names into one stream
        * joining(", ") — turn them into the nice comma-separated string
        * */
        List<String> description = new ArrayList<>();
        StringBuilder row;
        if (planets.isEmpty()) return List.of("No planets.");

        List<GoodsType> orderedGoods = List.of(GoodsType.BLUE, GoodsType.GREEN, GoodsType.YELLOW, GoodsType.RED);

        for (int i = 0; i < planets.size(); i++) {
            row = new StringBuilder();
            row.append("planet ").append(i).append(": ");

            int finalI = i;
            String goods = orderedGoods.stream()
                    .flatMap(type -> {
                        int count = planets.get(finalI).getOrDefault(type, 0);
                        return Collections.nCopies(count, type.name().toLowerCase()).stream();
                    })
                    .collect(Collectors.joining(", "));

            row.append(goods);
            description.add(row.toString());
        }

        return description;
    }

    private static List<String> describeGoods(Map<GoodsType, Integer> goods) {
        if (goods.isEmpty()) return List.of("No goods.");

        List<GoodsType> orderedGoods = List.of(GoodsType.BLUE, GoodsType.GREEN, GoodsType.YELLOW, GoodsType.RED);

        return orderedGoods.stream()
                .flatMap(type -> Collections.nCopies(goods.getOrDefault(type, 0), type.name().toLowerCase()).stream())
                .collect(Collectors.toList());
    }

    private static String describeProjectiles(List<Projectile> projectiles) {
        if(projectiles.isEmpty()) return "No projectiles.";

        StringBuilder result = new StringBuilder();
        for(Projectile projectile : projectiles) {
            result.append(projectile.type())
                    .append(" coming from ")
                    .append(projectile.direction());
        }
        return result.toString();
    }
}