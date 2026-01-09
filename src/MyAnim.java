import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyAnim extends JPanel implements ActionListener {
	private Polygon polyZ = new Polygon();
	private Polygon polyB = new Polygon();
	private double size = 1.0;
	private boolean shrinking = true;
	private Color color1;
	private Color color2;
	private double angle = 0;
	private int loc = 0;
	private int velX = 2;

	private MyAnim() {
		File filez = new File("Z.txt");
		poly(filez, polyZ);
		File fileb = new File("B.txt");
		poly(fileb, polyB);
		changeColor();
		Timer t = new Timer(20, this);
		t.start();
	}

	private void poly(File file, Polygon p) {
		try {
			Scanner reader = new Scanner(file);

			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				if (line.trim().isEmpty())
					continue;

				String[] parts = line.split(",");
				try {
					int x = Integer.parseInt(parts[0].trim());
					int y = Integer.parseInt(parts[1].trim());
					p.addPoint(x, y);
				} catch (NumberFormatException e) {
					System.out.println("coordinations must be numbers!");
					continue;
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");

		}
	}

	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;

	    Graphics2D gZ = (Graphics2D) g2.create();
	    gZ.setColor(color1);	    
	    gZ.translate(getWidth() / 2, getHeight() / 2);
	    gZ.rotate(angle);
	    gZ.scale(size, size);	   	    
	    gZ.fill(polyZ);

	    Graphics2D gB = (Graphics2D) g2.create();
	    gB.setColor(color2);     
	    gB.translate(loc, 100); 
	    gB.scale(size, size);	   	    
	    gB.fill(polyB);
	}

	public void changeColor() {
		color1 = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
		color2 = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (shrinking) {
			size -= 0.01;
			if (size <= 0.5) {
				shrinking = false;
				changeColor();
			}
		} else {
			size += 0.01;
			if (size >= 1.5) {
				shrinking = true;
				changeColor();
			}
		}
		angle += 0.1;
		loc += velX;

	    int bWidth = polyB.getBounds().width;
	    
	    if (loc + bWidth > getWidth() || loc < 0) {
	        velX = -velX;
	        changeColor(); 
	    }

		repaint();
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		MyAnim s = new MyAnim();
		f.add(s);
		f.setSize(600, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

	}
}
