package library.conditions;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import library.interfaces.Condition;

public class TrueCondition implements Condition {

	@Override
	public int prepareSQLStatement(PreparedStatement statement, int index)
			throws SQLException {
		return 0;
	}

	@Override
	public String getSQLCondition() {
		return "1 = 1";
	}

}
