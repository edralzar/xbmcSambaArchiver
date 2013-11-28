package net.edralzar.xbmc.archiver.screens;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.CheckBoxList;
import com.googlecode.lanterna.gui.listener.WindowAdapter;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal.Color;
import com.googlecode.lanterna.terminal.TerminalSize;

public class FileChooserWindow extends Window {

	public FileChooserWindow(final String baseDirectory) {
		super("Choose files to archive");

		final CheckBoxList folderList = new CheckBoxList();
		folderList.addItem("test1");
		folderList.addItem("test2");

		addComponent(folderList);
		
		Button okButton = new Button("Ok", new Action() {
			
			public void doAction() {
				Screen screen = getOwner().getScreen();
				screen.clear();
				screen.putString(0, 0, "Following files where choosen : ", Color.WHITE, Color.BLACK);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < folderList.getNrOfItems(); i++) {
					Object item = folderList.getItemAt(i);
					if (folderList.isChecked(i))
						sb.append(item).append(", ");
				}
				screen.putString(0, 1, sb.toString(), Color.BLUE, Color.BLACK);
				close();
				screen.refresh();
			}
		});

		addComponent(okButton);

		setSoloWindow(true);
		setWindowSizeOverride(new TerminalSize(400, 400));
		addWindowListener(new WindowAdapter() {
			public void onUnhandledKeyboardInteraction(Window paramWindow, Key paramKey) {
				if (paramKey.getKind() == Kind.Escape) {
					paramWindow.close();
					paramWindow.getOwner().getScreen().stopScreen();
				}
			}
		});
	}


}
