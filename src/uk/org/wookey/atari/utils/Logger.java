package uk.org.wookey.atari.utils;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.*;

public class Logger {
	private static PrintWriter _out = null;
	private static ArrayList<JTextPane> logPanes = null;
	
	private String _logName;
	private int _logLevel = 1;
	private SimpleAttributeSet _labAttribs;
	private SimpleAttributeSet _msgAttribs;

	private SimpleAttributeSet _okAttribs;
	private SimpleAttributeSet _warnAttribs;
	private SimpleAttributeSet _errAttribs;
	
	public Logger(JTextPane log) {
		initialise("");

		logPanes.add(log);
	}
	
	public Logger(String tag) {
		initialise(tag);
	}
	
	public Logger(String tag, int level) {
		initialise(tag);
		_logLevel = level;
	}
	
	private void initialise(String tag) {
		if (logPanes == null) {
			logPanes = new ArrayList<JTextPane>();
		}
		
		if (tag.equals("")) {
			_logName = "";
		}
		else {
			_logName = "[" + tag + "]:";
		}
		
		/*
		if (_out == null) {
			try {
				_out = new PrintWriter("debug.txt");
			} catch (FileNotFoundException e) {
			}
		}
		*/
		
		_labAttribs = new SimpleAttributeSet();
		StyleConstants.setForeground(_labAttribs, Color.blue);

		_msgAttribs = new SimpleAttributeSet();
		StyleConstants.setForeground(_msgAttribs, Color.white);
		StyleConstants.setBold(_msgAttribs, false);

		_okAttribs = new SimpleAttributeSet();
		StyleConstants.setForeground(_okAttribs, Color.green);

		_warnAttribs = new SimpleAttributeSet();
		StyleConstants.setForeground(_warnAttribs, Color.orange);

		_errAttribs = new SimpleAttributeSet();
		StyleConstants.setForeground(_errAttribs, Color.red);
	}
	
	public synchronized void logMsg(String msg, SimpleAttributeSet labAttribs, SimpleAttributeSet msgAttribs) {
		if (_logLevel == 0) {
			return;
		}
		
		if (_out != null) {
			_out.println(msg);
			_out.flush();
		}
	
		if (logPanes.size() > 0) {
			append(_logName, labAttribs);
			append(' ' + msg + '\n', msgAttribs);
		}
		else {
			// We don't have a text pane yet - buffer it for later display.
			String item[] = new String[2];
			item[0] = _logName;
			item[1] = msg;
			
			System.out.println(_logName + ' ' + msg);
		}		
	}
	
	private void logMsg(String msg, SimpleAttributeSet attribs) {
		logMsg(msg, _labAttribs, attribs);
	}
	
	public synchronized void printBacktrace(String msg, Exception e, SimpleAttributeSet attribs) {
		StackTraceElement[] trace = e.getStackTrace();

		logMsg(msg, attribs);
		for (int i=0; i<trace.length; i++) {
			append(trace[i].toString() + '\n', attribs);
		}
	}
	
	public synchronized void printBacktrace(Exception e) {
		printBacktrace("Caught an exception:", e, _errAttribs);
	}
	
	protected void append(String msg, SimpleAttributeSet attributes) {
		for (JTextPane log: logPanes) {
			Document doc = log.getDocument();
		
			try {
				doc.insertString(doc.getLength(), msg, attributes);
				log.setCaretPosition(doc.getLength());		
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void logMsg(String msg) {
		logMsg(msg, _labAttribs, _msgAttribs);
	}
	
	public synchronized void logInfo(String msg) {
		logMsg(msg);
	}
	
	public synchronized void logSuccess(String msg) {
		logMsg(msg, _okAttribs);
	}
	
	public synchronized void logWarn(String msg) {
		logMsg(msg, _warnAttribs);
	}
	
	public synchronized void logError(String msg) {
		logMsg(msg, _errAttribs);
	}
	
	public synchronized void logError(String msg, Exception e) {
		printBacktrace(msg, e, _errAttribs);
	}
}
