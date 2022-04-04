import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import java.io.*;
import java.util.*;

/**
 * Board with people that may be expanded (with automatic change of cell
 * number) with mouse event listener
 */

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Person[][] people;
	private int size = 14;
	public FileWriter raport;
	public String state = "healthy,infected,dead\n";

	public Board(int length, int height) {
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
	}

	// single iteration
	public void iteration() {
		int healthy = 0;
		int infected = 0;
		int dead = 0;
		for (int x = 0; x < people.length; ++x)
			for (int y = 0; y < people[x].length; ++y) {
				if(people[x][y].getState() == 0){
					healthy++;
				}
				else if(people[x][y].getState() == 1){
					infected++;
				}
				else{
					dead++;
				}
				people[x][y].calculateNewState();
			}
		try {
			raport = new FileWriter("raport.csv", true); //appending
			state = String.valueOf(healthy) + ',' + String.valueOf(infected) + ',' + String.valueOf(dead) + '\n';
			raport.write(state);
			raport.close();
			state = "";
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		for (int x = 0; x < people.length; ++x)
			for (int y = 0; y < people[x].length; ++y)
				people[x][y].changeState();
		this.repaint();
	}

	// clearing board
	public void clear() {
		for (int x = 0; x < people.length; ++x)
			for (int y = 0; y < people[x].length; ++y) {
				people[x][y].setState(0);
			}
		this.repaint();
	}

	private void initialize(int length, int height) {

		try {
			raport = new FileWriter(new File("raport.csv"));
			raport.write(state);
			raport.close();
			state = "";
		} catch (IOException e) {
			e.printStackTrace();
		}

		people = new Person[length][height];

		for (int x = 0; x < people.length; ++x)
			for (int y = 0; y < people[x].length; ++y)
				people[x][y] = new Person();

		for (int x = 0; x < people.length; ++x) {
			for (int y = 0; y < people[x].length; ++y) {
				for(int row = -1; row <= 1; row++){
					for(int col = -1; col <= 1; col++){
						if(x + row >= 0 && x + row < length && y + col >= 0 && y + col < height && (row != 0 || col != 0)){
							people[x][y].addNeighbour(people[x + row][y + col]);
						}
					}
				}
			}
		}
	}

	//paint background and separators between cells
	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		g.setColor(Color.GRAY);
		drawNetting(g, size);
	}

	// draws the background netting
	private void drawNetting(Graphics g, int gridSpace) {
		Insets insets = getInsets();
		int firstX = insets.left;
		int firstY = insets.top;
		int lastX = this.getWidth() - insets.right;
		int lastY = this.getHeight() - insets.bottom;

		int x = firstX;
		while (x < lastX) {
			g.drawLine(x, firstY, x, lastY);
			x += gridSpace;
		}

		int y = firstY;
		while (y < lastY) {
			g.drawLine(firstX, y, lastX, y);
			y += gridSpace;
		}

		int l1;
		if(this.people == null){
			l1 = 0;
		}
		else{
			l1 = this.people.length;
		}
		for (x = 0; x < l1; ++x) {
			for (y = 0; y < people[x].length; ++y) {
				if (people[x][y].getState() != 0) {
					switch (people[x][y].getState()) {
					case 0:
						g.setColor(new Color(0x00ff00));
						break;
					case 1:
						g.setColor(new Color(0xFFFF00));
						break;
					case 2:
						g.setColor(new Color(0xff0000));
						break;
					}
					g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
				}
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < people.length) && (x > 0) && (y < people[x].length) && (y > 0)) {
			people[x][y].clicked();
			this.repaint();
		}
	}

	public void componentResized(ComponentEvent e) {
		int dlugosc = (this.getWidth() / size) + 1;
		int wysokosc = (this.getHeight() / size) + 1;
		initialize(dlugosc, wysokosc);
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < people.length) && (x > 0) && (y < people[x].length) && (y > 0)) {
			people[x][y].setState(1);
			this.repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}
}