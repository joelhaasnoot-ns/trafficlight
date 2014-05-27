package nl.ns.demo;

import java.util.Timer;

/**
 * Created by joel on 27-5-14.
 */
public class TrafficLight {

    public TrafficLight() {  }

    private void run() {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new CheckTimesTask("ut"), 0, 5000);
    }

    public static void main(String [] args) {
        TrafficLight l = new TrafficLight();
        l.run();
    }




}
