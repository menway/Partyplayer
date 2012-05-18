package library.conditions;

import library.Library;
import library.exceptions.InvalidQueryException;

public class GenreCondition extends AbstractSongCondition {

	public GenreCondition(String query, int mode) throws InvalidQueryException {
		super(query, mode);
	}

	@Override
	public String getSQLCondition() {
		return getSQLCondition(Library.GENRE);
	}

}
