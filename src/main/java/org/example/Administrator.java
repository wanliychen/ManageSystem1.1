package org.example;

import java.io.*;
import java.util.*;

public class Administrator {

    private static final String ADMIN_FILE = "admins.txt";
   
    
    boolean isLogin;
    String adminPassword;

    // 插入默认管理员用户名和密码的方法
    public void insertDefaultAdmin() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMIN_FILE, true))) {
            writer.write("admin:ynuinfo#777"); // 默认用户名和密码
            writer.newLine();
            System.out.println("默认管理员用户名和密码已成功插入到文件中！");
        } catch (IOException e) {
            System.out.println("插入默认管理员用户名和密码失败: " + e.getMessage());
        }
    }

    // 登录管理员
    public boolean loginAdmin(Scanner scanner) {
        System.out.println("输入用户名:");
        String username = scanner.next();
        System.out.println("输入密码:");
        String password = scanner.next();

        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    isLogin = true;
                    adminPassword = password;
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // 登出管理员
    public void logoutAdmin() {
        isLogin = false;
    }

}

