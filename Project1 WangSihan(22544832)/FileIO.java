import java.io.*;
import java.util.*;
/**
 * Utility class for reading and writing files for Trip objects.
 * See UseCases.java for example code.
 *
 * @author Rachel Cardell-Oliver
 * @version August 2019
 */

class FileIO 
{

    // Private constructor to prevent instantiation
    private FileIO() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Generate a trip of random GPS pings around Perth WA and 
     * write lat,lon,time triples to a comma separated file
     * @param filename The file to write in csv format
     * @param numEntries How many ping entries to generate
     * @return true if the file was written successfully, false otherwise.
     */
    public static boolean createRandomTripFile (int numEntries,String outFilename)
    {
        //Some default values for the project
        double startLat = -31.977484; //start at CSSE UWA
        double startLon = 115.816228;
        int startTime = 1565063614; //start on (2019-08-06T03:53:34+00:00)
        GPSping gp = new GPSping(startLat, startLon, startTime);
        
        //some default move distances
        int minDdelta = 200; //meters
        int maxDdelta = 600;
        int minTdelta = 30; //seconds
        int maxTdelta = 120; 
        
        Random rand = new Random();
        boolean success = false;
        if(numEntries <= 0) {
            System.err.println("Your trip's must have at least one GPSping. Please try again with numEntries>0");
            return success;
        } else {
            try {
                // open the file for writing
                FileOutputStream fstream = new FileOutputStream(outFilename);
                // convert fstream to a DataInputStream
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fstream));
                // write GPSpings one at a time then calc next
                for(int i = 0; i < numEntries; i++) {
                    out.write(gp.toString() + System.lineSeparator());
                    //next ping
                    gp = randomStep(gp,minDdelta,maxDdelta,minTdelta,maxTdelta,rand);
                }
                // close the data stream
                out.close();
                success = true;
            } 
            catch (Exception e) {
                System.err.println("There was a problem writing to " + outFilename);
            }
        }
        return success;
    }

    /**
     * Calculate a random GPSping offset from a given one in N,S,E,W direction
     * Distance conversion source
     * http://jonisalonen.com/2014/computing-distance-between-coordinates-can-be-simple-and-fast/
     * @param GPSping the current position
     * @param minDdelta int minimum meters to next ping (approximately)
     * @param maxDdelta int maximum meters to next ping (approximately)
     * @param minTdelta int minimum seconds to next ping
     * @param maxTdelta int maximum seconds to next ping
     * @return GPSping the next position
     */
    private static GPSping randomStep(GPSping gp,
    int minDdelta,int maxDdelta,int minTdelta,int maxTdelta,
    Random rand) {
        double degreeLength = 110.25*1000; //=110.25 km on the equator
        double lat = gp.getLat();
        double lon = gp.getLon();
        int time = gp.getTime();
        int deltaD = rand.nextInt(maxDdelta-minDdelta+1)+minDdelta; //meters to move
        int dir = rand.nextInt(4)+1; //1..4 representing N,E,S,W direction
        switch(dir) {
            case 1: //north
            lat = lat + (deltaD / degreeLength);
            //lon unchanged
            break;
            case 2: //east
            //lat unchanged
            lon = lon + (deltaD / degreeLength / Math.cos(lat));
            break;
            case 3: //south
            lat = lat - deltaD / degreeLength;
            //lon unchanged
            break;
            case 4: //west
            //lat unchanged
            lon = lon - (deltaD / degreeLength / Math.cos(lat));
            break;
            default: 
                //should never be reached
        }
        //and a new time changed by at least one second but no more than 5 mins (300 sec)
        int deltaT = rand.nextInt(maxTdelta-minTdelta+1)+minTdelta;
        time = time + deltaT;
        return new GPSping(lat,lon,time);
    }

    /**
     * Generate a GPSping object from an input String such as "-31.953512,115.857048,1565063614"
     * @param String input in form lat,lon,time 
     * @return GPSping object for the lat,lon,time in the String input
     */
    public static GPSping makePingFromString(String inputline)
    {
        GPSping gp = null;
        int NUMFIELDS = 3; //expected number of fields in the input string
        String[] parts = inputline.split(","); //tokenise the string
        if (parts.length != NUMFIELDS) {
            System.out.println("Incorrect format of " + inputline + ". Expected lat,lon,time");
        } else {
            //initialise a GPSping object
            double lat = Double.parseDouble(parts[0]);
            double lon = Double.parseDouble(parts[1]);
            int time = Integer.parseInt(parts[2]);
            gp = new GPSping(lat,lon,time);
        }
        return gp;

    } 

    /**
     * Read a csv file of lat,lon,time trips and generate a Trip object from the data.s
     * @param inFilename The file to read. Lines must be in the format lat,lon,time.
     * @return Trip a Trip object containing a GPSping for each line from the file
     */
    public static Trip readTripFile (String inFilename) 
    {
        Trip trip = new Trip();
        try {
            // open the file
            FileInputStream fstream = new FileInputStream(inFilename);
            // convert fstream to a DataInputStream
            BufferedReader in = new BufferedReader(new InputStreamReader(fstream));
            // read lines one at a time
            while (in.ready()) {
                trip.addPing(makePingFromString(in.readLine()));
            }
            // close the data stream
            in.close();
        } 
        catch (Exception e) {
            System.err.println("File input error reading " + inFilename);
            System.err.println(e.getMessage());
        }
        return trip;
    }

    /**
     * Save a Trip object to a comma separated text file with one line for each ping
     * @param trip a Trip object to be saved
     * @param outFilename The file to be written.
     * @return true if the file was written successfully, false otherwise.
     */
    public static boolean saveTripToFile (Trip trip, String outFilename) 
    {
        boolean success = false;
        try {
            // open the file
            FileOutputStream fstream = new FileOutputStream(outFilename);
            // convert fstream to a DataInputStream
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fstream));
            //write header lines one at a time
            for (GPSping gp : trip.getPingList()) {
                out.write(gp.toString()+System.lineSeparator());

            }
            // close the data stream
            out.close();
            success = true;
        } 
        catch (Exception e) {
            System.err.println("File input error reading " + outFilename);
        }
        return success;
    }

    /**
     * Save a Trip object to a text file as a javascript object for a Leaflet map
     * that will be read by the web page LeafletMapDemo.html
     * Example of file format
     * var triplatlngs = [
     *     [-31.953457559397332,115.85719831939637],
     *     [-31.953281300532204,115.85736955087513] ]
     * The output file will be called "LeafletMapData.txt" and be written in the current directory.
     * @param trip a Trip object to be saved
     * @return true if the file was written successfully, false otherwise.
     */
    public static boolean writeLeafletTripFile (Trip trip) 
    {
        boolean success = false;
        String leafletFilename = "./LeafletMapData.txt";
        try {
            // open the file for writing
            FileOutputStream fstream = new FileOutputStream(leafletFilename);
            // convert fstream to a DataInputStream
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fstream));
            out.write("var triplatlngs = ["); //ping list header
            // write GPSpings one at a time
            for(GPSping gp : trip.getPingList()) {
                String fileline = String.valueOf("[" + gp.getLat()) + "," + String.valueOf(gp.getLon()) + "]," + 
                    System.lineSeparator();
                out.write(fileline);
            }
            //write close
            out.write("]" + System.lineSeparator());
            // close the data stream
            out.close();
            success = true;
        } 
        catch (Exception e) {
            System.err.println("There was a problem writing to " + leafletFilename);
        }

        return success;
    }

    //For a tutorial on reading JSON files, e.g. your own Google Location trace see
    //https://www.oracle.com/technetwork/articles/java/json-1973242.html

}
