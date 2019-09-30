package jtello.core.control;

import jtello.core.communication.Communication;

public class Control {

	Communication conn = new Communication();

	public Control() {
	}

	public Control connect() {
		conn.connect();
		return command().videoStreamOn();
	}

	public Control command() {
		conn.executeCommand(TelloCommandValues.COMMAND_MODE);
		return this;
	}

	public Control videoStreamOn() {
		conn.executeCommand(TelloCommandValues.ENABLE_VIDEO_STREAM);
		return this;
	}

	public Control takeOff() {
		conn.executeCommand(TelloCommandValues.TAKE_OFF);
		return this;
	}

	public Control land() {
		conn.executeCommand(TelloCommandValues.LAND);
		return this;
	}

	public Control left() {
		conn.executeCommand(TelloCommandValues.LEFT + " 50");
		return this;
	}

	public Control right() {
		conn.executeCommand(TelloCommandValues.RIGHT + " 50");
		return this;
	}

}
