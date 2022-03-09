package model;

//create an outsourced class that inherits the part class

/**
 * OutSourced class which inherits the Part class.
 */
public class OutSourced extends Part{

    /**
     * The OutSourced class has a company name.
     */
    private String companyName;

    //constructor for the outsourced object

    /**
     * Constructor for the OutSourced class.
     *
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);

        this.companyName = companyName;
    }


    /**
     * Get the company name for the OutSourced object.
     *
     * @return
     */
    public String getCompanyName() {
        return companyName;
    }


    /**
     * Set the company name for the OutSourced object.
     *
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
