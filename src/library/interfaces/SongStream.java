package library.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface SongStream {
	
	/**
	 * Returns the artist for this song
	 */
	public String getArtist();
	
	/**
	 * Returns the album for this song
	 */
	public String getAlbum();
	
	/**
	 * Returns the title for this song
	 */
	public String getTitle();
	
	/**
	 * Returns the track number for this song
	 */
	public int getTrackNum();
	
	/**
	 * Returns the publishing year for this song
	 */
	public int getYear();
	
	/**
	 * Returns the genre for this song
	 */
	public String getGenre();
	
	/**
	 * Returns the beats per minute value for this song
	 */
	public int getBpm();
	
	/**
	 * Returns the comment for this song
	 */
	public String getComment();

	/**
	 * Gets the InputStream for the underlying file or url
	 * @return the InputStream
	 */
	public InputStream getInputStream() throws IOException;
	
	/**
	 * Gets the url to the location of the underlying file or url
	 * @return the url
	 */
	public URL getURL();
	
	/**
	 * Gets the type of the underlying data
	 * @return the type as String
	 */
	public String getType();
}
