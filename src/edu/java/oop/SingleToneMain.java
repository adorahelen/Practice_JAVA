package edu.java.oop;

public class SingleToneMain {
    public static void main(String[] args) {


//        Singleton s1 = new Singleton();


        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        Singleton s3 = Singleton.getInstance();

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
//        System.out.println(Singleton());

    }
}
