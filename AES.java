import java.util.Random;
public class AES {
    private final int[][] key;
    private final int nRounds;
    int[][][] roundKeys;
    private String[] encOutputs;
    private String[] decOutputs;
    private static final int[][] inverseSBox = {
            {0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
            {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
            {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
            {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
            {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92},
            {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
            {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
            {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
            {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
            {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
            {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
            {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
            {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
            {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
            {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
            {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}};

    private static final int[][] sBox = {
            {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
            {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
            {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
            {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
            {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
            {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
            {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
            {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
            {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
            {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
            {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
            {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
            {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
            {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
            {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
            {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}};

    private static final int[][] mixColTransformation = {
            {0x02, 0x03, 0x01, 0x01},
            {0x01, 0x02, 0x03, 0x01},
            {0x01, 0x01, 0x02, 0x03},
            {0x03, 0x01, 0x01, 0x02}};

    private static final int[][] inverseMixColTransformation = {
            {0x0e, 0x0b, 0x0d, 0x09},
            {0x09, 0x0e, 0x0b, 0x0d},
            {0x0d, 0x09, 0x0e, 0x0b},
            {0x0b, 0x0d, 0x09, 0x0e}};

    public AES(int keyLength) {
        switch (keyLength) {
            case 128:
                nRounds = 10;
                break;
            case 192:
                nRounds = 12;
                throw new UnsupportedOperationException();
            case 256:
                nRounds = 14;
                throw new UnsupportedOperationException();
            default:
                throw new IllegalArgumentException();
        }
        key=new int[4][4];
        generateKey(keyLength);
        System.out.println("Key generated: "+ matrToString(key));
        System.out.println("");
        roundKeys=new int[10][4][4];
        generateAllRoundKeys();
    }

    public AES (String key) {
        int keyLength = key.length();
        switch (keyLength) {
            case 32:
                nRounds = 10;
                break;
            case 48:
                nRounds = 12;
                throw new UnsupportedOperationException();
            case 64:
                nRounds = 14;
                throw new UnsupportedOperationException();
            default:
                throw new IllegalArgumentException();
        }
        // Change to support 192 bit and 256 bit keys
        this.key = convertToMatrix(key);
        roundKeys=new int[10][4][4];
        generateAllRoundKeys();
    }
    private String getHexRep(String s) {
        char[] req=s.toCharArray();
        StringBuilder strb=new StringBuilder();
        for (char c: req){
            String s1=Integer.toHexString(c);
            if (s1.length()==1) strb.append('0');
            strb.append(s1);
        }
        return strb.toString().toUpperCase();
    }
    private String hexToString(String s) {
        char[] req=new char[8];
        for (int i=0;i<s.length();i+=2) {
            char c=(char) Integer.parseInt(s.substring(i,i+2),16);
            req[i]=c;
        }
        return req.toString();
    }
    private int[][] nextRoundKey(int[][] previousRoundKey, int iRC) {
        // Reimplement to support 192 bit and 256 bit keys
        int[][] ret = new int[4][4];
        int[] g = new int[4];
        for (int i=0;i<4;i++) g[i] = previousRoundKey[i][3];
        gFunction(g, iRC);
        for (int i=0;i<4;i++) ret[i][0] = previousRoundKey[i][0]^g[i];
        for (int i=0;i<4;i++) ret[i][1] = previousRoundKey[i][1]^ret[i][0];
        for (int i=0;i<4;i++) ret[i][2] = previousRoundKey[i][2]^ret[i][1];
        for (int i=0;i<4;i++) ret[i][3] = previousRoundKey[i][3]^ret[i][2];

        return ret;
    }
    private void generateAllRoundKeys() {
        int iRC=0x01;
        roundKeys[0]=nextRoundKey(key,iRC);
        iRC=GF256.product(iRC,0x02);
        for (int i=1;i<10;i++) {
            roundKeys[i]=nextRoundKey(roundKeys[i-1],iRC);
            iRC=GF256.product(iRC,0x02);
        }
    }

    private void gFunction(int[] w, int iRC) {
        circularLeftShift(w, 1);
        for (int i=0;i<w.length;i++) {
            int val = w[i];
            w[i] = sBox[val/16][val%16];
        }
        w[0]^=iRC;

    }

    private void addRoundKey(int[][] block, int[][] roundKey) {
        for (int i=0;i<4;i++) {
            for (int j=0;j<4;j++) {
                block[i][j] ^= roundKey[i][j];
            }
        }
    }

    private void circularLeftShift(int[] arr, int mag) {
        int n = arr.length;
        int[] list = new int[mag];
        for (int i=0;i<mag;i++) {
            list[i]=arr[i];
        }
        for (int i=0;i<n-mag;i++) {
           // if (i<mag) list[i] = arr[i];
            arr[i] = arr[i+mag];
        }

        int j = 0;
        for (int i=n-mag;i<n;i++) arr[i] = list[j++];
    }

    private void circularRightShift(int[] arr, int mag) {
        int n = arr.length;
        int[] list = new int[mag];
        int k=n-1;
        for (int j=mag-1;j>=0;j--) {
            list[j]=arr[k--];
        }
        for (int i=n-1;i>=mag;i--) {
           // if (i>=n-mag) list[j--] = arr[i];
            arr[i] = arr[i-mag];
        }

        for (int i=0;i<mag;i++) arr[i] = list[i];
    }

    private int[][] convertToMatrix(String s) {
        // Reimplement to support 192 bit and 256 bit keys
        int[][] matrix=new int[4][4];
        for (int i=0;i<s.length()-1;i+=2) {
            int j = i/2;
            matrix[j%4][j/4] = Integer.parseInt(s.substring(i,i+2), 16);
        }
        return matrix;
    }
    private String matrToString(int[][] arr) {
        StringBuilder strb=new StringBuilder();
        for (int i=0;i<4;i++)
            for (int j=0;j<4;j++){
                String req=Integer.toHexString(arr[j][i]);
                if (req.length()==1) strb.append("0");
                strb.append(req);
            }

        return strb.toString().toUpperCase();
    }

    private void shiftRows(int[][] block) {
        for (int i=1;i<4;i++) circularLeftShift(block[i], i);
    }

    private void inverseShiftRows(int[][] block) {
        for (int i=1;i<4;i++) circularRightShift(block[i], i);
    }
    private int[][] matrixMultiplication(int[][] x, int[][] y) {
        int[][] result=new int[4][4];
        for (int i=0;i<4;i++) {
            for (int j=0;j<4;j++) {
                int val=0;
                for (int k=0;k<4;k++) {

                    val=GF256.sum(val,GF256.product(x[i][k],y[k][j]));
                }
                result[i][j]=val;
            }
        }
        return result;
    }
    private void mixColumns(int[][] block) {
        int[][] res=matrixMultiplication(mixColTransformation,block);
        for (int i=0;i<4;i++)
            for (int j=0;j<4;j++) block[i][j]=res[i][j];
    }

    private void inverseMixColumns(int[][] block) {
        int[][] res=matrixMultiplication(inverseMixColTransformation,block);
        for (int i=0;i<4;i++)
            for (int j=0;j<4;j++) block[i][j]=res[i][j];
    }

    private void substituteBytes(int[][] block) {
        for (int i=0;i<4;i++)
            for (int j=0;j<4;j++) {
                int val = block[i][j];
                block[i][j] = sBox[val/16][val%16];
            }
    }

    private void inverseSubstituteBytes(int[][] block) {
        for (int i=0;i<4;i++)
            for (int j=0;j<4;j++){
                int val = block[i][j];
                block[i][j] = inverseSBox[val/16][val%16];
            }
    }

    private void generateKey(int keyLength) {
        Random r=new Random();
        for (int i=0;i<16;i++) {
            int val=r.nextInt(256);
            key[i%4][i/4]=val;
        }
    }
    private void roundEncryption(int[][] plaintext,int roundNum) {
        substituteBytes(plaintext);
        shiftRows(plaintext);
        if (roundNum<10) mixColumns(plaintext);
        addRoundKey(plaintext,roundKeys[roundNum-1]);
    }
    private void roundDecryption(int[][] ciphertext,int roundNum) {
        addRoundKey(ciphertext,roundKeys[10-roundNum]);
        if (roundNum>1) inverseMixColumns(ciphertext);
        inverseShiftRows(ciphertext);
        inverseSubstituteBytes(ciphertext);
    }
    public String encrypt(String plaintext) {
        //String input=getHexRep(plaintext);
        //System.out.println(input.length());
        int[][] inputMatrix=convertToMatrix(plaintext);
        encOutputs=new String[11];
        encOutputs[0]=plaintext;
        addRoundKey(inputMatrix,key);
       // encOutputs[1]=matrToString(inputMatrix);
        for (int i=1;i<=10;i++){
            roundEncryption(inputMatrix,i);
            encOutputs[i]= matrToString(inputMatrix);
        }
        String ciphertext= matrToString(inputMatrix);
        encOutputs[10]=ciphertext;
        return ciphertext;
    }
    public String decrypt(String ciphertext) {
        decOutputs=new String[11];
        int[][] inputMatrix=convertToMatrix(ciphertext);
        //addRoundKey(inputMatrix,roundKeys[9]);
        decOutputs[0]=ciphertext;
        for (int i=1;i<=10;i++){
            roundDecryption(inputMatrix,i);
            decOutputs[i]= matrToString(inputMatrix);
        }
        addRoundKey(inputMatrix,key);
        String plaintext= matrToString(inputMatrix);
        decOutputs[10]=plaintext;
        return plaintext;
    }
    public void displayRounds() {
        System.out.println("Encryption input (plaintext): "+encOutputs[0]);
        System.out.println("Decryption round 10 (plaintext): "+decOutputs[10]);
        System.out.println("Round key: "+matrToString(key));
        if (!encOutputs[0].equals(decOutputs[10])) {
            System.out.println("ERROR!");
            System.exit(0);
        }
        else System.out.println("MATCH SUCCESSFUL!");
        System.out.println("");
        for (int i=1;i<=9;i++) {
            System.out.println("Encryption round "+i+": "+encOutputs[i]);
            System.out.println("Decryption round "+(10-i)+": "+decOutputs[10-i]);
            System.out.println("Round key: "+matrToString(roundKeys[i-1]));
            if (!(encOutputs[i].equals(decOutputs[10-i]))) {
                System.out.println("ERROR!");
                System.exit(0);
            }
            else System.out.println("MATCH SUCCESSFUL!");
            System.out.println("");
        }
        System.out.println("Encryption round 10 (ciphertext): "+encOutputs[10]);
        System.out.println("Decryption input (ciphertext): "+decOutputs[0]);
        System.out.println("Round key: "+matrToString(roundKeys[9]));
        if (!encOutputs[10].equals(decOutputs[0])) {
            System.out.println("ERROR!");
            System.exit(0);
        }
        else System.out.println("MATCH SUCCESSFUL!");
        System.out.println("");
        System.out.println("VERIFIED: All rounds are functioning properly.");
    }
}