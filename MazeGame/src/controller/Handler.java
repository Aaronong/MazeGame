package controller;

import java.util.ArrayList;

import model.KeyManager;
import model.MazeGenerator;
import model.SocketIO;

public class Handler {
	private MazeGenerator maze;
	private int vote; //4,3,2,1 or 0 for inactive voting
	private ArrayList<String> options;
	private String answer;
	private int displayWidth, displayHeight;
	private int myHealth, enemyHealth;
	private int movetoX, movetoY;
	private boolean isBoss;
	private String display,status;
	private String playerMove, enemyMove;
	private int totalLives,currentLives;
	private SocketIO socketManager;
	private KeyManager keyManager;
	
	public Handler ()
	{
		
	}

	public MazeGenerator getMaze() {
		return maze;
	}
	public void setMaze(MazeGenerator maze) {
		this.maze = maze;
	}
	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
	}
	public ArrayList<String> getOptions() {
		return options;
	}
	public void setOptions(ArrayList<String> options) {
		for (String s : options)
			System.out.println("handler options = " + s);
		this.options = options;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getDisplayWidth() {
		return displayWidth;
	}
	public void setDisplayWidth(int displayWidth) {
		this.displayWidth = displayWidth;
	}
	public int getDisplayHeight() {
		return displayHeight;
	}
	public void setDisplayHeight(int displayHeight) {
		this.displayHeight = displayHeight;
	}
	public int getMyHealth() {
		return myHealth;
	}
	public void setMyHealth(int myHealth) {
		this.myHealth = myHealth;
	}
	public int getEnemyHealth() {
		return enemyHealth;
	}
	public void setEnemyHealth(int enemyHealth) {
		this.enemyHealth = enemyHealth;
	}
	public int getMovetoX() {
		return movetoX;
	}
	public void setMovetoX(int movetoX) {
		this.movetoX = movetoX;
	}
	public int getMovetoY() {
		return movetoY;
	}
	public void setMovetoY(int movetoY) {
		this.movetoY = movetoY;
	}
	public boolean isBoss() {
		return isBoss;
	}
	public void setBoss(boolean isBoss) {
		this.isBoss = isBoss;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getPlayerMove() {
		return playerMove;
	}
	public void setPlayerMove(String playerMove) {
		this.playerMove = playerMove;
	}
	public String getEnemyMove() {
		return enemyMove;
	}
	public void setEnemyMove(String enemyMove) {
		this.enemyMove = enemyMove;
	}

	public int getTotalLives() {
		return totalLives;
	}

	public void setTotalLives(int totalLives) {
		this.totalLives = totalLives;
	}

	public int getCurrentLives() {
		return currentLives;
	}

	public void setCurrentLives(int currentLives) {
		this.currentLives = currentLives;
	}

	public SocketIO getSocketManager() {
		return socketManager;
	}

	public void setSocketManager(SocketIO socketManager) {
		this.socketManager = socketManager;
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

	public void setKeyManager(KeyManager keyManager) {
		this.keyManager = keyManager;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
