package info.qianqiu.ashechoes.utils.string;

import java.util.List;

public class StringUtils {

    public static boolean isEmpty(final CharSequence str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(final CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(final CharSequence str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotBlank(final CharSequence str) {
        return !isBlank(str);
    }

    public static boolean isNumeric(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        return regionMatches(cs1, cs2, cs1.length());
    }

    static boolean regionMatches(final CharSequence cs,
                                 final CharSequence substring, final int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(true, 0, (String) substring, 0, length);
        }
        int index1 = 0;
        int index2 = 0;
        int tmpLen = length;

        // Extract these first so we detect NPEs the same as the java.lang.String version
        final int srcLen = cs.length();
        final int otherLen = substring.length();

        // Check for invalid parameters
        if (length < 0) {
            return false;
        }

        // Check that the regions are long enough
        if (srcLen < length || otherLen < length) {
            return false;
        }

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            // The real same check as in String.regionMatches():
            final char u1 = Character.toUpperCase(c1);
            final char u2 = Character.toUpperCase(c2);
            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                return false;
            }
        }

        return true;
    }

    public static String join(List<String> strs, String s) {
        StringBuilder e = new StringBuilder();
        for (String ss : strs) {
            e.append(ss).append(s);
        }
        return e.substring(0, e.length() - s.length());
    }
}
