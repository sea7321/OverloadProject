package components;

/**
 * A class that extends the Switch class and initializes an Appliance component type
 *
 * @author Savannah Alfaro, sea2985
 */
public class Appliance extends Switch {

    private int ratedDrawAndLoad;

    /**
     * Constructor that initializes an Appliance
     * @param name name of the Appliance
     * @param source source component of the Appliance
     * @param ratedDrawAndLoad load rating of the Appliance
     */
    public Appliance(String name, Component source, int ratedDrawAndLoad) {
        super(Types.APPLIANCE, name, source);
        this.ratedDrawAndLoad = ratedDrawAndLoad;
        this.draw = ratedDrawAndLoad;
    }

    /**
     * Returns the load of the Appliance
     * @return (String) load rating of the Appliance
     */
    public String getRating() {
        return Integer.toString(ratedDrawAndLoad);
    }

    public int getRatedDrawAndLoad() {
        return ratedDrawAndLoad;
    }


}
