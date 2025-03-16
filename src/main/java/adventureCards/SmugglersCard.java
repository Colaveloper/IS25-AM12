//package adventureCards;
//
//import adventureCards.utils.Choice;
//import adventureCards.utils.Goods;
//import adventureCards.utils.Projectile;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class SmugglersCard extends AdventureCard{
//
//    private int firePower;
//    private final List<Goods> loot;   //TODO: add Goods enum
//
//    public SmugglersCard(List<Goods> loot) {
//        super(Arrays.asList(
//                Choice.ACTIVATE_CANNON,
//                Choice.SUBMIT_POWER,
//                Choice.LOSE_GOODS,
//                Choice.GRAB_GOODS,
//                Choice.END_CARD
//        ));
//        this.loot = loot;
//    }
//
//
//    @Override
//    public void landOnPlanet(int i) {   //do nothing
//
//    }
//
//
//    @Override
//    public List<Boolean> getPlanets() { //do nothing
//        return null;
//    }
//
//    @Override
//    public int getFirePower() {
//        return firePower;
//    }
//
//    @Override
//    public int getCredits() {                           //do nothing
//        return 0;
//    }
//
//    @Override
//    public List<Goods> getGoods() {     //do nothing
//        return null;
//    }
//
//    @Override
//    public List<Integer> getProjectileDirections() {    //do nothing
//        return null;
//    }//do nothing
//
//    @Override
//    public List<Projectile> getProjectilesType() {  //do nothing
//        return null;
//    }
//
//    @Override
//    public int getSacrifice() {                         //do nothing
//        return 0;
//    }       //do nothing
//}
