//package it.polimi.ingsw.galaxytruckers.network.client.rmi;
//
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
//import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
//import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
//import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteController;
//import it.polimi.ingsw.galaxytruckers.network.server.rmi.RemoteServer;
//import it.polimi.ingsw.galaxytruckers.view.Direction;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//import java.awt.*;
//import java.rmi.RemoteException;
//import java.util.UUID;
//
//import static org.mockito.Mockito.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RmiClientTest {
//
//    RmiClient rmiClient;
//    ClientController clientController;
//    RemoteServer remoteServer;
//    RemoteController remoteController;
//
//    @BeforeEach
//    void setUp() throws RemoteException {
//        rmiClient = new RmiClient();
//    }
//
//    @Test
//    void connectWithNoServerThrowsException() {
//        assertThrows(RuntimeException.class, () -> rmiClient.start("", "127.0.0.1", 12345));
//    }
//
//    @Test
//    void setClientController() {
//        rmiClient.setClientController(clientController);
//        assertEquals(clientController, rmiClient.getClientController());
//    }
//
//    @Test
//    void testNetworkException() {
//        rmiClient.setServerController(new DisconnectedServerStub());
//        assertThrows(RuntimeException.class, () -> rmiClient.registerNickname(""));
//    }
//
//    @Nested
//    class MethodMatchTest {
//        MethodChecker checker;
//
//        @BeforeEach
//        void setUp() {
//            checker = mock(MethodChecker.class);
//            remoteServer = new RemoteServerStub(checker);
//            remoteController = new RemoteControllerStub(checker);
//            rmiClient.setServerController(remoteServer);
//            rmiClient.setRemoteController(remoteController);
//        }
//
//        @Test
//        void correctSetupTest() {
//            assertEquals(remoteController, rmiClient.getRemoteController());
//            assertEquals(remoteServer, rmiClient.getServer());
//        }
//
//        @Test
//        void registerNickname() {
//            rmiClient.registerNickname("nickname");
//           verify(checker).registerNickname("nickname");
//        }
//
//        @Test
//        void requestNewGame(){
//            rmiClient.requestNewGame(Level.SECOND, 2);
//           verify(checker).requestNewGame(Level.SECOND, 2);
//        }
//
//        @Test
//        void drawCard(){
//            rmiClient.drawCard();
//           verify(checker).drawCard();
//        }
//
//        @Test
//        void joinLobby(){
//            UUID id = UUID.randomUUID();
//            rmiClient.joinLobby(id);
//           verify(checker).joinLobby(id);
//        }
//
//        @Test
//        void leaveLobby(){
//            rmiClient.leaveLobby();
//           verify(checker).leaveLobby();
//        }
//
//        @Test
//        void requestRandComponent(){
//            rmiClient.requestRandComponent();
//           verify(checker).requestRandComponent();
//        }
//
//        @Test
//        void requestComponent(){
//            rmiClient.requestComponent(0);
//           verify(checker).requestComponent(0);
//        }
//
//        @Test
//        void rejectComponent(){
//            rmiClient.rejectComponent();
//           verify(checker).rejectComponent();
//        }
//
//        @Test
//        void stashComponent(){
//            rmiClient.stashComponent();
//           verify(checker).stashComponent();
//        }
//
//        @Test
//        void grabStashedComponent(){
//            rmiClient.grabStashedComponent(0);
//           verify(checker).grabStashedComponent(0);
//        }
//
//        @Test
//        void placeComponent(){
//            rmiClient.placeComponent(new Point(0, 0), Direction.UP);
//           verify(checker).placeComponent(new Point(0, 0), Direction.UP);
//        }
//
//        @Test
//        void flipHourglass(){
//            rmiClient.flipHourglass();
//           verify(checker).flipHourglass();
//        }
//
//        @Test
//        void placeShipOnFlightBoard(){
//            rmiClient.placeShipOnFlightBoard(0);
//           verify(checker).placeShipOnFlightBoard(0);
//        }
//
//        @Test
//        void acquireForecast(){
//            rmiClient.acquireForecast(0);
//           verify(checker).acquireForecast(0);
//        }
//
//        @Test
//        void releaseForecast(){
//            rmiClient.releaseForecast();
//           verify(checker).releaseForecast();
//        }
//
//        @Test
//        void removeComponent(){
//            rmiClient.removeComponent(new Point(0, 0));
//           verify(checker).removeComponent(new Point(0, 0));
//        }
//
//        @Test
//        void chooseShipPiece(){
//            rmiClient.chooseShipPiece(0);
//           verify(checker).chooseShipPiece(0);
//        }
//
//        @Test
//        void initializeCabin(){
//            rmiClient.initializeCabin(new Point(0, 0), CrewType.HUMAN);
//           verify(checker).initializeCabin(new Point(0, 0), CrewType.HUMAN);
//        }
//
//        @Test
//        void activateComponent(){
//            rmiClient.activateComponent(new Point(0, 0));
//           verify(checker).activateComponent(new Point(0, 0));
//        }
//
//        @Test
//        void loseCrew(){
//            rmiClient.loseCrew(new Point(0, 0));
//           verify(checker).loseCrew(new Point(0, 0));
//        }
//
//        @Test
//        void grabReward() {
//            rmiClient.grabReward();
//            verify(checker).grabReward();
//        }
//
//        @Test
//        void placeGoods() {
//            rmiClient.placeGoods(new Point(0,0),GoodsType.RED);
//            verify(checker).placeGoods(new Point(0,0),GoodsType.RED);
//        }
//
//        @Test
//        void removeGoods() {
//            rmiClient.removeGoods(new Point(0,0),GoodsType.RED);
//            verify(checker).removeGoods(new Point(0,0),GoodsType.RED);
//        }
//
//        @Test
//        void loseGoods() {
//            rmiClient.loseGoods(new Point(0,0));
//            verify(checker).loseGoods(new Point(0,0));
//        }
//
//        @Test
//        void useBattery() {
//            rmiClient.useBattery(new Point(0,0));
//            verify(checker).useBattery(new Point(0,0));
//        }
//
//        @Test
//        void choosePlanet() {
//            rmiClient.choosePlanet(0);
//            verify(checker).choosePlanet(0);
//        }
//
//        @Test
//        void goNext() {
//            rmiClient.goNext();
//            verify(checker).goNext();
//        }
//
//        @Test
//        void giveUp() {
//            rmiClient.giveUp();
//            verify(checker).giveUp();
//        }
//
//        @Test
//        void notifyEvent() {
//            //TODO: test this method
//        }
//
//    }
//
//}
//
//class DisconnectedServerStub implements RemoteServer {
//    @Override
//    public RemoteController registerNickname(RemoteClient client, String nickname) throws RemoteException {
//        throw new RemoteException();
//    }
//}
//
//class RemoteServerStub implements RemoteServer {
//    MethodChecker checker;
//
//    public RemoteServerStub(MethodChecker checker) {
//        this.checker = checker;
//    }
//
//    @Override
//    public RemoteController registerNickname(RemoteClient client, String nickname){
//        checker.registerNickname(nickname);
//        return null;
//    }
//}
//
//class RemoteControllerStub implements RemoteController {
//    MethodChecker checker;
//    public RemoteControllerStub(MethodChecker checker) {
//        this.checker = checker;
//    }
//
//    @Override
//    public void ping(){
//    }
//
//    @Override
//    public void newGame(Level level, int numPlayers){
//        checker.requestNewGame(level, numPlayers);
//    }
//
//    @Override
//    public void joinLobby(UUID lobbyID){
//        checker.joinLobby(lobbyID);
//    }
//
//    @Override
//    public void leaveLobby(){
//        checker.leaveLobby();
//    }
//
//    @Override
//    public void requestRandComponent(){
//        checker.requestRandComponent();
//    }
//
//    @Override
//    public void requestComponent(int componentID){
//        checker.requestComponent(componentID);
//    }
//
//    @Override
//    public void rejectComponent(){
//        checker.rejectComponent();
//    }
//
//    @Override
//    public void stashComponent(){
//        checker.stashComponent();
//    }
//
//    @Override
//    public void grabPlacedComponent() throws RemoteException {
//        checker.grabPlacedComponent();
//    }
//
//    @Override
//    public void grabStashedComponent(int index){
//        checker.grabStashedComponent(index);
//    }
//
//    @Override
//    public void placeComponent(Point point, Direction orientation){
//        checker.placeComponent(point, orientation);
//    }
//
//    @Override
//    public void flipHourglass(){
//        checker.flipHourglass();
//    }
//
//    @Override
//    public void placeShipOnFlightBoard(int startingPosition){
//        checker.placeShipOnFlightBoard(startingPosition);
//    }
//
//    @Override
//    public void placeShipOnFlightBoard() throws RemoteException {
//        checker.placeShipOnFlightBoard();
//    }
//
//    @Override
//    public void acquireForecast(int deckIndex){
//        checker.acquireForecast(deckIndex);
//    }
//
//    @Override
//    public void releaseForecast(){
//        checker.releaseForecast();
//    }
//
//    @Override
//    public void removeComponent(Point point){
//        checker.removeComponent(point);
//    }
//
//    @Override
//    public void chooseShipPiece(int pieceIndex){
//        checker.chooseShipPiece(pieceIndex);
//    }
//
//    @Override
//    public void initializeCabin(Point point, CrewType crewType){
//        checker.initializeCabin(point,crewType);
//    }
//
//    @Override
//    public void drawCard(){
//        checker.drawCard();
//    }
//
//    @Override
//    public void activateComponent(Point point){
//        checker.activateComponent(point);
//    }
//
//    @Override
//    public void loseCrew(Point point){
//        checker.loseCrew(point);
//    }
//
//    @Override
//    public void grabReward(){
//        checker.grabReward();
//    }
//
//    @Override
//    public void placeGoods(Point point, GoodsType goodsType){
//        checker.placeGoods(point,goodsType);
//    }
//
//    @Override
//    public void removeGoods(Point point, GoodsType goodsType){
//        checker.removeGoods(point,goodsType);
//    }
//
//    @Override
//    public void loseGoods(Point point){
//        checker.loseGoods(point);
//    }
//
//    @Override
//    public void useBattery(Point point){
//        checker.useBattery(point);
//    }
//
//    @Override
//    public void choosePlanet(int choice){
//        checker.choosePlanet(choice);
//    }
//
//    @Override
//    public void goNext(){
//        checker.goNext();
//    }
//
//    @Override
//    public void giveUp(){
//        checker.giveUp();
//    }
//}