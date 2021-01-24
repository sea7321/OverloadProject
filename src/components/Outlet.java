package components;

/**
 * A class that extends the Component class and initializes the Outlet Component Type
 *
 * @author Savannah Alfaro, sea2985
 */
public class Outlet extends Component {

    /**
     * Constructor that initializes a Outlet
     * @param name name of the Outlet
     * @param source source component of the Outlet
     */
    public Outlet(String name, CircuitBreaker source) {
        super(Types.OUTLET, name, source);
    }
}
