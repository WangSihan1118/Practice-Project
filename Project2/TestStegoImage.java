
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

/**
 * The test class TestStegoImage.
 * If needed uncomment the showPix lines for
 * debug printing of before and after pixel values
 *
 * @author  Rachel CO
 * @version October 2019
 */
public class TestStegoImage
{

    StegoImage img1, img2;

    /**
     * Default constructor for test class TestStegoImage
     */
    public TestStegoImage()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        img1 = new StegoImage("ProjectImages/covers/image0.png");
        img2 = new StegoImage("ProjectImages/secrets/image5.png");
        
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test   
    public void testConstructor() {
        //choose 2 random pixels and test for expected colour values
        Color c1, c2;
        assertEquals(300,img1.getWidth());
        assertEquals(300,img1.getHeight());
        c1 = new Color(img1.getImage().getRGB(100,150)); 
        //showPix(c1);
        assertEquals(67,c1.getRed());
        assertEquals(61,c1.getGreen());
        assertEquals(47,c1.getBlue());
        c2 = new Color(img1.getImage().getRGB(200,50)); 
        //showPix(c2);
        assertEquals(90,c2.getRed());
        assertEquals(119,c2.getGreen());
        assertEquals(135,c2.getBlue());

    }

    @Test   
    public void testClearLowBit() {
        Color c1;
        int r,g,b;
        img1.clearLowBit();
        c1 = new Color(img1.getImage().getRGB(100,150)); //arbitrary choice of pixel
        //showPix(c1);
        assertEquals(66,c1.getRed());
        assertEquals(60,c1.getGreen());
        assertEquals(46, c1.getBlue());
    }

    @Test   
    public void testSetZeroOne() {
        Color c1;
        //c1 = new Color(img1.getImage().getRGB(200,50)); 
        //showPix(c1);
        img1.setZeroOne();
        c1 = new Color(img1.getImage().getRGB(200,50)); //arbitrary choice of pixel
        //showPix(c1);
        assertEquals(0,c1.getRed());
        assertEquals(0,c1.getGreen());
        assertEquals(1,c1.getBlue());
    }

    @Test
    public void testSetBlackWhite() {
        Color c1;
        img1.setZeroOne();
        img1.setBlackWhite();
        c1 = new Color(img1.getImage().getRGB(200,50)); //arbitrary choice of pixel
        //showPix(c1);
        assertEquals(0,c1.getRed());
        assertEquals(0,c1.getGreen());
        assertEquals(255,c1.getBlue());
    }

    @Test
    public void testSetToLowBit() {
        Color c1;
        img1.setToLowBit();
        c1 = new Color(img1.getImage().getRGB(99,100)); //get it back
        assertEquals(1,c1.getRed()); 
        assertEquals(0,c1.getGreen()); 
        assertEquals(0,c1.getBlue());
    }

    @Test
    public void testScaleImage() {
        Color c1;
        int r,g,b;
        c1 = new Color(img1.getImage().getRGB(99,100)); 
        //showPix(c1); 
        img1.scaleImage(10, 5); //arbitrary scaling /10 and *5
        c1 = new Color(img1.getImage().getRGB(99,100)); 
        //showPix(c1); 
        assertEquals(20,c1.getRed()); 
        assertEquals(25,c1.getGreen()); 
        assertEquals(20,c1.getBlue());
    }

    @Test
    public void testMergeImage() {
        int xx=150, yy=151; //position of pixel to test
        Color c1;
        img1.clearLowBit(); //cleared cover gives 28,14,6
        //c1 = new Color(img1.getImage().getRGB(xx,yy)); 
        //showPix(c1);
        c1 = new Color(img2.getImage().getRGB(xx,yy)); //249,251,245
        //showPix(c1);
        img2.setZeroOne(); //secret
        c1 = new Color(img2.getImage().getRGB(xx,yy)); //1,1,1
        //showPix(c1);
        img1.mergeImage(img2);
        c1 = new Color(img1.getImage().getRGB(xx,yy)); 
        //showPix(c1);
        assertEquals(29,c1.getRed());
        assertEquals(15,c1.getGreen());
        assertEquals(7,c1.getBlue());
    }
    
    /**
     * Helper method to print out color values of a pixel
     * Can use this for debugging
     * @param Color c1 pixel of interest
     */
    private void showPix(Color c1) {
        int r,g,b;
        r = c1.getRed();
        g = c1.getGreen();
        b = c1.getBlue();
        System.out.println("("+r+","+g+","+b+")"); 
    }
}
