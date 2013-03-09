package org;

class InvalidMove extends Exception {
	public InvalidMove() {
		System.out.println("Invalid move!");
	}
}

interface Marble extends BoardInterface{
	void setkilled();
	
	int getkilled();
	
    int getcp();
    
    void setcp();
	
	int[] getPos(int i);// returns the position of marble number i

	void select(int i, int j);// selects a marble, also implements rules for
								// selection of more then 1 marble
	
	void setPos(int i, int a, int b) throws InvalidMove;// sets the position of
														// a marble (implements
														// rules for which
														// positions are
														// available

	void move(int i, int j) throws InvalidMove;// moves the selected group,
												// don't confuse it with
												// setpos!! Moves are made by
												// getting the left most
												// possible marble which can
												// move to that field and moving
												// it there then running the
												// move recursive for the rest;
												// Have to fix all the errors it
												// is throwing ...

	void push(int i, int j) throws InvalidMove;// basic idea of the algo is: we
												// check the positions around
												// (i,j) for the opposite player
												// color (if there is more then
												// 1 then the push isn't
												// possible so we should throw
												// an InvalidMove() this is kept
												// by the variable s). We call
												// firstmoved to find the
												// closest marble and create
												// diff array which keeps the
												// distance between the
												// firstmoved marble and the
												// rest of the selected ones. We
												// check if c<s if it is then an
												// InvalidMove() is thrown
												// otherwise we select and move
												// the opposite color marbles in
												// the way, then we move ours.
												//Exceptions should be added to this method!!

	int firstmoved(int i, int j);// returns the left most marble that can
									// also be moved to i,j from the selected

	void refresh();// due to the current representation (I think it is going to
					// be final) using one board object for both white and black
					// marbles the pos[][] array needs to be refreshed after a
					// move is made
}

class WhiteMarbles implements Marble {
	private int pos[][] = new int[14][4];
	private int i;
	private int j;
	private int cp=2;
	private int killed=0;

	WhiteMarbles(int i, int j) {
		this.i = i;
		this.j = j;
		int s = 0;
		for (int p = 0; p < i; p++)
			for (int q = 0; q < j; q++) {
				if (board.get(p, q) == -1) {
					pos[s][0] = p;
					pos[s][1] = q;
					pos[s][2] = 0;
					pos[s][3] = -1;
					s++;
				}
			}
	}
	
	public void setkilled(){
		killed++;
	}
	
	public int getkilled(){
		return killed;
	}
	
	public void setcp()
	{cp=1;}
	
	public int getcp()
	{	return cp;}
	
	public void refresh() {
		int s = 0;
		for (int p = 0; p < i; p++)
			for (int q = 0; q < j; q++) {
				if (board.get(p, q) == -1) {
					pos[s][0] = p;
					pos[s][1] = q;
					pos[s][2] = 0;
					pos[s][3] = -1;
					//board.set(p, q, -1);
					if(s<13)
						s++;
				}
			}

	}

	public int[] getPos(int i) {
		return pos[i];
	}

	public int firstmoved(int i, int j) {
		int c = 0;
		int s = 0;
		int[][] temp = new int[3][3];
		for (int p = 0; p < 3; p++)
			for (int q = 0; q < 2; q++)
				temp[p][q] = -100;
		for (int p = 0; p < 3; p++)
			temp[p][2] = pos.length;
		for (int p = 0; p < pos.length; p++) {
			if (pos[p][2] == 2) {
				temp[c][0] = pos[p][0];
				temp[c][1] = pos[p][1];
				temp[c][2] = p;
				if (c < 2)
					c++;
				else
					break;
			}
		}
		while (s <= c) {
			if (temp[s][2] < pos.length) {
				if (Math.abs(temp[s][0] - i) == 1
						&& Math.abs(temp[s][1] - j) == 1
						|| Math.abs(temp[s][0] - i) == 0
						&& Math.abs(temp[s][1] - j) == 1
						|| Math.abs(temp[s][0] - i) == 1
						&& Math.abs(temp[s][1] - j) == 0) {
					return temp[s][2];
				} else
					s++;
			}
		}
		return 14;
	}

	public void select(int i, int j) {
		int c = 0;
		int pointer1 = 14;
		int pointer2 = 14;
		for (int p = 0; p < 14; p++) {
			if (pos[p][2] == 2)
				c++;
			if (pos[p][3] == 0)
				pointer1 = p;
			if (pos[p][3] == 1)
				pointer2 = p;
		}
		if (c > 2)
			System.out.println("You can't select more marbles!");
		else {
			if (board.get(i, j) == -1)
				board.set(i, j, 2);// has a counter and a pointer to help track
									// of the order of selection and which
									// number marble has been selected
									// respectively
			for (int p = 0; p < 14; p++) {
				if (pos[p][0] == i) {
					if (pos[p][1] == j) {
						pos[p][3] = c;
						if (pos[p][3] == 0) {
							pos[p][2] = 2;
						} else {
							if (pos[p][3] == 1) {
								if (Math.abs(pos[p][0] - pos[pointer1][0]) < 2)
									if (Math.abs(pos[p][1] - pos[pointer1][1]) < 2)
										if(Math.abs(pos[p][0] - pos[pointer1][0]+pos[p][1] - pos[pointer1][1])!=0)
											pos[p][2] = 2;
							}// checks for two next to each other
							else {
								if (pos[p][3] == 2) {
									if (pos[pointer1][0] == pos[pointer2][0]
											&& pos[pointer2][0] == pos[p][0]) {
										if (Math.abs(pos[pointer2][1]
												- pos[p][1]) < 2
												|| Math.abs(pos[pointer1][1]
														- pos[p][1]) < 2)
											pos[p][2] = 2;
									} else if (pos[pointer1][1] == pos[pointer2][1]
											&& pos[pointer2][1] == pos[p][1]) {
										if (Math.abs(pos[pointer2][0]
												- pos[p][0]) < 2
												|| Math.abs(pos[pointer1][0]
														- pos[p][0]) < 2)
											pos[p][2] = 2;
									} else if (pos[pointer1][0] < pos[pointer2][0]) {
										if (pos[p][0] - pos[pointer1][0] == -1
												&& pos[p][1] - pos[pointer1][1] == -1
												|| pos[p][0] - pos[pointer1][0] == 2
												&& pos[p][1] - pos[pointer1][1] == 2)
											pos[p][2] = 2;
										else if (pos[p][0] - pos[pointer2][0] == -1
												&& pos[p][1] - pos[pointer2][1] == -1
												|| pos[p][0] - pos[pointer2][0] == 2
												&& pos[p][1] - pos[pointer2][1] == 2)
											pos[p][2] = 2;// checks for 3
															// marbles next to
															// each other
									}
								}
							}
						}
					}
				}
			}
		}
	}// select is given the coordinates of a marble to be selected. It first
		// checks if you can select more marbles, then it checks if the marble
		// is of the correct kind and then it selects it.

	public void setPos(int i, int a, int b) throws InvalidMove {
		int p, q;
		if (pos[i][2] == 2)
			if (board.get(a, b) != -1)
				if (Math.abs(pos[i][0] - a) < 2 && a >= 0 && a <= 8
						&& Math.abs(pos[i][1] - b) < 2 && b >= 0 && b <= 8)
					if (Math.abs(pos[i][0] - a + pos[i][1] - b) != 0) {
						p = pos[i][0];
						q = pos[i][1];
						pos[i][0] = a;
						pos[i][1] = b;
						pos[i][2] = 0;
						board.set(p, q, 0);
						board.set(a, b, -1);
					} else {
						board.set(pos[i][0], pos[i][1], -1);
						throw new InvalidMove();
					}
				else {
					board.set(pos[i][0], pos[i][1], -1);
					throw new InvalidMove();
				} // stays commeted until I
					// figure a
			// way to fix it to throw errors when the move is really not
			// valid
			else {
				board.set(pos[i][0], pos[i][1], -1);
				throw new InvalidMove();
			}
		else {
			board.set(pos[i][0], pos[i][1], -1);
			System.out.println(3);
			throw new InvalidMove();
		}
		// System.out.println("Not a valid move!");
	}

	// setPos is given the number of a marble, it first checks if the marble
	// is
	// selected, then checks if the move is valid and then it makes the
	// move.

	public void move(int i, int j) throws InvalidMove {
		System.out.println("In white move");
		int c = 0;
		int closest = firstmoved(i, j);
		int[][] temp = new int[3][3];// holds position and number of marble
		for (int p = 0; p < 3; p++)
			for (int q = 0; q < 2; q++)
				temp[p][q] = -100;
		for (int p = 0; p < 3; p++)
			temp[p][2] = 14;
		for (int p = 0; p < 14; p++)
			if (pos[p][2] == 2) {
				temp[c][0] = pos[p][0];
				temp[c][1] = pos[p][1];
				temp[c][2] = p;
				if (c < 3)
					c++;
				else
					break;
			}
		if (i >= 9 || j >= 9 || i<0 || j <0) {
			int current = firstmoved(i, j);
			board.set(pos[current][0],pos[current][1],-1);
		} else if (board.get(i, j) == 1) {
			push(i, j);
		} else
			for (int s = 0; s < 3; s++) {
				if (temp[s][2] == closest) {
					setPos(temp[s][2], i, j);
					pos[temp[s][2]][0]=i;
					pos[temp[s][2]][1]=j;
					board.print();
					if (c > 1) {
						closest = firstmoved(temp[s][0], temp[s][1]);
						for (int p = 0; p < 3; p++) {
							if (temp[p][2] == closest) {
								move(temp[p][0] - temp[s][0] + i, temp[p][1]
										- temp[s][1] + j);
							}
						}
					}
					break;
				}
			}
		refresh();
	}

	public void push(int i, int j) throws InvalidMove {
		System.out.println("push called");
		BlackMarbles black = new BlackMarbles(9, 9);
		int closest = pos.length;
		int c = 0;
		int s = 1;
		int diff[][] = { { -9, -9 }, { -9, -9 }, { -9, -9 } };
		int[] min = new int[2];
		int[][] temp = new int[3][3];// holds position and number of marble
		for (int p = 0; p < 3; p++)
			for (int q = 0; q < 2; q++)
				temp[p][q] = -100;
		for (int p = 0; p < 3; p++)
			temp[p][2] = pos.length;
		for (int p = 0; p < pos.length; p++)
			if (pos[p][2] == 2) {
				temp[c][0] = pos[p][0];
				temp[c][1] = pos[p][1];
				temp[c][2] = p;
				if (c < 2)
					c++;
				else
					break;
			}
		closest = firstmoved(i, j);
		for (int p = 0; p <= c; p++) {
			if (temp[p][2] == closest) {
				if (p == 0) {
					diff[0][0] = temp[p][0] - temp[p + 1][0];
					diff[0][1] = temp[p][1] - temp[p + 1][1];
					diff[1][0] = temp[p][0] - temp[p + 2][0];
					diff[1][1] = temp[p][1] - temp[p + 2][1];
				} else if (p == 1) {
					diff[0][0] = temp[p][0] - temp[p + 1][0];
					diff[0][1] = temp[p][1] - temp[p + 1][1];
					diff[1][0] = temp[p][0] - temp[p - 1][0];
					diff[1][1] = temp[p][1] - temp[p - 1][1];
				} else {
					diff[0][0] = temp[p][0] - temp[p - 1][0];
					diff[0][1] = temp[p][1] - temp[p - 1][1];
					diff[1][0] = temp[p][0] - temp[p - 2][0];
					diff[1][1] = temp[p][1] - temp[p - 2][1];
				}

			}
		}
		for (int q = 0; q <= c; q++) {
			if ((Math.abs(diff[q][0]) == 1 && Math.abs(diff[q][1]) == 0)
					|| (Math.abs(diff[q][0]) == 1 && Math.abs(diff[q][1]) == 1)
					|| (Math.abs(diff[q][0]) == 0 && Math.abs(diff[q][1]) == 1)) {
				min[0] = diff[q][0];
				min[1] = diff[q][1];
			}

		}
		if (board.get(min[0] + i, min[1] + j) == 1)
			s++;

		if (s < 3) {
			if (c >= s) {
				for (int q = 0; q <= c; q++) {
					if (Math.abs(pos[closest][0] - i) == Math.abs(min[0])
							&& Math.abs(pos[closest][1] - j) == Math
									.abs(min[1])) {
						if (Math.abs(diff[q][0]) < 3
								&& Math.abs(diff[q][1]) < 3) {
							if (s == 1) {
								black.select(i, j);
								break;
							} else {
								black.select(i, j);
								black.select(diff[q][0] + i, diff[q][1] + j);
								break;
							}
						}
					} else
						throw new InvalidMove();
				}
				try {
					black.move((s) * min[0] + i, (s) * min[1] + j);
				} catch (InvalidMove e) {
				}
				try {
					move(i, j);
				} catch (InvalidMove e) {
				}
			} else
				throw new InvalidMove();
		} else
			throw new InvalidMove();
		refresh();
	}
}

class BlackMarbles implements Marble {
	private int pos[][] = new int[14][4];
	private int i;
	private int j;
	private int cp=2;
	private int killed=0;
	
	BlackMarbles(int i, int j) {
		this.i = i;
		this.j = j;
		int s = 0;
		for (int p = 0; p < i; p++)
			for (int q = 0; q < j; q++) {
				if (board.get(p, q) == 1) {
					pos[s][0] = p;
					pos[s][1] = q;
					pos[s][2] = 0;
					pos[s][3] = -1;
					s++;
				}
			}
	}
	
	public void setkilled(){
		killed++;
	}
	
	public int getkilled(){
		return killed;
	}
	
	public void setcp(){
		cp=1;
	}
	
	public int getcp(){
		return cp;
	}
	
	public void refresh() {
		int s = 0;
		for (int p = 0; p < i; p++)
			for (int q = 0; q < j; q++) {
				if (board.get(p, q) == 1) {
					pos[s][0] = p;
					pos[s][1] = q;
					pos[s][2] = 0;
					pos[s][3] = -1;
					//board.set(p,q,1);
					if(s<13)
						s++;
				}
			}

	}

	public int[] getPos(int i) {
		return pos[i];
	}

	public int firstmoved(int i, int j) {
		int c = 0;
		int s = 0;
		int[][] temp = new int[3][3];
		for (int p = 0; p < 3; p++)
			for (int q = 0; q < 2; q++)
				temp[p][q] = -100;
		for (int p = 0; p < 3; p++)
			temp[p][2] = pos.length;
		for (int p = 0; p < pos.length; p++) {
			if (pos[p][2] == 2) {
				temp[c][0] = pos[p][0];
				temp[c][1] = pos[p][1];
				temp[c][2] = p;
				if (c < 2)
					c++;
				else
					break;
			}
		}
		while (s <= c) {
			if (temp[s][2] < pos.length) {
				if (Math.abs(temp[s][0] - i) == 1
						&& Math.abs(temp[s][1] - j) == 1
						|| Math.abs(temp[s][0] - i) == 0
						&& Math.abs(temp[s][1] - j) == 1
						|| Math.abs(temp[s][0] - i) == 1
						&& Math.abs(temp[s][1] - j) == 0) {
					return temp[s][2];
				} else
					s++;
			}
		}
		return 14;
	}

	public void select(int i, int j) {
		int c = 0;
		int pointer1 = 14;
		int pointer2 = 14;
		for (int p = 0; p < 14; p++) {
			if (pos[p][2] == 2)
				c++;
			if (pos[p][3] == 0)
				pointer1 = p;
			if (pos[p][3] == 1)
				pointer2 = p;
		}
		if (c > 2)
			System.out.println("You can't select more marbles!");
		else {
			if (board.get(i, j) == 1)
				board.set(i, j, 2);// has a counter and a pointer to help track
									// of the order of selection and which
									// number marble has been selected
									// respectively
			for (int p = 0; p < 14; p++) {
				if (pos[p][0] == i) {
					if (pos[p][1] == j) {
						pos[p][3] = c;
						if (pos[p][3] == 0) {
							pos[p][2] = 2;
						} else {
							if (pos[p][3] == 1) {
								if (Math.abs(pos[p][0] - pos[pointer1][0]) < 2)
									if (Math.abs(pos[p][1] - pos[pointer1][1]) < 2)
										if(Math.abs(pos[p][0] - pos[pointer1][0]+pos[p][1] - pos[pointer1][1])!=0)
											pos[p][2] = 2;
							}// checks for two next to each other
							else {
								if (pos[p][3] == 2) {
									if (pos[pointer1][0] == pos[pointer2][0]
											&& pos[pointer2][0] == pos[p][0]) {
										if (Math.abs(pos[pointer2][1]
												- pos[p][1]) < 2
												|| Math.abs(pos[pointer1][1]
														- pos[p][1]) < 2)
											pos[p][2] = 2;
									} else if (pos[pointer1][1] == pos[pointer2][1]
											&& pos[pointer2][1] == pos[p][1]) {
										if (Math.abs(pos[pointer2][0]
												- pos[p][0]) < 2
												|| Math.abs(pos[pointer1][0]
														- pos[p][0]) < 2)
											pos[p][2] = 2;
									} else if (pos[pointer1][0] < pos[pointer2][0]) {
										if (pos[p][0] - pos[pointer1][0] == -1
												&& pos[p][1] - pos[pointer1][1] == -1
												|| pos[p][0] - pos[pointer1][0] == 2
												&& pos[p][1] - pos[pointer1][1] == 2)
											pos[p][2] = 2;
										else if (pos[p][0] - pos[pointer2][0] == -1
												&& pos[p][1] - pos[pointer2][1] == -1
												|| pos[p][0] - pos[pointer2][0] == 2
												&& pos[p][1] - pos[pointer2][1] == 2)
											pos[p][2] = 2;// checks for 3
															// marbles next to
															// each other
									}
								}
							}
						}
					}
				}
			}
		}
	}// select is given the coordinates of a marble to be selected. It first

	public void setPos(int i, int a, int b) throws InvalidMove {
		int p, q;
		if (pos[i][2] == 2)
			if (board.get(a, b) != 1)
				if (Math.abs(pos[i][0] - a) < 2 && a >= 0 && a <= 8
						&& Math.abs(pos[i][1] - b) < 2 && b >= 0 && b <= 8)
					if (Math.abs(pos[i][0] - a + pos[i][1] - b) != 0) {
						p = pos[i][0];
						q = pos[i][1];
						pos[i][0] = a;
						pos[i][1] = b;
						pos[i][2] = 0;
						board.set(p, q, 0);
						board.set(a, b, 1);
					} else {
						board.set(pos[i][0], pos[i][1], 1);
						throw new InvalidMove();
					}
				else {
					board.set(pos[i][0], pos[i][1], 1);
					throw new InvalidMove();
				}
			else {
				board.set(pos[i][0], pos[i][1], 1);
				throw new InvalidMove();
			}
		else {
			board.set(pos[i][0], pos[i][1], 1);
			throw new InvalidMove();
		}
	}

	// setPos is given the number of a marble, it first checks if the marble is
	// selected, then checks if the move is valid and then it makes the
	// move.

	public void move(int i, int j) throws InvalidMove {
		System.out.println("In black move");
		int c = 0;
		int closest = firstmoved(i, j);
		int[][] temp = new int[3][3];// holds position and number of marble
		for (int p = 0; p < 3; p++)
			for (int q = 0; q < 2; q++)
				temp[p][q] = -100;
		for (int p = 0; p < 3; p++)
			temp[p][2] = 14;
		for (int p = 0; p < 14; p++)
			if (pos[p][2] == 2) {
				temp[c][0] = pos[p][0];
				temp[c][1] = pos[p][1];
				temp[c][2] = p;
				if (c < 3)
					c++;
				else
					break;
			}
		if (i >= 9 || j >= 9 || i<0 || j<0) {
			int current = firstmoved(i, j);
			board.set(pos[current][0],pos[current][1],1);
		} else if (board.get(i, j) == -1) {
			push(i, j);
		} else
			for (int s = 0; s < 3; s++) {
				if (temp[s][2] == closest) {
					setPos(temp[s][2], i, j);
					pos[temp[s][2]][0]=i;
					pos[temp[s][2]][1]=j;
					board.print();
					if (c > 1) {
						closest = firstmoved(temp[s][0], temp[s][1]);
						for (int p = 0; p < 3; p++) {
							if (temp[p][2] == closest) {
								move(temp[p][0] - temp[s][0] + i, temp[p][1]
										- temp[s][1] + j);
							}
						}
					}
					break;
				}
			}
		//refresh();
	}


	public void push(int i, int j) throws InvalidMove {
		WhiteMarbles white = new WhiteMarbles(9, 9);
		int closest = pos.length;
		int c = 0;
		int s = 1;
		int diff[][] = { { -9, -9 }, { -9, -9 }, { -9, -9 } };
		int[] min = new int[2];
		int[][] temp = new int[3][3];// holds position and number of marble
		for (int p = 0; p < 3; p++)
			for (int q = 0; q < 2; q++)
				temp[p][q] = -100;
		for (int p = 0; p < 3; p++)
			temp[p][2] = pos.length;
		for (int p = 0; p < pos.length; p++)
			if (pos[p][2] == 2) {
				temp[c][0] = pos[p][0];
				temp[c][1] = pos[p][1];
				temp[c][2] = p;
				if (c < 2)
					c++;
				else
					break;
			}
		closest = firstmoved(i, j);
		for (int p = 0; p <= c; p++) {
			if (temp[p][2] == closest) {
				if (p == 0) {
					diff[0][0] = temp[p][0] - temp[p + 1][0];
					diff[0][1] = temp[p][1] - temp[p + 1][1];
					diff[1][0] = temp[p][0] - temp[p + 2][0];
					diff[1][1] = temp[p][1] - temp[p + 2][1];
				} else if (p == 1) {
					diff[0][0] = temp[p][0] - temp[p + 1][0];
					diff[0][1] = temp[p][1] - temp[p + 1][1];
					diff[1][0] = temp[p][0] - temp[p - 1][0];
					diff[1][1] = temp[p][1] - temp[p - 1][1];
				} else {
					diff[0][0] = temp[p][0] - temp[p - 1][0];
					diff[0][1] = temp[p][1] - temp[p - 1][1];
					diff[1][0] = temp[p][0] - temp[p - 2][0];
					diff[1][1] = temp[p][1] - temp[p - 2][1];
				}

			}
		}
		for (int q = 0; q <= c; q++) {
			if ((Math.abs(diff[q][0]) == 1 && Math.abs(diff[q][1]) == 0)
					|| (Math.abs(diff[q][0]) == 1 && Math.abs(diff[q][1]) == 1)
					|| (Math.abs(diff[q][0]) == 0 && Math.abs(diff[q][1]) == 1)) {
				min[0] = diff[q][0];
				min[1] = diff[q][1];
			}

		}
		if (board.get(min[0] + i, min[1] + j) == 1)
			s++;
		if (s < 3) {
			if (c >= s) {
				for (int q = 0; q <= c; q++) {
					if (Math.abs(pos[closest][0] - i) == Math.abs(min[0])
							&& Math.abs(pos[closest][1] - j) == Math
									.abs(min[1])) {
						if (Math.abs(diff[q][0]) < 3
								&& Math.abs(diff[q][1]) < 3) {
							if (s == 1) {
								white.select(i, j);
								break;
							} else {
								white.select(i, j);
								white.select(diff[q][0] + i, diff[q][1] + j);
								break;
							}
						}
					} else
						throw new InvalidMove();
				}
				try {
					white.move((s) * min[0] + i, (s) * min[1] + j);
				} catch (InvalidMove e) {
				}
				try {
					move(i, j);
				} catch (InvalidMove e) {
				}
			} else
				throw new InvalidMove();
		} else
			throw new InvalidMove();
		//refresh();
	}
}


public class BoardGUI {
	public static void main(String[] args){
		int s = 0;
		WhiteMarbles white = new WhiteMarbles(9, 9);
		BlackMarbles black = new BlackMarbles(9, 9);
		white.select(2,4);
		try{
			white.move(3,4);	
		}catch(InvalidMove e){}

		black.select(4,2);
		black.select(5,2);
		black.select(6,2);
		try{
		    black.move(3,2);
		}catch(InvalidMove e){}

		white.select(3,4);
		white.select(3,5);
		white.select(3,6);
		try{
		    white.move(3,3);
		}catch(InvalidMove e){}
		black.select(3,0);
		try{
		    black.move(3,1);
		}catch(InvalidMove e){}

		white.select(3,3);
		white.select(3,4);
		white.select(3,5);
		try{
		    white.move(3,2);
		}catch(InvalidMove e){}
		
		white.select(3,2);
		white.select(3, 3);
		white.select(3,4);
		try{
			white.move(3, 1);
		}catch (InvalidMove e){}
		
		white.select(3,1);
		white.select(3, 2);
		white.select(3,3);
		try{
			white.move(3,4);
		}catch(InvalidMove e){}
		
		white.select(3,2);
		white.select(3, 3);
		white.select(3,4);
		try{
			white.move(3,5);
		}catch(InvalidMove e){}
		for(int i=0;i<14;i++){s++; System.out.println(black.getPos(i)[0]+" "+black.getPos(i)[1]+" "+s);}
		
	}

}

