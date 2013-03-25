package myAbalone.org;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI extends JPanel{
    BoardAI board;
    MiniMax ai = new MiniMax();
    ArrayList<int[]> selection;
    int[] selectDir;
    int aiNr;

    public GUI (int mode, int config){
	this.board = new BoardAI(config);
	if(mode == 0){
	    aiNr = -3;
	}
	else{
	    aiNr = 2;
	}
	this.selection = new ArrayList<int[]>();
	addMouseListener(new java.awt.event.MouseAdapter() {
	    public void mouseClicked(java.awt.event.MouseEvent evt) {
		formMouseClicked(evt);
	    }
	});
    }
    public void update(BoardAI b){
	board = b;
	update(this.getGraphics());
    }
    private void formMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_formMouseClicked
	if(board.getMarbles(1).size() > 8 && board.getMarbles(2).size() > 8){
	    if(board.cplayer == aiNr){
		int[] m = ai.nextMove(board, board.getCPlayer());
		board.move(m);
		board.setCPlayer(3- board.getCPlayer());
	    }
	    else {
		getPlayerMove(evt.getX(), evt.getY());
	    }
	    
	}
	else{
	    JOptionPane.showMessageDialog(this, "Game Over");
	}
	this.update(getGraphics());
    }
    private void getPlayerMove(int x, int y){
	int choicey = (y - 70) / 75;
	int choicex = (x - (choicey % 2) * (40) - Math.abs((choicey-4)/2)*80 - 40 ) / 80 + ((choicey > 4) ? (choicey - 5) : -1) ;
	System.out.println("Selected: " + choicex + " " + choicey + " ; " + board.get(choicex, choicey) + " Nr: " + selection.size());
	int[] point = {choicex,choicey};
	if(board.get(choicex, choicey) != board.getCPlayer() || selection.size() == 3){
	    selectDir = point;
	    int[] move = moveFinished();
	    if(board.isPossibleMove(move, board.getCPlayer())){
		int l = board.move(move);
		board.setCPlayer(3 - board.getCPlayer());
	    }
	}
	else{
	    System.out.println("Add to Selection: " + point[0] + " " + point[1]);
	    selection.add(point);
	}
    }
    private int[] moveFinished() {
	int minx = 9;
	int miny = 9;
	for(int i = 0; i < selection.size(); i++){
	    if(selection.get(i)[0] < minx){
		minx = selection.get(i)[0];
		miny = selection.get(i)[1];
	    }
	    if(selection.get(i)[1] < miny){
		miny = selection.get(i)[1];
	    }
	}  
	int ox = 0;
	int oy = 0;
	int[] move;
	int[] dir = {selectDir[0] - minx, selectDir[1] - miny};
	for(int[] d : board.dir){
	    int[] nextpoint = {minx + d[0], miny + d[1]};
	    for(int[] p : selection){
		if(selectDir[0] - p[0] == d[0] && selectDir[1] - p[1] == d[1]){
		    dir = d;
		}
		if(p[0] == nextpoint[0] && p[1] == nextpoint[1]){
		    ox = d[0];
		    oy = d[1];
		}
	    }
	}

	if(dir[0] == ox && dir[1] == oy || dir[0] == -ox && dir[1] == -oy){
	    int[] point = {minx + dir[0], miny + dir[1]};
	    for(int[] p : selection){
		if( (p[0] == minx - dir[0] * (selection.size() - 1) && (p[1] == miny - dir[1] * (selection.size() - 1)))){
		    minx = p[0];
		    miny = p[1];
		}
	    }
	    move = new int[4];
	    move[0] = minx;
	    move[1] = miny;
	    move[2] = dir[0];
	    move[3] = dir[1];
	}
	else{
	    move = new int[7];
	    move[0] = minx;
	    move[1] = miny;
	    move[2] = ox;
	    move[3] = oy;
	    move[4] = dir[0];
	    move[5] = dir[1];
	    move[6] = selection.size();
	}
	selection.clear();
	System.out.println(board.getMarbles(1).size() + " " + board.getMarbles(2).size());
	return move;

    }
    public void paintComponent(Graphics g) {

	super.paintComponent(g);
	g.setColor(Color.lightGray);

	Polygon background= new Polygon();
	for (int i = 0; i < 6; i++){
	    background.addPoint((int) (420 + 400 * Math.cos(i * 2 * Math.PI / 6)+50), (int) (340 + 400 * Math.sin(i * 2 * Math.PI / 6) + 50));
	}
	g.drawPolygon(background);

	g.fillPolygon(background);
	g.setColor(Color.gray);
	int value;
	Color color;
	for(int y = 0; y < 9; y ++){
	    int cx = (y % 2) * (40) + Math.abs((y-4)/2)*80 + 40 ;
	    int cy = 70 + 75 * y;
	    for(int x = 0; x < 9; x ++){
		if(board.get(x,y) != BoardAI.INVALID){
		    switch (board.get(x,y)) {
		    case BoardAI.WHITE:
			color = new Color(255,255,255);
			break;
		    case BoardAI.BLACK:
			color = new Color(0,0,0);
			break;
		    default:
			color = new Color(150,150,150);
			break;
		    }
		    cx += 80;
		    g.setColor(color);
		    g.fillOval( cx, cy, 50, 50);
		}
	    }
	}

    }
}

