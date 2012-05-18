package library.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A Condition that can be used to filter matching Songs
 * @author Joakim Reinert
 *
 */
public interface Condition {
	/**
	 * Prepares the given PreparedStatement by setting all parameters starting from the given index
	 * @param statement - the PreparedStatement to be prepared
	 * @param index - The index from where to start setting the parameters <b>Note:</b> 1 is the first index!
	 * @return the index following the index of the last set parameter
	 * @throws SQLException if there is a problem setting the statement
	 */
	public int prepareSQLStatement(PreparedStatement statement, int index) throws SQLException;
	
	/**
	 * Returns a (parameterized) String representing this condition<br />
	 * <b>Example</b>: "COLUMN_NAME LIKE ?"
	 * @return a String representation of this condition in SQL syntax
	 */
	public String getSQLCondition();
}
