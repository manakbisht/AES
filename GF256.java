public class GF256 {
    private final int[] irrPoly = {0,0,0,0,0,0,0,1,0,0,0,1,1,0,1,1};
    private static int[] hexToPoly(int hex) {
        int[] terms;
        terms = new int[16];
        int i=15;
        while (hex>0) {
            terms[i--]=hex%2;
            hex >>= 1;
        }
        return terms;
    }
    private static int polyToHex(int[] arr) {
        int n=arr.length;
        int ans=0;
        int pow=1;
        for (int i=n-1;i>=0;i--) {
            ans+=arr[i]*pow;
            pow*=2;
        }
        return ans;
    }
    public static int sum(int x, int y) {
       int[] xArr=hexToPoly(x);
       int[] yArr=hexToPoly(y);
       for (int i=0;i<xArr.length;i++) {
           xArr[i]^=yArr[i];
       }
       return polyToHex(xArr);
    }
    public static int multiply(int x, int y) {
        int[] xArr=hexToPoly(x);
        int[] yArr=hexToPoly(y);
        int[] prod=new int[16];
        for (int i=15;i>=8;i--) {
            int p1=15-i;
            if (xArr[i]==0) continue;
            for (int j=15;j>=8;j--) {
                if (yArr[j]!=1) continue;
                int p2=15-j;
                prod[15-(p1+p2)]+=1;
                prod[15-(p1+p2)]%=2;
            }
        }
        for (int i=0;i<prod.length;i++) System.out.println(prod[i]);
        return polyToHex(prod);
    }
    public static void main(String[] args) {
        int ans=multiply(0x53,0xca);
        System.out.println(Integer.toHexString(ans));
    }
}
