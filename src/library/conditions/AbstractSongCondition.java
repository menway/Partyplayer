package library.conditions;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import library.exceptions.InvalidQueryException;
import library.interfaces.Condition;

public abstract class AbstractSongCondition implements Condition {
	private String query;
	public static final int EXACT_MATCH_MODE = 2;
	public static final int SUBSTRING_MATCH_MODE = 3;
	public static final int PREFIX_MATCH_MODE = 4;
	public static final int POSTFIX_MATCH_MODE = 5;
	protected int mode;
	
	/**
	 * Creates a new Condition with the given arguments
	 * @param query - the value to be matched 
	 * @param mode - the matching mode. <b>Must be one of:</b> 2,3,4,5!
	 * @throws InvalidQueryException
	 */
	public AbstractSongCondition(String query, int mode) throws InvalidQueryException {
		this.mode = mode;
		this.query = getQueryString(query);
	}
	
	/**
	 * Returns the modified query string for the given query string according to the mode
	 * @param query
	 * @return the appropriate query string
	 */
	private String getQueryString(String query) {
		switch (mode) {
		case 2:
			return query;
		case 3:
			return "%" + query + "%";
		case 4:
			return query + "%";
		default:
			return "%" + query;
		}
	}
	/**
	 * Returns the SQL Condition String for the given column name
	 * @param columnName - the column to test the condition against
	 * @return the parameterized condition string for the given column name
	 */
	protected String getSQLCondition(String columnName) {
		if(mode == 2)
			return "((? is null and " + columnName + " is null)  or (" + columnName + " = ?))";
		else
			return "((? is null and " + columnName + " is null)  or (" + columnName + " like ?))";
	}
	@Override
	public int prepareSQLStatement(PreparedStatement statement, int index) throws SQLException {
		statement.setString(index++, query);
		statement.setString(index++, query);
		return index;
	}
}
