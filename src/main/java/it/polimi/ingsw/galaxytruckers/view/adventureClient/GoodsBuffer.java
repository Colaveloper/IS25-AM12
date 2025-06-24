//package it.polimi.ingsw.galaxytruckers.view.adventureClient;
//
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
//import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
//import it.polimi.ingsw.galaxytruckers.view.cli.CliElement;
//import javafx.scene.Node;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class GoodsBuffer extends CliElement {
//    private List<Optional<GoodsType>> goodsBuffer;
//
//    public GoodsBuffer(AdventureCard currentCard){
//        goodsBuffer = new ArrayList<>();
//        // creating flat buffer of all goods
//        if(currentCard.getGoods().isPresent()){
//            currentCard.getGoods().get().forEach((type, count) ->{
//                for (int i = 0; i < count; i++){
//                    goodsBuffer.add(Optional.of(type));
//                }
//            });
//        }
//        else{
//            goodsBuffer.add(Optional.empty());
//        }
//    }
//
//    public GoodsBuffer(Planets planets, int planetIndex){
//        goodsBuffer = new ArrayList<>();
//        planets.getPlanetGoods(planetIndex).forEach((type, count) ->{
//            for (int i = 0; i < count; i++){
//                goodsBuffer.add(Optional.of(type));
//            }
//        });
//    }
//
//    public boolean takeGood(int indexIn) {
//        int index = indexIn -1;
//        if (index < 0 || index >= goodsBuffer.size()) return false;
//        if (goodsBuffer.get(index).isEmpty()) return false;
//
//        goodsBuffer.set(index, Optional.empty());
//        return true;
//    }
//
//    public String describeGoodsBuffer() {
//        if (goodsBuffer == null || goodsBuffer.isEmpty()) return "[Goods]\n(empty)";
//
//        // dynamically building the visual "buffer" size based on the number of goods
//        final int boxWidth = 10;
//        int totalWidth = goodsBuffer.size() * boxWidth;
//
//        StringBuilder sb = new StringBuilder("[Goods]\n");
//        sb.append("-".repeat(totalWidth)).append("\n");
//
//        for (Optional<GoodsType> good : goodsBuffer) {
//            String label = good.map(t -> t.name()).orElse("TAKEN");
//            sb.append(String.format("| %-8s", label));
//        }
//        sb.append("|\n");
//
//        sb.append("-".repeat(totalWidth));
//        return sb.toString();
//    }
//
//    @Override
//    public List<String> getNewDescription() {
//        List<String> result = new ArrayList<>();
//        result.add("Here are the goods available as a reward:");
//        result.add(describeGoodsBuffer());
//        return result;
//    }
//
//    @Override
//    public Node getNode(VirtualServer server) {
//        return null;
//    }
//}
