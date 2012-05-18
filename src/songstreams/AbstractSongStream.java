package songstreams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import library.interfaces.SongStream;

public abstract class AbstractSongStream implements SongStream, Serializable {

	private static final long serialVersionUID = 5954836212512835718L;
	
	protected final Object[] metadata = new Object[8]; 
	protected final int ARTIST = 0;
	protected final int TITLE = 1;
	protected final int ALBUM = 2;
	protected final int GENRE = 3;
	protected final int COMMENT = 4;
	
	protected final int YEAR = 5;
	protected final int TRACK_NUM = 6;
	protected final int BPM = 7;

	public static SongStream getFileSongStream(String artist, String title,
			String album, String genre, int year, int trackNum,
		int bpm, String comment, String url, String type) throws Exception {
		URL url2 = new URL(url);
		File file = new File(url2.toURI());
		AbstractSongStream stream = AbstractFileSongStream.getInstance(file, type);
		stream.setMetadata(artist, title, album, genre, year, trackNum, bpm, comment);
		return stream;
	}
	public static SongStream getURLSongStream(String artist, String title,
			String album, String genre, int year, int trackNum,
		int bpm, String comment, String url, String type) throws Exception {
		URL url2 = new URL(url);
		AbstractSongStream stream = AbstractURLSongStream.getInstance(url2);
		stream.setMetadata(artist, title, album, genre, year, trackNum, bpm, comment);
		return stream;
	}
	
	public void init() {
		setMetaData();
	}
	private void setMetadata(String artist, String title, String album, String genre, int year, int trackNum, int bpm, String comment) {
		metadata[ARTIST] = artist;
		metadata[TITLE] = title;
		metadata[ALBUM] = album;
		metadata[GENRE] = genre;
		metadata[YEAR] = year;
		metadata[TRACK_NUM] = trackNum;
		metadata[BPM] = bpm;
		metadata[COMMENT] = comment;
	}
	protected void setMetadata(int field, Object data) {
		if(field < 5)
			metadata[field] = (String) data;
		else
			metadata[field] = (int) data;
	}
	protected abstract void setMetaData();

	public String getArtist() {
		return (String) metadata[ARTIST];
	}
	public String getAlbum() {
		return (String) metadata[ALBUM];
	}
	public String getName() {
		return (String) metadata[TITLE];
	}
	public int getTrackNum() {
		if(metadata[TRACK_NUM] instanceof Integer)
			return (int) metadata[TRACK_NUM];
		return -1;
	}
	public int getYear() {
		if(metadata[YEAR] instanceof Integer)
			return (int) metadata[YEAR];
		return -1;
	}
	public String getGenre() {
		return (String) metadata[GENRE];
	}
	public int getBpm() {
		if(metadata[BPM] instanceof Integer)
			return (int) metadata[BPM];
		return -1;
	}
	public String getComment() {
		return (String) metadata[COMMENT];
	}

	public abstract InputStream getInputStream() throws IOException;
	
	@Override
	public abstract boolean equals(Object o);

	public abstract URL getURL();
	
	public String toString() {
		return getArtist() +  " - " + getName() + " [" + getAlbum() + " (" + getYear() + ")]";  
	}
}
