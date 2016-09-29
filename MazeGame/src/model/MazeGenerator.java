package model;


public class MazeGenerator {
	private int width,height;
	private int [][] maze;
	public MazeGenerator (int width, int height)
	{
		this.width = width;
		this.height = height;
		/* 0 = Wall
		 * 1 = Unvisited Tile
		 * 2 = Visited Tile
		 * 3 = Boss Tile
		 * 4 = Player Tile*/
		generateMaze(this.width,this.height);
		//Todo, generate boss and player tile
	}
	
	public MazeGenerator (String mazeStr)
	{
		String[] strList = mazeStr.split("\n");
		this.height = strList.length - 3;
		this.width = strList[2].length() - 2;
		generateMaze (strList);
	}
	
	private void generateMaze(String[] strList)
	{
		this.maze = new int [this.width][this.height];
		for (int j = 0; j < this.height; j++)
		{
			for (int i = 0; i < this.width; i++)
			{
				int val = mapCharToInt(strList[j+2].charAt(i+1));
				this.setTile(i, j, val);
			}
		}
	}
	
	private void generateMaze (int width, int height)
	{
		this.maze = new int [this.width][this.height];
		for (int i = 0; i < this.height; i++)
		{
			for (int j = 0; j < this.width; j++)
			{
				if (i%2 == 0 && j%2 == 0)
				{
					this.maze[j][i] = 1;
				}
			}
		}
	}
	
	public void setTile (int x, int y, int val)
	{
		this.maze[x][y] = val;
	}
	
	public int getTile (int x, int y)
	{
		return this.maze[x][y];
	}
	
	public int getPlayerX ()
	{
		int playerX = -1;
		for (int i = 0; i < this.height; i++)
		{
			for (int j = 0; j < this.width; j++)
			{
				if (this.maze[j][i] == 4)
				{
					playerX = j;
				}
			}
		}
		return playerX;
	}
	
	public int getPlayerY ()
	{
		int playerY = -1;
		for (int i = 0; i < this.height; i++)
		{
			for (int j = 0; j < this.width; j++)
			{
				if (this.maze[j][i] == 4)
				{
					playerY = i;
				}
			}
		}
		return playerY;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public int mapCharToInt (char c)
	{
		if (c == '#')
			return 0;
		if (c == ' ')
			return 1;
		if (c == '.')
			return 2;
		if (c == 'x')
			return 3;
		return 4;
	}
	
}
