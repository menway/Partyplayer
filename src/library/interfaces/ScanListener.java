package library.interfaces;

import songstreams.SongStream;

public interface ScanListener {
	/**
	 * Is called, when the scan is finished
	 */
	public void scanFinished();
	/**
	 * Is called when a song has been found, that wasn't already contained in the Library
	 * @param song the SongStream that was found and added to the Library
	 */
	public void songFound(SongStream song);
}
