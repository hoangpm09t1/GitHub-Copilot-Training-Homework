public class Main {
    public static void main(String[] args) {

        // Create a long string by concatenating numbers
        StringBuilder result = new StringBuilder();

        for (int i = 0; i <= 10000; i++) {
            result.append(i);
        }

        System.out.println(result.toString());
    }
}