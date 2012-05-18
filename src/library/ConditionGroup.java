package library;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import library.interfaces.Condition;

public class ConditionGroup extends LinkedList<Condition> implements Condition {

	private static final long serialVersionUID = -4443447125751845278L;
	public boolean matchAll;
	
	public ConditionGroup(boolean matchAll) {
		this.matchAll = matchAll;
	}
	public ConditionGroup() {
		this(false);
	}
	@Override
	public int prepareSQLStatement(PreparedStatement statement, int index) throws SQLException {
		for(Condition query : this) {
			index = query.prepareSQLStatement(statement, index);
		}
		return index;
	}
	@Override
	public String getSQLCondition() {
		StringBuffer sb = new StringBuffer("(");
		for(Condition query : this) {
			sb.append(query.getSQLCondition());
			if(matchAll)
				sb.append(" AND ");
			else
				sb.append(" OR ");
		}
		if(matchAll)
			sb.append(" 1 = 1)");
		else
			sb.append(" 1 = 0)");
		return sb.toString();
	}
	
}
