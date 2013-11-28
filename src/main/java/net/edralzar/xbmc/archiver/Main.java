package net.edralzar.xbmc.archiver;

import net.edralzar.xbmc.archiver.screens.FileChooserWindow;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;


public class Main {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		GUIScreen screen = TerminalFacade.createGUIScreen();
		screen.getScreen().startScreen();

		Key k = null;
		while ((k = screen.getScreen().readInput()) == null
				|| (k.getKind() != Kind.Enter && k.getKind() != Kind.Escape))
			Thread.sleep(1000);

		screen.getScreen().stopScreen();
	}

}
