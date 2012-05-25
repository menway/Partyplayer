package songstreams;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IMetaData;


public class SongStream {
	
	private static final Pattern bpmPattern = Pattern.compile("(\\d+\\.\\d\\d)\\d*");
	private IContainer container;
	private final IMetaData metadata;
	protected URL url;
	
	public SongStream(URL url) throws UnsupportedEncodingException, MalformedURLException {
		File file = new File(url.getFile());
		if(file.exists())
			this.url = file.toURI().toURL();
		else
			this.url = url;
		container = IContainer.make();
		open(IContainer.Type.READ, null);
		metadata = container.getMetaData();
		close();
		for(String key : metadata.getKeys())
			System.out.println(key + " : " + "\"" + metadata.getValue(key) + "\"");
	}
	public boolean open(IContainer.Type mode, IContainerFormat format) throws UnsupportedEncodingException {
		File file = new File(URLDecoder.decode(url.getFile(),"UTF-8"));
		if(file.exists()) {
			if(container.open(file.toString(), mode, format) >= 0)
				return true;
			return false;
		} else if(mode.equals(IContainer.Type.WRITE)) {
			return false;	
		} else
			return container.open(url.toString(), mode, format) >= 0;
	}
	public void close() {
		container.close();
	}
	public SongStream(File file) throws MalformedURLException, UnsupportedEncodingException  {
		this(file.toURI().toURL());
	}
	public SongStream(String artist, String title, String album,
			String genre, int year, int tracknum, float bpm, String url) throws MalformedURLException {
		container = IContainer.make();
		metadata = IMetaData.make();
		setArtist(artist);
		setTitle(title);
		setAlbum(album);
		setGenre(genre);
		setYear(year);
		setTrackNum(tracknum);
		setBpm(bpm);
		this.url = new URL(url);
		
	}
	public String getArtist() {
		return metadata.getValue("artist");
	}
	public void setArtist(String artist) {
		metadata.setValue("artist", artist);
	}

	public String getAlbum() {
		return metadata.getValue("album");
	}
	public void setAlbum(String album) {
		metadata.setValue("album", album);
	}
	public String getTitle() {
		return metadata.getValue("title");
	}
	public void setTitle(String title) {
		metadata.setValue("title", title);
	}
	public int getTrackNum() {
		String tracknum = metadata.getValue("track");
		if(tracknum == null)
			return -1;
		if(tracknum.contains("/"))
			return Integer.parseInt(tracknum.split("/")[0]);
		return Integer.parseInt(tracknum);
	}
	public void setTrackNum(int trackNum) {
		metadata.setValue("track", "" + trackNum);
	}

	public int getYear() {
		return Integer.parseInt(metadata.getValue("date"));
	}
	public void setYear(int year) {
		metadata.setValue("date", "" + year);
	}
	public String getGenre() {
		return metadata.getValue("genre");
	}
	public void setGenre(String genre) {
		metadata.setValue("genre", genre);
	}
	public float getBpm() {
		if (metadata.getKeys().contains("fbpm"))
			return Float.parseFloat(metadata.getValue("fbpm"));
		if (metadata.getKeys().contains("TBPM"))
			return Float.parseFloat(metadata.getValue("TBPM"));
		return -1;
	}
	public void setBpm(float bpm) {
		if (metadata.getKeys().contains("TBPM"))
			metadata.setValue("TBPM", bpmPattern.matcher(String.valueOf(Math.round(bpm*100)/100.0)).group(1));
	}
	public void writeMetaData() throws UnsupportedEncodingException {
		File file = new File(URLDecoder.decode(url.getFile(),"UTF-8"));
		if(file.exists()) {
			container = IContainer.make();
			container.open(file.toString(), IContainer.Type.WRITE, container.getContainerFormat());
			container.setMetaData(metadata);
			container.close();
		}	
	}
	public URL getURL() {
		return url;
	}
	
	public IContainer getContainer() {
		return container;
	}
	@Override
	public String toString() {
		return getArtist() +  " - " + getTitle() + " [" + getAlbum() + " (" + getYear() + ")]";  
	}
	public String getTableRow(boolean clickable) {
		StringBuilder builder = new StringBuilder();
		builder.append("<tr ");
		if(clickable)
			builder.append("onmouseover=\"ChangeColor(this, true); this.style.cursor='pointer';\" onmouseout=\"ChangeColor(this,false);\" onClick=\"Queue('" + getURL() + "');\"");
		builder.append("><td>");
		builder.append(getArtist());
		builder.append("</td><td>");
		builder.append(getTitle());
		builder.append("</td><td>");
		builder.append(getAlbum());
		builder.append("</td><td>");
		builder.append(getYear());
		builder.append("</td></tr>");
		return builder.toString();
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof SongStream)
			return ((SongStream) o).getURL().equals(getURL());
		return false;
	}
	
}
