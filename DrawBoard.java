package org;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class DrawBoard extends JPanel{
	Board board = new Board(9, 9);

	public DrawBoard(Board board) {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				this.board.set(i, j, board.get(i, j));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.yellow);

		Polygon background = new Polygon();
		for (int i = 0; i < 6; i++) {
			background.addPoint(
					(int) (420 + 400 * Math.cos(i * 2 * Math.PI / 6)),
					(int) (340 + 400 * Math.sin(i * 2 * Math.PI / 6)));
		}
		g.drawPolygon(background);

		g.fillPolygon(background);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setPaint(Color.gray);
		int value;
		Color color;
		for (int i = 4; i >= 0; i--) {
			for (int j = 0; i + j < 9; j++) {
				color = new Color(150, 150, 150);
				value = board.get(i + j, j);
				if (value == 1)
					color = new Color(0, 0, 0);
				if (value == -1)
					color = new Color(255, 255, 255);
				g2.setColor(color);
				g2.fillOval(j * 84 + 40 + 45 * (i), 80 * i + 310, 40, 40);
				g2.drawString(Integer.toString(value), j * 84 + 40 + 45 * (i),
						80 * i + 310);
			}
		}
		for (int j = 1; j < 5; j++) {
			for (int i = 0; i < 9 - j; i++) {
				color = new Color(150, 150, 150);
				value = board.get(i, i + j);

				if (value == 1)
					color = new Color(0, 0, 0);
				if (value == -1)
					color = new Color(255, 255, 255);
				g2.setColor(color);
				g2.fillOval(i * 84 + 40 + 45 * (j), 310 - 80 * j, 40, 40);
				g2.drawString(Integer.toString(value), i * 84 + 40 + 45 * (j),
						310 - 80 * j);
			}

		}
	}

	/*public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("Abalone");
		frame.setSize(900, 900);
		Container contentPane = frame.getContentPane();
		//Board board = new Board(9, 9);
		// board.set(5,5,-1);
		contentPane.add(new DrawBoard(board));
		frame.show();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}*/
}