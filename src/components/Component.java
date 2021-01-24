package components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Abstract class that generates the method for a component in the circuit.
 *
 * @author Savannah Alfaro, sea2985
 */
public abstract class Component {

    private Types type;
    private String name;
    private Component source;

    protected int draw;
    private Collection<Component> loads;
    private boolean engaged;

    /**
     * Constructor to create a new Component. Attach it to its source. (See attach(Component) If there is a
     * source and it is engaged, this component will immediately have power.
     * @param type type of Component
     * @param name the name of this Component (for tracing, debugging)
     * @param source the Component providing this one with power (null if this
     *               Component generates its own power and therefore does not have a source)
     */
    protected Component (Types type, String name, Component source) {
        this.type = type;
        this.name = name;
        this.source = source;
        this.engaged = false;
        this.loads = new ArrayList<Component>();
    }

    /**
     * Add a new load to this component
     * @param newLoad the new component to be added.
     */
    protected void addLoad(Component newLoad){
        loads.add(newLoad);
    }

    /**
     * Add a new load (something that draws current) to this Component. If this component is engaged,
     * the new load becomes engaged.
     * @param load the Component to be "plugged in"
     */
    protected void attach(Component load) {
        Reporter.report(this, load, Reporter.Msg.ATTACHING);
        if (engaged) {
            load.engage();
        }
        addLoad(load);
    }

    /**
     * The source for this component is now being powered
     */
    public void engage() {
        Reporter.report(this, Reporter.Msg.ENGAGING);
        this.engaged = true;
    }

    /**
     * Inform all Components to which this Component acts as a source that
     * they may now draw current.
     */
    protected void engageLoads() {
        for (Component load : loads) {
            Reporter.report(load, Reporter.Msg.ENGAGING);
            this.engaged = true;

            load.engageLoads();
        }
    }

    /**
     * Engages a component and not all loads (children)
     */
    public void engageLoad() {
        for (Component load: loads) {
            Reporter.report(load, Reporter.Msg.ENGAGING);
            this.engaged = true;
        }
    }

    /**
     * Inform all Components to which this Component acts as a source that they
     * will no longer get any current
     */
    public void disengageLoads() {
        for (Component load : loads) {
            Reporter.report(load, Reporter.Msg.DISENGAGING);
            load.engaged = false;
            load.disengageLoads();
        }
    }

    /**
     * Display this (sub) tree vertically, with indentation
     */
    public void display() {
        if (this.getType().equals(Types.POWER_SOURCE)) {
            System.out.println(" + " + Reporter.identify(this));
        }
        for (Component load : loads) {
            if (load.getType().equals(Types.CIRCUIT_BREAKER)) {
                System.out.println("\t + " + Reporter.identify(load));
                load.display();
            } else if (load.getType().equals(Types.OUTLET)) {
                System.out.println("\t\t + " + Reporter.identify(load));
                load.display();
            } else {
                System.out.println("\t\t\t + " + Reporter.identify(load));
            }
        }
    }

    /**
     * Is this Component currently being fed power?
     * @return true iff this Component is engaged
     */
    protected boolean engaged() {
        return engaged;
    }

    /**
     * Find out how much current this component is drawing
     * @return the amount of current this Component is currently drawing from its source,
     * or providing, if a source of power
     */
    protected int getDraw() {
        return draw;
    }

    /**
     * Increment draw by delta of a given component
     * @param component (Component) component to increment
     * @param delta (int) draw change
     */
    public void incDraw(Component component, int delta)
    {
        component.draw += delta;
    }

    /**
     * Change the amount of current padding through this Component
     * @param delta The number of amperes by which to raise(+) or lower(-) the draw
     */
    protected void changeDraw(int delta) {

        if (this.getType().equals(Types.APPLIANCE)) {
            this.incDraw(this.getSource(), delta);
            Reporter.report(this.getSource(), Reporter.Msg.DRAW_CHANGE, draw);
            source.changeDraw(delta);
        } else if (this.getType().equals(Types.OUTLET)) {
            source.changeDraw(delta);
        } else if (this.getType().equals(Types.CIRCUIT_BREAKER)) {
            CircuitBreaker circuitBreaker = (CircuitBreaker) this;
            if (circuitBreaker.getDraw() > circuitBreaker.getLimitInt()) {
                Reporter.report(circuitBreaker, Reporter.Msg.BLOWN, circuitBreaker.getDraw());
                for (Component load: loads) {
                    load.engaged = false;
                    Reporter.report(load, Reporter.Msg.DISENGAGING);
                    load.draw = 0;
                    Reporter.report(load, Reporter.Msg.DRAW_CHANGE, delta);
                }
                this.incDraw(this.getSource(), delta);
            } else {
                this.draw += delta;
                Reporter.report(this, Reporter.Msg.DRAW_CHANGE, delta);
                source.changeDraw(delta);
            }
        } else {
            Reporter.report(this, Reporter.Msg.DRAW_CHANGE, delta);
        }
    }

    /**
     * What loads are attached under this Component?
     * @return an unmodifiable collection of this Component's loads
     */
    protected Collection<Component> getLoads() {
        return Collections.unmodifiableCollection(loads);
    }

    /**
     * Get Component's name
     * @return the name assigned in the constructor
     */
    public String getName() {
        return name;
    }

    /**
     * What Component is feeding power to this Component? Useful, for example
     * to loads that need to send changeDraw(int) to their sources.
     * @return the source
     */
    protected Component getSource() {
        return source;
    }

    /**
     * Get Component's type
     * @return the type (enum)
     */
    public Types getType() {
        return type;
    }

    /**
     * Describe a component in the manner of Reporter.identify(Component).
     * @return info about this component
     */
    @Override
    public String toString() {
        return Reporter.identify(this);
    }


}
