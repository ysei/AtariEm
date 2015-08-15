package uk.org.wookey.atari.editor;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

import uk.org.wookey.atari.exceptions.MemoryRangeException;
import uk.org.wookey.atari.utils.Logger;

public class AssemblyCodeEditor extends RSyntaxTextArea {
	private final static Logger _logger = new Logger("AssemblyCodeEditor");
	
	private static final long serialVersionUID = 1L;
	
	public AssemblyCodeEditor() {
		super();
		
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
		
		atmf.putMapping("text/as65", "uk.org.wookey.atari.editor.AssemblerTokenMaker");
		
		setSyntaxEditingStyle("text/as65");
	}
	
	public void openFile(File file) throws IOException {
		if (file.canRead()) {
			String content = new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())));
			
			setText(content);
			this.setCaretPosition(0);
		} else {
			throw new IOException("Cannot open file " + file);
		}
	}
}