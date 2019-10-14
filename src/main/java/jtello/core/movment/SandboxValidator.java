package jtello.core.movment;

/**
 * Callback interface to validate that drone location
 * @author Ariel
 *
 */
public interface SandboxValidator {

	/**
	 * @param position the next position the drone will fly to.
	 * @return true if valid and the drone can move to the new position;
	 */
	public boolean newPosition(Position position) throws NotValidPositionException;
	
}
