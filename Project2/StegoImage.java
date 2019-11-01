import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * StegoImage
 *
 * @Wang Sihan
 * 9/10/2019
 */
public class StegoImage
{
    // instance variables - replace the example below with your own
    private BufferedImage image;
    private int width;
    private int height;

    /**
     * Constructor for objects of class StegoImage
     */
    public StegoImage(String filename)
    {   
        width = 300;
        height = 300;

        try{
            File file = new File(filename);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(file);
            System.out.println("Reading complete.");
        }catch(IOException e){
            System.out.println("Error: "+e);
        }
    }

    /**
     * method
     */
    public BufferedImage getImage()
    {
        return image;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
    
    public int[] getRgbValue(int i, int j)
    {   
        int[] RGBvalue = new int[3];
        Color color = new Color(image.getRGB(i,j));
        //get pixel value
        int pixel = image.getRGB(i,j);
    
        //get alpha
        int alpha = (pixel>>24)&0xff;
    
        //get red
        int red = (pixel>>16) & 0xff;
    
        //get green
        int green = (pixel>>8) & 0xff;
    
        //get blue
        int blue = pixel & 0xff;
        
        RGBvalue[0] = red;
        RGBvalue[1] = green;
        RGBvalue[2] = blue;
        
        return RGBvalue;
    }
   
    
    public void scaleImage(int div, int mult)
    {
        for(int i =0; i < width ;i++){
            for (int j = 0; j < height; j++){
                Color color = new Color(image.getRGB(i,j));
                //get pixel value
                int pixel = image.getRGB(i,j);

                //get alpha
                int alpha = (pixel>>24)&0xff;

                //get red
                int red = (pixel>>16) & 0xff;

                //get green
                int green = (pixel>>8) & 0xff;

                //get blue
                int blue = pixel & 0xff;

                //set to zero or one
                alpha = 255;
                red = red/div*mult;
                green = green/div*mult;
                blue = blue/div*mult;

                //set the pixel value
                pixel = (alpha<<24)|(red<<16) | (green<<8) | blue;
                image.setRGB(i, j, pixel);

            }
        }
    }

    public void clearLowBit()
    {
        scaleImage(2,2);
    }

    public void setZeroOne()
    {
        scaleImage(128,1);
    }

    public void setBlackWhite()
    {
        scaleImage(1,255);
    }

    public void setToLowBit()
    {
         for(int i = 0; i < width ;i++){
            for (int j = 0; j < height; j++){
                Color color = new Color(image.getRGB(i,j));
                //get pixel value
                int pixel = image.getRGB(i,j);
                
                //get alpha
                int alpha = (pixel>>24) & 0xff;
                
                //get red
                int red = (pixel>>16) & 0xff;

                //get green
                int green = (pixel>>8) & 0xff;

                //get blue
                int blue = pixel & 0xff;

                //clearlowbit
                alpha = 255;
                red = red%2;
                green = green%2;
                blue = blue%2;

                //set the pixel value
                pixel = (alpha<<24)|(red<<16) | (green<<8) | blue;
                image.setRGB(i, j, pixel);

            }
        }
    }

    public void mergeImage(StegoImage newimage)
    {
        for(int i = 0; i < width ;i++){
            for (int j = 0; j < height; j++){
                 
                Color color = new Color(image.getRGB(i,j));
                Color new_color = new Color(newimage.getImage().getRGB(i,j));
                
                //get pixel value
                int pixel = image.getRGB(i,j);
                int new_pixel = newimage.getImage().getRGB(i,j);
                
                
                //get alpha
                int alpha = (pixel>>24) & 0xff;
                int new_alpha = (new_pixel>>24) & 0xff;
                
                //get red
                int red = (pixel>>16) & 0xff;
                int new_red = (new_pixel>>16) & 0xff;
                
                //get green
                int green = (pixel>>8) & 0xff;
                int new_green = (new_pixel>>8) & 0xff;
                
                //get blue
                int blue = pixel & 0xff;
                int new_blue = new_pixel & 0xff;
                
                //clearlowbit
                alpha = 255;
                red = red + new_red;
                green = green + new_green;
                blue = blue + new_blue;

                //set the pixel value
                pixel = (alpha<<24) | (red<<16) | (green<<8) | blue;
                image.setRGB(i, j, pixel);

            }
        }
    }
}
