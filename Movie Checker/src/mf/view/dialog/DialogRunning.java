package mf.view.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import mf.exception.net.ConnectionFail;
import mf.net.Stream;
import mf.view.modifier.ModifierDialog;

import com.sun.awt.AWTUtilities;


@SuppressWarnings("serial")
public class DialogRunning extends JDialog implements ActionListener{
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;
	public static final int CANCELLED = 2;
	public static final int NC = 3;
	
	private static int state = NC;
	
	private JLabel label = new JLabel(" Recherche en cours ");
	
	private JPanel panel_message = new JPanel(new GridLayout(1,1));
	private JPanel panel_bouton = new JPanel();
	
	private JButton bouton_cancel = new JButton("Annuler");
	
	ThreadRunning thread;
	
	public DialogRunning(JFrame parent, String titre) {
		super(parent,true);
		this.setUndecorated(true);
		AWTUtilities.setWindowOpaque(this,false);
		this.setSize(500,100);
		this.setLocationRelativeTo(rootPane);
		this.setTitle("");
		
		state = NC;
		
		panel_bouton.add(bouton_cancel);
		panel_message.add(label);
		
		bouton_cancel.addActionListener(this);
		
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Dialog",Font.BOLD,30));
		
		panel_message.setBackground(Color.WHITE);
		panel_bouton.setBackground(Color.WHITE);
		this.getContentPane().add(panel_message,BorderLayout.CENTER);
		this.getContentPane().add(panel_bouton,BorderLayout.SOUTH);
		
		thread = new ThreadRunning(titre);
		thread.start();
		
		ModifierDialog.createDialogBackPanel(this, parent.getContentPane());
		ModifierDialog.fadeIn(this);
	}
	
	public void close() {
		ModifierDialog.fadeOut(this);
	}
	
	private class ThreadAnimation extends Thread {
		private volatile boolean continuer = true;
		private short compteur = 0;
		
		public ThreadAnimation() {
			super();
		}
		
		public void run() {
			while(continuer) {
				try {
					sleep(100);
				} catch (InterruptedException e) {}
				if (compteur < 3) {
					label.setText("."+label.getText() + ".");
					compteur++;
				}
				else {
					label.setText(" Recherche en cours ");
					compteur = 0;
				}
			}
		}
		
		public void end(){
			continuer = false;
		}
	}
 	
	private class ThreadRunning extends Thread{
		private String titre;
		private volatile boolean stop = false;
		
		public ThreadRunning(String titre) {
			super();
			this.titre = titre;
		}
		
		public void run() {
			ThreadAnimation thread = new ThreadAnimation();
			thread.start();
			try {
				Stream.launchSearch(titre);
				if (!stop)
					state = SUCCESS;
					close();
			} catch (ConnectionFail e) {
				state = FAIL;
				if (!stop)
					close();
			} finally {
				thread.end();
			}
		}
		
		public void end () {
			stop = true;
		}
	}
	
	public static int getState(){
		return state;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bouton_cancel) {
			System.out.println("cancel");
			thread.end();
			state = CANCELLED;
			close();
		}
	}

}
