package components;

/**
 * A class that extends Switch and initializes the CircuitBreaker component type
 *
 * @author Savannah Alfaro, sea2985
 */
public class CircuitBreaker extends Switch {

    private int limit;

    /**
     * Constructor that initializes the CircuitBreaker
     * @param name name of the CircuitBreaker
     * @param source source component of the CircuitBreaker
     * @param limit load limit of the CircuitBreaker
     */
    public CircuitBreaker(String name, PowerSource source, int limit) {
        super(Types.CIRCUIT_BREAKER, name, source);
        this.limit = limit;
    }

    /**
     * Returns the limit of the CircuitBreaker
     * @return (int) limit of the CircuitBreaker
     */
    public int getLimitInt() {
        return limit;
    }

    /**
     * Returns the limit of the CircuitBreaker as a String
     * @return (String) limit of the CircuitBreaker
     */
    public String getLimit() {
        return Integer.toString(limit);
    }
}
