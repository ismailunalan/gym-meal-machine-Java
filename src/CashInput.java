/**
 * Class for accepting cash input.
 */
public class CashInput {
    /**
     * Processes cash input and returns the total amount.
     *
     * @param values The array of cash values to be processed.
     * @param path   The path to write error messages.
     * @return The total amount of cash input, or -1 if an invalid banknote is detected.
     */
    public int cashInput(String[] values, String path) {
        int total = 0;

        for (String value : values) {
            int cash = Integer.parseInt(value);
            if (cash == 1 || cash == 5 || cash == 10 || cash == 20
                    || cash == 50 || cash == 100 || cash == 200) {
                total += cash;
            } else {
                FileOutput.writeToFile(path, "Please feed one of the following valid banknotes into the machine." +
                        "Valid Banknotes : 1, 5, 10, 20, 50, 100 or 200 TL", true, true);
                System.out.println();
                return -1;
            }
        }
        return total;
    }
}
