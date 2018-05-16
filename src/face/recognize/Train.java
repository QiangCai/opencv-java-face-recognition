package face.recognize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Train {

  public static void main(String[] args) throws IOException {
    train("/home/david/Documents/code/opencv-java-face-recognition/imagedb",
        "/home/david/Documents/code/opencv-java-face-recognition/model");
  }

  public static void train(String imageFolder, String saveFolder)
      throws IOException {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
    CascadeClassifier faceCascade = new CascadeClassifier();
    faceCascade.load("/usr/share/OpenCV/haarcascades/haarcascade_frontalface_alt.xml");
    File[] files = new File(imageFolder).listFiles();
    Map<String, Integer> nameMapId = new HashMap<String, Integer>(10);
    List<Mat> images = new ArrayList<Mat>(files.length);
    List<String> names = new ArrayList<String>(files.length);
    List<Integer> ids = new ArrayList<Integer>(files.length);
    for (int index = 0; index < files.length; index++ ) {
      File file = files[index];
      String name = file.getName().split("\\.")[1];
      Integer id = nameMapId.get(name);
      if (id == null) {
        id = names.size();
        names.add(name);
        nameMapId.put(name, id);
        faceRecognizer.setLabelInfo(id, name);
      }

      Mat mat = Imgcodecs.imread(file.getCanonicalPath());
      Mat gray = new Mat();

      // convert the frame in gray scale
      Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
      images.add(gray);
      System.out.println("add total " + images.size());
      ids.add(id);
    }
    int[] idsInt = new int[ids.size()];
    for (int i = 0; i < idsInt.length; i++) {
      idsInt[i] = ids.get(i).intValue();
    }
    MatOfInt labels = new MatOfInt(idsInt);

    faceRecognizer.train(images, labels);
    faceRecognizer.save(saveFolder + "/face_model.yml");
  }

}
