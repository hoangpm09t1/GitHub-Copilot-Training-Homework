# Plan: Exercise 5 - StringUtils Utility Class (Day 2)

## Context
Day 2 Exercise 5 requires a reusable utility class following the standard Java utility pattern (final class, private constructor, static methods). No new dependencies needed.

## Target File
Create: `src/main/java/training/copilot/homework/util/StringUtils.java`

## Implementation

```java
package training.copilot.homework.util;

public final class StringUtils {

    private StringUtils() {}

    /**
     * Checks if a string is a palindrome, ignoring case and non-alphanumeric characters.
     *
     * Test cases:
     *   isPalindrome("racecar")        → true
     *   isPalindrome("A man a plan a canal Panama") → true
     *   isPalindrome("hello")          → false
     *   isPalindrome("Was it a car or a cat I saw?") → true
     *   isPalindrome("")               → true
     *   isPalindrome(null)             → false
     */
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
```

## Key Points
- `final` class — prevents subclassing
- `private` constructor — prevents instantiation
- `static` method — no instance needed
- Strips non-alphanumeric chars with regex `[^a-z0-9]` after lowercasing
- Two-pointer approach — O(n) time, O(n) space (for cleaned string)
- `null` input returns `false` (safe guard)

## Verification
1. Compile: `mvnw.cmd compile`
2. Manual check: call `StringUtils.isPalindrome("racecar")` → `true`
