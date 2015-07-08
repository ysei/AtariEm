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
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.loomcom.symon.exceptions.MemoryRangeException;

import uk.org.wookey.assembler.AssemblerInput;
import uk.org.wookey.atari.utils.Logger;

public class GenericEditor extends JTextPane implements KeyListener {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = new Logger(GenericEditor.class.getName());
	
	protected static final int NO_CONTEXT = -1;
	
	private static final char TAB = '\t';
	private static final char ENTER = '\n';
	
	protected int lineNumber;
	protected String wordSeperators;
	protected String quoteCharacters;

	protected SimpleAttributeSet attributeSet;
	
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
		setupContexts();
	    
	    setText("");
	    lineNumber = 1;
	    
	    try {
			this.loadFromFile("testprog.asm");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    statusBar.setLineNum(lineNumber);
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
	
	protected void insertChar(char c) {
		_logger.logInfo("Inserting char '" + c + "' at position " + getCaretPosition());
		
		int offset = getCaretPosition();
		int contextId = getContextId(offset);
		
		SimpleAttributeSet attr = getAttributeSet(contextId);
		//setCharacterAttributes(attr, true);
		try {
			getDocument().insertString(offset, "" + c,  attr);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setCaretPosition(offset+1);
	}
	
	protected void insertText(String text) {
		int len = text.length();
		
		for (int i=0; i<len; i++) {
			insertChar(text.charAt(i));
		}
	}
	
	protected void handleActionKey(int keyCode, String keyName) {
		_logger.logInfo("ActionKey: " + keyName);
		
		if (keyCode == KeyEvent.VK_UP) {
			_logger.logInfo("UP");
			if (lineNumber > 1) {
				lineNumber--;
			}
		}
		else if (keyCode == KeyEvent.VK_DOWN) {
			_logger.logInfo("UP");
			if (lineNumber < (getLineCount()-1)) {
				lineNumber++;
			}
		}
		else if (keyCode == KeyEvent.VK_ENTER) {
			_logger.logInfo("UP");
			insertText("\n");
			lineNumber++;
		}

	    statusBar.setLineNum(lineNumber);
	    statusBar.setLineCount(getLineCount());
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();

		if ((c == TAB) || (c == ENTER)) {
			return;
		}
		
		this.insertChar(c);
		e.consume();
		
		_logger.logInfo("Key Typed: '" + c + "' (" + (int)c + ")");
		
	    statusBar.setLineNum(lineNumber);
	    statusBar.setLineCount(getLineCount());
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	public void loadFromFile(String filename) throws IOException {
		File f = new File("code/" + filename);
		
		loadFromFile(f);
	}
	
    public void loadFromFile(File file) throws IOException {
        if (file.canRead()) {
            FileInputStream fis = new FileInputStream(file);
            
            int ch;
            while((ch = fis.read()) != -1){
                insertChar((char)ch);
            }
            
            fis.close();
        } else {
            throw new IOException("Cannot open file " + file);
        }
    }
    
    protected int getContextId(int offset) {
    	return NO_CONTEXT;
    }
    
    protected int getContextId() {
    	return getContextId(getCaretPosition());
    }
    
    protected SimpleAttributeSet getAttributeSet(int contextId) {
    	return attributeSet;
    }
    
    public int getLineCount() {
    	//TODO:
    	return 4;
    }
    
    protected void setupContexts() {   	
    }
    
    protected int getLineStart(int offset) {
    	int pos = offset;
    	String str = getText();
    	int len = str.length();
    	
    	if ((len == 0) || (offset >= len)) {
    		return len;
    	}
    	
    	while (pos >= 0) {
    		char c = str.charAt(pos);
    		
    		if (c == '\n') {
    			return pos+1;
    		}
    		
    		pos--;
    	}
    	
    	return 0;
    }
    
    protected int getLineEnd(int offset) {
    	int pos = offset;
    	String str = getText();
    	int len = str.length();
    	
    	while (pos < len) {
    		char c = str.charAt(pos);
    		
    		if (c == '\n') {
    			return pos;
    		}
    		
    		pos++;
    	}
    	
    	return len;
    	
    }
}