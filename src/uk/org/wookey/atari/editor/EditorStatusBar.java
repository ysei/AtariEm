package uk.org.wookey.atari.editor;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class EditorStatusBar extends JPanel {
	private int lineNum;
	private int lineCount;
	private int caretPosition;
	
	private JLabel positionLab;
	private JLabel lineCountLab;
	private JLabel debugLab;
	
	public EditorStatusBar() {
		super();
		
		positionLab = new JLabel();
		lineCountLab = new JLabel();
		debugLab = new JLabel();
		
		lineNum = 1;
		lineCount = 1;
		caretPosition = 1;
		
		add(positionLab);
		add(lineCountLab);
		add(debugLab);
		
		updateStatusInfo();
	}
	
	public void debugMessage(String msg) {
		debugLab.setText(msg);
	}
	
	public void setLineNum(int newLineNum) {
		lineNum = newLineNum;
		updateStatusInfo();
	}
	
	public void setLineCount(int newLineCount) {
		lineCount = newLineCount;
		updateStatusInfo();
	}
	
	public void setCaretPosition(int newCaretPosition) {
		caretPosition = newCaretPosition;
		updateStatusInfo();
	}
	
	private void updateStatusInfo() {
		positionLab.setText("" + lineNum + ": " + caretPosition);
		lineCountLab.setText("$=" + lineCount);
	}
}
