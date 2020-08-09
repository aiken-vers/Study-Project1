package com.company;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class Schnorr {
    private static BigInteger[] publicKeys = new BigInteger[4];
    private static BigInteger[] sign = new BigInteger[2];
    private static Random random = new Random();
    private static BigInteger two = new BigInteger("2");
    private static BigInteger p, q, g, y, w, a,h;

    public static BigInteger[] generateKeys() {
        System.out.println("Генерация ключей...");
        q = BigInteger.probablePrime(160, random);
        //q должен быть делителем числа p-1
        BigInteger k = BigInteger.probablePrime(96, random);
        do {
            k = k.add(BigInteger.ONE);
            p = (q.multiply(k)).add(BigInteger.ONE);
            //генерайи числа длинной 256 бит
        } while (!p.isProbablePrime(100));
        //проверяем что числа простое
        do {
            h = two;
            BigInteger pow = (p.subtract(BigInteger.ONE)).divide(q);
            g = g.modPow(pow, p);
            if (g.compareTo(BigInteger.ONE) != 0)
                break;
        } while(g.compareTo(BigInteger.ONE)==0);
        w = new BigInteger(128, random);
        y = g.modPow(w.negate(), p);
        System.out.println("Открытый ключ: \np= "+p);
        System.out.println("q= "+q);
        System.out.println("g= "+g);
        System.out.println("y= "+y);
        System.out.println("Секретный ключ: \nw= "+w);
        publicKeys[0]=p;
        publicKeys[1]=q;
        publicKeys[2]=g;
        publicKeys[3]=y;
        return publicKeys;
    }

    public static BigInteger[] Sign(String path){
        try{
            BigInteger r = new BigInteger(128, random);
            BigInteger x = g.modPow(r, p);

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(Files.readAllBytes(Paths.get(path)));
            md5.update(x.toString().getBytes());
            byte[] digest = md5.digest();
            System.out.println("md5="+Arrays.toString(digest));
            BigInteger s1 = new BigInteger(1, digest);
            System.out.println("s1="+s1);
            BigInteger s2 = r.add(w.multiply(s1).mod(q));
            System.out.println("s2="+s2);
            sign[0]=s1;
            sign[1]=s2;
            return sign;
        }
        catch(NoSuchAlgorithmException e) {
            System.out.println("MD5 not found");
            return null;
        }
        catch(IOException i) {
            System.out.println("File not found");
            return null;
        }
    }
    public static void check(String path, BigInteger[] sign){
        try{
            BigInteger X1=g.modPow(sign[1], p);
            BigInteger X2=y.modPow(sign[0],p);
            BigInteger X=X1.multiply(X2).mod(p);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(Files.readAllBytes(Paths.get(path)));
            md5.update(X.toString().getBytes());
            byte[] digest2 = md5.digest();
            BigInteger H = new BigInteger(1, digest2);
            System.out.println("s="+sign[0]);
            System.out.println("H="+H);
            if(H.equals(sign[0]))
                System.out.println("Подпись действительна");
            else
                System.out.println("Подпись недействительна");
        }
        catch(NoSuchAlgorithmException e) {
            System.out.println("MD5 not found");
        }
        catch(IOException i) {
            System.out.println("File not found");
        }
    }
}
