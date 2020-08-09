package com.company;
import java.math.BigInteger;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BigInteger[] pub = new BigInteger[4];
        BigInteger[] sign = new BigInteger[2];
        Scanner scan = new Scanner(System.in);
        String input; String path = "E:\\lab7.txt";
        System.out.println("gen - сгенерировать ключи\nsign - сгенерировать подпись\ncheck - проверить подпись");
        while(true) { input = scan.nextLine();
            if (input.equals("gen"))
                pub = Schnorr.generateKeys();
            else if (input.equals("sign"))
                sign = Schnorr.Sign(path);
            else if (input.equals("check"))
                Schnorr.check(path, sign);
            if (input.equals("exit"))
                break;
        }
    }
}
