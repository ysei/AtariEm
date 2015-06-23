package uk.org.wookey.atari.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.org.wookey.atari.sim.Simulator;
import uk.org.wookey.atari.utils.Logger;

public class CollapsablePanel extends JPanel {
	private final static Logger _logger = new Logger(Simulator.class.getName());

	private boolean selected;
    private JPanel contentPanel_;
    private HeaderPanel headerPanel_;
    
    public CollapsablePanel(String text, JPanel panel) {
        super(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 3, 0, 3);
        gbc.weightx = 1.0;
        gbc.fill = gbc.HORIZONTAL;
        gbc.gridwidth = gbc.REMAINDER;
        
        _logger.logInfo("Create new CollapsablePanel called '" + text + "'");
 
        selected = false;
        headerPanel_ = new HeaderPanel(text);
 
        setBackground(new Color(200, 200, 220));
        contentPanel_ = panel;
 
        add(headerPanel_, gbc);
        add(contentPanel_, gbc);
        contentPanel_.setVisible(false);
 
        JLabel padding = new JLabel();
        gbc.weighty = 1.0;
        add(padding, gbc);
 
    }
 
    public void toggleSelection() {
        selected = !selected;
 
        if (contentPanel_.isShowing())
            contentPanel_.setVisible(false);
        else
            contentPanel_.setVisible(true);
 
        validate();
 
        headerPanel_.repaint();
    }

    
    private class HeaderPanel extends JPanel implements MouseListener {
		private static final long serialVersionUID = 1L;

	    private ImageIcon open;
	    private ImageIcon closed;
	 
		String text_;
        Font font;
        final int OFFSET = 30, PAD = 5;
 
        public HeaderPanel(String text) {
            addMouseListener(this);
            text_ = text;
            font = new Font("sans-serif", Font.PLAIN, 12);
            // setRequestFocusEnabled(true);
            setPreferredSize(new Dimension(200, 32));
            int w = getWidth();
            int h = getHeight();
            
            open = new ImageIcon(this.getClass().getResource("/images/down_arrow.png"));
            closed = new ImageIcon(this.getClass().getResource("/images/right_arrow.png"));
        }
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            int h = getHeight();
            
            if (selected) {
            	open.paintIcon(this, g, 0, 0);
            }
            else {
            	closed.paintIcon(this,  g,  0,  0);
            }

            g2.setFont(font);
            FontRenderContext frc = g2.getFontRenderContext();
            LineMetrics lm = font.getLineMetrics(text_, frc);
            float height = lm.getAscent() + lm.getDescent();
            float x = OFFSET;
            float y = (h + height) / 2 - lm.getDescent();
            g2.drawString(text_, x, y);
        }
 
        public void mouseClicked(MouseEvent e) {
            toggleSelection();
        }
 
        public void mouseEntered(MouseEvent e) {
        }
 
        public void mouseExited(MouseEvent e) {
        }
 
        public void mousePressed(MouseEvent e) {
        }
 
        public void mouseReleased(MouseEvent e) {
        }

    } 
}
