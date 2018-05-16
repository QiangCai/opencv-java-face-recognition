package face.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

public class VideoPanel extends JPanel {

  private BufferedImage image;

  public void setImageWithMat(Mat mat) {
    image = (BufferedImage) HighGui.toBufferedImage(mat);
    this.repaint();
  }

  @Override protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (image != null) g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
  }

  public static VideoPanel show(String title, int width, int height) {
    JFrame frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(width, height);
    frame.setBounds(0, 0, width, height);
    VideoPanel videoPanel = new VideoPanel();
    videoPanel.setSize(width, height);
    frame.setContentPane(videoPanel);
    frame.setVisible(true);
    return videoPanel;
  }
}