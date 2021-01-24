package components;

/**
 * A class that extends Component and initializes the PowerSource component type
 *
 * @author Savannah Alfaro, sea2985
 */
public class PowerSource extends Component {

    /**
     * Constructor that initializes the PowerSource
     * @param name name of the PowerSource
     */
    public PowerSource(String name) {
        super(Types.POWER_SOURCE, name, null);
    }
}
