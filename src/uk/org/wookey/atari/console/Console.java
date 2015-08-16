package uk.org.wookey.atari.console;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Console {
	private final Font font = new Font("Monospaced", Font.PLAIN, CELL_HEIGHT);
	private static final int CELL_WIDTH = 8;
	private static final int CELL_HEIGHT = 12;
	
	private Cell screen[][];

	private int numCols;
	private int numRows;
	
	private int cursorCol;
	private int cursorRow;
	
	public Console() {
		numRows = 10;
		numCols = 40;
		
		cursorCol = 1;
		cursorRow = 1;
		
		screen = new Cell[numCols][numRows];
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

	private class Cell {
		private char ch;
		
		public Cell(char c) {
			ch = c;
		}
		
		public char getChar() {
			return ch;
		}
	}
}
