package components;

import javax.sound.midi.SysexMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Reads the input file and interacts with the user in the terminal.
 */
public class FileReader {

    private String fileName;
    private HashMap<String, Object> circuit;
    private String[] circuitArray;
    private ArrayList<Component> componentArray;
    private PowerSource power;

    /**
     * Constructor that initializes the FileReader and creates the circuit (hash map)
     * @param fileName name of the file from command line
     */
    public FileReader(String fileName) {
        this.fileName = fileName;
        this.circuit = new HashMap<String, Object>();
        this.componentArray = new ArrayList<>();
    }

    /**
     * Scans each line in the config file and calls creating to create/attach components
     * @throws FileNotFoundException file not found
     */
    public void ReadFile(int exception) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
        while (scan.hasNext()) {
            String line = scan.nextLine();
            String[] lineSplit = line.split(" ");

            creating(lineSplit, exception);

        }
        scan.close();
        System.out.println(circuit.size() + " components created.");
    }

    /**
     * Creates/attaches a component to another component. Adds the component to the circuit
     * and the componentArray.
     * @param lineSplit (String[]) split line input from the file/user
     */
    public void creating(String[] lineSplit, int exception) {
        if (lineSplit[0].equals(Types.POWER_SOURCE.getComponentName())) {
            PowerSource component = new PowerSource(lineSplit[1]);
            Reporter.report(component, Reporter.Msg.CREATING);

            circuit.put(lineSplit[1], component);
            componentArray.add(component);
            power = component;

        } else if (lineSplit[0].equals(Types.CIRCUIT_BREAKER.getComponentName())) {
            PowerSource source = (PowerSource) searchComponent(lineSplit[2]);
            CircuitBreaker component = new CircuitBreaker(lineSplit[1], source, Integer.parseInt(lineSplit[3]));
            Reporter.report(component, Reporter.Msg.CREATING);

            circuit.put(lineSplit[1], component);
            componentArray.add(component);
            source.attach(component);

        } else if (lineSplit[0].equals(Types.OUTLET.getComponentName())) {
            CircuitBreaker source = (CircuitBreaker) searchComponent(lineSplit[2]);
            Outlet component = new Outlet(lineSplit[1], source);
            Reporter.report(component, Reporter.Msg.CREATING);

            circuit.put(lineSplit[1], component);
            componentArray.add(component);
            source.attach(component);

        } else if (lineSplit[0].equals(Types.APPLIANCE.getComponentName())) {
            Component sourceType = (Component) searchComponent(lineSplit[2]);
            if (sourceType.getType().equals(Types.OUTLET)) {
                Outlet source = (Outlet) searchComponent(lineSplit[2]);
                Appliance component = new Appliance(lineSplit[1], source, Integer.parseInt(lineSplit[3]));
                Reporter.report(component, Reporter.Msg.CREATING);

                circuit.put(lineSplit[1], component);
                componentArray.add(component);
                source.attach(component);

            } else{
                PowerSource source = (PowerSource) searchComponent(lineSplit[2]);
                Appliance component = new Appliance(lineSplit[1], source, Integer.parseInt(lineSplit[3]));
                Reporter.report(component, Reporter.Msg.CREATING);

                circuit.put(lineSplit[1], component);
                componentArray.add(component);
                source.attach(component);
            }
        } else {
            Reporter.usageError(exception);
        }
    }

    /**
     * Searches for a component based off a string value of circuitArray keys (unique name)
     * @param component String value of the unique object name for a component
     * @return (Component) of that specified circuitArray key
     */
    public Object searchComponent(String component) {
        circuitArray = convertToArray();
        for (int i = 0; i < circuitArray.length; i++) {
            if (circuitArray[i].equals(component)) {
                return circuit.get(circuitArray[i]);
            }
        }
        return null; //TODO Error??
    }

    /**
     * Converts the hashMap into an array for random accessing implementation
     * @return (array) - accessor for the HashMap to an array
     */
    public String[] convertToArray() {
        return circuit.keySet().toArray(new String[0]);
    }

    /**
     * Returns the fileName
     * @return (String) fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Allows the user to quit, display, toggle, and connect components
     * @param prompt (String) prompt for user input
     */
    public void commands(String prompt, int exception1, int exception2, int exception3) {
        Scanner scan = new Scanner(System.in);
        System.out.print(prompt + " -> ");
        String userInput = scan.nextLine();

        while (!userInput.equals("quit")) {

            String[] userInputSplit = userInput.split(" ");
            String commandName = userInputSplit[0];

            if (commandName.equals("display")) {
                power.display();
            } else if (commandName.equals("toggle")) {
                Component component = (Component) searchComponent(userInputSplit[1]);
                if (component instanceof Switch) {
                    ((Switch) component).toggle();
                } else {
                    Reporter.usageError(exception1);
                }
            } else if (commandName.equals("connect")) {
                String[] newLine = Arrays.copyOfRange(userInputSplit, 1, userInputSplit.length);
                creating(newLine, exception3);
            } else {
                Reporter.usageError(exception2);
            }

            System.out.print(prompt + " -> ");
            userInput = scan.nextLine();
        }
    }

    /**
     * Powers and engages the PowerSource
     */
    public void poweringUp() {
        // powering up & engaging PowerSource
        System.out.println("Starting up the main circuit(s).");
        Reporter.report(power, Reporter.Msg.POWERING_UP);
        power.engage();
        power.engageLoad();
    }
}
