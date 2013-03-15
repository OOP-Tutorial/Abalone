package org;

import java.awt.Container;

import javax.swing.JFrame;

public class Board {
	private int board[][] /*= {
			{-1,-1,-1,-1,-1,-2,-2,-2,-2},
			{-1,-1,-1,-1,-1,-1,-2,-2,-2},
			{0,0,-1,-1,-1,0,0,-2,-2},
			{0,0,0,0,0,0,0,0,-2},
			{0,0,0,0,0,0,0,0,0},
			{-2,0,0,0,0,0,0,0,0},
			{-2,-2,0,0,1,1,1,0,0},
			{-2,-2,-2,1,1,1,1,1,1},
			{-2,-2,-2,-2,1,1,1,1,1}
			    }*/;
	private int cplayer;
	
	public int getCplayer() {
	    return cplayer;
	}

	public void setCplayer(int cplayer) {
	    this.cplayer = cplayer;
	}

	Board(int i, int j) {
		board = new int[i][j];
		for (int p = 0; p < i; p++)
			for (int q = 0; q < j; q++)
				board[p][q] = -2; // Due to some combinations not making sense
		for (int p = -(Math.abs(i / 2)); p <= 0; p++)
			for (int q = -(Math.abs(j / 2)); q <= p + i / 2; q++)
				board[p + i / 2][q + j / 2] = 0;
		for (int p = 1; p <= i / 2; p++)
			for (int q = -(Math.abs(j / 2)) + p; q <= j / 2; q++)
				board[p + i / 2][q + j / 2] = 0;// Init of board
		for (int p = 0; p < i; p++)
			for (int q = 0; q < j; q++)
				if (board[p][q] == 0
						&& (q - p) > 2
						|| (p >= 2 && p <= 4 && q >= 4 && q <= 6 && Math.abs(p
								- q) > 1))
					board[p][q] = -1;// Init of white marbles (upper part of
										// board) I know it's bad and doesn't
										// cover the general case. If someone
										// comes up with a better idea I am up
										// for it.
		for (int p = 0; p < i; p++)
			for (int q = 0; q < j; q++)
				if (board[p][q] == 0
						&& (p - q) > 2
						|| (q >= 2 && q <= 4 && p >= 4 && p <= 6 && Math.abs(p
								- q) > 1))
					board[p][q] = 1;// Init of black marbles 
	}

	public int get(int i, int j) throws ArrayIndexOutOfBoundsException{
		return board[i][j];
	}
	
	public int getT(int j, int i) throws ArrayIndexOutOfBoundsException{
		if(-(4+i-j)<0||-(4+i-j)>8) return -2;
		else
			return board[i][-(4+i-j)];
	}

	public void set(int i, int j, int v) throws ArrayIndexOutOfBoundsException{
		if (board[i][j] != -2)
			board[i][j] = v;
		else
			System.out.println("Invalid set location!");
	}
	
	public void setT(int j, int i, int v) throws ArrayIndexOutOfBoundsException{
		if(-(4+i-j)<0||-(4+i-j)>8){System.out.println("Invalid set location!");}
		else{
			if (board[i][-(4+i-j)] != -2)
				board[i][-(4+i-j)] = v;
			else
				System.out.println("Invalid set location!");}
	}
	
	public int[][] getBoard(){
		return this.board;
	} 
	public void setBoard(int[][] b ){
		this.board = b;
	} 
	public void print() {
		

		/*for (int y = 0; y < this.board.length; y++) {
			System.out.print(new String(new char[Math.abs(y - 4)]).replace(
					"\0", " "));
			for (int x = 0; x < this.board[y].length; x++) {
				if (board[x][y] != -2) {
					System.out.print(board[x][y] + " ");
				}
			}
			System.out.println();
		}	*/
	}
}
