package mf.view.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.awt.AWTUtilities;

import mf.files.Fichier;
import mf.net.Stream;
import mf.view.Fenetre;
import mf.view.View;
import mf.view.modifier.ModifierDialog;

@SuppressWarnings("serial")
public class ComponentAffiche extends JComponent {

	public static final int LOCAL = 0;
	public static final int WEB = 1;
	
	private JFrame parent;
	private Image affiche;
	private Image affiche_origin;
	
	public ComponentAffiche(String url, int width, JFrame parent) throws IOException {
		this.parent = parent;
		
		// chargement de l'image depuis le disque
		affiche = ImageIO.read(new File(Fichier.AFFICHE_DIRECTORY+url));
		affiche_origin = affiche;
		
		// Si l'image est trop grande, on la redimessionne selon la valeur width passée en paramètre
		if (affiche.getWidth(this) > width)
			affiche = scale(affiche,width,(int)(View.affiche_width*((float)affiche.getHeight(this)/(float)affiche.getWidth(this))));
	
		this.setPreferredSize(new Dimension(affiche.getWidth(this),affiche.getHeight(this)));
		this.addMouseListener(new JComponentMouseEvent());
	}
	
	public ComponentAffiche(String url, int width, int location, JFrame parent) throws IOException {
		this.parent = parent;
		
		// chargement de l'image, soit depuis le disque, soit depuis internet
		if (location == LOCAL) {
			affiche = ImageIO.read(new File(Fichier.AFFICHE_DIRECTORY+url));
		}
		else {
			affiche = Stream.downloadImageToImage(url);
		}
		affiche_origin = affiche;
		
		// Si l'image est trop grande, on la redimessionne selon la valeur width passée en paramètre
		if (affiche.getWidth(this) > width)
			affiche = scale(affiche,width,(int)(width*((float)affiche.getHeight(this)/(float)affiche.getWidth(this))));
		
		this.setPreferredSize(new Dimension(affiche.getWidth(this),affiche.getHeight(this)));
		this.addMouseListener(new JComponentMouseEvent());
	}
	
	public int getHeight() {
		return affiche.getHeight(this);
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(this.getBackground());
		g.fillRect(0,0,getWidth(), getHeight() );
		g.drawImage(affiche,(getWidth()-affiche.getWidth(this))/2,(getHeight()-affiche.getHeight(this))/2,null);
	}
	
	private Image scale(Image source, int width, int height) {
		// Création de l'image aux bonnes dimensions
	    BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    
	    // On dessine sur le Graphics de l'image bufferisée
	    Graphics2D g = buf.createGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.drawImage(source, 0, 0, width, height, null);
	    g.dispose();
	 
	    return (Image)buf;
	}
	
	private class JComponentMouseEvent implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent arg0) {
			
		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent arg0) {
			
		}

		public void mouseReleased(MouseEvent e) {
			new DialogWideAffiche();
		}
		
		private class DialogWideAffiche extends JDialog implements MouseListener{
			private PanelWideAffiche panel = new PanelWideAffiche();
			private Image affiche_temp;
			
			public DialogWideAffiche() {
				super(parent,true);
				
				this.setUndecorated(true);
				AWTUtilities.setWindowOpaque(this,false);
				
				if (affiche_origin.getHeight(this) < Fenetre.WINDOW_HEIGHT)
					this.setSize(affiche_origin.getWidth(this), affiche_origin.getHeight(this));
				else
					this.setSize((int)(Fenetre.WINDOW_HEIGHT*((float)affiche.getWidth(this)/(float)affiche.getHeight(this)))-60, Fenetre.WINDOW_HEIGHT-60);
				this.setLocationRelativeTo(rootPane);
				this.setTitle("");
				
				this.getContentPane().add(panel);
				panel.addMouseListener(this);
				
				ModifierDialog.createDialogBackPanel(this, parent.getContentPane());
				ModifierDialog.fadeIn(this);
			}
			
			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {	
			}

			public void mouseReleased(MouseEvent arg0) {
				this.getContentPane().remove(panel);
				affiche_temp = null;
				panel = null;
				System.gc();
				ModifierDialog.fadeOut(this);
			}
			
			private class PanelWideAffiche extends JPanel{
				
				public PanelWideAffiche() {}
				
				public void paintComponent(Graphics g){
					super.paintComponent(g);
					if (affiche_origin.getHeight(this) < Fenetre.WINDOW_HEIGHT)
						g.drawImage(affiche_origin, 0, 0, this);
					else {
						affiche_temp = scale(affiche_origin,(int)(Fenetre.WINDOW_HEIGHT*((float)affiche.getWidth(this)/(float)affiche.getHeight(this)))-60,Fenetre.WINDOW_HEIGHT-60);
						g.drawImage(affiche_temp, 0, 0, this);
					}
				}
			}
		}
	}

	public void free() {
		affiche = null;
		affiche_origin = null;
		System.gc();
	}
}
