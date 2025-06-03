package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ModelObserver {
    //region Event update methods
    void notifyMetaState(MetaState metaState);
    void notifyCurrentState(GameState gameState);

    void notifyRequestRandComponent(ShipBoard shipBoard, Component component);

    void notifyRequestComponent(ShipBoard shipBoard, Component component);

    void notifyRejectComponent(ShipBoard shipBoard, Component component);
    void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition);

    void notifyStashComponent(ShipBoard shipBoard, Component component);
    void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition);

    void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component);

    void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, Direction orientation);
    void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, Direction orientation, Point oldPosition);

    void notifyFlipHourglass(ShipBoard shipBoard);

    void notifyHourglassEnd();

    void notifyFlightBoardPosition(ShipBoard shipBoard, int position);

    void notifyPeekForecast(ShipBoard shipBoard, int deckIndex);

    void setForecastDeck(java.util.List<AdventureCard> adventureCards);

    void notifyReleaseForecast(ShipBoard shipBoard, int index);

    void notifyRemoveComponent(ShipBoard shipBoard, Point point);

    void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed);

    void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces);

    void notifyShipValidated(ShipBoard shipBoard);

    void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents);

    void notifyDrawCard(AdventureCard adventureCard);

    void notifyActivateComponent(ShipBoard shipBoard, Point point);

    void notifyLoseCrew(ShipBoard shipBoard, Point point);

    void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed);

    void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType);

    void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType);

    void notifyUseBattery(ShipBoard shipBoard, Point point);

    void notifyChoosePlanet(ShipBoard shipBoard, int choice);

    void notifyGiveUp(ShipBoard shipBoard);

    void setFinalScores(Map<Player, Integer> finalScores);
    //endregion
}
