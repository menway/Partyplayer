package library.conditions;

import library.Library;
import library.exceptions.InvalidQueryException;


public class TitleCondition extends AbstractSongCondition {

	public TitleCondition(String query, int mode) throws InvalidQueryException {
		super(query, mode);
	}

	@Override
	public String getSQLCondition() {
		return getSQLCondition(Library.TITLE);
	}

}
