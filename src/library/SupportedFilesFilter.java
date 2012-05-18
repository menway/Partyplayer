package library;

import java.io.File;
import java.io.FileFilter;

public class SupportedFilesFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".mp3");
	}

}
