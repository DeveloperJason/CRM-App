package inventory;

/**
 * Outsourced part type
 * @author Jason Philpy
 */

public class Outsourced extends Part {

    /**
     * Company name
     */
    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Gets company name
     * @return the company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets company name
     * @param companyName the company name to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
