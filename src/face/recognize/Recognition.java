package face.recognize;

import java.text.DecimalFormat;
import javax.swing.*;

import face.util.VideoPanel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

public class Recognition {

  public static void main(String[] args) {

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    CascadeClassifier faceCascade = new CascadeClassifier();
    faceCascade.load("/usr/share/OpenCV/haarcascades/haarcascade_frontalface_alt.xml");
    FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
    faceRecognizer.read("/home/david/Documents/code/OpenCvObjectDetection/model/face_model.yml");
    VideoCapture capture = new VideoCapture();
    try {
      capture.open(0);
      VideoPanel videoPanel = VideoPanel.show("Recognition", 640, 480);

      if (capture.isOpened()) {
        Mat image = new Mat();
        while (true) {
          capture.read(image);
          if (!image.empty()) {
            detectAndDisplay(image, faceCascade, faceRecognizer);
            videoPanel.setImageWithMat(image);
          } else {
            break;
          }
        }
      }
    } finally {
      capture.release();
    }
  }

  public static void detectAndDisplay(Mat frame, CascadeClassifier faceCascade,
      FaceRecognizer faceRecognizer) {
    MatOfRect faces = new MatOfRect();
    Mat grayFrame = new Mat();

    Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
    Imgproc.equalizeHist(grayFrame, grayFrame);

    // detect faces
    faceCascade.detectMultiScale(grayFrame, faces);
    Imgproc.equalizeHist(grayFrame, grayFrame);

    Rect[] facesArray = faces.toArray();
    for (int i = 0; i < facesArray.length; i++) {
      int[] label = new int[1];
      double[] confidence = new double[1];
      faceRecognizer.predict(grayFrame.submat(facesArray[i]), label, confidence);
      String name = faceRecognizer.getLabelInfo(label[0]);
      Scalar color;
      if (confidence[0] < 50) {
        if (label[0] == 0) {
          color = new Scalar(255, 0, 0);
        } else if (label[0] == 1) {
          color = new Scalar(0, 255, 0);
        } else {
          color = new Scalar(0, 255, 255);
        }
        name = name + " " + new DecimalFormat("#.0").format(confidence[0]);
      } else {
        name = "face";
        color = new Scalar(0, 0, 255);
      }

      Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), color, 2);
      setLabel(frame, name, facesArray[i].tl(), color);
    }

  }

  static void setLabel(Mat im, String label, Point or, Scalar color) {
    int fontface = Core.FONT_HERSHEY_SIMPLEX;
    double scale = 0.8;
    int thickness = 2;
    int[] baseline = new int[1];

    Size text = Imgproc.getTextSize(label, fontface, scale, thickness, baseline);
    Imgproc.rectangle(im, new Point(or.x, or.y),
        new Point(or.x + text.width, or.y - text.height - baseline[0] - baseline[0]), color,
        Core.FILLED);

    Imgproc.putText(im, label, new Point(or.x, or.y - baseline[0]), fontface, scale,
        new Scalar(255, 255, 255), thickness);
  }

}
