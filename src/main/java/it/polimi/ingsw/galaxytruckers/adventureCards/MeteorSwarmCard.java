//package adventureCards;
//
//import adventureCards.utils.CardState;
//import adventureCards.utils.Goods;
//import adventureCards.utils.Projectile;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class MeteorSwarmCard extends AdventureCard{
//    private final List<Integer> projectileDirections;
//    private final List<Projectile> projectileTypes;
//
//
//    public MeteorSwarmCard(List<Integer> projectileDirections, List<Projectile> projectileTypes) {
//        super(Arrays.asList(
//                CardState.ACTIVATE_CANNON,
//                CardState.ASK_NEXT_PLAYER,
//                CardState.END_CARD
//        ));
//        this.projectileDirections = projectileDirections;
//        this.projectileTypes = projectileTypes;
//    }
//    @Override
//    public List<Boolean> getPlanets() {     //do nothing
//        return null;
//    }
//
//
//    @Override
//    public int getFirePower() {     //do nothing
//        return 0;
//    }   //do nothing
//
//    @Override
//    public int getCredits() {       //do nothing
//        return 0;
//    }   //do nothing
//
//    @Override
//    public List<Goods> getGoods() {         //do nothing
//        return null;
//    }
//
//    @Override
//    public List<Integer> getProjectileDirections() {
//        return projectileDirections;
//    }
//
//    @Override
//    public List<Projectile> getProjectilesType() {
//        return projectileTypes;
//    }
//
//    @Override
//    public int getSacrifice() {     //do nothing
//        return 0;
//    }   //do nothing
//
//
//    @Override
//    public void landOnPlanet(int i) {       //do nothing
//
//    }
//
//
//
//}