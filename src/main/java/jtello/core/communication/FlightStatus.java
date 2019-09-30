package jtello.core.communication;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The flight Status the the drone provides 
 * @author Ariel
 *
 */
public class FlightStatus {

	final Map<String, String> attributesMap;

	public FlightStatus(String udpStatus) {
		udpStatus = udpStatus.trim();
		attributesMap = Arrays.stream(udpStatus.split(";")).map(s -> s.split(":"))
				.collect(Collectors.toMap(s -> s[0], s -> s[1]));
	}

	public String getPitch() {
		return attributesMap.get("pitch");
	}

	public String getRoll() {
		return attributesMap.get("roll");
	}

	public String getYaw() {
		return attributesMap.get("yaw");
	}

	public String getSpeedX() {
		return attributesMap.get("vgx");
	}

	public String getSpeedY() {
		return attributesMap.get("vgy");
	}

	public String getSpeedZ() {
		return attributesMap.get("vgz");
	}

	public String getTempLow() {
		return attributesMap.get("templ");
	}

	public String getTempHigh() {
		return attributesMap.get("temph");
	}

	public String getTofDistance() {
		return attributesMap.get("tof");
	}

	public String getHeight() {
		return attributesMap.get("h");
	}

	public String getBattery() {
		return attributesMap.get("bat");
	}

	public Object getBarometer() {
		return attributesMap.get("baro");
	}

	public String getMotorsOnTime() {
		return attributesMap.get("time");
	}

	public String getAccelerationx() {
		return attributesMap.get("agx");
	}

	public String getAccelerationY() {
		return attributesMap.get("agy");
	}

	public String getAccelerationZ() {
		return attributesMap.get("agz");
	}

	@Override
	public String toString() {
		return "FlightStatus [getPitch()=" + getPitch() + ", getRoll()=" + getRoll() + ", getYaw()=" + getYaw()
				+ ", getSpeedX()=" + getSpeedX() + ", getSpeedY()=" + getSpeedY() + ", getSpeedZ()=" + getSpeedZ()
				+ ", getTempLow()=" + getTempLow() + ", getTempHigh()=" + getTempHigh() + ", getTofDistance()="
				+ getTofDistance() + ", getHeight()=" + getHeight() + ", getBattery()=" + getBattery()
				+ ", getBarometer()=" + getBarometer() + ", getMotorsOnTime()=" + getMotorsOnTime()
				+ ", getAccelerationx()=" + getAccelerationx() + ", getAccelerationY()=" + getAccelerationY()
				+ ", getAccelerationZ()=" + getAccelerationZ() + "]";
	}

}
