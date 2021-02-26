package AngryBirds;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class GameMain extends JFrame {

	    public GameMain() {
	        initUI();
	    }

	    private void initUI() {

	        final GameBoard surface = new GameBoard();
	        add(surface);

	        addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
//	                Timer timer = surface.getTimer();
//	                timer.stop();
	            }
	        });
	        setTitle("Happy Ball");

	        setSize(1200,600);
	        setLocationRelativeTo(null);
	        setResizable(false);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }
	    

	    
	    public static void main(String[] args) {

//	        EventQueue.invokeLater(new Runnable() {
//	            @Override
//	            public void run() {

	            	GameMain ex = new GameMain();
	                ex.setVisible(true);
//	            }
//	        });
	    }
}

