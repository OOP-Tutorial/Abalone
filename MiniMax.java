package org;

import java.util.ArrayList;


class MiniMax{
    
    final int DEPTH = 4;
    private float[] weight = {1,1,1,1,1,1,1};
    
    public MiniMax(float[] w){
	this.weight = w;
    }
    public float[] getWeights(){
	return this.weight;
    }
    public void setWeights(float[] w){
	this.weight = w;
    }
    public int eval(BoardAI board, int player){
      int result = 0;
      result += this.getWeights()[0]*gameResult(board, player);
      result += this.getWeights()[1]*marbles(board, player);
      result += this.getWeights()[2]*centreDistance(board, player);
      result += this.getWeights()[3]*grouping(board, player);
      result += this.getWeights()[4]*grouping(board, (3 - player));
      result += this.getWeights()[5]*attack(board, player);
      result += this.getWeights()[6]*attack(board, (3 - player));
      return result;
    }
    public int[] nextMove(BoardAI board, int player){
            ArrayList<int[]> moves = board.getMoves(player);
            int[] values = new int[moves.size()];
            for(int i = 0; i < moves.size();i++){
              int l = board.move(moves.get(i));
              values[i]=min(board, DEPTH-1, player);
              board.move(board.reverse(moves.get(i),l));
            }
            int best = maxInt(values);
            return moves.get(best);
    }
    public int max(BoardAI board, int depth, int player){
        if(depth == 0 || board.getMarbles(1).size() == 8 || board.getMarbles(2).size() == 8){
          return eval(board, player);
        }
        else{
          ArrayList<int[]> moves = board.getMoves(player);
          int[] values = new int[moves.size()];
          for(int i = 0; i < moves.size();i++){
            int l = board.move(moves.get(i));
            values[i]=min(board, depth-1, player);
            board.move(board.reverse(moves.get(i),l));
          }
          return maxInt(values);
        }
    }
        
    public int min(BoardAI board, int depth, int player){
        if(depth == 0 || board.getMarbles(1).size() == 8 || board.getMarbles(2).size() == 8){
          return eval(board, player);
        }
        else{
          ArrayList <int[]> moves = board.getMoves(2);
          int[] values = new int[moves.size()];
          for(int i = 0; i < moves.size();i++){
              int l = board.move(moves.get(i));
              values[i]= max(board, depth-1, player);
              board.move(board.reverse(moves.get(i),l));
          }
          return minInt(values);
        }
    }
    
    private int minInt(int[] values) {
	int min = values[0];       // start with max = first element

	     for(int i = 1; i<values.length; i++)
	     {
	          if(values[i] < min)
	                min = values[i];
	     }
	return min;   
    }
    private int maxInt(int[] values) {
	int max = values[0];       // start with max = first element

	     for(int i = 1; i<values.length; i++)
	     {
	          if(values[i] >   max)
	                max = values[i];
	     }
	return max;  
    }
    public int gameResult(BoardAI board, int player){
        if(board.getMarbles(player).size() == 8){
            return Integer.MIN_VALUE;
        }
        else if(board.getMarbles(3 - player).size() == 8){
            return Integer.MAX_VALUE;
        }
        else{
            return 0;
        }
    }
    public int marbles(BoardAI board, int player){
        return board.getMarbles(player).size();
    }
    public float centreDistance(BoardAI board, int player){
	float distance = 0;
	for(int[] cood : board.getMarbles(player)){
	    distance += Math.sqrt((cood[0] - 4)*(cood[0] - 4) + (cood[1] - 4)*(cood[1] - 4));
	}
        return distance;
    }
    public int grouping(BoardAI board, int player){
	int g = 0;
        for(int[] marble : board.getMarbles(player)){
            for(int[] d : board.dir){
        	if(board.getBoard()[marble[1] + d[1]][marble[1] + d[1]] == player){
        	    g++;
        	}
            }
        }
        return g;
    }
    public int attack(BoardAI board, int player){
	int att = 0;
        for(int[] move : board.getLineMoves(player)){
            if(board.getBoard()[move[2] + move[4]][move[1] + move[3]] == player && board.getBoard()[move[2] + 2*move[4]][move[1] + 2 * move[3]] == 3 - player){
        	att++;
            }
            else if(board.getBoard()[move[2] + 2*move[4]][move[1] + 2*move[3]] == player && board.getBoard()[move[2] + 3*move[4]][move[1] + 3 * move[3]] == 3 - player){
        	att++;
            }
        }
        return att;
    }
}
