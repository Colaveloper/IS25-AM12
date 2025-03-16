//package adventureCards;
//
//import adventureCards.utils.Choice;
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
//                Choice.ACTIVATE_CANNON,
//                Choice.SUBMIT_POWER,
//                Choice.LOSE_RESIDENT,
//                Choice.GRAB_CREDITS,
//                Choice.ASK_NEXT_PLAYER,
//                Choice.LOSE_GOODS,
//                Choice.END_CARD
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
