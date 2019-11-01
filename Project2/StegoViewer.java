import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * StegoViewer The user interface.
 *
 * @Wang Sihan
 * 9/10/2019
 */
public class StegoViewer
{
    // instance variables - replace the example below with your own
    public static int IMG_WIDTH = 300;
    public static int IMG_HEIGHT = 300;
    public static int CAPTION_MARGIN = 50;
    public static int NUM_IMAGES = 4;
    SimpleCanvas canvas;
    
    /**
     * Constructor for objects of class StegoViewer
     */
    public StegoViewer()
    {
        //Create Canvas
        this.canvas = new SimpleCanvas("Canvas",(NUM_IMAGES*IMG_WIDTH),(IMG_HEIGHT +CAPTION_MARGIN),Color.white);
        
        //Add the bottom caption string
        canvas.drawString("Cover", 120, 325, Color.black);
        canvas.drawString("Secret", 420, 325, Color.black);
        canvas.drawString("Encrypted Message", 720, 325, Color.black);
        canvas.drawString("crypted Message", 1020, 325, Color.black);
        
    }

    /**
     * display
     */
    public void displayImage(BufferedImage image, int window)
    {
        
        switch (window){
           case 0:
                canvas.drawImage(image, 0, 0);
                break;
           case 1:
                canvas.drawImage(image, 300, 0);
                break;
           case 2:
                canvas.drawImage(image, 600, 0);
                break;
           case 3:
                canvas.drawImage(image, 900, 0);
                break;
            }
    }
}
