package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.Lobby;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class Screen {
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {}

    public void notifyRequestComponent(ShipBoard shipBoard, Component component){}

    public void notifyRejectComponent(ShipBoard shipBoard, Component component){}

    public void notifyStashComponent(ShipBoard shipBoard, Component component){}

    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component){}

    public void notifyGrabPlacedComponent(ShipBoard shipBoard, Point prevPosition) {}

    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation){}

    public void notifyFlipHourglass(ShipBoard shipBoard){}

    public void notifyHourglassEnd(){}

    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position){}

    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex){}

    public void setForecastDeck(List<AdventureCard> adventureCards){}

    public void notifyReleaseForecast(ShipBoard shipBoard, int index){}

    public void notifyRemoveComponent(ShipBoard shipBoard, Point point){}

    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed){}

    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces){}

    public void notifyShipValidated(ShipBoard shipBoard){}

    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents){}

    public void notifyDrawCard(AdventureCard adventureCard){}

    public void notifyActivateComponent(ShipBoard shipBoard, Point point){}

    public void notifyLoseCrew(ShipBoard shipBoard, Point point){}

    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed){}

    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType){}

    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType){}

    public void notifyUseBattery(ShipBoard shipBoard, Point point){}

    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipBoard){}

    public void notifyGiveUp(ShipBoard shipBoard){}

    public void setFinalScores(Map<Player, Integer> finalScores){}

    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition){}

    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {}

    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, Direction orientation, Point oldPosition) {}

    public void notifyNewLobby(Lobby lobby){}

    public void notifyRemoveLobby(UUID LobbyId){}
}
