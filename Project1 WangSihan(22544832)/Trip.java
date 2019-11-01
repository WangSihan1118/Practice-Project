/**
 * In this project I will write a Java application to create and analyse GPS traces.
 * 
 * @author: Wang Sihan
 * @student number: 22544832
 * @version 12/09/2019
 */
import java.util.ArrayList;
public class Trip
{
    //An ArrayList for list of GPSping objects
    private ArrayList<GPSping> triplist;

    /**
     * Constructor
     */
    public Trip()
    {
        triplist = new ArrayList<GPSping>();
    }

    public Trip(ArrayList<GPSping>triplist)
    {
        this.triplist = triplist;
    }

    /**
     * Method
     */
    public ArrayList<GPSping> getPingList()
    { 
        return triplist;
    }

    public boolean addPing(GPSping p)
    {
        if(triplist.size() == 0){
            triplist.add(p);
            return true;
        }
        if(p.getTime() >= getFinish().getTime()){
            triplist.add(p);
            return true;
        }else{
            return false;
        }
    }

    public GPSping getStart()
    {
        return triplist.get(0);
    }

    public GPSping getFinish()
    {
        if(triplist.size() == 0){
            return null;
        }
        return triplist.get(triplist.size()-1);
    }

    public int tripDuration()
    {
        return getFinish().getTime() - getStart().getTime();
    }

    public String toString()
    {
        return "This trip has"+triplist.size()+"pings."+"Start from"+getStart() +"Finished at"+getFinish();
    }

    public int tripLength()
    {
        int n = 1;
        int sumDistance = 0;
        while(n <= triplist.size()-1){
            GPSping startPing = triplist.get(n-1);
            GPSping endPing = triplist.get(n);
            n++;
            sumDistance += startPing.distTo(endPing);
        }

        return sumDistance ;
    }

    public ArrayList<GPSping> timeZoom(int startTime, int finishTime)
    {
        ArrayList<GPSping> timeZoomList = triplist;
        while(timeZoomList.get(0).getTime() < startTime){
            timeZoomList.remove(0);
        }

        while(timeZoomList.get(timeZoomList.size()-1).getTime() > finishTime){
            timeZoomList.remove(timeZoomList.size()-1);
        }
        return timeZoomList;
    }

    public GPSping NEbound()
    {
        int n = 0;
        double northBound = triplist.get(0).getLat();
        double northTest;
        while (n <= triplist.size()-1){
            northTest = triplist.get(n).getLat();
            if(northTest >= northBound){
                northBound = northTest;
            }
            n++;
        }

        n = 0;
        double eastBound = triplist.get(0).getLon();
        double eastTest;
        while (n <= triplist.size()-1){
             eastTest = triplist.get(n).getLon();
            if(eastTest >= eastBound){
                eastBound = eastTest;
            }
            n++;
        }

        GPSping NEbound = new GPSping(northBound,eastBound,0);
        return NEbound;
    }

    public GPSping SWbound()
    {
        int n = 0;
        double southBound = triplist.get(0).getLat();
        double southTest;
        while (n <= triplist.size()-1){
            southTest = triplist.get(n).getLat();
            if(southTest <= southBound){
                southBound = southTest;
            }
            n++;
        }

        n = 0;
        double westBound = triplist.get(0).getLon();
        double westTest;
        while (n <= triplist.size()-1){
            westTest = triplist.get(n).getLon();
            if(westTest <= westBound){
                westBound = westTest;
            }
            n++;
        }

        GPSping SWbound = new GPSping(southBound,westBound,0);
        return SWbound;
    }

    public ArrayList<GPSping> spaceZoom(GPSping southwest, GPSping northeast)
    {
        //Set the bound
        double northBound = northeast.getLat();
        double eastBound = northeast.getLon();
        double southBound = southwest.getLat();
        double westBound = southwest.getLon();

        ArrayList <GPSping> spacelist = triplist;

        int n = 0;
        double  testLat;
        double  testLon;
        while(n <= spacelist.size()-1){
            testLat = spacelist.get(n).getLat();
            testLon = spacelist.get(n).getLon();
            if(testLat > northBound || testLat < southBound || testLon > eastBound || testLon < westBound){
               spacelist.remove(n);
            }
            n++;
        }
        return spacelist;
    }
}
