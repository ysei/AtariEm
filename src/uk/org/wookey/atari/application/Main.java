package uk.org.wookey.atari.application;

import javax.swing.SwingUtilities;

import uk.org.wookey.atari.ui.ApplicationWindow;

public class Main {
	 public static void main(String[] args) {
		 SwingUtilities.invokeLater(new Runnable() {

			 @Override
			 public void run() {
				 @SuppressWarnings("unused")
				ApplicationWindow win = new ApplicationWindow();
			 }
		 });
	 }
}
