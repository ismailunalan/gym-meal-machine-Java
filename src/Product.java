import java.util.ArrayList;
import java.util.List;

/**
 * The Product class represents a product in the inventory.
 * Each product has a name, price, nutritional information, calorie value, and stock quantity.
 */
public class Product {
    private String productName;
    private float productPrice;
    private float productProtein;
    private float productCarbohydrate;
    private float productFat;
    private float productCalorie;
    private int productStock;

    /**
     * Default constructor for the Product class.
     */
    public Product() {
    }

    /**
     * Constructor for creating a Product object with specified properties.
     * @param productName       The name of the product.
     * @param productPrice      The price of the product.
     * @param productProtein    The protein content of the product.
     * @param productCarbohydrate   The carbohydrate content of the product.
     * @param productFat        The fat content of the product.
     * @param productSlotNumber The slot number of the product in the inventory.
     */
    public Product(String productName, float productPrice, float productProtein, float productCarbohydrate, float productFat, int productSlotNumber) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productProtein = productProtein;
        this.productCarbohydrate = productCarbohydrate;
        this.productFat = productFat;
        productCalorie = (4 * productProtein) + (4 * productCarbohydrate) + (9 * productFat);
    }

    // Getters and setters for product properties

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductStock(int amount) { productStock += amount; }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductProtein(float productProtein) {
        this.productProtein = productProtein;
    }

    public void setProductCarbohydrate(float productCarbohydrate) {
        this.productCarbohydrate = productCarbohydrate;
    }

    public void setProductFat(float productFat) {
        this.productFat = productFat;
    }

    public void setProductCalorie(float productCalorie) {
        this.productCalorie = productCalorie;
    }

    public String getProductName() {
        return productName;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public float getProductProtein() {
        return productProtein;
    }

    public float getProductCarbohydrate() {
        return productCarbohydrate;
    }

    public float getProductFat() {
        return productFat;
    }

    public float getProductCalorie() {
        return productCalorie;
    }

    public int getProductStock() { return productStock; }

    /**
     * Reads product information from a file and adds products to the inventory.
     * @param args The arguments passed to the method.
     * @return A list of products added to the inventory.
     */
    public static List<Product> addProduct(String[] args) {
        List<Product> productList = new ArrayList<>();
        String[] lines = FileInput.readFile(args[0], false, false);
         //Reads the text file line by line and adds products to the slots.
        for (String line : lines) {
            String[] infos = line.split("\t");
            String productName = infos[0];
            float productPrice = Float.parseFloat(infos[1]);
            String[] nutrientInfos = infos[2].split(" ");
            float productProtein = Float.parseFloat(nutrientInfos[0]);
            float productCarbohydrate = Float.parseFloat(nutrientInfos[1]);
            float productFat = Float.parseFloat(nutrientInfos[2]);
            int slotNumber = 1 + productList.size();
            Product foundProduct = null;
            boolean found = false;
            //Checks whether the product has been added previously.
            for (Product existingProduct : productList) {
                if (existingProduct.getProductName().equals(productName)) {
                    foundProduct = existingProduct;
                    found = true;
                }
            }
            //Increases the stock if the product has been previously added.
            if(found) {
                if (foundProduct.getProductStock() < 10) {
                    foundProduct.setProductStock(1);
                } else {
                    //Adds a product to an empty slot if the machine is not full.
                    if (productList.size() < 24) {
                        Product product = new Product(productName, productPrice, productProtein, productCarbohydrate, productFat, slotNumber);
                        product.setProductStock(1);
                        productList.add(product);
                    } else {
                        boolean stocksFull = true;
                        //Checks if all slots are full.
                        for (Product product : productList) {
                            if (product.getProductStock() < 10) {
                                stocksFull = false;
                            }
                        }
                        //It gives information message depending on whether the slots are full or not.
                        if (!stocksFull){
                            FileOutput.writeToFile(args[2], "INFO: There is no available place to put " + productName,true,true);
                        }else {
                            FileOutput.writeToFile(args[2], "The machine is full",true,true);
                            break;
                        }
                    }
                }
            }else {
                //Adds a product to an empty slot if the machine is not full.
                if (productList.size() < 24) {
                    Product product = new Product(productName, productPrice, productProtein, productCarbohydrate, productFat, slotNumber);
                    product.setProductStock(1);
                    productList.add(product);
                } else {
                    boolean stocksFull = true;
                    //Checks if all slots are full.
                    for (Product product : productList) {
                        if (product.getProductStock() < 10) {
                            stocksFull = false;
                        }
                    }
                    //It gives information message depending on whether the slots are full or not.
                    if (!stocksFull){
                        FileOutput.writeToFile(args[2], "INFO: There is no available place to put " + productName,true,true);
                    }else {
                        FileOutput.writeToFile(args[2], "INFO: There is no available place to put " + productName,true,true);
                        FileOutput.writeToFile(args[2], "INFO: The machine is full!",true,true);
                        break;
                    }
                }
            }
        }
        return productList;
    }

}
