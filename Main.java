import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter input filename: ");
        StringBuilder f_addr=new StringBuilder();
        f_addr.append("./files/");
        String f_name=br.readLine();
        f_addr.append(f_name);
        String path=f_addr.toString();
        File f;
        f=new File(path);
        boolean f_exists=f.exists();
        if (!f_exists) {
            System.out.println("This file does not exist. FILE CREATION begins....");
            randomHexInput(path);
            f=new File(path);
        }
        else System.out.println("File found!");
        br=new BufferedReader(new FileReader(path));
        int numInputs=0;
        while (true) {
            String plaintext=br.readLine();
            if (plaintext==null) break;
            numInputs++;
            System.out.println("----INPUT "+numInputs+" processing begins----");
            AES obj = new AES(128);
            if (plaintext.length()>32) plaintext=plaintext.substring(0,32);
            else if (plaintext.length()==0) break;
            String ciphertext=obj.encrypt(plaintext);
            String retVal=obj.decrypt(ciphertext);
            obj.displayRounds();
            System.out.println("----INPUT "+numInputs+" processing ends----");
            System.out.println("");
        }
        System.out.println("############## END ##############");
    }
    public static void randomHexInput(String s) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(s, "UTF-8");
        Random r=new Random();
        int num1=r.nextInt(10)+3;
        System.out.println("Generating new input file with random text...");
        for (int i=0;i<num1;i++) {
            StringBuilder strb=new StringBuilder();
            for (int j=0;j<16;j++) {
                int num2= r.nextInt(256);
                String str=Integer.toHexString(num2);
                if (str.length()==1) strb.append("0");
                strb.append(str);
            }
            String req=strb.toString().toUpperCase();
            writer.println(req);
        }
        writer.close();
    }
}