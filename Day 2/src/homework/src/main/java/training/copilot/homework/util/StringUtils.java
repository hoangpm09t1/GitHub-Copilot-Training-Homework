package training.copilot.homework.util;

public final class StringUtils {

    private StringUtils() {}

    /**
     * Checks if a string is a palindrome, ignoring case and non-alphanumeric characters.
     *
     * Test cases:
     *   isPalindrome("racecar")                      → true
     *   isPalindrome("A man a plan a canal Panama")  → true
     *   isPalindrome("hello")                        → false
     *   isPalindrome("Was it a car or a cat I saw?") → true
     *   isPalindrome("")                             → true
     *   isPalindrome(null)                           → false
     */
    // Test: isPalindrome("racecar")                      → true
    // Test: isPalindrome("A man a plan a canal Panama")  → true
    // Test: isPalindrome("hello")                        → false
    // Test: isPalindrome("Was it a car or a cat I saw?") → true
    // Test: isPalindrome("")                             → true
    // Test: isPalindrome(null)                           → false
    public static boolean isPalindrome(String str) {
        if (str == null) return false;
        String cleaned = str.toLowerCase().replaceAll("[^a-z0-9]", "");
        int left = 0, right = cleaned.length() - 1;
        while (left < right) {
            if (cleaned.charAt(left) != cleaned.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}
