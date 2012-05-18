package library.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Condition {
	public int prepareSQLStatement(PreparedStatement statement, int index) throws SQLException;
	public String getSQLCondition();
}
