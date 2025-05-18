//package it.polimi.ingsw.galaxytruckers.view;
//
//import it.polimi.ingsw.galaxytruckers.view.adventureClient.AdventureCard;
//import it.polimi.ingsw.galaxytruckers.view.adventureClient.GoodsBuffer;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GoodsBufferTest {
//
//    @Test
//    void testDescribeGoodsBufferFillAndTake() throws IOException {
//        AdventureCard currentCard = new AdventureCard(32);
//        GoodsBuffer goods = new GoodsBuffer(currentCard);
//
//        // simulate taking the second good - GREEN
//        goods.takeGood(2);
//        String result = goods.describeGoodsBuffer();
//
//        String expected = """
//                [Goods]
//                ------------------------------
//                | YELLOW  | TAKEN   | GREEN   |
//                ------------------------------""";
//
//        assertEquals(expected, result);
//    }
//}