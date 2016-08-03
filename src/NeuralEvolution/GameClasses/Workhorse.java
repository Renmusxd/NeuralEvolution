package NeuralEvolution.GameClasses;

import NeuralEvolution.SpecificGameClasses.NeuronWindow;

public class Workhorse {

	public final boolean MULTICORE = true; // will run in multiple cores if the option is available, cannot be changed in-game
	public final boolean FULLSCREEN = false;
	public final boolean RESIZABLE = false; //TODO get resizing working
	
	public WorldController wc;
	public GameWindow gw;
        public NeuronWindow nw;
	
	public Workhorse() {
            wc = new WorldController(this);
            gw = new GameWindow(this);
            nw = new NeuronWindow(this);
            Thread loop = new Thread() {
            public void run(){
                System.out.println("Starting threaded loop");
                wc.loop();
                System.exit(0);
            }};
            loop.start();
	}
	
	public WorldController getWorldController(){return wc;}
	public GameWindow getGameWindow(){return gw;}

	public static void main(String[] args) {
		if (System.getProperty("os.name").equals("Max OS X")){
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Workhorse();
            }
        });
	}

}
