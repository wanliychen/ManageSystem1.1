package org.example;

import java.util.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomerPasswordManage {
    private Scanner scanner = new Scanner(System.in);
    private CustomerDatabase customerDatabase;

    public CustomerPasswordManage(CustomerDatabase customerDatabase) {
        this.customerDatabase = customerDatabase;
    }
    
    
    public void changePassword() {
        System.out.println("输入用户名：");
        String username = scanner.nextLine();
        System.out.println("输入旧密码：");
        String oldPassword = scanner.nextLine();
        System.out.println("输入新密码：");
        String newPassword = scanner.nextLine();

        if (isValidPassword(newPassword)) {
            if (updatePassword(username, oldPassword, newPassword)) {
                System.out.println("密码修改成功！");
            } else {
                System.out.println("旧密码不正确，密码修改失败！");
            }
        } else {
            System.out.println("新密码不符合要求，密码修改失败！");
        }
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    private boolean updatePassword(String username, String oldPassword, String newPassword) {
        Customer customer = customerDatabase.findCustomerByUsername(username);
        if (customer != null && verifyPassword(oldPassword, customer.getPassword())) {
            String hashedPassword=hashPassword(newPassword);
            customer.setPassword(hashedPassword);
            customerDatabase.updateCustomer(username, customer);
            return true;
        }
        return false;
    }

    void resetPassword(String username, String oldPassword) {
        System.out.println("输入注册邮箱：");
        String email = scanner.nextLine();
        String newPassword = generateRandomPassword();
        String hashedPassword = hashPassword(newPassword);
        if (isEmailCorrect(username, email)) {

            Customer customer = customerDatabase.findCustomerByUsername(username);
            if (customer != null) {
                customer.setPassword(hashedPassword);
                customerDatabase.updateCustomer(username, customer);
                System.out.println("密码已成功重置为： " + newPassword);
            } else {
                System.out.println("用户不存在！");
            }
            // updatePassword(username, oldPassword, hashedPassword);
            // sendPasswordToEmail(email, newPassword);
            System.out.println("新密码已发送到您的邮箱，请查收！");
        } else {
            System.out.println("用户名或邮箱不正确，密码重置失败！");
        }
    }

    private boolean isEmailCorrect(String username, String email) {
        Customer customer = customerDatabase.findCustomerByUsername(username);
        return customer != null && customer.getEmail().equals(email);
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

    private void sendPasswordToEmail(String email, String password) {
        // 模拟发送邮件功能
        System.out.println("模拟发送邮件到 " + email + "，新密码为：" + password);
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

    private static boolean verifyPassword(String inputPassword, String storedHash) {
        String hashedInput = hashPassword(inputPassword);
        return hashedInput.equals(storedHash);
    }
}
