package jtello.core.ui;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.opencv.core.Mat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jtello.core.ui.utils.Utils;

/**
 * The UI controller
 * 
 * @author Ariel
 *
 */
public class DroneController {

	final static Logger log = Logger.getLogger(DroneController.class);

	@FXML
	private Label commandLabel;

	@FXML
	private Label statusLabel;

	@FXML
	private ImageView currentFrame;

	Optional<ObjDetector> objDetectorOptional = Optional.empty();

	public DroneController() {
	}

	public void setObjDetector(ObjDetector objDetector) {
		objDetectorOptional = Optional.ofNullable(objDetector);
	}

	public void updateImageView(Image image) {
		Mat frame = Utils.image2Mat(image);
		objDetectorOptional.ifPresent(detector -> detector.detectAndDisplay(frame));
		Image displayImage = Utils.mat2Image(frame);
		Platform.runLater(() -> {
			currentFrame.setImage(displayImage);
		});
	}

	public void setStatus(String value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				statusLabel.setText(value);
			}
		});
	}

	public void setCommand(String value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				commandLabel.setText(value);
			}
		});
	}

}
