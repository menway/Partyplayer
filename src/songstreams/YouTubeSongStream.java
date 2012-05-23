package songstreams;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class YouTubeSongStream extends SongStream {

	public YouTubeSongStream(URL url) throws MalformedURLException, UnsupportedEncodingException {
		super(url);
		TbCm decrypter = new TbCm();
		try {
			HashMap<Integer, String[]> links = decrypter.decryptIt(url);
			if(links.containsKey(34))
				this.url = new URL(links.get(34)[0]);
			else if(links.containsKey(35))
				this.url = new URL(links.get(35)[0]);
			else if(links.containsKey(22))
				this.url = new URL(links.get(22)[0]);
			else
				this.url = new URL(links.values().iterator().next()[0]);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
