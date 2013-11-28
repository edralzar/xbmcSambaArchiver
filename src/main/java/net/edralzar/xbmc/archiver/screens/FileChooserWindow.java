package net.edralzar.xbmc.archiver.screens;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import net.edralzar.xbmc.archiver.Core;

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


		List<File> files = Core.listFiles(baseDirectory, new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".avi");
			}
		});

		final CheckBoxList folderList = new CheckBoxList() {
			protected String createItemString(int index) {
				String original = super.createItemString(index);

				return original.replaceFirst("\\Q" + baseDirectory + "\\E", "");
			}
		};
		for (File file : files) {
			folderList.addItem(file);
		}

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
