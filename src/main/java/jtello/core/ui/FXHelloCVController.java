package jtello.core.ui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jtello.core.communication.Video;
import jtello.core.ui.utils.Utils;

/**
 * The controller for our application, where the application logic is
 * implemented. It handles the button for starting/stopping the camera and the
 * acquired video stream.
 *
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @author <a href="http://max-z.de">Maximilian Zuleger</a> (minor fixes)
 * @version 2.0 (2016-09-17)
 * @since 1.0 (2013-10-20)
 *
 */
public class FXHelloCVController {
	// the FXML button
	@FXML
	private Button button;
	// the FXML image view
	@FXML
	private ImageView currentFrame;

	// a flag to change the button behavior
	private boolean cameraActive = false;

	
	
	public FXHelloCVController() {
		Video video = new Video(i -> updateImageView(currentFrame, i));
		new Thread(video).start();
	}

	/**
	 * The action triggered by pushing the button on the GUI
	 *
	 * @param event the push button event
	 * @throws org.bytedeco.javacv.FrameGrabber.Exception
	 */
	@FXML
	protected void startCamera(ActionEvent event) throws org.bytedeco.javacv.FrameGrabber.Exception {
		Video video = new Video(i -> updateImageView(currentFrame, i));

		if (!this.cameraActive) {

			new Thread(video).start();

			this.cameraActive = true;

		} else {
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.button.setText("Start Camera");

			try {
				video.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Update the {@link ImageView} in the JavaFX main thread
	 * 
	 * @param view  the {@link ImageView} to update
	 * @param image the {@link Image} to show
	 */
	private void updateImageView(ImageView view, Image image) {
		//System.out.println("got image");
		Utils.onFXThread(view.imageProperty(), image);
	}

	public void setClosed() {
		// TODO Auto-generated method stub
		
	}

}
