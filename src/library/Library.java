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

import songstreams.SongStream;
import songstreams.YouTubeSongStream;


import library.conditions.TrueCondition;
import library.conditions.URLCondition;
import library.exceptions.InvalidQueryException;
import library.interfaces.Condition;

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
	public static final String YEAR = "Year";
	public static final String TRACK_NUM = "TrackNum";
	public static final String BPM = "BPM";
	public static final String URL = "URL";
	
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
				YEAR + " INTEGER," +
				TRACK_NUM + " INTEGER," +
				BPM + " FLOAT," +
				URL + " VARCHAR(1000) NOT NULL UNIQUE)");
	}
	/**
	 * Adds a new song to this Library and its database
	 * @param song - the song to be added
	 * @return <b>true</b> if the song was added, <b>false</b> otherwise 
	 * @throws SQLException if an error occurs storing the song in the database
	 * @throws IOException if an error occurs writing the Song to a blob
	 */
	public boolean addSong(SongStream song) throws SQLException, IOException {
		if(song instanceof YouTubeSongStream)
			return addSongToTable(song, URL);
		if(song instanceof SongStream)
			return addSongToTable(song, URL);
		return false;
	}
	private boolean addSongToTable(SongStream song, String table) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO " +  table + "(" + 
				ARTIST + "," + 
				TITLE + "," + 
				ALBUM + "," +
				GENRE + "," +
				YEAR + "," +
				TRACK_NUM + "," +
				BPM + "," +
				URL + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
		statement.setString(1, song.getArtist());
		statement.setString(2, song.getTitle());
		statement.setString(3, song.getAlbum());
		statement.setString(4, song.getGenre());
		statement.setInt(5, song.getYear());
		statement.setInt(6, song.getTrackNum());
		statement.setFloat(7, song.getBpm());
		statement.setString(8, song.getURL().toString());
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
			songs.add(new SongStream(
					result.getString(ARTIST),
					result.getString(TITLE),
					result.getString(ALBUM),
					result.getString(GENRE),
					result.getInt(YEAR),
					result.getInt(TRACK_NUM),
					result.getInt(BPM),
					result.getString(URL)));
		}
		return songs;
	}
	/**
	 * Get all songs from this library that match the given condition
	 * @param cond - the condition which the songs have to match
	 * @param sortBy - the table column to sort the results by
	 * @return a List of all songs matching the given condition in this library
	 * @throws Exception
	 */
	public List<SongStream> getSongsByCondition(Condition cond, String sortBy) throws Exception {
		List<SongStream> songs = new LinkedList<SongStream>();
		songs.addAll(getSongsByConditionFromTable(cond, FILES, sortBy));
		songs.addAll(getSongsByConditionFromTable(cond, STREAMS, sortBy));
		return songs;
	}
	public List<SongStream> getAllSongs() throws Exception {
		return getAllSongs(ARTIST);
	}
	/**
	 * Get all songs from this library that match the given condition
	 * @param cond - the condition which the songs have to match
	 * @return a List of all songs matching the given condition in this library
	 * @throws Exception
	 */
	public List<SongStream> getSongsByCondition(Condition cond) throws Exception {
		return getSongsByCondition(cond, ARTIST);
	}
	/**
	 * Checks if the Library contains a Song with the given url
	 * @param url - the url to be matched.
	 * @return <b>true</b> if this Library contains a song with the given URL, <b>false</b> otherwise.
	 */
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
	/**
	 * Gets the Song with the given URL 
	 * @param string - the URL of the song
	 * @return a SongStream for the URL or null if no such song exists
	 * @throws Exception 
	 * @throws InvalidQueryException 
	 */
	public SongStream getSong(String string) throws InvalidQueryException, Exception {
		List<SongStream> result = getSongsByCondition(new URLCondition(string));
		if(result.size() == 0)
			return null;
		return result.get(0);
	}
	
}
