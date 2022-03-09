package model;


/**
 * InHouse class which inherits the Part class.
 */
public class InHouse extends Part{

    /**
     * InHouse parts have an integer machine ID.
     */
    private int machineId;


    /**
     * Constructor for the InHouse class.
     *
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);

        this.machineId = machineId;
    }


    /**
     * Set the machine ID for the InHouse object.
     * @param machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }


    /**
     * Get the machine ID for the in-house object.
     *
     * @return
     */
    public int getMachineId() {
        return machineId;
    }
}
