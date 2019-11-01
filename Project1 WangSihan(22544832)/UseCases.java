import java.util.ArrayList;
/**
 * Utility class with use cases to demonstrate the Trip project.
 * This class is provided to help you test your project.
 * It can be used to generate a random trip, print a summary to the terminal 
 * and write the trip coordinates to a file LeafletMapData.csv for 
 * display by LeafletMapDemo.html
 *
 * @author Rachel Cardell-Oliver
 * @version August 2019
 */
public class UseCases
{

    Trip testtrip;
    
    public UseCases() {
        testtrip = new Trip();
    }
    
     /**
     * Use sample data in file "./TestTrip.csv" to create a Trip
     * and write it to "./LeafletMapData.txt" for viewing with "LeafletMapDemo.html"
     */
    public void runTestUseCase() {
        testtrip = FileIO.readTripFile("./TestTrip.csv");
        //save mytrip to (default) "./LeafletMapData.txt" for display with Leaflet
        FileIO.writeLeafletTripFile(testtrip);
        //print summary of the trip to standard out
        System.out.print(testtrip.toString());
    }
    
    
    /**
     * Generate a random trip file with numEntries pings
     * and write it to "./LeafletMapData.txt" for viewing with "LeafletMapDemo.html"
     * @param numEntries number of pings in the trip
     */
    public void runRandomUseCase(int numEntries) {
        FileIO.createRandomTripFile(numEntries,"./RandomTrip.csv");
        testtrip = FileIO.readTripFile("./RandomTrip.csv");
        //save mytrip to default , "./LeafletMapData.txt" for display with Leaflet
        FileIO.writeLeafletTripFile(testtrip);
        //print summary of the trip to standard out
        System.out.print(testtrip.toString());
    }
    


    
}
