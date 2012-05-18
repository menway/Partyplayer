package library.conditions;

import library.Library;
import library.exceptions.InvalidQueryException;

public class AlbumCondition extends AbstractSongCondition {

	public AlbumCondition(String query, int mode) throws InvalidQueryException {
		super(query, mode);
	}

	@Override
	public String getSQLCondition() {
		return getSQLCondition(Library.ALBUM);
	}

}
