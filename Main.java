package org;

public class Main {

    public static void main(String[] args) {
	WhiteMarbles player1 = new WhiteMarbles(9, 9);
	BlackMarbles player2 = new BlackMarbles(9, 9);
	BoardAI ai = new BoardAI();
	while(true){
	    // if human - wait gui and do turn
	    // if ai - ask for next move
	}
    }

}
