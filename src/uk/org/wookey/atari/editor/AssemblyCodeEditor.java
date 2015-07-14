package uk.org.wookey.atari.editor;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

import uk.org.wookey.atari.utils.Logger;

public class AssemblyCodeEditor extends RSyntaxTextArea {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = new Logger(AssemblyCodeEditor.class.getName());
	
	public AssemblyCodeEditor(EditorStatusBar sb) {
		super();
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
		
		atmf.putMapping("text/as65", "uk.org.wookey.atari.editor.AssemblerTokenMaker");
		
		setSyntaxEditingStyle("text/as65");
	}
}
