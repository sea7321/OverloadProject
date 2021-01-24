package components;

public enum Types {

    POWER_SOURCE("PowerSource"),
    CIRCUIT_BREAKER("CircuitBreaker"),
    OUTLET("Outlet"),
    APPLIANCE("Appliance");

    private String componentName;

    Types(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentName() {
        return componentName;
    }
}
