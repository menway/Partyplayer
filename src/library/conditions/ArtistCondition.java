package library.conditions;

import library.Library;
import library.exceptions.InvalidQueryException;


public class ArtistCondition extends AbstractSongCondition {

	public ArtistCondition(String query, int mode) throws InvalidQueryException {
		super(query, mode);
	}

	@Override
	public String getSQLCondition() {
		return getSQLCondition(Library.ARTIST);
	}

}
