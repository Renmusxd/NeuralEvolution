package NeuralEvolution.GameClasses;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class WorldController { // A multithread compatible update and graphics looper

	public static final double A_BILLION = 1000000000;
	
	private Workhorse myWorkhorse;
	private GameWindow myGameWindow;
	
	private World myWorld;
	
	public boolean looping = true;
	public boolean paused = false;
	public double g_fps = 0;
	public double u_fps = 0;
	
	private ArrayList<Drawable> addToDrawObjects;
	private ArrayList<Drawable> drawObjects;
	private ArrayList<Drawable> removeDrawObjects;
	private ArrayList<Updatable> addToUpdateObjects;
	private ArrayList<Updatable> updateObjects;
	private ArrayList<Updatable> removeUpdateObjects;

	public WorldController(Workhorse myWH) {
		this.myWorkhorse = myWH;
		this.drawObjects = new ArrayList<Drawable>();
		this.updateObjects = new ArrayList<Updatable>();
		this.removeDrawObjects = new ArrayList<Drawable>();
		this.removeUpdateObjects = new ArrayList<Updatable>();
		this.addToUpdateObjects = new ArrayList<Updatable>();
		this.addToDrawObjects = new ArrayList<Drawable>();
	}
	
	public void loop(){
		myGameWindow = myWorkhorse.getGameWindow();
		// read prefs
		final double GAME_U_GHERTZ = 400.0; // update frequency //also graphics for single core systems
		final double GAME_G_GHERTZ = 60.0; // graphics frequency
		final double G_TIME_BETWEEN_UPDATES = A_BILLION / GAME_G_GHERTZ;
		final double U_TIME_BETWEEN_UPDATES = A_BILLION / GAME_U_GHERTZ;
		double lastupdateTime = System.nanoTime();
		double now;
		
		// makes basic updatable and drawable object 
		this.myWorld = new World(this);
		this.addUpdatable(myWorld);
		this.addDrawable(myWorld);
		// this code can be removed for a more customizable system
		
		boolean multipleCores = Runtime.getRuntime().availableProcessors()>1; // if the computer only has one core it will default to single thread u/g
		if (multipleCores && myWorkhorse.MULTICORE){
			System.out.println("Starting with updates and graphics on separate threads");
			//start update or render thread
			Thread updateThread = new Thread(new Runnable(){
				double lastupdateTime = System.nanoTime();
				double now = System.nanoTime();
				@Override
				public void run() {
					while (looping){
						now = System.nanoTime();
						if (!paused){tick();} 							// update call
						while ( (now - lastupdateTime) < U_TIME_BETWEEN_UPDATES){		// if not enough time please wait
							Thread.yield();							// threading sleep stuff
							try{Thread.sleep(1);} catch (Exception e){}			// """"
							now = System.nanoTime();					// """"
						}
						u_fps = A_BILLION / (now - lastupdateTime);				// update fps
						lastupdateTime = now;
					}
				}
			});
			updateThread.start();
			while (looping){
				render();
				now = System.nanoTime();
				while ( (now - lastupdateTime) < G_TIME_BETWEEN_UPDATES){
					Thread.yield();
					try{Thread.sleep(1);} catch (Exception e){}
					now = System.nanoTime();
				}
				g_fps = A_BILLION / (now - lastupdateTime);
				lastupdateTime = now;
			}
		} else { // single core and single stream
			System.out.println("Starting with updates and graphics on single thread");
			double fps = 0;
			while (looping) {
				now = System.nanoTime();
				if (!paused){tick();}
				
				render();
				
				while ( (now - lastupdateTime) < U_TIME_BETWEEN_UPDATES){
					Thread.yield();
					try{Thread.sleep(1);} catch (Exception e){}
					now = System.nanoTime();
				}
				fps = A_BILLION / (now - lastupdateTime);
				u_fps = fps;
				g_fps = fps;
				lastupdateTime = now;
			}
		}
		
		shutdownSequence();
	} /// main loop
	
	public void shutdownSequence(){
		// stuff before exiting
	}
	
	// -------------------------------------------------------------------- UPDATING AND RENDERING ----------------------------------------------------------------
	
	public void tick(){ // implementation of update
		// Add new guys
		updateObjects.addAll(addToUpdateObjects);
		addToUpdateObjects.clear();
		// First updates everything
		for (Updatable up:this.updateObjects){up.update();}
		// then removes things which need removing
		updateObjects.removeAll(removeUpdateObjects);
		removeUpdateObjects.clear();
		// by not doing it all at once we remove the possibility updates due to removed objects
	} // updates all added updatables
	
	public void render(){
		myGameWindow.drawWindow(); // gets graphics then calls drawAll // for now only window
		// Added
		this.myWorkhorse.nw.drawWindow();
	}
	public void drawAll(Graphics2D g){
		drawObjects.addAll(addToDrawObjects);
		addToDrawObjects.clear();
		for (Drawable d:this.drawObjects){d.draw(g);}
		drawObjects.removeAll(removeDrawObjects);
		removeDrawObjects.clear();
	} // Tells each item to draw itself on the given graphics object
	
	// -------------------------------------------------------------------- ADDING AND REMOVING -------------------------------------------------------------------
	
	public void addUpdatable(Updatable item){this.addToUpdateObjects.add(item);}
	public void removeUpdatable(Updatable item){this.removeUpdateObjects.add(item);}
	public void addDrawable(Drawable item){this.addToDrawObjects.add(item);}
	public void removeDrawable(Drawable item){this.removeDrawObjects.add(item);}
	
	// -------------------------------------------------------------------- GETTERS AND SETTERS -------------------------------------------------------------------
	
	public GameWindow getGameWindow(){return this.myGameWindow;}
	public World getWorld(){return myWorld;}
}
