package jtello.core.movment;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PositionTest {

	@Test
	public void test_moveX() {
		Position testObj = new Position();
		Position newPosition = testObj.moveX(10);
		assertEquals(10, newPosition.getLocation().getX(), 0.1);
		
		testObj = new Position();
		
		testObj.rotate(45);
		newPosition = testObj.moveX(10);
		assertEquals(7.07, newPosition.getLocation().getX(), 0.1);
		assertEquals(7.07, newPosition.getLocation().getY(), 0.1);
	}

	
	@Test
	public void test_moveY() {
		Position testObj = new Position();
		Position newPosition = testObj.moveY(10);
		assertEquals(10, newPosition.getLocation().getY(), 0.1);
		
		testObj = new Position();
		
		testObj.rotate(30);
		newPosition = testObj.moveY(10);
		assertEquals(5, newPosition.getLocation().getX(), 0.1);
		assertEquals(8.7, newPosition.getLocation().getY(), 0.1);
	}
}
