package it.polimi.ingsw.galaxytruckers.view;

//usable characters
//  □   ●   ◘	▼▶▲◀	⊐⊏⊓⊔	Ͳ	Θ	Ϫ	Ѫ	֎	①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳    ♠♥♦♣
//  ─ 	━ 	│ 	┃ 	┄ 	┅ 	┆ 	┇ 	┈ 	┉ 	┊ 	┋ 	┌ 	┍ 	┎ 	┏
//  ┐ 	┑ 	┒ 	┓ 	└ 	┕ 	┖ 	┗ 	┘ 	┙ 	┚ 	┛ 	├ 	┝ 	┞ 	┟
//  ┠ 	┡ 	┢ 	┣ 	┤ 	┥ 	┦ 	┧ 	┨ 	┩ 	┪ 	┫ 	┬ 	┭ 	┮ 	┯
//  ┰ 	┱ 	┲ 	┳ 	┴ 	┵ 	┶ 	┷ 	┸ 	┹ 	┺ 	┻ 	┼ 	┽ 	┾ 	┿
//  ╀ 	╁ 	╂ 	╃ 	╄ 	╅ 	╆ 	╇ 	╈ 	╉ 	╊ 	╋ 	╌ 	╍ 	╎ 	╏
//  ═ 	║ 	╒ 	╓ 	╔ 	╕ 	╖ 	╗ 	╘ 	╙ 	╚ 	╛ 	╜ 	╝ 	╞ 	╟
//  ╠ 	╡ 	╢ 	╣ 	╤ 	╥ 	╦ 	╧ 	╨ 	╩ 	╪ 	╫ 	╬ 	╭ 	╮ 	╯
//  ╰ 	╱ 	╲ 	╳ 	╴ 	╵ 	╶ 	╷ 	╸ 	╹ 	╺ 	╻ 	╼ 	╽ 	╾ 	╿


public class Printer {

    public void printShip() {

        System.out.println(
                "▲ cannon\t\t"  +
                "⊓ engine\t\t"  +
                " shield\n"     +
                "Θ battery\t\t" +
                "▞ storage\t\t" +
                "● cabin\t\t"   +
                "Ѫ life support\n"
        );

        for (int matRow = 0; matRow < 4; matRow++) {                    //all components by rows from matrix
            //for (int compRow = 0; compRow < 3; compRow++) {             //single component rows
                System.out.print("\t\t");
                for (int matColumn = 0; matColumn < 5; matColumn++) {   //all component columns from matrix
                    //TODO: if(component != vuoto) else print space
                    //TODO: get connector in print
                    System.out.print("╭─" + "┴" + "─╮");                //single comp columns
                }
                System.out.println();

                System.out.print("\t\t");
                for (int matColumn = 0; matColumn < 5; matColumn++) {   //all component columns from matrix
                    //TODO: if(component != vuoto) else print space
                    //TODO: get component type in print (batteries could print quantity too)
                    System.out.print("┤" + " ▲ " + "╞");                //single comp columns
                }
                System.out.println();

                System.out.print("\t\t");
                for (int matColumn = 0; matColumn < 5; matColumn++) {   //all component columns from matrix
                    //TODO: if(component != vuoto) else print space
                    //TODO: get connector in print
                    System.out.print("╰─" + "╥" + "─╯");                //single comp columns
                }
                System.out.println();
            //}
        }
        System.out.println("\n");




//        System.out.println(
//                "\t\t     ╭───╮     ╭───╮     \n" +
//                "\t\t     ╣▲ ▲╞     ╣ ▲ ╞     \n" +
//                "\t\t     ╰─╥─╯     ╰─╥─╯     \n" +
//                "\t\t╭───╮╭─┴─╮╭───╮╭─┴─╮╭─┴─╮\n" +
//                "\t\t│ ▲ ╞╡ ▞ ╞╣ ● ╠┤ Ѫ ││ ● │\n" +
//                "\t\t╰─╥─╯╰───╯╰─╦─╯╰─╦─╯╰─╥─╯\n" +
//                "\t\t╭─┴─╮╭───╮╭─┴─╮╭─┴─╮╭─┴─╮\n" +
//                "\t\t│ ▞ ╠┤   ╞╣ ● ╞╡   ╞╣ ▶ │\n" +
//                "\t\t╰─╥─╯╰─╥─╯╰───╯╰─┬─╯╰─╥─╯\n" +
//                "\t\t╭─┴─╮╭─┴─╮     ╭─┴─╮╭─┴─╮\n" +
//                "\t\t╣ ⊓ ╞╣ ▞ │     ┤ Θ ││⊓ ⊓│\n" +
//                "\t\t╰───╯╰───╯     ╰───╯╰───╯"
//                +"\n\n"
//        );
    }

    public void printCurrentCard() {
        //TODO: get current state
        System.out.println("[here goes what's happening now]\n\n");
    }



    public void printCheckOtherPlayers() {
        System.out.println(
            "A) previous player\t\t" +
            "D) next player\n"
        );
    }

    public void printChoices() {
        System.out.println(
                "1) choice1\t\t" +
                "2) choice2\n" +
                "3) choice3\t\t" +
                "4) choice4\n"
        );
    }

    public void colorTest() {
        System.out.println("\\033[2K");
    }

    public void printAdventureDrawState(){
        colorTest();
        printCurrentCard();
//        printFlightBoard();
        printShip();
        printCheckOtherPlayers();
        printChoices();
    }
}
