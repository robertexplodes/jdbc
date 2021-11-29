package main;

import domain.Kunde;

public class Main {
    public static void main(String[] args) {
        var s = new Kunde(1, "null", "df");
        System.out.println(s.getId());
    }
}
