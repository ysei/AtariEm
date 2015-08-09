package uk.org.wookey.atari.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

public class Cabinet extends JPanel {
    private static final int EMPTY_BORDER = 10;

	public Cabinet() {
		super();
		
		Dimension d = new Dimension(300, 600);
		
		setSize(d);
		setPreferredSize(d);
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		add(new JPanel(), gbc);
		
		gbc.gridy++;
		gbc.fill = GridBagConstraints.NONE;
		add(new VectorMonitor(), gbc);
		
		gbc.gridy++;
		add(new ControlPanel(), gbc);
		
		gbc.gridy++;		
		add(new CoinDoor(), gbc);
		
		//setBorder(new LineBorder(Color.BLACK));
		
        Border emptyBorder = BorderFactory.createEmptyBorder(EMPTY_BORDER, EMPTY_BORDER,
                EMPTY_BORDER, EMPTY_BORDER);
        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        setBorder(BorderFactory.createCompoundBorder(emptyBorder, etchedBorder));
	
		//setBackground(new Color(200, 200, 0));
	}
}
