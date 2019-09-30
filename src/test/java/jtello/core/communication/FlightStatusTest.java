package jtello.core.communication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
public class FlightStatusTest {

	@Test
	public void test_parse_status() {
		FlightStatus status = new FlightStatus("pitch:0;roll:0;yaw:87;vgx:0;vgy:0;vgz:0;templ:67;temph:69;tof:10;h:0;bat:92;baro:289.65;time:18;agx:-9.00;agy:1.00;agz:-1003.00;\r\n");
		 assertEquals("0", status.getPitch());
		 assertEquals("0", status.getRoll());
		 assertEquals("87", status.getYaw());
		 assertEquals("0", status.getSpeedX());
		 assertEquals("0", status.getSpeedY());
		 assertEquals("0", status.getSpeedZ());
		 assertEquals("67", status.getTempLow());
		 assertEquals("69", status.getTempHigh());
		 assertEquals("10", status.getTofDistance());
		 assertEquals("0", status.getHeight());
		 assertEquals("92", status.getBattery());
		 assertEquals("289.65", status.getBarometer());
		 assertEquals("18", status.getMotorsOnTime());
		 assertEquals("-9.00", status.getAccelerationx());
		 assertEquals("1.00", status.getAccelerationY());
		 assertEquals("-1003.00", status.getAccelerationZ());
	}
}
