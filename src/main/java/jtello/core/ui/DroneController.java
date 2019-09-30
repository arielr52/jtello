package jtello.core.ui;

import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The UI controller 
 * @author Ariel
 *
 */
public class DroneController {

	final static Logger logger = Logger.getLogger(DroneController.class);

	@FXML
	private Label commandLabel;

	@FXML
	private Label statusLabel;

	@FXML
	private ImageView currentFrame;

	public DroneController() {
	}


	public void updateImageView(Image image) {
		Platform.runLater(() -> {
			currentFrame.setImage(image);
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
