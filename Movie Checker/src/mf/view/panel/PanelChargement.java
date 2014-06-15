package mf.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelChargement extends JPanel {
	private int satellite1X = -100;
	private int satellite1Y = -100;
	private int satellite2X = -100;
	private int satellite2Y = -100;
	boolean unset = true;
	Image img_croix = null;
	Image img_planet = null;
	Image img_satellite1 = null;
	Image img_satellite2 = null;
	
	public PanelChargement() {super(new BorderLayout());}
	
	public void paintComponent(Graphics g){
		int x1 = this.getWidth()*11/32;
		int y1 = this.getHeight()*10/32;
		
		if (unset)
		{
			try {
				img_croix = ImageIO.read(new File("fermer.jpg"));
				img_planet = ImageIO.read(new File("planet.png"));
				img_satellite1 = ImageIO.read(new File("satellite.png"));
				img_satellite2 = ImageIO.read(new File("satellite.png"));
			} catch (Exception e) {
				System.out.println("Cannot open picture");
			} finally {
				unset = false;
			}
		}
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.drawImage(img_satellite1,satellite1X, satellite1Y, this);
		g.drawImage(img_satellite2,satellite2X, satellite2Y, this);
		g.drawImage(img_planet,x1,y1,this);
		//g.drawImage(img_croix, this.getWidth()-35, 5, this);
	}

	public int getSatellite1X() {
		return satellite1X;
	}

	public void setSatellite1X(int satellite1x) {
		satellite1X = satellite1x;
	}

	public int getSatellite1Y() {
		return satellite1Y;
	}

	public void setSatellite1Y(int satellite1y) {
		satellite1Y = satellite1y;
	}

	public int getSatellite2X() {
		return satellite2X;
	}

	public void setSatellite2X(int satellite2x) {
		satellite2X = satellite2x;
	}

	public int getSatellite2Y() {
		return satellite2Y;
	}

	public void setSatellite2Y(int satellite2y) {
		satellite2Y = satellite2y;
	}       
}
