//package adventureCards;
//
//import adventureCards.utils.CardState;
//import adventureCards.utils.Goods;
//import adventureCards.utils.Projectile;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class SlaversCard extends AdventureCard{
//
//    private final int firePower;
//    private final int credits;
//    private final int sacrifices;
//
//    public SlaversCard(int firePower, int credits, int sacrifices) {
//        super(Arrays.asList(
//                CardState.ACTIVATE_CANNON,
//                CardState.SUBMIT_POWER,
//                CardState.LOSE_RESIDENT,
//                CardState.GRAB_CREDITS,
//                CardState.ASK_NEXT_PLAYER,
//                CardState.LOSE_GOODS,
//                CardState.END_CARD
//        ));
//        this.firePower = firePower;
//        this.credits = credits;
//        this.sacrifices = sacrifices;
//    }
//
//
//
//    @Override
//    public void landOnPlanet(int i) {       //do nothing
//
//    }
//
//    @Override
//    public List<Boolean> getPlanets() {     //do nothing
//        return null;
//    }
//
//    @Override
//    public int getFirePower() {
//        return firePower;
//    }
//
//    @Override
//    public int getCredits() {
//        return credits;
//    }
//
//    @Override
//    public List<Goods> getGoods() {         //do nothing
//        return null;
//    }
//
//    @Override
//    public List<Integer> getProjectileDirections() {//do nothing
//        return null;
//    }//do nothing
//
//    @Override
//    public List<Projectile> getProjectilesType() {                  //do nothing
//        return null;
//    }
//
//    @Override
//    public int getSacrifice() {
//        return sacrifices;
//    }
//}
