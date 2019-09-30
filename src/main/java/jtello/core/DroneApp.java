package jtello.core;

import org.opencv.core.Core;

import jtello.core.ui.DroneModel;

/**
 * Main class 
 * @author Ariel
 *
 */
public class DroneApp {

	public static void main(String[] args) throws InterruptedException {
		//to install opencv https://opencv.org/releases/
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		DroneModel.main(args);
	}

}
