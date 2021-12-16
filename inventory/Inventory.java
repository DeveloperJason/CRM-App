package inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Primary inventory class.  Serves as model.
 * @author Jason Philpy
 */
public class Inventory {
    /**
     * List of all inventory parts
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * List of all inventory products
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Index for assigning unique part id
     */
    private static int partIndex = 0;

    /**
     * Index for assigning unique product id
     */
    private static int productIndex = 0;

    /**
     * Add part to the allParts list
     * @param newPart to add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Add product to the all Prodcuts list
     * @param newProduct to add
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Find part based on it's id
     * @param partId to identify part
     * @return the part identifed
     */
    public static Part lookupPart(int partId) {
        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * Find product based on it's id
     * @param productId to identify product
     * @return the product identified
     */
    public static Product lookupProduct(int productId) {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Get all parts that start with partName
     * RUNTIME ERROR - Originally had the parts list declared without an assignment which caused a null exception when trying to add a part. By assigning an empty list instead, the function became usable.
     * @param partName string to identify parts
     * @return parts identified
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().startsWith(partName)) {
                parts.add(part);
            }
        }
        return parts;
    }

    /**
     * Get all products that start with productName
     * @param productName string to identify products
     * @return products identified
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().startsWith(productName)) {
                products.add(product);
            }
        }
        return products;
    }

    /**
     * Update a part from allParts at a specified index
     * @param index list location for part
     * @param selectedPart to update
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Update a product from allProducts at a specified index
     * @param index list location for product
     * @param newProduct to update
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Delete a part from allParts
     * @param selectedPart to delete
     * @return the success value of part deletion
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Delete a product from allProducts
     * @param selectedProduct to delete
     * @return the success value of part deletion
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Get list of all parts
     * @return list of all parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Get list of all products
     * @return list of all products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Gets a unique number for a part id
     * @return incremented number for a unique part id
     */
    public static int getPartId() {
        partIndex++;
        return partIndex;
    }

    /**
     * Gets a unique number for a product id
     * @return incremented number for a unique product id
     */
    public static int getProductId() {
        productIndex++;
        return productIndex;
    }

}
