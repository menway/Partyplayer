package library.conditions;

import library.Library;
import library.exceptions.InvalidQueryException;

public class CommentCondition extends AbstractSongCondition {

	public CommentCondition(String query, int mode) throws InvalidQueryException {
		super(query, mode);
	}

	@Override
	public String getSQLCondition() {
		return getSQLCondition(Library.COMMENT);
	}

}
