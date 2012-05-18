package library.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface SongStream {
	
	public String getArtist();
	public String getAlbum();
	public String getName();
	public int getTrackNum();
	public int getYear();
	public String getGenre();
	public int getBpm();
	public String getComment();

	public InputStream getInputStream() throws IOException;
	
	public URL getURL();
	public String getType();
}
