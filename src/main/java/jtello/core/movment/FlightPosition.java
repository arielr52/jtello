package jtello.core.movment;

import java.util.ArrayList;
import java.util.List;

import jtello.core.control.Fliying;

/**
 * Track the drone position, allow validating the next position with
 * <code>SandboxValidator</code>
 * 
 * @author Ariel
 *
 */
public class FlightPosition implements Fliying {

	private final Fliying fliying;

	private Position position;

	private List<SandboxValidator> sandboxValidators = new ArrayList<SandboxValidator>();

	public FlightPosition(Fliying fliying) {
		this.position = new Position();
		this.fliying = fliying;
	}

	public Position getPosition() {
		return position;
	}

	public void addSandboxValidator(SandboxValidator sandboxValidator) {
		sandboxValidators.add(sandboxValidator);
	}

	void validatetMove(Position nextPosition) throws NotValidPositionException {
		for (SandboxValidator validator : sandboxValidators) {
			validator.newPosition(nextPosition);
		}
	}

	@Override
	public boolean takeOff() {
		boolean result = fliying.takeOff();
		if (result) {
			position.moveZ(50);
		}
		return result;
	}

	@Override
	public boolean land() {
		boolean result = fliying.land();
		if (result) {
			this.position = new Position();
		}
		return result;
	}

	@Override
	public boolean left(int cm) throws NotValidPositionException {
		validatetMove(position.clone().moveX(-cm));
		boolean result = fliying.left(cm);
		if (result)
			position.moveX(-cm);
		return result;
	}

	@Override
	public boolean right(int cm) throws NotValidPositionException {
		validatetMove(position.clone().moveX(cm));
		boolean result = fliying.right(cm);
		if (result)
			position.moveX(cm);
		return result;
	}

	@Override
	public boolean forward(int cm) throws NotValidPositionException {
		validatetMove(position.clone().moveY(cm));
		boolean result = fliying.forward(cm);
		if (result)
			position.moveY(cm);
		return result;
	}

	@Override
	public boolean back(int cm) throws NotValidPositionException {
		validatetMove(position.clone().moveY(-cm));
		boolean result = fliying.back(cm);
		if (result)
			position.moveY(-cm);
		return result;
	}

	@Override
	public boolean up(int cm) throws NotValidPositionException {
		validatetMove(position.clone().moveZ(cm));
		boolean result = fliying.up(cm);
		if (result)
			position.moveZ(cm);
		return result;
	}

	@Override
	public boolean down(int cm) throws NotValidPositionException {
		validatetMove(position.clone().moveZ(-cm));
		boolean result = fliying.down(cm);
		if (result)
			position.moveZ(-cm);
		return result;
	}

	@Override
	public boolean rotateRight(int deg) {
		boolean result = fliying.rotateRight(deg);
		if (result)
			position.rotate(deg);
		return result;
	}

	@Override
	public boolean rotateLeft(int deg) {
		boolean result = fliying.rotateLeft(deg);
		if (result)
			position.rotate(-deg);
		return result;
	}

	@Override
	public boolean flipForward() throws NotValidPositionException {
		validatetMove(position.clone().moveY(30));
		boolean result = fliying.flipForward();
		if (result)
			position.moveY(30);
		return result;
	}

}
