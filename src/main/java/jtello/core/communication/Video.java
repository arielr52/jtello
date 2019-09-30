package jtello.core.communication;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * Receive h264 video stream from the drone and supply the converted Image  
 * @author Ariel
 *
 */
public class Video implements Runnable, Supplier<Optional<Image>> {

	final static Logger log = Logger.getLogger(Video.class);

	Java2DFrameConverter java2dFrameConverter = new Java2DFrameConverter();

	BlockingQueue<Image> imageQueue = new LinkedBlockingDeque<>(1);

	@Override
	public void run() {
		try(FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("udp://@0.0.0.0:11111");) {			
			grabber.setFormat("h264");
			grabber.start();
			Frame frame;
			while ((frame = grabber.grab()) != null) {
				Image imageToShow = SwingFXUtils.toFXImage(java2dFrameConverter.convert(frame), null);
				imageQueue.clear();
				imageQueue.offer(imageToShow);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public Optional<Image> get() {
		try {
			return Optional.ofNullable(imageQueue.poll(1, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}			
	}

}
