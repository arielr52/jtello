package jtello.core.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

/**
 * The UI Model
 * 
 * @author Ariel
 *
 */
public class DroneModel extends Application {

	final static Logger log = Logger.getLogger(DroneModel.class);

	ExecutorService executor = Executors.newFixedThreadPool(5);

	DroneController controller;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("DroneView.fxml"));
			BorderPane rootElement = (BorderPane) loader.load();
			Scene scene = new Scene(rootElement, 1100, 800);
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
		boolean connected = control.commandAndVideoStream();
		if (!connected) {
			log.fatal("Fail to connect to the Drone! exiting");
			 System.exit(-1);
		}

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
				executor.submit(() -> executeKeyEvent(control, key));
			}
		});
	}

	private void executeKeyEvent(Control control, KeyEvent key) {
		log.debug("addEventHandler " + key.getCode());
		if (key.getCode() == KeyCode.UP) {
			control.forward(20);
		}
		if (key.getCode() == KeyCode.DOWN) {
			control.back(20);
		}
		if (key.getCode() == KeyCode.LEFT) {
			control.left(20);
		}
		if (key.getCode() == KeyCode.RIGHT) {
			control.right(20);
		}
		if (key.getCode() == KeyCode.W) {
			control.up(20);
		}
		if (key.getCode() == KeyCode.S) {
			control.down(20);
		}
		if (key.getCode() == KeyCode.A) {
			control.rotateLeft(36);
		}
		if (key.getCode() == KeyCode.D) {
			control.rotateRight(36);
		}
		if (key.getCode() == KeyCode.T) {
			control.takeOff();
		}
		if (key.getCode() == KeyCode.L) {
			control.land();
		}
		if (key.getCode() == KeyCode.F) {
			control.flipForward();
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
