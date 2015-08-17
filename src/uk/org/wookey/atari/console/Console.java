package uk.org.wookey.atari.console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import uk.org.wookey.atari.utils.Logger;

public class Console extends JComponent {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = new Logger("Console");
	
	private final static String PRINTABLE_CHAR_ACTION = "Printable";

	private final Font font = new Font("Monospaced", Font.PLAIN, CELL_HEIGHT);
	private static final int CELL_WIDTH = 8;
	private static final int CELL_HEIGHT = 12;
	
	private Cell screen[][];

	private int numCols;
	private int numRows;
	
	private int cursorCol;
	private int cursorRow;
	
	private InputMap inputMap;
	
	public Console() {
		cursorCol = 0;
		cursorRow = 0;
		
		inputMap = new ConsoleInputMap();
		
		@SuppressWarnings("serial")
		Action injectPrintable = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		    	put(e.getActionCommand());
		        _logger.logInfo("Inject char: " + e.getActionCommand());
		    }
		};
		
		ActionMap am = new ActionMap();
		am.put(PRINTABLE_CHAR_ACTION, injectPrintable);
		
		setInputMap(WHEN_FOCUSED, inputMap);
		
		setActionMap(am);
		
		setEnabled(true);
		setFocusable(true);
		
		setMinimumSize(new Dimension(25, 80));
		setMaximumSize(new Dimension(100, 200));
		setPreferredSize(getMinimumSize());

		numRows = 25;
		numCols = 80;

		screen = new Cell[numCols][numRows];
		for (int col=0; col<numCols; col++) {
			for (int row=0; row<numRows; row++) {
				screen[col][row] = new Cell('*');
			}
		}
        
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
                super.mouseClicked(e);
            }
        });
	}
	
	private void scroll() {
		for (int col=0; col<numCols; col++) {
			for (int row=1; row<numRows; row++) {
				screen[col][row-1] = screen[col][row];
			}
		}
		
		for (int col=0; col<numCols; col++) {
			screen[col][numRows-1].setChar(' ');
		}
	}
	
	private void cursorRight() {
		cursorCol++;
		if (cursorCol >= numCols) {
			cursorRow++;
			cursorCol = 0;
			if (cursorRow >= numRows) {
				scroll();
				cursorRow = numRows-1;
			}
		}
	}
	
	public void put(char c) {
		_logger.logInfo("Put char: " + c + " at " + cursorCol + ", " + cursorRow);
		
		screen[cursorCol][cursorRow].setChar(c);
		cursorRight();
	}
	
	public void put(char c[]) {
		for (char ch: c) {
			put(ch);
		}
		
		repaint();
	}
	
	public void put(String str) {
		for (char c: str.toCharArray()) {
			put(c);
		}
		
		repaint();
	}
	
	public void paint(Graphics g) {
		g.setFont(font);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, numCols * CELL_WIDTH, numRows * CELL_HEIGHT);

		for (int y = 0; y < numRows; y++) {
			for (int x = 0; x < numCols; x++) {
				Cell cell = screen[x][y];
				boolean cursorHere = (cursorRow == y) && (cursorCol == x);

				if (cursorHere && cell == null) {
					cell = new Cell('_');
				}

				if (cell != null) {
					int px = x * CELL_WIDTH;
					int py = y * CELL_HEIGHT;

					g.setColor(Color.BLACK);
					g.fillRect(px, py, CELL_WIDTH, CELL_HEIGHT);

					g.setColor(Color.GREEN);
					g.drawChars(new char[] { cell.getChar() }, 0, 1, px, py + CELL_HEIGHT);
				}
			}
		}
	}
	
	@SuppressWarnings("serial")
	private class ConsoleInputMap extends InputMap {
		private final static String printables = 
				"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		public ConsoleInputMap() {
			super();
			
			for (char c: printables.toCharArray()) {
				_logger.logInfo("Mapping char '" + c + "' as injectable");
				put(KeyStroke.getKeyStroke(c),  PRINTABLE_CHAR_ACTION);
			}
		}
	}
	
	private class Cell {
		private char ch;
		
		public Cell(char c) {
			ch = c;
		}
		
		public char getChar() {
			return ch;
		}
		
		public void setChar(char c) {
			ch = c;
		}
	}
}
