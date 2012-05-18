package player;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import library.interfaces.SongStream;

public class FilePlayerThread extends PlayerThread {
	
	private File file;
	public FilePlayerThread(SongStream stream, PlayerListener listener) throws UnsupportedEncodingException {
		super(stream, listener);
		file = new File(URLDecoder.decode(stream.getURL().getFile(), "UTF-8"));
	}
	@Override
	public void run() {
		super.run();
		try {
		    AudioInputStream in= AudioSystem.getAudioInputStream(file);
		    AudioInputStream din = null;
		    AudioFormat baseFormat = in.getFormat();
		    AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
		                                                                                  baseFormat.getSampleRate(),
		                                                                                  16,
		                                                                                  baseFormat.getChannels(),
		                                                                                  baseFormat.getChannels() * 2,
		                                                                                  baseFormat.getSampleRate(),
		                                                                                  false);
		    din = AudioSystem.getAudioInputStream(decodedFormat, in);
		    // Play now.
		    rawplay(decodedFormat, din);
		    in.close();
		  } catch (Exception e)
		    {
		        e.printStackTrace();
		    }
		if(!shouldStop)
			listener.stoppedPlayback(stream);
	}
	private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException,                                                                                                LineUnavailableException
	{
	  byte[] data = new byte[4096];
	  SourceDataLine line = getLine(targetFormat);
	  if (line != null)
	  {
	    // Start
	    line.start();
	    int nBytesRead = 0, nBytesWritten = 0;
	    while (nBytesRead != -1 && !shouldStop)
	    {
	        nBytesRead = din.read(data, 0, data.length);
	        if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
	    }
	    // Stop
	    line.drain();
	    line.stop();
	    line.close();
	    din.close();
	  }
	}

	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
	{
	  SourceDataLine res = null;
	  DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	  res = (SourceDataLine) AudioSystem.getLine(info);
	  res.open(audioFormat);
	  return res;
	}
}
