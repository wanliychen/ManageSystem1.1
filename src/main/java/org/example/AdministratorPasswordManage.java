package org.example;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

public class AdministratorPasswordManage {
    private static final String ADMIN_FILE = "admins.txt";
    private static final String CUSTOMER_FILE = "customers.txt";

    private CustomerDatabase customerDatabase; 
    
    private Scanner scanner = new Scanner(System.in);

    public AdministratorPasswordManage(CustomerDatabase customerDatabase) {
        this.customerDatabase = customerDatabase;
    }

    public void run() {
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    setAdminPassword();
                    break;
                case 2:
                    resetCustomerPassword();
                    break;
                case 3:
                    System.out.println("退出管理员密码管理");
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    private void displayMenu() {
        System.out.println("请选择操作：");
        System.out.println("1. 修改管理员密码");
        System.out.println("2. 重置用户密码");
        System.out.println("3. 退出管理员密码管理");
    }

    public void setAdminPassword() {
        System.out.println("输入用户名：");
        String username = scanner.nextLine();
        System.out.println("输入密码：");
        String password = scanner.nextLine();
        System.out.println("输入新密码：");
        String newPassword = scanner.nextLine();

        File inputFile = new File(ADMIN_FILE);
        File tempFile = new File("tempAdmins.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    writer.write(username + ":" + newPassword);
                    found = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

            if (!found) {
                System.out.println("用户名或密码错误！");
            } else {
                System.out.println("密码已成功修改！");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Delete old file and rename temp file
        inputFile.delete();
        tempFile.renameTo(inputFile);
    }

    public void resetCustomerPassword() {
        System.out.println("输入用户名：");
        String username = scanner.nextLine();
        String newPassword = generateRandomPassword();
        String hashedPassword = hashPassword(newPassword);

        Customer customer = customerDatabase.findCustomerByUsername(username);
        if (customer != null) {
            customer.setPassword(hashedPassword);
            customerDatabase.updateCustomer(username, customer);
            System.out.println("密码已成功重置为： " + newPassword);
        } else {
            System.out.println("用户不存在！");
        }
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder password = new StringBuilder();
        Random rnd = new Random();
        while (password.length() < 8) {
            int index = (int) (rnd.nextFloat() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
}
