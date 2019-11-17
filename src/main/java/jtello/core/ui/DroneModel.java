package jtello.core.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jtello.core.communication.Communication;
import jtello.core.communication.Video;
import jtello.core.control.Control;
import jtello.core.movment.FlightPosition;
import jtello.core.movment.NotValidPositionException;

/**
 * The UI Model
 * 
 * @author Ariel
 *
 */
public class DroneModel extends Application {

	final static Logger log = Logger.getLogger(DroneModel.class);

	ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1));

	DroneController controller;

	FlightPosition flightPosition;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("DroneView.fxml"));
			BorderPane rootElement = (BorderPane) loader.load();
			Scene scene = new Scene(rootElement, 1200, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("JTello");
			primaryStage.setScene(scene);
			primaryStage.show();

			primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					System.exit(0);
				}
			}));

			controller = loader.getController();
			startVideo();

			new Thread(() -> controlDrone(primaryStage)).start();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void startVideo() {
		Video video = new Video();
		new Thread(video).start();

		new Thread(() -> {
			while (true) {
				video.get().ifPresent(image -> controller.updateImageView(image));
			}
		}).start();
	}

	void controlDrone(Stage primaryStage) {
		Communication communication = new Communication();

		communication.addCommandListener(controller::setCommand);

		new Thread(() -> {
			while (true) {
				communication.get().ifPresent(
						flightStatus -> controller.setStatus("Battery:" + flightStatus.getBattery() + ", Height:"
								+ flightStatus.getHeight() + ", TofDistance:" + flightStatus.getTofDistance()));
			}
		}).start();

		Control control = new Control(communication);
		flightPosition = new FlightPosition(control);
		boolean connected = control.commandAndVideoStream();
		if (!connected) {
			log.fatal("Fail to connect to the Drone! exiting");
	//		System.exit(-1);
		}

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
				executor.submit(() -> executeKeyEvent(control, key));
			}
		});

		/*
		ObjDetector objDetector = new ObjDetector();
		controller.setObjDetector(objDetector);

		new Thread(() -> {
			while (true) {
				objDetector.get().ifPresent(objCenter -> {
					Point2D frameCenter = objDetector.getFrameCenter();
					// 20% of screen
					int offset = (int) (frameCenter.x / 10);
					log.debug("objCenter= " + objCenter + ", offset=" + offset);
					if (objCenter.x + offset < frameCenter.x) {
						control.rotateLeft(20);
					}
					if (frameCenter.x + offset < objCenter.x) {
						control.rotateRight(20);
					}
				});
			}
		}).start();
*/
	}

	private void executeKeyEvent(Control control, KeyEvent key) {
		log.debug("addEventHandler " + key.getCode());
		try {
			if (key.getCode() == KeyCode.UP) {
				flightPosition.forward(20);
			}
			if (key.getCode() == KeyCode.DOWN) {
				flightPosition.back(20);
			}
			if (key.getCode() == KeyCode.LEFT) {
				flightPosition.left(20);
			}
			if (key.getCode() == KeyCode.RIGHT) {
				flightPosition.right(20);
			}
			if (key.getCode() == KeyCode.W) {
				flightPosition.up(20);
			}
			if (key.getCode() == KeyCode.S) {
				flightPosition.down(20);
			}
			if (key.getCode() == KeyCode.A) {
				flightPosition.rotateLeft(36);
			}
			if (key.getCode() == KeyCode.D) {
				flightPosition.rotateRight(36);
			}
			if (key.getCode() == KeyCode.T) {
				flightPosition.takeOff();
			}
			if (key.getCode() == KeyCode.L) {
				flightPosition.land();
			}
			if (key.getCode() == KeyCode.F) {
				flightPosition.flipForward();
			}
			controller.updatePosition(flightPosition.getPosition());
		} catch (NotValidPositionException e) {
			log.warn("Move is not valid " + e);
		}
	};

	/**
	 * For launching the application...
	 * 
	 * @param args optional params
	 */
	public static void main(String[] args) {
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		launch(args);
	}
}
