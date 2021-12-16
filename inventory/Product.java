package inventory;

import javafx.collections.ObservableList;

/**
 * Product class using Part class
 * @author Jason Philpy
 */
public class Product extends Part {

    /**
     * List of parts for the product
     */
    private ObservableList<Part> associatedParts;

    public Product(int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
    }
    /**
     * Sets the list of parts
     * @param parts (list) to set
     */
    public void setAssociatedParts(ObservableList<Part> parts) {
        associatedParts = parts;
    }

    /**
     * Adds a part to the list of parts
     * @param part (individual) to set
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes the part from the list of parts
     * @return the success value of part deletion
     * @param part part to delete
     */
    public boolean deleteAssociatedPart(Part part) {
        return associatedParts.remove(part);
    }

    /**
     * Gets list of associated parts
     * @return the associatedParts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

}
