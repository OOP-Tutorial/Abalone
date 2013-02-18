class InvalidMove extends Exception {
	public InvalidMove() {
		System.out.println("Invalid move!");
	}
}

interface Marble {
	Board board = new Board(9, 9);

	int[] getPos(int i);// returns the position of marble number i

	void select(int i, int j);// selects a marble, also implements rulls for
								// selection of more then 1 marble

	void setPos(int i, int a, int b) throws InvalidMove;// sets the position of
														// a marble (implements
														// rulls for which
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

	int firstmoved(int i, int j);// returns the left most marble that is can
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

	public void refresh() {
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

		if (c > 3)
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
						&& Math.abs(pos[i][1] - b) < 2 && b >= 0 && b <= 8) {
					p = pos[i][0];
					q = pos[i][1];
					pos[i][0] = a;
					pos[i][1] = b;
					pos[i][2] = 0;
					board.set(p, q, 0);
					board.set(a, b, -1);
				} else
					throw new InvalidMove(); // stays commeted until I figure a
			// way to fix it to throw errors when the move is really not
			// valid
			else
				throw new InvalidMove();
		else
			throw new InvalidMove();
		// System.out.println("Not a valid move!");
	}// setPos is given the number of a marble, it first checks if the marble is
		// selected, then checks if the move is valid and then it makes the
		// move.

	public void move(int i, int j) throws InvalidMove {
		if (i < 0 && j < 0)
			return;
		try {
			int c = 0;
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
					if (c < 2)
						c++;
					else
						break;
				}
			if (board.get(i, j) == 1)
				push(i, j);
			else
				for (int p = 0; p < 3; p++) {
					if (temp[p][2] < 14)
						if (Math.abs(temp[p][0] - i) == 1
								&& Math.abs(temp[p][1] - j) == 1
								|| Math.abs(temp[p][0] - i) == 0
								&& Math.abs(temp[p][1] - j) == 1
								|| Math.abs(temp[p][0] - i) == 1
								&& Math.abs(temp[p][1] - j) == 0) {
							setPos(temp[p][2], i, j);
							if (p == 0) {
								if (Math.abs(temp[p][0] - temp[p + 1][0]) < 2
										&& Math.abs(temp[p][1] - temp[p + 1][1]) < 2) {
									if (Math.abs(temp[p][0] - temp[p + 2][0]) < 2
											&& Math.abs(temp[p][1]
													- temp[p + 2][1]) < 2) {
										move(temp[p + 1][0] - temp[p][0] + i,
												temp[p + 1][1] - temp[p][1] + j);
										move(temp[p + 2][0] - temp[p][0] + i,
												temp[p + 2][1] - temp[p][1] + j);
									} else
										move(temp[p + 1][0] - temp[p][0] + i,
												temp[p + 1][1] - temp[p][1] + j);
								} else
									move(temp[p + 2][0] - temp[p][0] + i,
											temp[p + 2][1] - temp[p][1] + j);
							}
							if (p == 1) {
								if (Math.abs(temp[p][0] - temp[p + 1][0]) < 2
										&& Math.abs(temp[p][1] - temp[p + 1][1]) < 2) {
									if (Math.abs(temp[p][0] - temp[p - 1][0]) < 2
											&& Math.abs(temp[p][1]
													- temp[p - 1][1]) < 2) {
										move(temp[p + 1][0] - temp[p][0] + i,
												temp[p + 1][1] - temp[p][1] + j);
										move(temp[p - 1][0] - temp[p][0] + i,
												temp[p - 1][1] - temp[p][1] + j);
									} else
										move(temp[p + 1][0] - temp[p][0] + i,
												temp[p + 1][1] - temp[p][1] + j);
								} else
									move(temp[p - 1][0] - temp[p][0] + i,
											temp[p - 1][1] - temp[p][1] + j);
							}
							if (p == 2) {
								if (Math.abs(temp[p][0] - temp[p - 1][0]) < 2
										&& Math.abs(temp[p][1] - temp[p - 1][1]) < 2) {
									if (Math.abs(temp[p][0] - temp[p - 2][0]) < 2
											&& Math.abs(temp[p][1]
													- temp[p - 2][1]) < 2) {
										move(temp[p - 1][0] - temp[p][0] + i,
												temp[p - 1][1] - temp[p][1] + j);
										move(temp[p - 2][0] - temp[p][0] + i,
												temp[p - 2][1] - temp[p][1] + j);
									} else
										move(temp[p - 1][0] - temp[p][0] + i,
												temp[p - 1][1] - temp[p][1] + j);
								} else
									move(temp[p - 2][0] - temp[p][0] + i,
											temp[p - 2][1] - temp[p][1] + j);
							}
						}
				}
		} catch (InvalidMove e) {
		}
		refresh();
	}

	public void push(int i, int j) throws InvalidMove {
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
		{
			if (board.get(i + 1, j) == 1)
				s++;

			// if (board.get(i + 1, j - 1) == 1)
			// s++;

			// if (board.get(i + 1, j + 1) == 1)
			// s++;

			if (board.get(i, j + 1) == 1)
				s++;

			if (board.get(i, j - 1) == 1)
				s++;

			if (board.get(i - 1, j) == 1)
				s++;

			// if (board.get(i - 1, j + 1) == 1)
			// s++;

			// if (board.get(i - 1, j - 1) == 1)
			// s++;
		}
		if (s < 3) {
			if (c >= s) {
				for (int q = 0; q <= c; q++) {
					if (Math.abs(diff[q][0]) < 3 && Math.abs(diff[q][1]) < 3) {
						if (s == 1) {
							black.select(i, j);
							break;
						} else {
							black.select(i, j);
							black.select(diff[q][0] + i, diff[q][1] + j);
							break;
						}
					}
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

	public void refresh() {
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
			if (board.get(a, b) != 1)
				if (Math.abs(pos[i][0] - a) < 2 && a >= 0 && a <= 8
						&& Math.abs(pos[i][1] - b) < 2 && b >= 0 && b <= 8) {
					p = pos[i][0];
					q = pos[i][1];
					pos[i][0] = a;
					pos[i][1] = b;
					pos[i][2] = 0;
					board.set(p, q, 0);
					board.set(a, b, 1);
				} else
					throw new InvalidMove();
			else
				throw new InvalidMove();
		else
			throw new InvalidMove();
		// System.out.println("Not a valid move!");
	}// setPos is given the number of a marble, it first checks if the marble is
		// selected, then checks if the move is valid and then it makes the
		// move.

	public void move(int i, int j) throws InvalidMove {
		if (i < 0 && j < 0)
			return;
		try {
			int c = 0;
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
					if (c < 2)
						c++;
					else
						break;
				}
			if (board.get(i, j) == -1)
				push(i, j);
			else
				for (int p = 0; p < 3; p++) {
					if (temp[p][2] < 14)
						if (Math.abs(temp[p][0] - i) == 1
								&& Math.abs(temp[p][1] - j) == 1
								|| Math.abs(temp[p][0] - i) == 0
								&& Math.abs(temp[p][1] - j) == 1
								|| Math.abs(temp[p][0] - i) == 1
								&& Math.abs(temp[p][1] - j) == 0) {
							setPos(temp[p][2], i, j);
							if (p == 0) {
								if (Math.abs(temp[p][0] - temp[p + 1][0]) < 2
										&& Math.abs(temp[p][1] - temp[p + 1][1]) < 2) {
									if (Math.abs(temp[p][0] - temp[p + 2][0]) < 2
											&& Math.abs(temp[p][1]
													- temp[p + 2][1]) < 2) {
										move(temp[p + 1][0] - temp[p][0] + i,
												temp[p + 1][1] - temp[p][1] + j);
										move(temp[p + 2][0] - temp[p][0] + i,
												temp[p + 2][1] - temp[p][1] + j);
									} else
										move(temp[p + 1][0] - temp[p][0] + i,
												temp[p + 1][1] - temp[p][1] + j);
								} else
									move(temp[p + 2][0] - temp[p][0] + i,
											temp[p + 2][1] - temp[p][1] + j);
							}
							if (p == 1) {
								if (Math.abs(temp[p][0] - temp[p + 1][0]) < 2
										&& Math.abs(temp[p][1] - temp[p + 1][1]) < 2) {
									if (Math.abs(temp[p][0] - temp[p - 1][0]) < 2
											&& Math.abs(temp[p][1]
													- temp[p - 1][1]) < 2) {
										move(temp[p + 1][0] - temp[p][0] + i,
												temp[p + 1][1] - temp[p][1] + j);
										move(temp[p - 1][0] - temp[p][0] + i,
												temp[p - 1][1] - temp[p][1] + j);
									} else
										move(temp[p + 1][0] - temp[p][0] + i,
												temp[p + 1][1] - temp[p][1] + j);
								} else
									move(temp[p - 1][0] - temp[p][0] + i,
											temp[p - 1][1] - temp[p][1] + j);
							}
							if (p == 2) {
								if (Math.abs(temp[p][0] - temp[p - 1][0]) < 2
										&& Math.abs(temp[p][1] - temp[p - 1][1]) < 2) {
									if (Math.abs(temp[p][0] - temp[p - 2][0]) < 2
											&& Math.abs(temp[p][1]
													- temp[p - 2][1]) < 2) {
										move(temp[p - 1][0] - temp[p][0] + i,
												temp[p - 1][1] - temp[p][1] + j);
										move(temp[p - 2][0] - temp[p][0] + i,
												temp[p - 2][1] - temp[p][1] + j);
									} else
										move(temp[p - 1][0] - temp[p][0] + i,
												temp[p - 1][1] - temp[p][1] + j);
								} else
									move(temp[p - 2][0] - temp[p][0] + i,
											temp[p - 2][1] - temp[p][1] + j);
							}
						}
				}
		} catch (InvalidMove e) {
		}
		refresh();
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
		{
			if (board.get(i + 1, j) == -1)
				s++;

			if (board.get(i, j + 1) == -1)
				s++;

			if (board.get(i, j - 1) == -1)
				s++;

			if (board.get(i - 1, j) == -1)
				s++;

		}
		if (s < 3) {
			if (c >= s) {
				for (int q = 0; q <= c; q++) {
					if (Math.abs(diff[q][0]) < 3 && Math.abs(diff[q][1]) < 3) {
						if (s == 1) {
							white.select(i, j);
							break;
						} else {
							white.select(i, j);
							white.select(diff[q][0] + i, diff[q][1] + j);
							break;
						}
					}
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
		refresh();
	}
}

public class Board {
	private int board[][];

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

	public int get(int i, int j) {
		return board[i][j];
	}

	public void set(int i, int j, int v) {
		if (board[i][j] != -2)
			board[i][j] = v;
		else
			System.out.println("Invalid set location!");
	}

	public static void main(String[] args) {
		int s = 0;
		WhiteMarbles white = new WhiteMarbles(9, 9);
		BlackMarbles black = new BlackMarbles(9, 9);
		white.select(4, 6);
		white.select(4, 7);
		white.select(4, 8);
		black.select(4, 1);
		black.select(4, 2);
		try {
			white.move(4, 5);
			black.move(4, 3);
		} catch (InvalidMove e) {
		}
		white.select(4, 5);
		white.select(4, 6);
		white.select(4, 7);
		for (int i = 0; i < 14; i++)
			if (white.getPos(i)[2] == 2)
				System.out.println(white.getPos(i)[0] + " "
						+ white.getPos(i)[1]);
		black.select(4, 2);
		black.select(4, 3);

		try {
			black.move(4, 4);
		} catch (InvalidMove e) {
		}

		try {
			white.move(4, 4);
		} catch (InvalidMove e) {
		}

		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (Marble.board.get(i, j) == 1) {
					s++;
					System.out.println(i + " " + j + " " + s);
				}
		/*
		 * white.select(3,5); white.select(2,6); for (int i = 0; i < 14; i++) if
		 * (white.getPos(i)[2] == 2) System.out.println(white.getPos(i)[0] + " "
		 * + white.getPos(i)[1] + " " + i + " White Selected"); black.select(5,
		 * 3); for (int i = 0; i < 14; i++) if (black.getPos(i)[2] == 2)
		 * System.out.println(black.getPos(i)[0] + " " + black.getPos(i)[1] +
		 * " " + i + " Black Selected"); try { black.move(4, 4); } catch
		 * (InvalidMove e) { } for (int i = 0; i < 14; i++) if (i == 4)
		 * System.out.println(black.getPos(i)[0] + " " + black.getPos(i)[1] +
		 * " " + i + " Black Moved"); try { white.move(4, 4); } catch
		 * (InvalidMove e) { } for (int i = 0; i < 14; i++) if (i == 6 || i ==
		 * 7) System.out.println(white.getPos(i)[0] + " " + white.getPos(i)[1] +
		 * " " + i + " White Moved"); for(int i=0;i<9;i++) for(int j=0;j<9;j++)
		 * if(Marble.board.get(i,j)==1){s++; System.out.println(i+" "+j+" "+s);}
		 */

		/*
		 * Board test = new Board(9,9); for(int i=0;i<9;i++) for(int
		 * j=0;j<9;j++) if(test.get(i,j)==1) System.out.println(i+" "+j);
		 * System.out.println(test.get(0,5));
		 */
		/*
		 * WhiteMarbles test = new WhiteMarbles(9, 9); test.select(2, 4);
		 * test.select(3, 5); test.select(4, 6); for (int i = 0; i < 14; i++) if
		 * (test.getPos(i)[2] == 2) System.out.println(test.getPos(i)[0] + " " +
		 * test.getPos(i)[1] + " " + i + " Selected"); /* test.setPos(4,4,2);
		 * test.setPos(7,4,4); test.setPos(10,5,5);
		 */
		// System.out.println(test.firstmoved(5, 6)
		// + " is the first marlbe that will be moved");
		/*
		 * try { test.move(3, 4); } catch (InvalidMove e) { } for (int i = 0; i
		 * < 14; i++) if (i == 4 || i == 7 || i == 10)
		 * System.out.println(test.getPos(i)[0] + " " + test.getPos(i)[1] + " "
		 * + i);
		 */
		/*
		 * test.select(1, 4); test.select(2, 4); test.select(3, 4); try {
		 * test.move(0, 4); } catch (InvalidMove e) { } for (int i = 0; i < 14;
		 * i++) if (i == 1 || i == 2 || i == 4)
		 * System.out.println(test.getPos(i)[0] + " " + test.getPos(i)[1] + " "
		 * + i);
		 */
		// System.out.println(test.getPos(0)[0]+" "+test.getPos(0)[1]+" "+test.getPos(0)[2]);
		// test.setPos(0,1,3);
		// System.out.println(test.getPos(0)[0]+" "+test.getPos(0)[1]+" "+test.getPos(0)[2]);
	}
}

// the number 14 everywhere really shouldn't be 14 but be some number n that is
// the number of marbles
// write a statement that throws InvalidMove if after giving a move command the
// marbles stay in their original places!!!
// write a method that if there is an error issued returns all marbles to their
// original places!!
// try and fix select method but for now it isn't that important
// Need to impelent the selection of black marbles in push and work out how to
// get push into move without fucking everything
// up!!!!!!!!!!!!!!!!!!!!!!!!!!!!1111111111111!!!!!!!!!!1
