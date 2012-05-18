package songstreams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import library.interfaces.SongStream;

/**
 * 
 * @author Joakim Reinert
 *
 */
public abstract class AbstractSongStream implements SongStream, Serializable {

	private static final long serialVersionUID = 5954836212512835718L;
	
	protected final Object[] metadata = new Object[8]; 
	
	/**
	 * Array indexes for the meta data
	 */
	protected final int ARTIST = 0;
	protected final int TITLE = 1;
	protected final int ALBUM = 2;
	protected final int GENRE = 3;
	protected final int COMMENT = 4;
	
	protected final int YEAR = 5;
	protected final int TRACK_NUM = 6;
	protected final int BPM = 7;

	/**
	 * Creates a new FileSongStream for the given arguments
	 * @param artist - The artist of the song
	 * @param title - The title of the song
	 * @param album - The album of the song
	 * @param genre - The genre of the song
	 * @param year - The publishing year of the song
	 * @param trackNum - The track number on the album
	 * @param bpm - The beats per minute value of the song
	 * @param comment - The comments for the song
	 * @param url - The url pointing to the location of the song
	 * @param type - The AudioFileFormat type of the song
	 * @return a FileSongStream for the given arguments
	 * @throws Exception if there is a Problem with the URL or if the type is invalid or unsupported
	 */
	public static SongStream getFileSongStream(String artist, String title,
			String album, String genre, int year, int trackNum,
		int bpm, String comment, String url, String type) throws Exception {
		URL url2 = new URL(url);
		File file = new File(url2.toURI());
		AbstractSongStream stream = AbstractFileSongStream.getInstance(file, type);
		stream.setMetadata(artist, title, album, genre, year, trackNum, bpm, comment);
		return stream;
	}
	/**
	 * Creates a new URLSongStream for the given arguments
	 * @param artist - The artist of the song
	 * @param title - The title of the song
	 * @param album - The album of the song
	 * @param genre - The genre of the song
	 * @param year - The publishing year of the song
	 * @param trackNum - The track number on the album
	 * @param bpm - The beats per minute value of the song
	 * @param comment - The comments for the song
	 * @param url - The url pointing to the location of the song
	 * @param type - The AudioFileFormat type of the song
	 * @return a URLSongStream for the given arguments
	 * @throws Exception if there is a Problem with the URL
	 */
	public static SongStream getURLSongStream(String artist, String title,
			String album, String genre, int year, int trackNum,
		int bpm, String comment, String url, String type) throws Exception {
		URL url2 = new URL(url);
		AbstractSongStream stream = AbstractURLSongStream.getInstance(url2);
		stream.setMetadata(artist, title, album, genre, year, trackNum, bpm, comment);
		return stream;
	}
	/**
	 * Initializes the SongStream
	 */
	public void init() {
		setMetaData();
	}
	/**
	 * Sets the meta data to the given values.
	 * @param artist - The artist of the song
	 * @param title - The title of the song
	 * @param album - The album of the song
	 * @param genre - The genre of the song
	 * @param year - The publishing year of the song
	 * @param trackNum - The track number on the album
	 * @param bpm - The beats per minute value of the song
	 * @param comment - The comments for the song
	 */
	private void setMetadata(String artist, String title, String album, String genre, int year, int trackNum, int bpm, String comment) {
		setMetadata(ARTIST, artist);
		setMetadata(TITLE, title);
		setMetadata(ALBUM, album);
		setMetadata(GENRE, genre);
		setMetadata(YEAR, year);
		setMetadata(TRACK_NUM, trackNum);
		setMetadata(BPM ,bpm);
		setMetadata(COMMENT, comment);
	}
	/**
	 * Sets the meta data of the given index to the given data
	 * @param index - the index of the meta data in the metadata array
	 * @param data - the data to set
	 */
	protected void setMetadata(int index, Object data) {
		if(index < 5)
			metadata[index] = (String) data;
		else
			metadata[index] = (Integer) data;
	}
	/**
	 * Sets the meta data read from the underlying file or url
	 */
	protected abstract void setMetaData();
	
	@Override
	public String getArtist() {
		return (String) metadata[ARTIST];
	}
	@Override
	public String getAlbum() {
		return (String) metadata[ALBUM];
	}
	@Override
	public String getTitle() {
		return (String) metadata[TITLE];
	}
	@Override
	public int getTrackNum() {
		if(metadata[TRACK_NUM] instanceof Integer)
			return (Integer) metadata[TRACK_NUM];
		return -1;
	}
	@Override
	public int getYear() {
		if(metadata[YEAR] instanceof Integer)
			return (Integer) metadata[YEAR];
		return -1;
	}
	@Override
	public String getGenre() {
		return (String) metadata[GENRE];
	}
	
	@Override
	public int getBpm() {
		if(metadata[BPM] instanceof Integer)
			return (Integer) metadata[BPM];
		return -1;
	}
	
	@Override
	public String getComment() {
		return (String) metadata[COMMENT];
	}
		
	@Override
	public abstract boolean equals(Object o);
	
	@Override
	public abstract URL getURL();
	
	@Override
	public String toString() {
		return getArtist() +  " - " + getTitle() + " [" + getAlbum() + " (" + getYear() + ")]";  
	}
}
