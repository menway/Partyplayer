package library.conditions;

import library.Library;
import library.exceptions.InvalidQueryException;

public class YearCondition extends AbstractNumberCondition {

	public YearCondition(int query, int mode) throws InvalidQueryException {
		super(query, mode);
	}

	@Override
	public String getSQLCondition() {
		return getSQLCondition(Library.YEAR);
	}

}
