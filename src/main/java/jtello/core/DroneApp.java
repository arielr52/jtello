package jtello.core;

import org.opencv.core.Core;

import jtello.core.control.Control;
import jtello.core.ui.FXHelloCV;

public class DroneApp {

	public static void main(String[] args) throws InterruptedException {
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Control control = new Control();
		control.connect();
		System.out.println("1");
		new Thread(()->FXHelloCV.main(args)).start();;
		System.out.println("2");
		Thread.sleep(5000);
		control.takeOff().right().left().land();
		System.out.println("3");
		//Thread.sleep(10000);
	}

}
