package myAbalone.org;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main extends JFrame{

    public Main(){

	this.setTitle("Abalone");
	this.setSize(900, 900);
	Container contentPane = this.getContentPane();
	Object[] modes = {"PvP", "Player vs. Machine"};
	int mode = JOptionPane.showOptionDialog(this, "What mode would you like to use?", "Set up", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
		    null, modes, modes[0]); //default button title
	Object[] config = {"Default", "Belgian Daisy"};
	int start = JOptionPane.showOptionDialog(this, "What start configuration?", "Set up", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
		    null, config, config[0]); //default button title
	contentPane.add(new GUI(mode, start));
	this.show();
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
