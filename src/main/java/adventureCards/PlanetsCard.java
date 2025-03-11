package adventureCards;

import java.util.Optional;
import java.util.Set;

public class PlanetsCard extends AdventureCard{
    //attributes
    private Optional<Set<Integer>> planets;

    //methods
    @Override
    public void activate(){
        //TODO: implement method
    }

    public Optional<Set<Integer>> choosePlanet(){return planets;}
}
