package songstreams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import library.interfaces.SongStream;

/**
 * A SongStream with an url as underlying data
 * @author Joakim Reinert
 *
 */
public class AbstractURLSongStream extends AbstractSongStream implements SongStream {

		private static final long serialVersionUID = 7057445447272365143L;

	@Override
	protected void setMetaData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public URL getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public static AbstractURLSongStream getInstance(URL url) {
		// TODO
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
