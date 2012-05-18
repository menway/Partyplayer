package library.conditions;

import library.Library;
import library.exceptions.InvalidQueryException;

public class URLCondition extends AbstractSongCondition {

	public URLCondition(String query) throws InvalidQueryException {
		super(query, EXACT_MATCH_MODE);
	}

	@Override
	public String getSQLCondition() {
		return getSQLCondition(Library.URL);
	}

}
