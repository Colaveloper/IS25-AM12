import java.util.Random;

// DESCRIPTION:
// returns a number between 2 and 12, sum of two random numbers between 1 and 6
//
// USAGE EXAMPLE:
//Dice dice = Dice.create();
//for (int i = 0; i < 5; i++) {
//    System.out.println("You rolled " + dice.roll());
//}


@FunctionalInterface
public interface Dice {
    // single abstract method roll()
    int roll();

    static Dice create() {
        Random random = new Random();

        // implementing roll()
        return () -> random.nextInt(6) + random.nextInt(6) + 2;
    }
}

