package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import controller.Handler;

public class SocketIO extends Thread{

	private Handler handler;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private Thread thread;
	public SocketIO (Socket socket, Handler handler)
	{
		this.handler = handler;
		this.socket = socket;
		try {
			this.out = new PrintWriter(this.socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

	}
	public void run()
	{
		String str;
		try {
			while ((str = in.readLine())!= null)
			{
				if (str.startsWith("maze"))
				{
					String mazeStr = str + "\n";
					for (int i = 0 ; i < 17; i++)
						mazeStr += in.readLine() + "\n";
					this.processIn(mazeStr);
				}
				else
					this.processIn(str);
			}
			System.out.println("exitttttT");
			stop2();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	public void vote (String s)
	{
		this.out.println(s);
		System.out.println("voted = "+s);
	}
	
	
	public Socket getSocket() {
		return socket;
	}
	public PrintWriter getOut() {
		return out;
	}
	public BufferedReader getIn() {
		return in;
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop2(){
		try {
			thread.join();
			System.out.println("exit on stop 2");
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
	public void processIn (String s)
	{
		System.out.println(s);
		if (s.startsWith("maze"))
		{
			this.handler.setMaze(new MazeGenerator (s));
			model.State.setState(new MazeState(this.handler.getDisplayWidth(), this.handler.getDisplayHeight(),this.handler));
		}
		else if (s.startsWith("voted"))
		{
			this.handler.setVote(0);
			String[] strList = s.split("\\s+");
			for (String move : new String[] {"scissors", "paper", "stone"})
			{
				if (strList[2].equals(move))
				{
					Thread t = new Thread ()
					{
						public void run()
						{
							handler.setPlayerMove(move);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}
							handler.setPlayerMove(null);
						}
					};
					t.start();
					this.handler.setPlayerMove(move);
				}
			}
			if (model.State.getState().getStateID() == 1)
				if  (strList.length >= 2)
					this.handler.setAnswer(strList[2]);
		}
		else if (s.startsWith("vote"))
		{
			ArrayList<String> votes = new ArrayList<String>();
			String[] strList = s.split("\\s+");
			for (int i = 2; i < strList.length; i++)
				votes.add(strList[i]);
			this.handler.setOptions(votes);
			this.handler.setVote(4);
			Thread t = new Thread()
			{
				public void run()
				{
					for (int k = 4; k >0 ; k--)
					{
						handler.setVote(k);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}
					handler.setVote(0);
				}
			};
			t.start();
			this.handler.setDisplay("");
		}
		else if (s.startsWith("status"))
		{
			String[] strList = s.split("\\s+");
			this.handler.setStatus(strList[2]);
		}
		else if (s.startsWith("event"))
		{
			this.handler.setAnswer(null);
			this.handler.setPlayerMove(null);
			String[] strList = s.split("\\s+");
			if (strList[2].equals("no"))
				this.handler.setDisplay("NO RANDOM ENCOUNTER");
			else if (strList[2].equals("rez"))
				this.handler.setDisplay("REZ POTION");
			else if (strList[2].equals("death"))
				this.handler.setDisplay("DEATH POTION");
			else
				this.handler.setDisplay(s);
		}
		else if (s.startsWith("state"))
		{
			String[] strList = s.split("\\s+");
			if (strList[2].equals("battle"))
			{
				Thread t = new Thread()
						{
					public void run()
					{
						handler.setDisplay("MONSTER FIGHT");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
						handler.setDisplay(null);
						model.State.setState(new BattleState(handler));

					}
						};
				t.start();

			}
			else if (strList[2].equals("boss"))
			{
				Thread t = new Thread()
				{
			public void run()
			{
				handler.setDisplay("BOSS FIGHT");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				handler.setDisplay(null);
				model.State.setState(new BattleState(handler));

			}
				};
		t.start();
			}
			else if (strList[2].equals("maze"))
				model.State.setState(new MazeState(this.handler.getDisplayWidth(), this.handler.getDisplayHeight(),this.handler));
		}
		else if (s.startsWith("my"))
		{
			String[] strList = s.split("\\s+");
			int oldHealth = this.handler.getMyHealth();
			this.handler.setMyHealth(Integer.parseInt(strList[3]));
			if (oldHealth > this.handler.getMyHealth())
			{
			Thread t = new Thread()
			{
				public void run()
				{
					for (int k = 0; k < 3; k ++)
					{
						try {		
							Thread.sleep(200);
							handler.setMyHealth(handler.getMyHealth() + 1);
							Thread.sleep(200);
							handler.setMyHealth(handler.getMyHealth() - 1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}
				}
			};
			t.start();
			}

		}
		else if (s.startsWith("total lives"))
		{
			String[] strList = s.split("\\s+");
			this.handler.setTotalLives(Integer.parseInt(strList[3]));
		}
		else if (s.startsWith("current lives"))
		{
			String[] strList = s.split("\\s+");
			this.handler.setCurrentLives(Integer.parseInt(strList[3]));
		}
		else if (s.startsWith("enemy"))
		{
			String[] strList = s.split("\\s+");
			if (strList[1].equals("health"))
			{
				int oldHealth = this.handler.getEnemyHealth();
				this.handler.setEnemyHealth(Integer.parseInt(strList[3]));
				if (oldHealth > this.handler.getEnemyHealth())
				{
					Thread t = new Thread()
					{
						public void run()
						{
							for (int k = 0; k < 3; k ++)
							{
								try {
									Thread.sleep(200);
									handler.setEnemyHealth(handler.getEnemyHealth() + 1);
									Thread.sleep(200);
									handler.setEnemyHealth(handler.getEnemyHealth() - 1);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								}
							}
						}
					};
					t.start();
				}
			}
			else if (strList[1].equals("move")){
				for (String move : new String[] {"scissors", "paper", "stone"})
				{
					if (strList[3].equals(move))
					{
						Thread t = new Thread ()
						{
							public void run()
							{
								handler.setEnemyMove(move);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								}
								handler.setEnemyMove(null);
							}
						};
						t.start();
					}
				}
			}
		}
			
	}
}
