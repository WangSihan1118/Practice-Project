

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestGPSpingPhase2.
 * For GPSping phase 2 tasks in CITS1001 project 1.
 *
 * @author  Rachel Cardell-Oliver
 * @version August 2019
 */
public class TestGPSpingPhase2
{
    double PRECISION = 0.000001; //precision for testing double equality
    
    // GPS ping examples for testing
    GPSping csse, perth, paris, err1, err2;
    /**
     * Default constructor for test class TestGPSping
     */
    public TestGPSpingPhase2()
    {
    }

    /**
     * Sets up the test fixture with some sample GPSpings
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        //ping for CSSE UWA
        csse = new GPSping(-31.977484,115.816228,1565063614);
        //ping for Perth train station 1 hour later
        perth = new GPSping(-31.951078,115.859945,1565063614+3600);
        //ping for Musee d'Orsay Paris on 20 Aug 2019
        paris = new GPSping(48.859741,2.326609,1566270756);
        //erroneous pings
        err1 = new GPSping(115.816228,-31.977484,1565063614);
        err2 = new GPSping(200,100,20);
    }

   
 
    
    /**
     * Check GPSping constructor with legal inputs
     * Double equality is checked to precision 0.000001
     * Repeated test case from Phase1
     * 
     */
    @Test
    public void testGPSping() {
        assertEquals(-31.977484, csse.getLat(), PRECISION);
        assertEquals(115.859945, perth.getLon(), PRECISION);
        assertEquals(48.859741, paris.getLat(), PRECISION);
        assertEquals(1565063614,csse.getTime());
    }
    
    
    
    @Test
    public void testTimeTo() {
        assertEquals(csse.timeTo(perth),3600);
        assertEquals(paris.timeTo(paris),0);
    }
    
    /**
     * Test that distTo measure is accurate for known distances and symmetric
     */
    @Test
    public void testdistTo() {
         assertEquals(5039,perth.distTo(csse));
         assertEquals(perth.distTo(csse),csse.distTo(perth));
         assertEquals(paris.distTo(perth),perth.distTo(paris)); 
         assertEquals(0,csse.distTo(csse));
    }
    
   
    
}
