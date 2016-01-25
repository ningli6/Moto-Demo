package utility;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;

/*
 * Use this class to visualize the result of probability matrx
 */

public class ColorPan extends JComponent {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private int[] data;
  private int width;
  private int height;

  public ColorPan(int[] data, int width, int height) {
    this.data = data;
    this.width = width;
    this.height = height;
  }

  public void paint(Graphics g) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    image.setRGB(0, 0, width, height, data, 0, width);
    g.drawImage(image, 0, 0, this);
  }
  // usage example
  public static void main(String[] args) {
    int width = 200;
    int height = 500;
    int[] data = new int[width * height];
    int i = 0;
    for (int y = 0; y < height; y++) {
      int red = (y * 255) / (height - 1);
      for (int x = 0; x < width; x++) {
        int green = (x * 255) / (width - 1);
        int blue = 128;
        data[i++] = (red << 16) | (green << 8) | blue;
      }
    }
    JFrame frame = new JFrame("Probability Matrix");
    frame.getContentPane().add(new ColorPan(data, width, height));
    frame.setSize(width, height);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}