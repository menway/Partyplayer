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
	
	public AbstractSongCondition(String query, int mode) throws InvalidQueryException {
		this.query = query;
		this.mode = mode;
	}
	public String getQueryString() {
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
