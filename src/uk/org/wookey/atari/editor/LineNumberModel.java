package uk.org.wookey.atari.editor;

import java.awt.Rectangle;

/**
 * A generic model interface which defines an underlying component with line numbers.
 * @author Greg Cope
 *
 */
public interface LineNumberModel {
	public int getNumberLines();
	public Rectangle getLineRect(int line);
}