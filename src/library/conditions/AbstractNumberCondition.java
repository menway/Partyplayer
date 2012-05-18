package library.conditions;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import library.exceptions.InvalidQueryException;

public abstract class AbstractNumberCondition extends AbstractSongCondition {
	private int number;
	public static final int EQUALS_MODE = 0;
	public static final int LESS_THAN_EQUALS_MODE = -1;
	public static final int GREATER_THAN_EQUALS_MODE = 1;
	
	public AbstractNumberCondition(int query, int mode) throws InvalidQueryException {
		super(String.valueOf(query), mode);
		number = query;
	}

	public int getQueryNumber() {
		return number;
	}
	
	@Override
	protected String getSQLCondition(String columnName) {
		String condition = columnName;
		switch(mode) {
		case 0:
			condition += " = ?";
			break;
		case -1:
			condition += " <= ?";
			break;
		case 1:
			condition += " >= ?";
			break;
		default:
			return null;
		}
		return condition;
	}
	@Override
	public int prepareSQLStatement(PreparedStatement statement, int index) throws SQLException {
		statement.setInt(index++, number);
		return index;
	}
}
