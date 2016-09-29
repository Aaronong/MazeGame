package model;

import java.awt.Graphics;

import controller.Game;

public abstract class State {
	
	private static State currentState = null;
	
	public static void setState(State state){
		currentState = state;
	}
	
	public static State getState(){
		return currentState;
	}
	
	public abstract int getStateID();
	
	
	//CLASS
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
}
