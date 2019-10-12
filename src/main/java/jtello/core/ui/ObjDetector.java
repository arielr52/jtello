package jtello.core.ui;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import com.sun.javafx.geom.Point2D;

import jtello.core.DroneApp;

/**
 * Uses the classifier to identify objects in the frame, then draw a rectangle of the object and supply the location of the object  
 * @author Ariel
 *
 */
public class ObjDetector implements Supplier<Optional<Point2D>>{

	final static Logger log = Logger.getLogger(DroneController.class);
	
	private CascadeClassifier classifier = new CascadeClassifier();
	private int absoluteFaceSize =0;
	
	private Point2D frameCenter;
	
	private BlockingQueue<Point2D> objCenterQueue = new LinkedBlockingDeque<>(1);
	
	private static final String CLASSIFIER = "lbpcascade_frontalface.xml";
	public ObjDetector() {
		String filePath =DroneApp.class.getClassLoader().getResource(CLASSIFIER).getFile();
		filePath = filePath.substring(1,filePath.length());
		this.classifier.load(filePath);
	}
	
	public Point2D getFrameCenter() {
		return frameCenter;
	}

	public void detectAndDisplay(Mat frame)
	{
		frameCenter = new Point2D(frame.width()/2, frame.height()/2);
		
		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();
		
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(grayFrame, grayFrame);
		
		if (this.absoluteFaceSize == 0)
		{
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0)
			{
				this.absoluteFaceSize = Math.round(height * 0.2f);
			}
		}
		
		this.classifier.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());
				
		Arrays.asList(faces.toArray()).stream().findFirst().ifPresent(rect->handleObject(frame, rect));
	}
	
	void handleObject(Mat frame, Rect rect){
		Point2D objCenter = new Point2D(rect.x+(rect.width/2), rect.y+(rect.height/2));
		objCenterQueue.clear();
		objCenterQueue.offer(objCenter);
		Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 3);
	}

	@Override
	public Optional<Point2D> get() {
		try {
			return Optional.ofNullable(objCenterQueue.poll(1, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
