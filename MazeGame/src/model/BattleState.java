package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.Handler;
import view.ImageLoader;

public class BattleState extends State {

	private Handler handler;
	private BufferedImage background,character,enemy,scissors,paper,stone,boss;
	
	public BattleState (Handler handler){
		this.handler = handler;
		this.background = ImageLoader.loadImage("/BattleBar.png");
		this.character = ImageLoader.loadImage("/character2.png");
		if (this.handler.isBoss())
			this.enemy = ImageLoader.loadImage("/pirate.png");
		else
			this.enemy = ImageLoader.loadImage("/samurai.png");
		this.scissors = ImageLoader.loadImage("/scissors.png");
		this.paper = ImageLoader.loadImage("/paper.png");
		this.stone = ImageLoader.loadImage("/stone.png");
	}
	@Override
	public int getStateID() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		if (this.handler.getKeyManager().isDownr())
		{
			this.handler.getSocketManager().vote("s");
			System.out.println("sssssS");
			this.handler.getKeyManager().setDownr(false);
		}
		if (this.handler.getKeyManager().isLeftr())
		{
			this.handler.getSocketManager().vote("a");
			this.handler.getKeyManager().setLeftr(false);
		}
		if (this.handler.getKeyManager().isRightr())
		{
			this.handler.getSocketManager().vote("d");
			this.handler.getKeyManager().setRightr(false);
		}
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		// draw win loss health, you win, loseq
		drawBackground(g);
		drawVote(g);
		drawHealth(g);
		drawChoice(g);
	}

	
	private void drawBorderString(Graphics g,String string,int x, int y, int color){
		g.setFont(new Font("Sans-serif", Font.PLAIN, 40)); 
		g.setColor(new Color (41,36,24));
		g.drawString(string, x+1, y+1);
		g.drawString(string, x-1, y-1);
		g.drawString(string, x+1, y-1);
		g.drawString(string, x-1, y+1);
		g.drawString(string, x+1, y);
		g.drawString(string, x-1, y);
		g.drawString(string, x, y-1);
		g.drawString(string, x, y+1);
		if (color == 1)
			g.setColor(new Color (0,128,0)); // HP bar Green
		else if (color == 2)
			g.setColor(new Color (0,0,128)); //MP bar Blue
		else if (color == 3)
			g.setColor(new Color (128,0,0)); //MP bar Blue
		g.drawString(string, x, y);
	}
	
	private void drawBackground (Graphics g)
	{
		g.drawImage(this.background, 0, 0, null);
		g.drawImage(this.character, 100, 200,null);
		g.drawImage (this.enemy, 500,200,null);
		g.drawImage(this.scissors, 50, 480,100, 100, null);
		g.drawImage(this.paper, 320, 480,100, 100, null);
		g.drawImage(this.stone, 590, 480,100, 100, null);
	}
	
	private void drawVote(Graphics g)
	{
		if (this.handler.getVote() != 0)
		{
			drawBorderString(g,"Vote", 380, 270,2);
			drawBorderString(g,""+this.handler.getVote(), 400, 300,2);
			
			drawBorderString (g,"A", 200,540,1);
			drawBorderString (g,"S", 470,540,2);
			drawBorderString (g,"D", 740,540,3);

		}
		else
		{
			if (this.handler.getAnswer() != null)
			{
				drawBorderString(g,"Voted : " + this.handler.getAnswer(), 360, 270,2);
			}
		}
	}
	
	private void drawHealth (Graphics g)
	{
		drawBorderString (g,"Health: ", 20,180,1);
		drawBorderString (g,"Health: ", 500,180,3);
		for (int i = 0 ; i < this.handler.getMyHealth(); i++)
		{
			g.setColor(new Color (0,128,0));
			g.fillOval(150 + 60*i, 140, 50, 50);
		}
		for (int i = 0 ; i < this.handler.getEnemyHealth(); i++)
		{
			g.setColor(new Color (128,0,0));
			g.fillOval(630 + 60*i, 140, 50, 50);
		}
	}
	
	private void drawChoice (Graphics g)
	{
		if (this.handler.getPlayerMove() == "scissors")
			g.drawImage(this.scissors, 250, 300,100, 100, null);
		else if (this.handler.getPlayerMove() == "paper")
			g.drawImage(this.paper, 250, 300,100, 100, null);
		else if (this.handler.getPlayerMove() == "stone")
			g.drawImage(this.stone, 250, 300,100, 100, null);
		
		if (this.handler.getEnemyMove() == "scissors")
			g.drawImage(this.scissors, 550, 300,100, 100, null);
		else if (this.handler.getEnemyMove() == "paper")
			g.drawImage(this.paper, 550, 300,100, 100, null);
		else if (this.handler.getEnemyMove() == "stone")
			g.drawImage(this.stone, 550, 300,100, 100, null);
	}
}
