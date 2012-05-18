package library.interfaces;

public interface ScanListener {
	public void scanFinished();
	public void songFound(SongStream song);
}
