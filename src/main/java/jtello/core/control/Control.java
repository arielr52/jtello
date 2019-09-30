package jtello.core.control;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import jtello.core.communication.Communication;

/**
 * Implements the Flying interface to control the drone
 * @author Ariel
 *
 */
public class Control implements Fliying {

	private final Communication conn;

	public Control(Communication communication) {
		this.conn = communication;
	}
	
	public boolean commandAndVideoStream() {
		List<Supplier<Boolean>> commands = Arrays.asList(this::command, this::videoStreamOn);
		return commands.stream().map(Supplier::get).allMatch(i -> i);
	}

	boolean command() {
		return conn.executeCommand(TelloCommandValues.COMMAND_MODE);
	}

	boolean videoStreamOn() {
		return conn.executeCommand(TelloCommandValues.ENABLE_VIDEO_STREAM);
	}

	@Override
	public boolean takeOff() {
		return conn.executeCommand(TelloCommandValues.TAKE_OFF);
	}

	@Override
	public boolean land() {
		return conn.executeCommand(TelloCommandValues.LAND);
	}

	@Override
	public boolean left(int cm) {
		return conn.executeCommand(TelloCommandValues.LEFT + " "+cm);
	}

	@Override
	public boolean right(int cm) {
		return conn.executeCommand(TelloCommandValues.RIGHT + " "+cm);
	}

	@Override
	public boolean forward(int cm) {
		return conn.executeCommand(TelloCommandValues.FORWARD + " "+cm);
	}

	@Override
	public boolean back(int cm) {
		return conn.executeCommand(TelloCommandValues.BACK + " "+cm);
	}

	@Override
	public boolean up(int cm) {
		return conn.executeCommand(TelloCommandValues.UP + " "+cm);
	}

	@Override
	public boolean down(int cm) {
		return conn.executeCommand(TelloCommandValues.DOWN + " "+cm);
	}

	@Override
	public boolean rotateRight(int deg) {
		return conn.executeCommand(TelloCommandValues.CW + " "+deg);
	}

	@Override
	public boolean rotateLeft(int deg) {
		return conn.executeCommand(TelloCommandValues.CCW + " "+deg);
	}

	@Override
	public boolean flipForward() {
		return conn.executeCommand(TelloCommandValues.FLIP + " f");
	}
	
}
