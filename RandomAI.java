package org;

import java.util.*;
public class RandomAI {
    
    Random r;
    
    public RandomAI(){
	r = new Random();
    }
    
    public int[] nextMove(BoardAI board, int player){
	int i = r.nextInt(board.getMoves(player).size());
	return board.getMoves(player).get(i);
    }
}
