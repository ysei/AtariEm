package uk.org.wookey.atari.editor;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class EditorStatusBar extends JPanel {
	private int lineNum;
	private int caretPosition;
	
	private JLabel positionLab;
	private JLabel debugLab;
	
	public EditorStatusBar() {
		super();
		
		positionLab = new JLabel();
		debugLab = new JLabel();
		
		lineNum = 1;
		caretPosition = 1;
		
		add(positionLab);
		add(debugLab);
		
		updatePositionInfo();
	}
	
	public void debugMessage(String msg) {
		debugLab.setText(msg);
	}
	
	public void setLineNum(int newLineNum) {
		lineNum = newLineNum;
		updatePositionInfo();
	}
	
	public void setCaretPosition(int newCaretPosition) {
		caretPosition = newCaretPosition;
		updatePositionInfo();
	}
	
	private void updatePositionInfo() {
		positionLab.setText("" + lineNum + ": " + caretPosition);
	}
}
