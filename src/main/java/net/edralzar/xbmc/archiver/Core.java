package net.edralzar.xbmc.archiver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class Core {

	private static final int BUFFER_SIZE = 5096;

	public static final List<File> listFiles(String commonBaseDir, FilenameFilter acceptFilter) {
		File baseDir = new File(commonBaseDir);
		if (!baseDir.isDirectory() || !baseDir.exists())
			throw new IllegalArgumentException("Base dir " + commonBaseDir
					+ " is not an existing directory");

		return recursiveListFiles(baseDir, acceptFilter);
	}

	public static final List<File> recursiveListFiles(File base, FilenameFilter acceptFilter) {
		if (!base.isDirectory())
			return Collections.emptyList();

		File[] subFiles = base.listFiles();
		ArrayList<File> result = new ArrayList<File>(subFiles.length);
		for (File f : subFiles) {
			if (f.isFile() && acceptFilter.accept(base, f.getName()))
				result.add(f);

			if (f.isDirectory())
				result.addAll(recursiveListFiles(f, acceptFilter));
		}

		result.trimToSize();
		return result;
	}


	public static final Set<File> listCommonSubDirectories(List<File> files) {
		Set<File> subDirs = new HashSet<File>();
		for (File file : files) {
			subDirs.add(file.getParentFile());
		}
		return subDirs;
	}

	public static final String sambaCopy(File source, String sambaBase, String destSubPath,
			NtlmPasswordAuthentication auth) {
		try {
			SmbFile sambaDestDir = new SmbFile(sambaBase + "/" + destSubPath, auth);
			if (sambaDestDir.isDirectory() && sambaDestDir.canWrite()) {
				SmbFile dest = new SmbFile(sambaDestDir.getPath(), source.getName(), auth);
				dest.createNewFile();

				try (BufferedOutputStream out = new BufferedOutputStream(dest.getOutputStream(),
						BUFFER_SIZE);
						BufferedInputStream in = new BufferedInputStream(
								new FileInputStream(source), BUFFER_SIZE);) {
					byte[] buffer = new byte[BUFFER_SIZE];
					int readBytes = 0;
					while ((readBytes = in.read(buffer)) > -1)
						out.write(buffer, 0, readBytes);

					return dest.getPath();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
