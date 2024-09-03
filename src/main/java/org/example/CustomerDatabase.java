package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerDatabase {
    private static final String CUSTOMER_FILE = "customers.txt";
    public List<Customer> customerList;

    public CustomerDatabase(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public static List<Customer> loadCustomersFromFile() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(CUSTOMER_FILE);
        
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 6) {
                        Customer customer = new Customer(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                        customers.add(customer);
                    }
                }
            } catch (IOException e) {
                System.err.println("加载用户文件时出错: " + e.getMessage());
            }
        }
        return customers;
    }

    public static void saveCustomersToFile(List<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            System.out.println("客户列表为空，无法保存！");
            return;
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
            for (Customer customer : customers) {
                writer.write(customer.getUsername() + ";" + customer.getPassword() + ";" + customer.getEmail() + ";" +
                        customer.getPhone() + ";" + customer.getRegistrationDate() + ";" + customer.getUserLevel());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("保存用户到文件时出错: " + e.getMessage());
        }
    }

    public void addCustomer(Customer customer) {
        customerList.add(customer);
        System.out.println("用户已成功添加: " + customer.getUsername());
    }

    public void deleteCustomerByUsername(String username) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("您确定要删除用户 " + username + " 吗？该操作不可撤销。 (y/n)");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("y")) {
            System.out.println("删除操作已取消。");
            return;
        }

        boolean removed = customerList.removeIf(p -> p.getUsername().equals(username));
        if (removed) {
            System.out.println("用户已成功删除，用户名: " + username);
        } else {
            System.out.println("未找到对应用户，用户名: " + username);
        }
    }

    public Customer findCustomerByUsername(String username) {
        for (Customer customer : customerList) {
            if (customer.getUsername().equals(username)) {
                System.out.println("找到用户，用户名: " + username);
                return customer;
            }
        }
        System.out.println("未找到对应用户，用户名: " + username);
        return null;
    }

    public void updateCustomer(String username, Customer updatedCustomer) {
        for (int i = 0; i < customerList.size(); i++) {
            if (customerList.get(i).getUsername().equals(username)) {
                customerList.set(i, updatedCustomer);
                System.out.println("用户已成功更新，用户名: " + username);
                break;
            }
        }
    }

    public List<Customer> getAllCustomers() {
        return customerList;
    }
}
