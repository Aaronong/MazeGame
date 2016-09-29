package view;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;

import controller.Handler;

public class Display {

	private JFrame frame;
	private Canvas canvas;
	private Handler handler;
	private String title;
	private int width, height;
	
	public Display(String title, int width, int height, Handler handler){
		this.title = title;
		this.width = width;
		this.height = height;
		this.handler = handler;
		createDisplay();
	}

	private void createDisplay(){
		frame = new JFrame(title);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		        // close sockets, etc
		    	try {
		    		System.out.println("JFrame close");
		    		handler.getSocketManager().getOut().close();
		    		handler.getSocketManager().getIn().close();
					handler.getSocketManager().getSocket().close();
					System.exit(0);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width,height));
		canvas.setPreferredSize(new Dimension(width,height));
		canvas.setPreferredSize(new Dimension(width,height));
		//It makes sure that the JFrame is the only thing that has focus
		//Basically allows the application to focus itself instead
		//of the part that was drawn.
		canvas.setFocusable(false);
		
		frame.add(canvas);
		frame.pack();
	}
	
	//Getters and Setters
	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}


