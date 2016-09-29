package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.Handler;
import view.ImageLoader;


public class MazeState extends State {
	private Handler handler;
	private int TileHeight,TileWidth;
	private int FrameHeight,FrameWidth;
	private int MazeHeight,MazeWidth;
	private BufferedImage treeTile;
	private BufferedImage grassTile;
	private BufferedImage darkgrassTile;
	private BufferedImage character;
	public MazeState (int width, int height, Handler handler)
	{
		this.FrameWidth = width;
		this.FrameHeight = height;
		this.MazeWidth = 21;
		this.MazeHeight = 15;
		this.TileWidth = this.FrameWidth/this.MazeWidth;
		this.TileHeight = this.FrameHeight/this.MazeHeight;
		this.handler = handler;
		this.character = ImageLoader.loadImage("/charactersmall.png");
		this.treeTile = ImageLoader.loadImage("/tree.png");
		this.grassTile = ImageLoader.loadImage("/grass.png");
		this.darkgrassTile = ImageLoader.loadImage("/darkgrass.png");
		
	}
	@Override
	public int getStateID() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		if (this.handler.getKeyManager().isUpr())
		{
			this.handler.getSocketManager().vote("w");
			this.handler.getKeyManager().setUpr(false);
		}
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


		drawMaze(g);
		drawInfo(g);
		drawVote(g);
		drawMessage(g);
	}
	
	private void drawBorderString(Graphics g,String string,int x, int y, int color){
		g.setFont(new Font("Sans-serif", Font.PLAIN, 20)); 
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
			g.setColor(new Color (98,198,82)); // HP bar Green
		else if (color == 2)
			g.setColor(new Color (57,190,230)); //MP bar Blue
		g.drawString(string, x, y);
	}
	
	private void drawMaze(Graphics g)
	{
		for(int y = 0; y < this.MazeHeight; y++){
			for(int x = 0; x < this.MazeWidth; x++){
				int tileID = this.handler.getMaze().getTile(x,y);
				/* 0 = Wall
				 * 1 = Unvisited Tile
				 * 2 = Visited Tile
				 * 3 = Boss Tile
				 * 4 = Player Tile*/
				g.drawImage(this.grassTile, x*this.TileWidth, y*this.TileHeight, this.TileWidth, this.TileHeight, null);
				if (tileID == 0)
				{
					g.drawImage(this.treeTile, x*this.TileWidth, (y*this.TileHeight) - 20, this.TileWidth, this.TileHeight + 20, null);
				}
				else if (tileID == 2)
				{
					g.drawImage(this.darkgrassTile, x*this.TileWidth, y*this.TileHeight, this.TileWidth, this.TileHeight, null);
				}
				else if (tileID == 3)
				{
					g.setColor(Color.red);
					g.fillRect(x*this.TileWidth, y*this.TileHeight, this.TileWidth, this.TileHeight);
				}
				else if (tileID == 4)
				{
					g.drawImage(this.character, x*this.TileWidth, y*this.TileHeight, this.TileWidth, this.TileHeight, null);
				}
				
			}
		}
	}
	
	private void drawInfo(Graphics g)
	{
		int totalHealth = this.handler.getTotalLives();
		int currentHealth = this.handler.getCurrentLives();
		String status = this.handler.getStatus();
		drawBorderString(g,"Total Lives: "+ totalHealth, 670, 20,2);
		drawBorderString(g,"Current Lives: "+ currentHealth, 670, 40,2);
		drawBorderString(g,"Status: "+status,670,60,2);
	}
	
	private void drawVote(Graphics g)
	{
		if (this.handler.getVote() != 0)
		{
			drawBorderString(g,"Vote", 420, 270,2);
			drawBorderString(g,""+this.handler.getVote(), 435, 300,2);
			
			int playerX = this.handler.getMaze().getPlayerX();
			int playerY = this.handler.getMaze().getPlayerY();
			
			for (String dir : this.handler.getOptions())
			{
				if (dir.equals("up"))
					drawBorderString(g,"w", (playerX*this.TileWidth)+15, ((playerY-1)* this.TileHeight)+20,2);
				if (dir.equals("down"))
					drawBorderString(g,"s", (playerX*this.TileWidth)+15, ((playerY+1)* this.TileHeight)+20,2);
				if (dir.equals("left"))
					drawBorderString(g,"a", ((playerX-1)*this.TileWidth)+15, (playerY* this.TileHeight)+20,2);
				if (dir.equals("right"))
					drawBorderString(g,"d", ((playerX+1)*this.TileWidth)+15, (playerY* this.TileHeight)+20,2);
			}
		}
		else
		{
			if (this.handler.getAnswer() != null)
			{
				drawBorderString(g,"Voted : " + this.handler.getAnswer(), 390, 270,2);
			}
		}
	}
	
	private void drawMessage(Graphics g)
	{
		
		if (this.handler.getDisplay() != null)
			drawBorderString(g,this.handler.getDisplay(), 380, 300,2);
	}

}
