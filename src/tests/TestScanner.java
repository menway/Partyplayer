package tests;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import library.Library;
import library.Scanner;
import library.SupportedFilesFilter;
import library.conditions.AbstractSongCondition;
import library.conditions.GenreCondition;
import library.interfaces.ScanListener;
import library.interfaces.SongStream;

public class TestScanner implements ScanListener {

	private boolean scanFinished;
	private FileFilter filter;
	private Library library;
	private File directory;
	
	public void init() {
		scanFinished = false;
		filter = new SupportedFilesFilter();
		try {
			library = new Library(new File(System.getProperty("user.dir") + "/music"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		directory = new File("/home/jokke/.gvfs/media auf 192.168.2.102/Music/iTunes/iTunes Media/Music");
	}
	public void testScan() {
		System.out.println("Scan Results:");
		Scanner scanner = new Scanner(this, directory, filter, library);
		Executor executor = Executors.newSingleThreadExecutor();
		executor.execute(scanner);
		while(!scanFinished)
			try {
				Thread.sleep(5000);
				//testQuery();
				//testDatabase();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println();
	}
	public void testQuery() {
		try {
			List<SongStream> songs = library.getSongsByCondition(new GenreCondition("Drum", AbstractSongCondition.SUBSTRING_MATCH_MODE));
			for(SongStream song : songs)
				System.err.println(song.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void testDatabase() {
		try {
			List<SongStream> songs = library.getAllSongs();
			for(SongStream song : songs)
				System.err.println(song.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void scanFinished() {
		scanFinished = true;
	}

	@Override
	public void songFound(SongStream song) {
		System.out.println(song.toString());
	}
	public static void main(String[] args) {
		TestScanner scanner = new TestScanner();
		scanner.init();
		scanner.testScan();
		scanner.testQuery();
	}
}
