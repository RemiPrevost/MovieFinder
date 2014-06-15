package mf.view.modifier;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ModifierDialog extends Object {
	public static void createDialogBackPanel(JDialog dialog, Component parent) {
        DialogBackPanel newContentPane = new DialogBackPanel(dialog);
        dialog.setContentPane(newContentPane); // dialog content is replaced by the panel
        dialog.setSize(parent.getSize()); // the dialog is made as wide as the underlying window
        dialog.setLocation(parent.getLocationOnScreen()); // the dialog is placed over this window
    }
 
    // -------------------------------------------------------------------------
 
    @SuppressWarnings("serial")
	private static class DialogBackPanel extends JPanel {
        private static final Paint fill = new Color(0xAA000000, true);
        private final JComponent cmp;
        private final JLabel title = new JLabel();
 
        public DialogBackPanel(JDialog dialog) {
            this.cmp = (JComponent) dialog.getContentPane();
 
             // Misc
            setOpaque(false); // the panel is transparent
            title.setText(dialog.getTitle());
 
             // Layout of components
            setLayout(null); // absolute layout
            add(cmp);
            add(title);
 
            cmp.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
			title.setFont(new Font("SquareFont", Font.PLAIN, 26));
			title.setForeground(Color.WHITE);

			title.setText(dialog.getTitle());
			title.setSize(title.getPreferredSize());
			cmp.setSize(dialog.getSize());
        }
 
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
 
            int w = getWidth();
            int h = getHeight();
 
            // Location of the components:
            // - the dialog original content panel is centered
            // - the title label is placed over it, aligned left
            cmp.setLocation(w/2-cmp.getWidth()/2, h/2-cmp.getHeight()/2);
            title.setLocation(w/2-cmp.getWidth()/2, h/2-cmp.getHeight()/2-title.getHeight());
 
            // Paint
            Graphics2D gg = (Graphics2D) g.create();
            gg.setPaint(fill);
            gg.fillRect(0, 0, w, h);
            gg.dispose();
        }
    }
    
    public static void fadeIn(final JDialog dialog) {
        final Timer timer = new Timer(10, null);
        timer.setRepeats(true);
        timer.addActionListener(new ActionListener() {
            private float opacity = 0;
            @Override public void actionPerformed(ActionEvent e) {
                opacity += 0.15f;
                dialog.setOpacity(Math.min(opacity, 1));
                if (opacity >= 1) timer.stop();
            }
        });
 
        dialog.setOpacity(0);
        timer.start();
        dialog.setVisible(true);
    }
 
    /**
     * Creates an animation to fade the dialog opacity from 1 to 0.
     */
    public static void fadeOut(final JDialog dialog) {
        final Timer timer = new Timer(10, null);
        timer.setRepeats(true);
        timer.addActionListener(new ActionListener() {
            private float opacity = 1;
            @Override public void actionPerformed(ActionEvent e) {
                opacity -= 0.15f;
                dialog.setOpacity(Math.max(opacity, 0));
                if (opacity <= 0) {
                	timer.stop(); 
                	dialog.dispose();
                }
            }
        }); 
        dialog.setOpacity(1);
        timer.start();
        
    }
}