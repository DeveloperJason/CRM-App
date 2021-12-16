package inventory;

/**
 * InHouse part type
 * @author Jason Philpy
 */
public class InHouse extends Part {

    /**
     * Machine ID
     */
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Sets machine ID
     * @param machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Gets machine ID
     * @return the machine id
     */
    public int getMachineId() {
        return machineId;
    }
}
