package nl.ns.demo;

/**
 * Created by joel on 27-5-14.
 */
public enum DelayStatus {

    UNKNOWN("002"), // BLUE
    SMALL("020"), // GREEN
    MEDIUM("220"), // YELLOW
    HIGH("200"); // RED

    private String colorString;

    DelayStatus(String colorString) {
        this.colorString = colorString;
    }

    public String getColorString() {
        return colorString;
    }
}
