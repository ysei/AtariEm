package uk.org.wookey.atari.editor;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uk.org.wookey.atari.utils.Logger;

public class CodeEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = new Logger(CodeEditor.class.getName());

	private AssemblyCodeEditor editor;
	private EditorStatusBar statusBar;
	
	public CodeEditor() {
		super();
		
		setLayout(new BorderLayout());
		
		statusBar = new EditorStatusBar();
		editor = new AssemblyCodeEditor(statusBar);
		
		JScrollPane scroller = new JScrollPane(editor);
		scroller.setRowHeaderView(new TextLineNumber(editor));
		//scroller.setRowHeaderView(new AssemblerOutput(editor));

		add(statusBar, BorderLayout.SOUTH);
		add(scroller, BorderLayout.CENTER);
	}
	
	public InputStream toInputStream() {
		return new CodeStream(editor);
	}
	
	
	private class CodeStream extends InputStream {
		private AssemblyCodeEditor editor;
		private int index;
		private int length;
		
		private int markIndex;
		private int readLimit;
		private String buffer;
		
		public CodeStream(AssemblyCodeEditor editor) {
			this.editor = editor;
			
			buffer = editor.getText();
			index = 0;
			length = buffer.length();
			
			markIndex = 0;
			readLimit = 0;
		}
		
		public void reset() {
			index = markIndex;
		}
		
		public void close() {
			index = 0;
			length = -1;
		}
		
		public boolean markSupported() {
			return false;
		}
		
		public void mark(int readlimit) {
			markIndex = index;
			readLimit = readlimit;
		}
		
		public int available() {
			return length - index;
		}
		
		public long skip(long numbytes) {
			if (numbytes > (length - index)) {
				// Not that many bytes available
				int res = length - index;
				
				index = length;
				return res;
			}
			
			index += numbytes;
			
			return numbytes;
		}
		
		@Override
		public int read() throws IOException {
			if (index >= length) {
				throw new IOException("Read past end of buffer");
			}
			
			char c = buffer.charAt(index++);
			
			_logger.logInfo("c='" + c + "' (" + (int)c + ")");
			
			return c;
		}
	}
}
