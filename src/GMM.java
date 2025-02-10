import java.util.List;

/**
 * GMM (Gym Meal Machine) class represents the main functionality of a vending machine for gym meals.
 */
public class GMM {

    /**
     * Displays the current status of the vending machine.
     *
     * @param productList The list of products in the vending machine.
     * @param args        The arguments containing file paths.
     */
    public static void machineStatus(List<Product> productList, String[] args) {
        FileOutput.writeToFile(args[2], "-----Gym Meal Machine-----", true, true);
        int count = 0;
        for (Product product : productList) {
            if (product.getProductName() == null) {
                FileOutput.writeToFile(args[2], "___(0, 0)___", true, false);
                count++;
                continue;
            }
            if (count % 4 == 0 && count != 0) {
                FileOutput.writeToFile(args[2], "", true, true);
            }
            int calorieInt = Math.round(product.getProductCalorie());
            FileOutput.writeToFile(args[2], product.getProductName() + "(" + calorieInt + ", " + product.getProductStock() + ")___", true, false);
            count++;
        }
        if (productList.size() < 24) {
            for (int i = productList.size(); i < 24; i++) {
                if (count % 4 == 0 && count != 0) {
                    FileOutput.writeToFile(args[2], "", true, true);
                }
                FileOutput.writeToFile(args[2], "___(0, 0)___", true, false);
                count++;
            }
        }
        FileOutput.writeToFile(args[2], "\n----------\n", true, false);
    }

    /**
     * Simulates the process of buying a product from the GMM.
     *
     * @param args The arguments containing file paths.
     */
    public static void productBuy(String[] args) {
        FileOutput.writeToFile(args[2], "", false, false);
        GMM gmm = new GMM();
        CashInput cashInput = new CashInput();
        List<Product> productList = Product.addProduct(args);
        machineStatus(productList, args);
        String[] lines = FileInput.readFile(args[1], false, false);
        //Makes product purchase by reading line by line.
        for (String line : lines) {
            FileOutput.writeToFile(args[2], "INPUT: " + line, true, true);
            String[] infos = line.split("\t");
            String[] values = infos[1].split(" ");
            int cash = cashInput.cashInput(values, args[2]);
            String selectionMethod = infos[2].split(" ")[0];
            int selectionValue = Integer.parseInt(infos[3]);
            Product selectedProduct = (Product) gmm.productSelector(selectionMethod, selectionValue, productList, cash, args);
            if (selectedProduct != null) {
                if (gmm.eligibilityCheck(cash, selectedProduct, args)) {
                    FileOutput.writeToFile(args[2], "PURCHASE: You have bought one " + selectedProduct.getProductName(), true, true);
                    selectedProduct.setProductStock(-1);
                    int change = (int) gmm.changeCalculator(cash, selectedProduct);
                    FileOutput.writeToFile(args[2], "RETURN: Returning your change: " + change + " TL", true, true);
                    if (selectedProduct.getProductStock() == 0) {
                        productRemover(selectedProduct);
                    }
                }
            }
        }
        machineStatus(productList, args);
    }

    /**
     * Removes a product from the GMM.
     *
     * @param removedProduct The product to be removed.
     */
    public static void productRemover(Product removedProduct) {
        removedProduct.setProductStock(-10);
        removedProduct.setProductCalorie(-10);
        removedProduct.setProductProtein(-10);
        removedProduct.setProductCarbohydrate(-10);
        removedProduct.setProductFat(-10);
        removedProduct.setProductName(null);
        removedProduct.setProductPrice(-10);
    }

    /**
     * Selects a product based on the selection method and value provided.
     *
     * @param selectionMethod The method used for selecting the product.
     * @param selectionValue  The value used for selection.
     * @param productList     The list of products in the GMM.
     * @param cash            The amount of money loaded by customer.
     * @param args            The arguments containing file paths.
     * @return The selected product.
     */
    public Product productSelector(String selectionMethod, int selectionValue, List<Product> productList, int cash, String[] args) {
        switch (selectionMethod) {
            case "NUMBER":
                if (selectionValue < productList.size()) {
                    return productList.get(selectionValue);
                } else {
                    if (selectionValue < 24) {
                        FileOutput.writeToFile(args[2], "INFO: This slot is empty, your money will be returned.\n" +
                                "RETURN: Returning your change: " + cash + " TL", true, true);
                        return null;
                    } else {
                        FileOutput.writeToFile(args[2], "INFO: Number cannot be accepted. Please try again with another number.\n" +
                                "RETURN: Returning your change: " + cash + " TL", true, true);
                        return null;
                    }
                }
            case "CALORIE":
                for (Product product : productList) {
                    if (Math.abs(product.getProductCalorie() - selectionValue) <= 5) {
                        return product;
                    }
                }
                FileOutput.writeToFile(args[2], "INFO: Product not found, your money will be returned.\n" +
                        "RETURN: Returning your change: " + cash + " TL", true, true);
                return null;
            case "PROTEIN":
                for (Product product : productList) {
                    if (Math.abs(product.getProductProtein() - selectionValue) <= 5) {
                        return product;
                    }
                }
                FileOutput.writeToFile(args[2], "INFO: Product not found, your money will be returned.\n" +
                        "RETURN: Returning your change: " + cash + " TL", true, true);
                return null;
            case "CARB":
                for (Product product : productList) {
                    if (Math.abs(product.getProductCarbohydrate() - selectionValue) <= 5) {
                        return product;
                    }
                }
                FileOutput.writeToFile(args[2], "INFO: Product not found, your money will be returned.\n" +
                        "RETURN: Returning your change: " + cash + " TL", true, true);
                return null;
            case "FAT":
                for (Product product : productList) {
                    if (Math.abs(product.getProductFat() - selectionValue) <= 5) {
                        return product;
                    }
                }
                FileOutput.writeToFile(args[2], "INFO: Product not found, your money will be returned.\n" +
                        "RETURN: Returning your change: " + cash + " TL", true, true);
                return null;
        }
        return null;
    }

    /**
     * Checks if the selected product is eligible for purchase.
     *
     * @param cash           The amount of money loaded by customer.
     * @param selectedProduct The selected product.
     * @param args           The arguments containing file paths.
     * @return True if the product is eligible for purchase, false otherwise.
     */
    public boolean eligibilityCheck(int cash, Product selectedProduct, String[] args) {
        boolean eligibility = false;
        if (selectedProduct.getProductPrice() <= cash) {
            if (selectedProduct.getProductStock() > 0) {
                eligibility = true;
            } else {
                FileOutput.writeToFile(args[2], "INFO: This slot is empty, your money will be returned.\n" +
                        "RETURN: Returning your change: " + cash + " TL", true, true);
                return eligibility;
            }
        } else {
            FileOutput.writeToFile(args[2], "INFO: Insufficient money, try again with more money.\n" +
                    "RETURN: Returning your change: " + cash + " TL", true, true);
            return eligibility;
        }
        return eligibility;
    }

    /**
     * Calculates the change to be returned to the user.
     *
     * @param cash           The amount of money loaded by customer.
     * @param selectedProduct The selected product.
     * @return The change to be returned.
     */
    public float changeCalculator(int cash, Product selectedProduct) {
        return cash - selectedProduct.getProductPrice();
    }
}
