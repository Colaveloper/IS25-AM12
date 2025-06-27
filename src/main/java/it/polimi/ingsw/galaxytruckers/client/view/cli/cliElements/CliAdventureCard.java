package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements;


import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.polimi.ingsw.galaxytruckers.client.model.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.client.model.adventureCards.penalty.*;

/**
 * Represents an adventure card in the CLI.
 * It provides a detailed description of the card's effects and requirements.
 */
public class CliAdventureCard extends CliElement{
    AdventureCard card;

    /**
     * Creates a CLI representation of an adventure card.
     *
     * @param card The adventure card to be represented
     */
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
                List<String> penaltyStrings = new ArrayList<>();
                for(Penalty penalty : penalties){
                    switch (penalty) {
                        case ProjectileThreat projectileThreat -> penaltyStrings.add(" penalty: projectile threat");
                        case CrewLoss crewLoss -> penaltyStrings.add(" penalty: crew loss");
                        case FlightDaysLoss flightDaysLoss -> penaltyStrings.add(" penalty: flight days loss");
                        case GoodsLoss goodsLoss -> penaltyStrings.add(" penalty: goods loss");
                        default -> throw new IllegalStateException("Unexpected value: " + penalty);
                    }
                }
                for (int i = 0; i < checks.size(); i++) {
                    description.add("Check the " + checks.get(i) + penaltyStrings.get(i));
                }
            }
            case EpidemicCard epidemicCard -> {
                description.add("[EPIDEMIC]");
                description.add("An epidemic strikes!");
            }
            case MeteorSwarmCard meteorSwarmCard -> {
                description.add("[METEOR SWARM]");
                description.addAll(describeProjectiles(meteorSwarmCard.getProjectiles()));
            }
            case OpenSpaceCard openSpaceCard -> {
                description.add("[OPEN SPACE]");
                description.add("Warm up those engines!");
            }
            case PiratesCard piratesCard -> {
                description.add("[PIRATES]");
                description.addAll(describeProjectiles(piratesCard.getProjectiles()));
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
            default -> throw new IllegalStateException("Unexpected AdventureCard: " + card);
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

    private static List<String> describeProjectiles(List<Projectile> projectiles) {
        if(projectiles.isEmpty()) return List.of("No projectiles.");
        List<String> description = new ArrayList<>();

        StringBuilder result;
        for(Projectile projectile : projectiles) {
            result = new StringBuilder();
            result.append(projectile.type())
                    .append(" coming from ")
                    .append(projectile.direction());
            description.add(result.toString());
        }

        return description;
    }
}