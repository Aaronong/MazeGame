package controller;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import view.Display;
import model.KeyManager;
import model.MazeGenerator;
import model.MazeState;
import model.State;

public class Game implements Runnable {
	private Display display;
	private Handler handler;
	public String title;
	private int width,height;
	private Thread thread;
	private boolean running = false;
	private KeyManager keyManager;
	private BufferStrategy bs;
	private Graphics g;
	
	//States
	private MazeState mazeState;
	
	public Game(String title, int width, int height, Handler handler){
		this.width = width;
		this.height = height;
		this.title = title;
		this.handler = handler;
		this.handler.setDisplayHeight(this.height);
		this.handler.setDisplayWidth(this.width);
		this.keyManager = new KeyManager();
		this.handler.setKeyManager(this.keyManager);
	}
	
	private void init(){
		display = new Display(title, width, height, this.handler);
		display.getFrame().addKeyListener(keyManager);
		this.mazeState = new MazeState(width, height, this.handler);
		State.setState(mazeState);
	}
	
	private void tick(){
		this.keyManager.tick();
		if (State.getState() != null)
			State.getState().tick();
	}
	
	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null){
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		//Clear Screen
		
		g.clearRect(0, 0, width, height);
		
		//Draw Here!
		
		if(State.getState() != null)
		{
			State.getState().render(g);
		}
		
		// All future Drawings will be drawn in the Different States
		//To draw filled rectangle g.fillRect(0, 0, width, height);
		// Change drawing colour to red, g.setColor(Color.red);
		// To draw the whole damn spritesheet g.drawImage(GSsprites, 20, 20, null);
		// This draws isaac at position 40 40
		//g.drawImage(sheet.crop(32, 0, 32, 32),40,40, null);
		// Drawing with Assets
		//g.drawImage(Assets.Isaac, 50, 50, null);
//		g.drawImage(Assets.cactus, 50, 50,50,50, null);
//		g.drawRect(100, 100, 100, 100);
//		g.drawImage(ImageLoader.loadImage("/textures/BattleBG/YellowRightArrow.png"), 250, 50, null);
//		g.drawImage(Assets.desertBG, 250, 150, null);
//		g.drawImage(Assets.castleFloor, 400,400, 50,50, null);
		//End Drawing!
		
		bs.show();
		g.dispose();
		
	}
	
	public void run(){
		init();
		
		//frames per second
		int fps = 60;
		// 1 bil = 1 second
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while(running ){
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			if(delta >= 1){
				tick();
				render();
				ticks++;
				delta--;
			}
			
			if (timer >= 1000000000){
				//System.out.println("Ticks and Frames: " + ticks);
				ticks = 0;
				timer = 0;
			}
		}
		
		stop();
		
	}
	
	public synchronized void start(){
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop(){
		if (!running)
			return;
		running = false;
		try {
			thread.join();
			System.out.println("game exit");
			this.handler.getSocketManager().getOut().close();
			this.handler.getSocketManager().getIn().close();
			this.handler.getSocketManager().getSocket().close();
			Runtime.getRuntime().exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
