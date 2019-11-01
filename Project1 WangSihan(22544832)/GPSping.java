
/**
 * In this project I will write a Java application to create and analyse GPS traces.
 * 
 * @author Wang Sihan
 * @student number:22544832
 * @version 12/09/2019
 */
public class GPSping
{
    /**
     * Fields
     */
    //The latitude of ping
    private double lat;
    //The longtitude of ping
    private double lon;
    //The time of ping of ping
    private int time;

    /**
     * Constructor
     */
    public GPSping(double lat, double lon, int time)
    {
        if(lat > -90&&lat < 90) {
            this.lat = lat;
        }
        if(lon > -180&&lon < 180) {
            this.lon = lon;
        }
        if(time > -2147483647&&time < 2147483647){
            this.time = time;
        }
    }
    
    /**
     * Method
     */
    
    /**
     * @Return the latitude of ping
     */
    public double getLat()
    {
        return lat;
    }
    
    /**
     * @Return the longtitude of ping
     */
    public double getLon()
    {
        return lon;
    }
    
    /**
     * @Return the time of ping
     */
    public int getTime()
    {
        return time;
    }
    
    /**
     * @Generate a String as a comma separated lat,lon,time triple for this GPSping. 
     */
    public String toString()
    {
        return String.valueOf(lat) + "," +String.valueOf(lon) + "," +String.valueOf(time);
    }
    
    public int timeTo(GPSping anotherPing)
    {
        return  anotherPing.getTime() - time;
    }
    
    public int distTo(GPSping anotherPing)
    {         
        //Set degreelength
        double degreelength = 110.25*1000;
        
        //Set lantitude
        double lat0 = this.lat;
        double lat1 = anotherPing.getLat();
        double xd = lat0 - lat1;
        double avglat = (lat1+lat0)/2;
     
        //Set longtitude
        double lon0 = this.lon;
        double lon1 = anotherPing.getLon();
        double yd = (lon0 - lon1) * (Math.cos(avglat));
        
        double distance0 = degreelength * (Math.sqrt(xd*xd + yd*yd));
        int distance = (int)Math.round(distance0);
        return distance;
    }
}
