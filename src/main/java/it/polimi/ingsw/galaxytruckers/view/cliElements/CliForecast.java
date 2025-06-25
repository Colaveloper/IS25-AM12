package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;

import java.util.ArrayList;
import java.util.List;

public class CliForecast extends CliElement {
    private final GameColor[] blockedForecasts;

    /**
     * Constructs a CliForecast with the given blocked forecasts.
     * Each blocked forecast is represented by its GameColor.
     *
     * @param blockedForecasts Array of ShipBoard objects representing blocked forecasts
     */
    public CliForecast(ShipBoard[] blockedForecasts) {
        this.blockedForecasts = new GameColor[blockedForecasts.length];
        for (int i = 0; i < blockedForecasts.length; i++) {
            if (blockedForecasts[i] != null) {
                this.blockedForecasts[i] = blockedForecasts[i].getColor();
            } else {
                this.blockedForecasts[i] = null;
            }
        }
    }

    /**
     * Releases the blocked forecast at the specified index.
     * @param index The index of the blocked forecast to retrieve
     */
    public void removeBlockedForecast(int index) {
        this.blockedForecasts[index] = null;
        setDirty();
    }

    /**
     * Sets the forecast at the specified index as blocked.
     *
     * @param index The index of the blocked forecast to set
     * @param gameColor The GameColor of the player that took it
     */
    public void setBlockedForecasts(int index, GameColor gameColor) {
        this.blockedForecasts[index] = gameColor;
        setDirty();
    }

    @Override
    protected List<String> getNewDescription() {
        List<String> result = new ArrayList<>();

        for (int i=0; i<blockedForecasts.length; i++) {
            List<String> forecastDescription = new ArrayList<>();
            forecastDescription.add(blockedForecasts[i] != null
                            ? "   "+GameColorCliMapper.toAnsiBullet(blockedForecasts[i])+"   "
                            : " free  "
                    );
            forecastDescription.add("   "+i+"   ");
            result = DescriptionUtils.sideBySide(result, forecastDescription);
        }

        return DescriptionUtils.borderAndTitle(result, "forecast decks");
    }
}