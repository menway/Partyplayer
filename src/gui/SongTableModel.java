package gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class SongTableModel extends DefaultTableModel implements Reorderable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5840979978104811246L;
	public SongTableModel() {
		super();
	}
	public String getArtistForRow(int row) {
		return (String)getValueAt(row, 1);
	}
	public String getSongTitleForRow(int row) {
		return (String)getValueAt(row, 2);
	}
	public String getAlbumNameForRow(int row) {
		return (String)getValueAt(row, 3);
	}
	public int getTrackNumberForRow(int row) {
		return parseInt((String)getValueAt(row, 4));
	}
	public int getYearForRow(int row) {
		return parseInt((String)getValueAt(row, 5));
	}
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	public void addRow(String artist, String title, String album,
			String trackNr, String year, String url) {
		super.addRow(new String[] {"", artist, title, album, trackNr, year, url});
	}
	private int parseInt(String string) {
		try {
			return Integer.parseInt(string);
		}
		catch (Exception e) {
			return 0;
		}
	}
	public void setNowPlaying(int row, boolean value) {
		if(value)
			setValueAt("\u25B6", row, 0);
		else
			setValueAt("", row, 0);
	}
	@Override
	public void reorder(int fromIndex, int toIndex) {
		String[] row = xRemoveRow(fromIndex);
		int index = toIndex;
		if(index > fromIndex)
			index--;
		insertRow(index, row);
	}
	private String[] xRemoveRow(int row) {
		String[] rowData = new String[10];
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Vector<Object> rawRowData = (Vector)getDataVector().get(row);
		for(int i = 0; i < 10; i++)
			rowData[i] = (String)rawRowData.get(i);
		removeRow(row);
		return rowData;
	}
	public URL getURL(int row) throws MalformedURLException {
		return new URL((String) getValueAt(row, 6));
	}
	
}
