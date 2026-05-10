import java.util.HashMap;
import java.util.Map;

public class FibonacciMemo {
    public static void main(String[] args) {
        int n = 10;
        long result = fib(n);
        System.out.println("Fibonacci(" + n + ") = " + result);
    }

    /**
     * Calculates the nth Fibonacci number using recursion with memoization.
     *
     * Example test cases:
     * - n = 0 should return 0 because the first Fibonacci number is 0.
     * - n = 1 should return 1 because the second Fibonacci number is 1.
     * - n = 10 should return 55, which verifies normal recursive behavior and memoization.
     *
     * These tests can be run by calling fib(n) for each value and comparing the result to the expected output.
     */
    public static long fib(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative");
        }
        Map<Integer, Long> memo = new HashMap<>();
        return fibMemo(n, memo);
    }

    private static long fibMemo(int n, Map<Integer, Long> memo) {
        if (n <= 1) {
            return n;
        }
        if (memo.containsKey(n)) {
            return memo.get(n);
        }
        long result = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
        memo.put(n, result);
        return result;
    }
}
