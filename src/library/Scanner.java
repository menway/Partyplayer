package library;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.sql.SQLException;

import songstreams.AbstractFileSongStream;

import library.exceptions.InvalidSongFileException;
import library.interfaces.ScanListener;
import library.interfaces.SongStream;

public class Scanner implements Runnable {
	private ScanListener listener;
	private File directory;
	private FileFilter filter;
	private Library library;
	private boolean scanning;
	
	public Scanner(ScanListener listener, File directory, FileFilter filter, Library library) {
		this.listener = listener;
		this.directory = directory;
		this.filter = filter;
		this.library = library;
		scanning = false;
	}
	public boolean isScanning() {
		return scanning;
	}
	private void traverseFiles(File directory) {
		File[] files = directory.listFiles(filter);
		File[] dirs = directory.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		for(File file : files) {
			try {
				if(library.contains(file.toURI().toURL()))
					return;
				SongStream song = AbstractFileSongStream.getInstance(file);
				if(library.addSong(song))
					listener.songFound(song);
			} catch (InvalidSongFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for(File dir : dirs)
			traverseFiles(dir);
	}
	@Override
	public void run() {
		scanning = true;
		traverseFiles(directory);
		scanning = false;
		listener.scanFinished();
	}
}
