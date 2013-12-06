package NeuralEvolution.GameClasses;

public class Workhorse {

	public final boolean MULTICORE = true; // will run in multiple cores if the option is available, cannot be changed in-game
	public final boolean FULLSCREEN = false;
	public final boolean RESIZABLE = false; //TODO get resizing working
	
	public WorldController wc;
	public GameWindow gw;
	
	public Workhorse() {
		wc = new WorldController(this);
		gw = new GameWindow(this);
		Thread loop = new Thread()
	      {
	         public void run()
	         {
	            wc.loop();
	            System.exit(0);
	         }
	      };
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
