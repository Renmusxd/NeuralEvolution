package NeuralEvolution.GameClasses;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends Canvas{	// A double buffered Canvas
	private static final long serialVersionUID = 1L;
	private Workhorse myWorkhorse;
	private WorldController myWorldController;
	private JFrame myFrame;
	private JPanel myPanel;
	private BufferStrategy strategy;
	public final int HEIGHT = 750;
	public final int WIDTH = 750;
	public GameWindow(Workhorse myWH){
		super();
		this.myWorkhorse = myWH;
		myWorldController = myWorkhorse.getWorldController();
		myFrame = new JFrame("GameWindow");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myPanel = (JPanel) myFrame.getContentPane();
		myPanel.setPreferredSize(new Dimension(750,750));
		myPanel.setLayout(null);
		this.setBounds(0,0,750,750);
		myPanel.add(this);
		this.setIgnoreRepaint(true);
		myFrame.pack();myFrame.setResizable(myWorkhorse.RESIZABLE);myFrame.setVisible(true); //usual
		this.requestFocus();
		this.createBufferStrategy(2);
		strategy = this.getBufferStrategy();
	}
	
	public void drawWindow(){
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		World myWorld = myWorkhorse.getWorldController().getWorld();
		g.setColor(Color.white); g.fillRect(0, 0, myWorld.getWindowWidth(), myWorld.getWindowHeight());
		myWorldController.drawAll(g);
		g.dispose(); strategy.show();
	}
	
	public void drawFullScreen(){
		
	}
	
	public void drawFullScreenExclusive(){
		
	}
	
	public JFrame getFrame(){return this.myFrame;}
}
