package jtello.core.communication;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class Video implements Runnable, Closeable {

	private final Consumer<Image> consumer;

	//FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("udp://@0.0.0.0:11111");

	Java2DFrameConverter java2dFrameConverter = new Java2DFrameConverter();
	
	public Video(Consumer<Image> consumer) {
		this.consumer = consumer;
		//grabber.setFormat("h264");
	}

	@Override
	public void run() {
		try {
			FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("udp://@0.0.0.0:11111");
			grabber.setFormat("h264");
			grabber.start();
			Frame frame;
			while ((frame = grabber.grab()) != null) {
				Image imageToShow = SwingFXUtils.toFXImage(java2dFrameConverter.convert(frame), null);
				consumer.accept(imageToShow);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
		//grabber.release();
		//grabber.close();
	}

}
