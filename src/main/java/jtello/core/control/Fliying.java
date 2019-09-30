package jtello.core.control;

/**
 * Direction for flying
 * @author Ariel
 *
 */
public interface Fliying {

	boolean takeOff();

	boolean land();

	boolean left(int cm);

	boolean right(int cm);

	boolean forward(int cm);

	boolean back(int cm);

	boolean up(int cm);

	boolean down(int cm);

	boolean rotateRight(int deg);

	boolean rotateLeft(int deg);

	boolean flipForward();

}