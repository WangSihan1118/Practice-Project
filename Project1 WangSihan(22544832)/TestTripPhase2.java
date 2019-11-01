import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestTripPhase2
 * For Trip class phase 2 tasks in CITS1001 project 1.
 *
 * @author  Rachel Cardell-Oliver
 * @version August 2019
 */
public class TestTripPhase2
{
    double PRECISION = 0.000001; //precision for testing double equality
    //trips for testing
    Trip shorttrip, testtrip; 
    //ping for CSSE UWA
    GPSping csse = new GPSping(-31.977484,115.816228,1565063614);
    //ping for Perth train station 1 hour later
    GPSping perth = new GPSping(-31.951078,115.859945,1565063614+3600);

    /**
     * Default constructor for test class TestTrip
     */
    public TestTripPhase2()
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
        shorttrip = new Trip();
        shorttrip.addPing(csse);
        shorttrip.addPing(perth);  
        
        testtrip = FileIO.readTripFile("./testtrip.csv");
    }



    @Test
    public void testLength() {
        assertEquals(5039,shorttrip.tripLength());
        assertEquals(742139,testtrip.tripLength());
    }

    @Test
    public void testBoundsSimple() {
        assertEquals(csse.getLat(),shorttrip.SWbound().getLat(),PRECISION);
        assertEquals(csse.getLon(),shorttrip.SWbound().getLon(),PRECISION);
        assertEquals(perth.getLat(),shorttrip.NEbound().getLat(),PRECISION);
        assertEquals(perth.getLon(),shorttrip.NEbound().getLon(),PRECISION);
    }
    
    @Test
    public void testBoundsBox() {
        //case for testtrip where SW, NE not examples
        assertEquals(56.5019088,testtrip.SWbound().getLat(),PRECISION);	
        assertEquals(-4.6829746,testtrip.SWbound().getLon(),PRECISION);
        assertEquals(57.6542418,testtrip.NEbound().getLat(),PRECISION);	
        assertEquals(-3.3051918,testtrip.NEbound().getLon(),PRECISION);
    }
    
    @Test
    public void testTimeZoom() {   
        int oneday = 24*60*60; //seconds in one day
        int t1 = testtrip.getStart().getTime()+oneday;
        int t2 = testtrip.getFinish().getTime()-oneday;
        ArrayList<GPSping> zoomtesttrip = testtrip.timeZoom(t1,t2);
        assertEquals(2708,zoomtesttrip.size());
        assertEquals(1522573317,zoomtesttrip.get(0).getTime());
    }
    
    @Test
    public void testSpaceZoom() {
         //csse -31.977484,115.816228
         ArrayList<GPSping> zoomcsse = shorttrip.spaceZoom(
                new GPSping (-31.977488,115.816224,0),
                new GPSping (-31.977480,115.816232,0) );
         assertEquals(1,zoomcsse.size());
         assertEquals(csse.getLat(),zoomcsse.get(0).getLat(),PRECISION);
         assertEquals(csse.getLon(),zoomcsse.get(0).getLon(),PRECISION);

    }

    
    

}
