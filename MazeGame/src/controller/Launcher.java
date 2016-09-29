package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.KeyManager;
import model.MazeGenerator;
import model.SocketIO;

public class Launcher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		 * Maze generation
		 * Connection to clients
		 * Log with timestamp
		 * - Awake/Asleep "status = awake/asleep"
		 * - Maze Data "maze i j x"
		 * - Voting "vote = a b c d"
		 * - BattleMode/ MazeMode "mode = battle/maze"
		 * - lives "total lives = k" "active lives = k"
		 * - Vote outcome
		 * upon battlemode command, countdown starts locally on each client and on server
		 * upon vote outcome, every client will decide how to deal with it
		 * 
		 * Sleep Stack
		 */
		
		//Handler - to pass information around
		Handler handler = new Handler ();
		
		//Maze generation
		int mazeWidth = 21;
		int mazeHeight = 15;
		MazeGenerator maze = new MazeGenerator(mazeWidth,mazeHeight);
		handler.setMaze(maze);
		
		
		
		//Initialize Graphics
		Game game = new Game("The Maze Runner - Client", 840,600, handler);
		game.start();
		
		//Initialize connection to server
		Socket mysocket = getSocket(handler);
		SocketIO socketIO = new SocketIO (mysocket, handler);
		handler.setSocketManager(socketIO);
		socketIO.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		    public void run() {
		    	// what you want to do

	    		try {

	    			System.out.println("launcher exit");
	    			handler.getSocketManager().getOut().close();
	    			handler.getSocketManager().getIn().close();
	    			handler.getSocketManager().getSocket().close();
	    			System.exit(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.exit(0);
				}
		    	
		    }
		}));

		

	}
	
	private static Socket getSocket(Handler handler){
		Socket mysocket = null;
		try {
			mysocket = new Socket ("localhost",6212);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		return mysocket;
	}
	

}
