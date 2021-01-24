package components;

/**
 * Class that extends the abstract Component class to incorporate the switch methods (toggle)
 *
 * @author Savannah Alfaro, sea2985
 */
public class Switch extends Component {

    private boolean switcher;
    private Outlet outlet;
    private CircuitBreaker circuitBreaker;
    private PowerSource powerSource;

    /**
     * Constructor to create a new Component. Attach it to its source. (See attach(Component) If there is a
     * source and it is engaged, this component will immediately have power.
     *
     * @param type   type of Component
     * @param name   the name of this Component (for tracing, debugging)
     * @param source the Component providing this one with power (null if this
     */
    protected Switch(Types type, String name, Component source) {
        super(type, name, source);
        this.switcher = false;
    }

    /**
     * Returns a boolean value if the switch is on (switcher)
     *
     * @return (boolean) true if the switch is on
     */
    public boolean isSwitchOn() {
        return switcher;
    }

    /**
     * Turns on the switch (sets switcher to true)
     */
    public void turnOn() {
        this.switcher = true;
        Reporter.report(this, Reporter.Msg.SWITCHING_ON);
        engageLoads();
        changeDraw(getDraw());
    }

    /**
     * Turns of the switch (sets switcher to false)
     */
    public void turnOff() {
        this.switcher = false;
        Reporter.report(this, Reporter.Msg.SWITCHING_OFF);
        disengageLoads();
        changeDraw(getDraw());
    }

    /**
     * Turns off/on the switch depending if it is already turned off/on
     */
    public void toggle() {

        if (this.isSwitchOn()) {
            turnOff();
        } else {
            turnOn();
        }
    }
}