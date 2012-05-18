package library;

import java.io.File;
import java.io.FileFilter;

/**
 * File filter that accepts only supported audio files
 * @author ek36wori
 *
 */
public final class SupportedFilesFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".mp3");
	}

}
