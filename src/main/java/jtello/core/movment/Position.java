package jtello.core.movment;

import javafx.geometry.Point3D;

/**
 * An estimation of the drone location
 * @author Ariel
 *
 */
public class Position {

	// in cm
	private Point3D location = new Point3D(0, 0, 0);

	// 0-359
	private double direction = 0;

	public Position moveX(double value) {
		double x = Math.cos(Math.toRadians(direction))*value;
		location = location.add(x, 0, 0);
		double y = Math.sin(Math.toRadians(direction))*value;
		location = location.add(0, y, 0);
		return this;
	}

	public Position moveY(double value) {
		double x = Math.sin(Math.toRadians(direction))*value;
		location = location.add(x, 0, 0);
		double y = Math.cos(Math.toRadians(direction))*value;
		location = location.add(0, y, 0);		
		return this;
	}

	public Position moveZ(double value) {
		location = location.add(0, 0, value);
		return this;
	}

	public Position rotate(double value) {
		direction += value;
		direction = direction % 360;
		return this;
	}

	public Point3D getLocation() {
		return location;
	}

	public Position clone() {
		Position clone = new Position();
		clone.direction=direction;
		clone.location=new Point3D(location.getX(), location.getY(), location.getZ());
		return clone;
	}
	
	@Override
	public String toString() {
		return "Position [location=" + location + ", direction=" + direction + "]";
	}

}
