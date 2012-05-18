package library.conditions;

import library.Library;
import library.exceptions.InvalidQueryException;

public class TrackNumCondition extends AbstractNumberCondition {

	public TrackNumCondition(int query, int mode) throws InvalidQueryException {
		super(query, mode);
	}

	@Override
	public String getSQLCondition() {
		return getSQLCondition(Library.TRACK_NUM);
	}

}
