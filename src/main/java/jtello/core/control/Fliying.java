package jtello.core.control;

import jtello.core.movment.NotValidPositionException;

/**
 * Direction for flying
 * @author Ariel
 *
 */
public interface Fliying {

	boolean takeOff() ;

	boolean land();

	boolean left(int cm)throws NotValidPositionException;

	boolean right(int cm)throws NotValidPositionException;

	boolean forward(int cm)throws NotValidPositionException;

	boolean back(int cm)throws NotValidPositionException;

	boolean up(int cm)throws NotValidPositionException;

	boolean down(int cm)throws NotValidPositionException;

	boolean rotateRight(int deg);

	boolean rotateLeft(int deg);

	boolean flipForward() throws NotValidPositionException;

}