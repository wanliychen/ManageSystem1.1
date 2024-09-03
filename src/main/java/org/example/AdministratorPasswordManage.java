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


     // 生成随机密码
    private String generateRandomPassword() {
        String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
        String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String DIGITS = "0123456789";
        String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        
    
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // 确保密码中包含至少一个小写字母
        password.append(LOWERCASE_CHARS.charAt(random.nextInt(LOWERCASE_CHARS.length())));

        // 确保密码中包含至少一个大写字母
        password.append(UPPERCASE_CHARS.charAt(random.nextInt(UPPERCASE_CHARS.length())));

        // 确保密码中包含至少一个数字
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));

        // 确保密码中包含至少一个特殊字符
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        // 填充剩余的密码长度
        for (int i = 4; i < 10; i++) {
            String allChars = LOWERCASE_CHARS + UPPERCASE_CHARS + DIGITS + SPECIAL_CHARS;
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // 将密码字符随机打乱
        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = random.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }

        return new String(passwordArray);
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
