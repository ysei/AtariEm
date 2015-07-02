package uk.org.wookey.atari.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.loomcom.symon.exceptions.MemoryRangeException;

import uk.org.wookey.atari.utils.Logger;

public class GenericEditor extends JTextPane implements KeyListener {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = new Logger(GenericEditor.class.getName());
	
	private static char TAB = '\t';
	private static char ENTER = '\n';
	
	private int lineNumber;
	
	private SimpleAttributeSet attributeSet;
	
	protected EditorStatusBar statusBar;

	public GenericEditor(EditorStatusBar sb) {
		super();

		statusBar = sb;
		
		setBackground(new Color(0xe0, 0xff, 0xf0));
		setFont(new Font("Courier", Font.PLAIN, 12));
		
		attributeSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attributeSet, Color.black);

		addKeyListener(this);
		setActionMap();
	    
	    setText("");
	    lineNumber = 1;
	    
	    try {
			this.loadFromFile("testprog.asm");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    statusBar.setLineNum(lineNumber);
	    statusBar.setLineCount(getLineCount());
	}
	
	private void setActionMap() {
	    InputMap im = getInputMap();
	    
	    KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
	    KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
	    KeyStroke up = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
	    KeyStroke down = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
	    KeyStroke left = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
	    KeyStroke right = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
	    
	    ActionMap aMap = getActionMap();

	    aMap.put(im.get(enter), new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				handleActionKey(KeyEvent.VK_ENTER, "Enter");
	    	}
	    });
	    
	    aMap.put(im.get(left), new AbstractAction() {
			private static final long serialVersionUID = 1L;

	    	public void actionPerformed(ActionEvent e) {
				handleActionKey(KeyEvent.VK_LEFT,"Left");
	    	}
	    });
	    
	    aMap.put(im.get(right), new AbstractAction() {
			private static final long serialVersionUID = 1L;

	    	public void actionPerformed(ActionEvent e) {
				handleActionKey(KeyEvent.VK_RIGHT, "Right");
	    	}
	    });
	    
	    aMap.put(im.get(up), new AbstractAction() {
			private static final long serialVersionUID = 1L;

	    	public void actionPerformed(ActionEvent e) {
				handleActionKey(KeyEvent.VK_UP, "Up");
	    	}
	    });
	    
	    aMap.put(im.get(down), new AbstractAction() {
			private static final long serialVersionUID = 1L;

	    	public void actionPerformed(ActionEvent e) {
				handleActionKey(KeyEvent.VK_DOWN, "Down");
	    	}
	    });
	    
	    aMap.put(im.get(tab), new AbstractAction() {
			private static final long serialVersionUID = 1L;

	    	public void actionPerformed(ActionEvent e) {
				handleActionKey(KeyEvent.VK_TAB, "TAB");
	    	}
	    });
	}
	
	public void setAttributeSet(SimpleAttributeSet attributes) {
		attributeSet = attributes;
	}
	
	public int getLineOfOffset(int offset) throws BadLocationException {
		if ((offset < 0) || (offset > getDocument().getLength())) {
			throw new BadLocationException("Offset out of range", offset);
		}

		_logger.logInfo("What line # is at offset " + offset + "?");
		
		String str = getText();
		
		str = str.substring(0, offset);
		
		int line = 1;
		if (offset > 0) {
			line = countLines(str);
		}
		
		_logger.logInfo("  it is line " + line);
		
		return line;
	}
	
	public int getLineEndOffset(int line) throws BadLocationException {
		// TODO: Write some code
		return 0;
	}
	
	public int getLineStartOffset(int line) throws BadLocationException {
		// TODO: Write some code
		return 0;
	}
	
	public int getLineCount() {
		return countLines(getText());
	}

	protected void handleActionKey(int keyCode, String keyName) {
		_logger.logInfo("ActionKey: " + keyName);
		
		if (keyCode == KeyEvent.VK_UP) {
			if (lineNumber > 1) {
				lineNumber--;
			}
		}
		else if (keyCode == KeyEvent.VK_DOWN) {
			if (lineNumber < (getLineCount()-1)) {
				lineNumber++;
			}
		}
		else if (keyCode == KeyEvent.VK_ENTER) {
			insertText("\n");
			lineNumber++;
		}

	    statusBar.setLineNum(lineNumber);
	    statusBar.setLineCount(getLineCount());
	}
	
	private int getCurrentLineNumber() throws BadLocationException {
		try {
			return getLineOfOffset(getCaretPosition());
		} catch (BadLocationException e) {
			_logger.logError("Caret position must be corrupt!", e);
		}
		
		return getLineOfOffset(getCaretPosition());
	}
	
	protected void colorizeCurrentLine() {
		try {
			colorizeLine(getCurrentLineNumber());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	protected void colorizeLine(int lineNum) throws BadLocationException {
		if ((lineNum < 1) || (lineNum > getLineCount())) {
			throw new BadLocationException("Line number out of range", lineNum);
		}
		
		_logger.logInfo("Colorize line " + lineNum);
		
		String line = getLine(lineNum);
		_logger.logInfo("Line is: '" + line + "'");
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();

		if ((c == TAB) || (c == ENTER)) {
			return;
		}
		
		_logger.logInfo("Key Typed: '" + c + "' (" + (int)c + ")");
		colorizeCurrentLine();
		
	    statusBar.setLineNum(lineNumber);
	    statusBar.setLineCount(getLineCount());
	}

	protected void insertText(String text) {
		insertText(text, attributeSet);
	}
	
	protected void insertText(String text, SimpleAttributeSet attributes) {
		_logger.logInfo("Inserting text '" + text + "' at position " + getCaretPosition());
		int caretPos = getCaretPosition();
		
		try {
			Document doc = getDocument();
			doc.insertString(caretPos, text, attributeSet);
			setCaretPosition(caretPos + text.length());				
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    statusBar.setLineNum(lineNumber);
	    statusBar.setLineCount(getLineCount());
	}
	
	public void setCaretAtEnd() {
		setCaretPosition(getDocument().getLength());
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	protected String getLine(int lineNum) throws BadLocationException {
		if ((lineNum < 1) || (lineNum > getLineCount())) {
			throw new BadLocationException("Line number out of range", lineNum);
		}

		String str = getText();
		int startPos = skipLines(str, lineNum-1);
		
		if (startPos == -1) {
			throw new BadLocationException("Couldn't find start of line ", lineNum);
		}
		
		str = str.substring(startPos);
		int endPos = skipLines(str, 1);
		
		if (endPos != -1) {
			str = str.substring(0, endPos-1);
		}
		
		return str;
	}
	
	private int countLines(String str) {
	    if (str == null || str.length() == 0) {
	        return 0;
	    }
	    
	    int lines = 1;
	    int len = str.length();
	    
	    for(int pos=0; pos < len; pos++) {
	        char c = str.charAt(pos);
	        if (c == '\r') {
	            lines++;
	            
	            if ((pos+1 < len) && (str.charAt(pos+1) == '\n' )) {
	                pos++;
	            }
	        } else if( c == '\n' ) {
	            lines++;
	        }
	    }
	 
	    return lines;
	}
	
	private int getLineStartOffset(String str, int offset) {
		for (int pos=offset; pos >= 0; pos--) {
			char c = str.charAt(pos);
			
			if ((c == '\r') || (c == '\n')) {
				return pos+1;
			}
		}
		
		// No preceding \r or \n found - must be the first line
		return 0;
	}
	
	public void loadFromFile(String filename) throws IOException {
		File f = new File("code/" + filename);
		
		loadFromFile(f);
	}
	
    public void loadFromFile(File file) throws IOException {
        if (file.canRead()) {
            FileInputStream fis = new FileInputStream(file);
            StringBuilder builder = new StringBuilder();
            
            int ch;
            while((ch = fis.read()) != -1){
                builder.append((char)ch);
            }
            
            setText(builder.toString());
            
            for (int i=1; i<=getLineCount(); i++) {
            	try {
					colorizeLine(i);
				} catch (BadLocationException e) {
					_logger.logError("Something wrong with linecount after reading from file", e);
					e.printStackTrace();
				}
            }
        } else {
            throw new IOException("Cannot open file " + file);
        }
    }
    
    private int skipLines(String str, int lines) {
    	if (lines == 0) {
    		return 0;
    	}
    	
    	int offset = 0;
    	int len = str.length();
    	
    	for (int pos=0; pos<len; pos++) {
    		if (lines == 0) {
    			return offset;
    		}
    		
	        char c = str.charAt(pos);
	        if (c == '\r') {
	            lines--;
	            offset = pos+1;
	            
	            if ((pos+1 < len) && (str.charAt(pos+1) == '\n')) {
	                pos++;
	                offset++;
	            }
	        } else if (c == '\n') {
	            lines--;
	            offset = pos+1;
	        }
    	}
    	
    	_logger.logInfo("Fell out of loop in skipLines() - offset=" + offset + ", lines=" + lines);
    	
    	if (lines == 0) {
    		// just in time!
    		return offset;
    	}
    	
    	return -1;
    }
}