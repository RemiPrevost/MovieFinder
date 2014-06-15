package mf.view;

import javax.swing.JFrame;

import mf.view.panel.PanelChargement;

@SuppressWarnings("serial")
public class Chargement extends JFrame{
	private int size_window = 400;
	private PanelChargement pc = new PanelChargement();
	
	public Chargement() {
		super();

		this.setTitle("MovieFinder : LOADING");
		this.setSize(size_window,size_window);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setContentPane(pc);

		
		this.setVisible(true);
		go();
	}
	
	private void go() {
		double teta;

		while(true) {
			pc.setSatellite1X(pc.getWidth()/2-this.getWidth()/24);
			pc.setSatellite1Y(pc.getHeight()/2-this.getHeight()/24);
			pc.setSatellite2X(pc.getWidth()/2-this.getWidth()/24);
			pc.setSatellite2Y(pc.getHeight()/2-this.getHeight()/24);
			teta = Math.PI;
			
			while (pc.getSatellite1X() > pc.getHeight()/4-this.getHeight()/24){
				pc.setSatellite1X(pc.getSatellite1X() - 1);
				pc.setSatellite2X(pc.getSatellite2X() + 1);
				this.repaint();
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			while (pc.getSatellite1X() <= pc.getWidth()/2-this.getHeight()/24) {
				pc.setSatellite1X((int)(pc.getWidth()/2-this.getWidth()/24+pc.getWidth()*1/4*Math.cos(-teta)));
				pc.setSatellite1Y((int)(pc.getHeight()/2-this.getHeight()/24+pc.getHeight()*1/4*Math.sin(-teta)));
				pc.setSatellite2X((int)(pc.getWidth()/2-this.getWidth()/24+pc.getWidth()*1/4*Math.cos(teta-Math.PI)));
				pc.setSatellite2Y((int)(pc.getHeight()/2-this.getHeight()/24+pc.getHeight()*1/4*Math.sin(-teta-Math.PI)));
				teta += Math.PI / 500;
				this.repaint();
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			while (pc.getSatellite1Y() > pc.getWidth()/2-this.getWidth()/24){
				pc.setSatellite1Y(pc.getSatellite1Y() - 1);
				pc.setSatellite2Y(pc.getSatellite2Y() + 1);
				this.repaint();
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
