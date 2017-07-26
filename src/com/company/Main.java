package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an option: Server or Client");
        while (true) {
            String answer = scanner.nextLine();
            if (answer.equals("server")) {
                new Server();
                break;
            } else if (answer.equals("client")) {
                new Client();
                break;
            } else {
                System.out.println("Incorrect input. Try again");
            }
        }
    }
}
