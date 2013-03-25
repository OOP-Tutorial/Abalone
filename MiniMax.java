package myAbalone.org;

import java.util.ArrayList;


class MiniMax{

    final int DEPTH = 2;
    private float[] weight = {1000000,100,5,1000,-1000,15,-15};

    public MiniMax(){}
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
	ArrayList<int[]> marbles = board.getMarbles(player);
	ArrayList<int[]> marblesOpp = board.getMarbles(3 - player);
	result += this.getWeights()[0]*gameResult(marbles, marblesOpp);
	result += this.getWeights()[1]*marbles(marbles);
	result += this.getWeights()[2]*centreDistance(marbles, player);
	result += this.getWeights()[3]*grouping(board, player);
	result += this.getWeights()[4]*grouping(board, (3 - player));
	result += this.getWeights()[5]*attack(board, player);
	result += this.getWeights()[6]*attack(board, (3 - player));
	return result;
    }
    public int[] nextMove(BoardAI board, int player){
	ArrayList<int[]> moves = board.getMoves(player);
	int[] values = new int[moves.size()];
	BoardAI cp;
	for(int i = 0; i < moves.size();i++){
	    cp = new BoardAI(board.getCPlayer(), copyArray(board.getBoard()));
	    cp.move(moves.get(i));
	    values[i] = min(cp, DEPTH-1, player);
	}
	int best = maxPos(values);
	return moves.get(best -1);
    }
    public int max(BoardAI board, int depth, int player){
	if(depth == 0 || board.getMarbles(1).size() == 8 || board.getMarbles(2).size() == 8){
	    return eval(board, player);
	}
	else{
	    ArrayList<int[]> moves = board.getMoves(player);
	    int[] values = new int[moves.size()];
	    BoardAI cp;
	    for(int i = 0; i < moves.size();i++){
		cp = new BoardAI(board.getCPlayer(), copyArray(board.getBoard()));
		cp.move(moves.get(i));
		values[i] = min(cp, depth-1, player);
	    }
	    return maxPos(values);
	}
    }
    public int min(BoardAI board, int depth, int player){
	if(depth == 0 || board.getMarbles(1).size() == 8 || board.getMarbles(2).size() == 8){
	    return eval(board, player);
	}
	else{
	    ArrayList <int[]> moves = board.getMoves(2);
	    int[] values = new int[moves.size()];
	    BoardAI cp;
	    for(int i = 0; i < moves.size();i++){
		cp = new BoardAI(board.getCPlayer(), copyArray(board.getBoard()));
		cp.move(moves.get(i));
		values[i]= max(cp, depth-1, player);
	    }
	    return minPos(values);
	}
    }
    private int minPos(int[] values) {
	int min = values[0];       // start with max = first element
	int pos = 0;

		for(int i = 1; i<values.length; i++)
		{
		    if(values[i] < min)
			min = values[i];
		    pos = i;
		}
	return pos;   
    }
    private int maxPos(int[] values) {
	int max = values[0];       // start with max = first element
	int pos = 0;
		for(int i = 1; i<values.length; i++)
		{
		    if(values[i] >   max)
			max = values[i];
		    pos = i;
		}
		return pos;  
    }
    public int gameResult(ArrayList<int[]> marbles, ArrayList<int[]> marblesOpp){
	if(marbles.size() == 8){
	    return Integer.MIN_VALUE;
	}
	else if(marblesOpp.size() == 8){
	    return Integer.MAX_VALUE;
	}
	else{
	    return 0;
	}
    }
    public int marbles(ArrayList<int[]> marbles){
	return marbles.size();
    }
    public float centreDistance(ArrayList<int[]> marbles, int player){
	float distance = 0;
	for(int[] cood : marbles){
	    distance += Math.sqrt((cood[0] - 4)*(cood[0] - 4) + (cood[1] - 4)*(cood[1] - 4));
	}
	return distance;
    }
    public int grouping(BoardAI board, int player){
	int g = 0;
	for(int[] marble : board.getMarbles(player)){
	    for(int[] d : board.dir){
		if(board.get(marble[1] + d[1], marble[1] + d[1]) == player){
		    g++;
		}
	    }
	}
	return g;
    }
    public int attack(BoardAI board, int player){
	int att = 0;
	for(int[] move : board.getLineMoves(player)){
	    if(board.get(move[1] + move[3], move[0] + move[2]) == player && board.get(move[1] + 2*move[3], move[0] + 2 * move[1]) == 3 - player){
		att++;
	    }
	    else if(board.get(move[1] + 2*move[3],move[0] + 2*move[2]) == player && board.get(move[1] + 3*move[3], move[0] + 3 * move[2]) == 3 - player){
		att++;
	    }
	}
	return att;
    }
    private int[][] copyArray(int[][] a){
	int[][] ret = new int[a.length][a[0].length];
	for(int i = 0; i < a.length; i++){
	    for(int j = 0; j < a[0].length; j++){
		ret[i][j] = a[i][j];
	    }
	}
	return ret;
    }
}
