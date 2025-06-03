//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package info.qianqiu.ashechoes.utils.id;

import java.security.MessageDigest;

public class MD5 {
    private static final String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public MD5() {
    }

    public static String byteArrayToString(byte[] b) {
        StringBuffer bths = new StringBuffer();

        for(int i = 0; i < b.length; ++i) {
            bths.append(byteToHexString(b[i]));
        }

        return bths.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String ToMD5(String origin) {
        String resultString = null;

        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToString(md.digest(resultString.getBytes()));
        } catch (Exception var3) {
        }

        return resultString;
    }
}
