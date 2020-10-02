public class GF256 {
    private static final int[] irrPoly = {0,0,0,0,0,0,0,1,0,0,0,1,1,0,1,1};

    public static int sum(int x, int y) {
        int[] xPoly = hexToPoly(x);
        int[] yPoly = hexToPoly(y);
        return polyToHex(add(xPoly, yPoly));
    }

    private static int[] add(int[] x, int[] y) {
        // Polynomial addition modulo 2
        for (int i=0;i<16;i++) x[i] ^= y[i];
        return x;
    }

    private static int[] hexToPoly(int hex) {
        int[] poly;
        poly = new int[16];
        int i = 15;
        while (hex>0) {
            poly[i--] = hex%2;
            hex >>= 1;
        }
        return poly;
    }

    private static int polyToHex(int[] arr) {
        int n = arr.length;
        int hex = 0;
        int pow = 1;
        for (int i=n-1;i>=0;i--) {
            hex += arr[i]*pow;
            pow *= 2;
        }
        return hex;
    }

    private static int[] multiply(int[] x, int[] y) {
        // Polynomial multiplication modulo 2
        int[] prod = new int[16];

        for (int i=15;i>=8;i--) {
            int p1 = 15-i;
            if (x[i]==0) continue;
            for (int j=15;j>=8;j--) {
                if (y[j]!=1) continue;
                int p2 = 15-j;
                prod[15-(p1+p2)] += 1;
                prod[15-(p1+p2)] %= 2;
            }
        }
        return prod;
    }

    public static int product(int x, int y) {
        // dividend modulo irreducible polynomial, x^8 + x^4 + x^3 + x + 1
        int[] xPoly = hexToPoly(x);
        int[] yPoly = hexToPoly(y);
        int[] dividend = multiply(xPoly, yPoly);
        int[] divisor = new int[16];

        int i = 0;
        while (true) {
            for (;i<16;i++) {
                if (dividend[i]==1) break;
            }
            if (i>7) break;

            for (int j=0;j<i;j++) divisor[j] = 0;
            int k = 7;
            int j=i;
            while (k<=15) {
                divisor[j++] = irrPoly[k++];
            }
            //for (;j<16;j++) divisor[j] = 0;
            add(dividend, divisor);
        }
        return polyToHex(dividend);
    }


    public static void main(String[] args) {
        int ans = product(0x53, 0xca);
        System.out.println(Integer.toHexString(ans));
    }
}