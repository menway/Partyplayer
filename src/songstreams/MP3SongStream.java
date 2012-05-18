package songstreams;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import org.tritonus.share.sampled.file.TAudioFileFormat;
 
/**
 * 
 * @author Joakim Reinert
 *
 */
public class MP3SongStream extends AbstractFileSongStream {

	private static final long serialVersionUID = -3525933543569218513L;

	public MP3SongStream(File file) {
		super(file);
	}

	@Override
	protected void setMetaData() {
		try {
			AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
			if(aff instanceof TAudioFileFormat) {
				Map<?,?> props = ((TAudioFileFormat)aff).properties();
				if (props.containsKey("title")) setMetadata(TITLE, props.get("title"));
	            if (props.containsKey("author")) setMetadata(ARTIST, props.get("author"));
	            if (props.containsKey("album")) setMetadata(ALBUM, props.get("album"));
	            if (props.containsKey("date")) setMetadata(YEAR, parseInt(props.get("date")));
	            if (props.containsKey("mp3.id3tag.genre")) setMetadata(GENRE, props.get("mp3.id3tag.genre"));
	            if (props.containsKey("mp3.id3tag.track")) setMetadata(TRACK_NUM, parseInt(props.get("mp3.id3tag.track")));
	            if (props.containsKey("mp3.id3tag.bpm")) setMetadata(BPM, props.get("mp3.id3tag.bpm"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Parses an integer from an Object
	 * @param object - the object containing the integer
	 * @return -1 if the object doesn't contain an integer, the contained integer otherwise
	 */
	private int parseInt(Object object) {
		try {
			return Integer.parseInt((String) object);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	@Override
	public URL getURL() {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getType() {
		return "mp3";
	}

}
