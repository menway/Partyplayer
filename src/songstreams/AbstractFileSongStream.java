package songstreams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import library.exceptions.InvalidSongFileException;

/**
 * A SongStream with a File as the underlying data
 * @author Joakim Reinert
 *
 */
public abstract class AbstractFileSongStream extends AbstractSongStream {
	
	private static final long serialVersionUID = 8742287430052968729L;
	
	protected File file;
	
	public AbstractFileSongStream(File file) {
		this.file = file;
	}
	public void init() {
		
	}
	/**
	 * Returns the correct SongStream depending on the AudioFileFormat of the file
	 * @param file - the file to create the SongStream from
	 * @return a SongStream for the given file
	 * @throws InvalidSongFileException if the AudioFileFormat type is not supported
	 * @throws IOException if there is a problem reading the File
	 */
	public static AbstractFileSongStream getInstance(File file) throws InvalidSongFileException, IOException {
		String name = file.getName();
		try {
			AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
			String type = aff.getType().toString();
			if(name.endsWith(".mp3")) {
				AbstractFileSongStream stream = getInstance(file, type);
				stream.setMetaData();
				return stream;
			}
			else
				throw new InvalidSongFileException();
		} catch (UnsupportedAudioFileException e) {
			throw new InvalidSongFileException();
		}
	}
	@Override
	public InputStream getInputStream() throws FileNotFoundException {
		return new FileInputStream(file);
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof AbstractFileSongStream)
			return file.equals(((AbstractFileSongStream)o).file);
		return false;
	}
	/**
	 * Returns the File for this FileSongStream
	 * @return
	 */
	public File getFile() {
		return file;
	}
	/**
	 * Returns the correct SongStream for the given file and type
	 * @param file - the file to create the SongStream from
	 * @param type - the AudioFileFormat type of the Audio file
	 * @return a SongStream for the given file and type
	 * @throws InvalidSongFileException if the type is not supported
	 */
	public static AbstractFileSongStream getInstance(File file, String type) throws InvalidSongFileException {
		if(type.equalsIgnoreCase("mp3"))
			return new MP3SongStream(file);
		throw new InvalidSongFileException();
	}
}
