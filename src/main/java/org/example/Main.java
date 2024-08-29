package org.example;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("欢迎使用购物管理系统");

// //   文件路径
//         String CUSTOMER_FILE = "customers.txt";

//         // 创建一个空的用户列表
//         List<Customer> emptyCustomers = new ArrayList<>();

//         // 将空的用户列表写入文件
//         try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CUSTOMER_FILE))) {
//             oos.writeObject(emptyCustomers);
//             System.out.println("customers.dat 文件中的所有数据已删除。");
//         } catch (IOException e) {
//             System.err.println("Error deleting customers from file: " + e.getMessage());
//         }



        // 初始化数据库和用户
        Administrator admin=new Administrator();
        

        admin.insertDefaultAdmin();
        
        CustomerRegister customerRegister=new CustomerRegister();
        CustomerLogin customerLogin=new CustomerLogin();
        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                displayMenu();

                int choice = getUserChoice(scanner);

                switch (choice) {
                    case 1:
                        handleAdminLogin(scanner, admin);
                        break;
                    case 2:
                        customerRegister.run();
                        break;
                    case 3:
                        customerLogin.run();
                        break;
                    case 4:
                        System.out.println("退出系统");
                        return; // 退出主循环
                    default:
                        System.out.println("无效的选择，请重新输入！");
                }
            }
        } finally {
            scanner.close(); // 确保 Scanner 被关闭
        }
    }

    private static void displayMenu() {
        System.out.println("请选择操作：");
        System.out.println("1. 管理员登录");
        System.out.println("2. 用户注册");
        System.out.println("3. 用户登录");
        System.out.println("4. 退出");
    }

    private static int getUserChoice(Scanner scanner) {
        while (true) {
            try {
                System.out.print("请输入选项：");
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("无效的输入，请输入一个数字！");
                scanner.next(); // 清除错误的输入
            }
        }
    }

    private static void handleAdminLogin(Scanner scanner, Administrator admin) {
        if (admin.loginAdmin(scanner)) {
            System.out.println("管理员登录成功！");
            AdministratorAction adminAction = new AdministratorAction();
            adminAction.run();
        } else {
            System.out.println("管理员登录失败！");
        }
    }

    
} 
