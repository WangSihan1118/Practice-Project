import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestTripPhase1.
 * For Trip phase 1 tasks in CITS1001 project 1.
 *
 * @author  Rachel Cardell-Oliver
 * @version August 2019
 */
public class TestTripPhase1
{
    double PRECISION = 0.000001; //precision for testing double equality
    //trips for testing
    Trip shorttrip, testtrip; 
    //ping for CSSE UWA
    GPSping csse = new GPSping(-31.977484,115.816228,1565063614);
    //ping for Perth train station 1 hour later
    GPSping perth = new GPSping(-31.951078,115.859945,1565063614+3600);
    
    //start and end of TestTrip.csv
    GPSping r1 = new GPSping(56.5019088,-3.4460218,1522486775);
    GPSping r2 = new GPSping(56.6742354,-3.6833027,1523049871);
    /**
     * Default constructor for test class TestTrip
     */
    public TestTripPhase1()
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
        
        testtrip = FileIO.readTripFile("./TestTrip.csv");
    }

    @Test
    public void testTripSize() {
        assertEquals(2,shorttrip.getPingList().size());
        assertEquals(4070,testtrip.getPingList().size());
    }

    

    @Test
    public void testStartFinish() {
        assertEquals(csse.toString(),shorttrip.getStart().toString());
        assertEquals(perth.toString(),shorttrip.getFinish().toString());
        
        assertEquals(r1.toString(),testtrip.getStart().toString());
        assertEquals(r2.toString(),testtrip.getFinish().toString());
    }
    
    @Test
    public void testTripDuration() {
        assertEquals(3600, shorttrip.tripDuration());
        assertEquals(563096, testtrip.tripDuration());
    }
    
    

}
