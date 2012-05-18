package gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import songstreams.AbstractSongStream;

import library.interfaces.SongStream;

public class SongTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1606280135979175363L;
	protected MyTableRowSorter sorter;
	protected XTableColumnModel xColumnModel;
	protected SongTableModel model;
	protected String[] columnNames;
	private Properties settings;

	public SongTable(String[] columnNames, Properties settings) {
		super();
		this.settings = settings;
		this.columnNames = columnNames;
		model = new SongTableModel();
		initialize(model);
	}

	protected void initialize(SongTableModel model) {
		this.model = model;
		setModel(model);
		setColumnSelectionAllowed(false);
		sorter = new MyTableRowSorter(this.model);
		setRowSorter(sorter);
		getTableHeader().addMouseListener(getHeaderListener());
		this.model.setColumnIdentifiers(columnNames);
		xColumnModel = new XTableColumnModel();
		setColumnModel(xColumnModel);
		createDefaultColumnsFromModel();
		setVisibleColumns();
		for (ActionMap map = getActionMap(); map!=null; map = map.getParent()) {

			map.remove(TransferHandler.getCutAction().getValue(Action.NAME));

			map.remove(TransferHandler.getCopyAction().getValue(Action.NAME));

			map.remove(TransferHandler.getPasteAction().getValue(Action.NAME));

			}
	}
	public void clear() {
		while (true) {
			try {
				model.removeRow(0);
			} catch (ArrayIndexOutOfBoundsException e) {
				return;
			}
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		super.tableChanged(e);
		if(e.getType() != TableModelEvent.UPDATE)
			autoResizeColWidth();
	}

	protected void setVisibleColumns() {
		for (int i = 0; i < columnNames.length; i++) {
			String setting = settings.getProperty(getColumnVisibilitySettingKey(columnNames[i]));
			if (setting == null) {
				if (i < 4)
					setting = "true";
				else
					setting = "false";
			}
			xColumnModel.setColumnVisible(getColumn(columnNames[i]),
					Boolean.parseBoolean(setting));
		}
	}
	protected String getColumnVisibilitySettingKey(String string) {
		String key = string.replace(" ", "_");
		if(key.contains("\u2713"))
			key = key.substring(0, key.length() - 3);
		key += "_visible";
		return key;
	}
	private MouseListener getHeaderListener() {
		final JPopupMenu popup = new JPopupMenu();
		ActionListener popupMenuItemListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem source = (JMenuItem) e.getSource();
				String key = getColumnVisibilitySettingKey(source.getText());
				int index;
				for(index = 0; index < columnNames.length; index++)
					if(source.getText().contains(columnNames[index]))
						break;
				if (source.getText().contains("\u2713")) {
					settings.setProperty(key, "false");
					String newText = source.getText().substring(0,
							source.getText().length() - 3);
					source.setText(newText);
					xColumnModel.setColumnVisible(
							xColumnModel.getColumnByModelIndex(index), false);
				} else {
					settings.setProperty(key, "true");
					xColumnModel.setColumnVisible(
							xColumnModel.getColumnByModelIndex(index), true);
					source.setText(source.getText().concat("  \u2713"));
				}			
			}
		};
		JMenuItem artistName = new JMenuItem(columnNames[0]);
		artistName.addActionListener(popupMenuItemListener);
		if (Boolean.parseBoolean(settings.getProperty(getColumnVisibilitySettingKey("Artist_name"),
				"true")))
			artistName.setText(artistName.getText().concat("  \u2713"));
		JMenuItem songName = new JMenuItem(columnNames[1]);
		songName.addActionListener(popupMenuItemListener);
		if (Boolean.parseBoolean(settings.getProperty(getColumnVisibilitySettingKey("Song title"),
				"true")))
			songName.setText(songName.getText().concat("  \u2713"));
		JMenuItem albumName = new JMenuItem(columnNames[2]);
		albumName.addActionListener(popupMenuItemListener);
		if (Boolean.parseBoolean(settings.getProperty(getColumnVisibilitySettingKey("Album name"),"true")))
			albumName.setText(albumName.getText().concat("  \u2713"));
		JMenuItem trackNumber = new JMenuItem(columnNames[3]);
		trackNumber.addActionListener(popupMenuItemListener);
		if (Boolean.parseBoolean(settings.getProperty(getColumnVisibilitySettingKey("Track number"),"false")))
			trackNumber.setText(trackNumber.getText().concat("  \u2713"));
		JMenuItem year = new JMenuItem(columnNames[4]);
		year.addActionListener(popupMenuItemListener);
		if (Boolean.parseBoolean(settings
				.getProperty(getColumnVisibilitySettingKey("Release year"), "false")))
			year.setText(year.getText().concat("  \u2713"));
		popup.add(artistName);
		popup.add(songName);
		popup.add(albumName);
		popup.add(trackNumber);
		popup.add(year);
		return new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					popup.show(e.getComponent(), e.getX(), e.getY());
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					popup.show(e.getComponent(), e.getX(), e.getY());
			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}
		};
	}
	public String getArtistForRow(int row) {
		return model.getArtistForRow(sorter.convertRowIndexToModel(row));
	}

	public String getTitleForRow(int row) {
		return model.getSongTitleForRow(sorter.convertRowIndexToModel(row));
	}

	public String getAlbumForRow(int row) {
		return model.getAlbumNameForRow(sorter.convertRowIndexToModel(row));
	}

	public int getTrackNrForRow(int row) {
		return model.getTrackNumberForRow(sorter.convertRowIndexToModel(row));
	}

	public int getYearForRow(int row) {
		return model.getYearForRow(sorter.convertRowIndexToModel(row));
	}
	protected void autoResizeColWidth() {
		try {
			setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			setModel(model);

			int margin = 5;

			for (int i = 0; i < getColumnCount(); i++) {
				int vColIndex = i;
				DefaultTableColumnModel colModel = (DefaultTableColumnModel) getColumnModel();
				TableColumn col = colModel.getColumn(vColIndex);
				if (i == 0) {
					col.setPreferredWidth(10);
					col.setMaxWidth(10);
				} else {
					int width = 0;

					// Get width of column header
					TableCellRenderer renderer = col.getHeaderRenderer();

					if (renderer == null) {
						renderer = getTableHeader().getDefaultRenderer();
					}

					Component comp = renderer.getTableCellRendererComponent(
							this, col.getHeaderValue(), false, false, 0, 0);

					width = comp.getPreferredSize().width;

					// Get maximum width of column data
					for (int r = 0; r < getRowCount(); r++) {
						renderer = getCellRenderer(r, vColIndex);
						comp = renderer.getTableCellRendererComponent(this,
								this.getValueAt(r, vColIndex), false, false, r,
								vColIndex);
						width = Math.max(width, comp.getPreferredSize().width);
					}

					// Add margin
					width += 2 * margin;

					// Set the width
					col.setPreferredWidth(width);
				}
			}

			((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer())
					.setHorizontalAlignment(SwingConstants.LEFT);
			getTableHeader().setReorderingAllowed(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addRow(SongStream stream) {
		addRow(stream.getArtist(), stream.getTitle(),
					stream.getAlbum(), String.valueOf(stream.getTrackNum()),
					String.valueOf(stream.getYear()), stream.getURL().toString());
	}

	private void addRow(String artist, String title, String album,
			String trackNr, String year, String url) {
		model.addRow(artist, title, album, trackNr, year, url);
	}

	@Override
	public TableRowSorter<TableModel> getRowSorter() {
		return sorter;
	}
	public Vector<SongStream> getSelectedSongs() throws Exception {
		Vector<SongStream> result = new Vector<SongStream>();
		int[] selectedRows = getSelectedRows();
		for(int i = 0; i < selectedRows.length; i++)
			result.add(getRow(selectedRows[i]));
		return result;
	}
	public Vector<SongStream> getRows() {
		Vector<SongStream> result = new Vector<SongStream>();
		for (int i = 0; i < getRowCount(); i++) {
			try {
				result.add(getRow(i));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public void setRowSorter(RowSorter<? extends TableModel> sorter) {
		this.sorter = (MyTableRowSorter) sorter;
		super.setRowSorter(sorter);
	}

	private SongStream getRow(int i) throws Exception {
		String artist = getArtistForRow(i);
		String title = getTitleForRow(i);
		String album = getAlbumForRow(i);
		int trackNr = getTrackNrForRow(i);
		int year = getYearForRow(i);
		URL url = getURLForRow(i);
		// TODO
		if(new File(URLDecoder.decode(url.getFile(), "UTF-8")).exists())
			return AbstractSongStream.getFileSongStream(artist, title, album, null, year, trackNr, -1, null, url.toString(), "mp3");
		throw new Exception();
	}

	private URL getURLForRow(int i) {
		try {
			return model.getURL(sorter.convertRowIndexToModel(i));
		} catch (Exception e) {
			return null;
		}
	}

	protected void filterResults(String text) {
		if(this.model.getRowCount() < 2)
			return;
		String[] regexes = text.toLowerCase().trim().split(" ");
		RowFilter<TableModel, Integer> rf = null;
		try {
			Vector<RowFilter<Object, Object>> filtersForAtLeast = new Vector<RowFilter<Object, Object>>();
			for (int k = 0; k < regexes.length; k++) {
				Vector<RowFilter<Object, Object>> filtersForOr = new Vector<RowFilter<Object, Object>>();
				String regex = "(?i)" + regexes[k];
				for (int i = 0; i < getColumnCount(); i++) {
					filtersForOr.add(RowFilter.regexFilter(regex, i));
				}
				filtersForAtLeast.add(RowFilter.orFilter(filtersForOr));
			}
			rf = new AtLeastFilter(filtersForAtLeast, regexes.length);
		} catch (PatternSyntaxException e) {
			return;
		}
		sorter.setRowFilter(rf);
	}

	protected class MyTableRowSorter extends TableRowSorter<TableModel> {
		private Comparator<?> comparator;

		public MyTableRowSorter(SongTableModel model) {
			super(model);
			comparator = new Comparator<Object>() {

				@Override
				public int compare(Object o1, Object o2) {
					String o1String = (String) o1;
					String o2String = (String) o2;
					try {
						return Integer.parseInt(o1String)
								- Integer.parseInt(o2String);
					} catch (Exception e) {
						return o1String.compareTo(o2String);
					}
				}

			};
			setComparator();
		}

		private void setComparator() {
			for (int i = 0; i < getModel().getColumnCount(); i++) {
				setComparator(i, comparator);
			}
		}

		@Override
		public void toggleSortOrder(int column) {
			super.toggleSortOrder(column);
		}

		@Override
		public void sort() {
			setComparator();
			super.sort();
		};

		@Override
		public void allRowsChanged() {
		}

		@Override
		public void modelStructureChanged() {
		}
	}

	public void renewSorter() {
		setRowSorter(new MyTableRowSorter(model));
	}

	private class AtLeastFilter extends RowFilter<TableModel, Integer> {
		private Vector<RowFilter<Object, Object>> filters;
		private int matchesAtLeast;

		public AtLeastFilter(Vector<RowFilter<Object, Object>> filters,
				int matchesAtLeast) {
			this.filters = filters;
			this.matchesAtLeast = matchesAtLeast;
		}

		@Override
		public boolean include(
				javax.swing.RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {
			int count = 0;
			for (RowFilter<Object, Object> currentFilter : filters) {
				if (currentFilter.include(entry))
					count++;
				if (count == matchesAtLeast)
					return true;
			}
			return false;
		}

	}

	public void removeRow(int i) {
		model.removeRow(i);
	}

	public void setNowPlaying(int row, boolean value) {
		model.setNowPlaying(sorter.convertRowIndexToModel(row), value);
	}
}
