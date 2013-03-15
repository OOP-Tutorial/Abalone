package org;

import java.util.*;
public class Candidate implements Comparable<Candidate> {
    public static final int SIZE = 7;
    public float[] genotype;
    private Random rand;

    public Candidate() {
	genotype = new float[SIZE];	
	rand = new Random();
    }

    void random()
    {
	for (int i = 0; i < genotype.length; i++) {
	    genotype[i] = (float) ((rand.nextFloat() - 0.5)*2*Float.MAX_VALUE);
	}
    }

    private String gene()
    {
	String sb = "" + genotype[0];
	for (int i = 1; i < genotype.length; i++) {				
	    sb += ", " + genotype[i];
	}
	return sb;
    }

    int fitness() {
	return 0;
    }
    public int stageGame(){
	int res = 0;
	BoardAI b = new BoardAI();
	MiniMax me = new MiniMax(this.genotype);
	while(b.getMarbles(1).size() > 8 && b.getMarbles(2).size() > 8){
	    if( b.isPossibleMove(m, b.getCPlayer())){
		b.move(m);
		b.setCPlayer(3 - b.getCPlayer());
	    }
	    else{
		System.out.println("Invalid move");
	    }
	}
	
	
    }
    public int compareTo(Candidate o) {
	int f1 = this.fitness();
	int f2 = o.fitness();

	if (f1 < f2)
	    return 1;
	else if (f1 > f2)
	    return -1;
	else
	    return 0;
    }


    @Override
    public String toString()
    {
	return "gene="+gene()+" fit="+fitness();
    }
}