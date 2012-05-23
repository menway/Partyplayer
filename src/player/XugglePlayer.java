package player;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import songstreams.SongStream;

import com.xuggle.ferry.JNIMemoryManager;
import com.xuggle.ferry.JNIMemoryManager.MemoryModel;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class XugglePlayer implements Runnable {

	private static SourceDataLine mLine;
	private boolean shouldStop;
	private boolean shouldPause;
	private PlayerListener listener;
	private SongStream stream;

	public XugglePlayer(SongStream stream, PlayerListener listener) {
		this.stream = stream;
		this.listener = listener;
	}

	@Override
	public void run() {
		JNIMemoryManager.setMemoryModel(MemoryModel.NATIVE_BUFFERS);
		try {
			if (!stream.open(IContainer.Type.READ, null))
				throw new RuntimeException("Error opening stream: "
						+ stream.toString());
			IContainer container = stream.getContainer();
			int streamCount = container.getNumStreams();
			int audioStreamId = -1;
			IStreamCoder coder = null;
			for (int i = 0; i < streamCount; i++) {
				IStream stream = container.getStream(i);
				coder = stream.getStreamCoder();
				if (coder.getCodecType().equals(ICodec.Type.CODEC_TYPE_AUDIO)) {
					audioStreamId = i;
					break;
				}
			}
			if (audioStreamId == -1)
				throw new RuntimeException(
						"Could not find audio stream in stream: "
								+ stream.toString());
			if (coder.open(null, null) < 0)
				throw new RuntimeException(
						"Could not open audio decoder for stream: "
								+ stream.toString());
			openJavaSound(coder);

			IPacket packet = IPacket.make();
			listener.startedPlayback(stream);
			while (container.readNextPacket(packet) >= 0 && !shouldStop) {
				if (packet.getStreamIndex() == audioStreamId) {
					IAudioSamples samples = IAudioSamples.make(1024,
							coder.getChannels());
					int offset = 0;
					while (offset < packet.getSize()) {
						int bytesDecoded = coder.decodeAudio(samples, packet,
								offset);
						if (bytesDecoded < 0)
							throw new RuntimeException(
									"Error decoding audio from stream: "
											+ stream.toString());
						offset += bytesDecoded;
						if (samples.isComplete())
							playJavaSound(samples);
					}
				}
			}
			closeJavaSound();
			if (coder != null) {
				coder.close();
				coder = null;
			}
			if (container != null) {
				container.close();
				coder = null;
			}

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void playJavaSound(IAudioSamples samples) throws InterruptedException {
		while(shouldPause)
			Thread.sleep(50);
		byte[] bytes = samples.getData().getByteArray(0, samples.getSize());
		mLine.write(bytes, 0, bytes.length);
	}

	private void openJavaSound(IStreamCoder coder) {
		AudioFormat af = new AudioFormat(
				coder.getSampleRate(),
				(int) IAudioSamples.findSampleBitDepth(coder.getSampleFormat()),
				coder.getChannels(), true, false);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
		try {
			mLine = (SourceDataLine) AudioSystem.getLine(info);
			mLine.open(af);
			mLine.start();
		} catch (LineUnavailableException e) {
			throw new RuntimeException("Could not open data line");
		}
	}

	private void closeJavaSound() {
		if(!shouldStop)
			listener.stoppedPlayback(stream);
		if (mLine != null) {
			mLine.drain();
			/*
			 * Close the line.
			 */
			mLine.close();
			mLine = null;
		}
	}

	public void pause() {
		this.shouldPause = true;
	}

	public void stop() {
		this.shouldStop = true;
	}

	public void resume() {
		this.shouldPause = false;
	}
}
