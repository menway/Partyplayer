package library;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import songstreams.AbstractFileSongStream;
import songstreams.AbstractSongStream;
import songstreams.AbstractURLSongStream;


import library.conditions.TrueCondition;
import library.conditions.URLCondition;
import library.interfaces.Condition;
import library.interfaces.SongStream;

public class Library {
	
	/**
	 * Holds the connection to the database
	 */
	private Connection connection;
	
	/**
	 * Table column names
	 */
	public static final String ARTIST = "Artist";
	public static final String ALBUM = "Album";
	public static final String TITLE = "Title";
	public static final String GENRE = "Genre";
	public static final String COMMENT = "Comment";
	public static final String YEAR = "Year";
	public static final String TRACK_NUM = "TrackNum";
	public static final String BPM = "BPM";
	public static final String URL = "URL";
	public static final String TYPE = "Type";
	
	public static final String FILES = "files";
	public static final String STREAMS = "streams";
	
	
	/**
	 * Instantiates a Library with the specified database path 
	 * @param path - the path to the database
	 * @throws Exception if an error occurs opening/creating the database
	 */
	public Library(File path) throws Exception {
		Class.forName("org.h2.Driver");
		connection = DriverManager.getConnection("jdbc:h2:" + path.getAbsolutePath(),"sa","");
		createTable(FILES);
		createTable(STREAMS);
	}
	private void createTable(String tableName) throws SQLException {
		Statement statement = connection.createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + "(" +
				ARTIST + " VARCHAR (100)," +
				TITLE + " VARCHAR (100)," +
				ALBUM + " VARCHAR (100)," +
				GENRE + " VARCHAR (30)," +
				COMMENT + " VARCHAR (500)," +
				YEAR + " INTEGER," +
				TRACK_NUM + " INTEGER," +
				BPM + " INTEGER," +
				URL + " VARCHAR(1000) NOT NULL UNIQUE," +
				TYPE + " VARCHAR(4))");
	}
	/**
	 * Adds a new song to this Library and its database
	 * @param song - the song to be added
	 * @return <b>true</b> if the song was added, <b>false</b> otherwise 
	 * @throws SQLException if an error occurs storing the song in the database
	 * @throws IOException if an error occurs writing the Song to a blob
	 */
	public boolean addSong(SongStream song) throws SQLException, IOException {
		if(song instanceof AbstractFileSongStream)
			return addSongToTable(song, FILES);
		if(song instanceof AbstractURLSongStream)
			return addSongToTable(song, URL);
		return false;
	}
	private boolean addSongToTable(SongStream song, String table) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO " +  table + "(" + 
				ARTIST + "," + 
				TITLE + "," + 
				ALBUM + "," +
				GENRE + "," +
				COMMENT + "," +
				YEAR + "," +
				TRACK_NUM + "," +
				BPM + "," +
				URL + "," +
				TYPE + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		statement.setString(1, song.getArtist());
		statement.setString(2, song.getName());
		statement.setString(3, song.getAlbum());
		statement.setString(4, song.getGenre());
		statement.setString(5, song.getComment());
		statement.setInt(6, song.getYear());
		statement.setInt(7, song.getTrackNum());
		statement.setInt(8, song.getBpm());
		statement.setString(9, song.getURL().toString());
		statement.setString(10, song.getType());
		statement.executeUpdate();
		connection.commit();
		statement.close();
		return true;
	}
	/**
	 * Returns a List with all Songs in this Library
	 * @param sortBy - the column to sort by
	 * @return a List with all Songs in this Library
	 * @throws Exception Exception if an error occurs fetching the songs from the database or reading the Songs from the blobs
	 */
	public List<SongStream> getAllSongs(String sortBy) throws Exception {
		return getSongsByCondition(new TrueCondition(), sortBy);
	}
	private List<SongStream> getSongsByConditionFromTable(Condition cond, String table, String sortBy) throws SQLException, Exception {
		String condition = cond.getSQLCondition();
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + condition + " ORDER BY " + sortBy);
		cond.prepareSQLStatement(statement, 1);
		ResultSet result = statement.executeQuery();
		List<SongStream> songs = new LinkedList<SongStream>();
		while(result.next()) {
			songs.add(AbstractSongStream.getFileSongStream(
					result.getString(ARTIST),
					result.getString(TITLE),
					result.getString(ALBUM),
					result.getString(GENRE),
					result.getInt(YEAR),
					result.getInt(TRACK_NUM),
					result.getInt(BPM),
					result.getString(COMMENT),
					result.getString(URL),
					result.getString(TYPE)));
		}
		return songs;
	}
	public List<SongStream> getSongsByCondition(Condition cond, String sortBy) throws Exception {
		List<SongStream> songs = new LinkedList<SongStream>();
		songs.addAll(getSongsByConditionFromTable(cond, FILES, sortBy));
		songs.addAll(getSongsByConditionFromTable(cond, STREAMS, sortBy));
		return songs;
	}
	public List<SongStream> getAllSongs() throws Exception {
		return getAllSongs(ARTIST);
	}
	public List<SongStream> getSongsByCondition(Condition cond) throws Exception {
		return getSongsByCondition(cond, ARTIST);
	}
	public boolean contains(URL url) {
		try {
			Condition cond = new URLCondition(url.toString());
			List<SongStream> songs = getSongsByCondition(cond);
			return songs.size() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
