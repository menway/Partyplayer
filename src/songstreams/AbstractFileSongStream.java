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

public abstract class AbstractFileSongStream extends AbstractSongStream {

	private static final long serialVersionUID = 8742287430052968729L;
	
	protected File file;
	
	public AbstractFileSongStream(File file) {
		this.file = file;
	}
	public void init() {
		
	}
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
	public File getFile() {
		return file;
	}
	public static AbstractFileSongStream getInstance(File file, String type) throws InvalidSongFileException {
		if(type.equalsIgnoreCase("mp3"))
			return new MP3SongStream(file);
		throw new InvalidSongFileException();
	}
}
