package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDatabase {
    private static final String CUSTOMER_FILE = "customers.txt";

    // 保存用户列表到文件
    public void saveCustomersToFile(List<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
            for (Customer customer : customers) {
                writer.write(customer.getUsername() + ";" + customer.getPassword() + ";" + customer.getEmail() + ";" +
                        customer.getPhone() + ";" + customer.getRegistrationDate() + ";" + customer.getUserLevel());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving customers to file: " + e.getMessage());
        }
    }

    // 从文件加载用户列表
    public List<Customer> loadCustomersFromFile() {
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
                System.err.println("Error loading customers from file: " + e.getMessage());
            }
        }
        return customers;
    }

    // 添加用户
    public void addCustomer(Customer customer) {
        List<Customer> customers = loadCustomersFromFile();
        customers.add(customer);
        saveCustomersToFile(customers);
    }

    // 删除用户
    public void deleteCustomerByUsername(String username) {
        List<Customer> customers = loadCustomersFromFile();
        customers.removeIf(c -> c.getUsername().equals(username));
        saveCustomersToFile(customers);
    }

    // 查找用户（通过用户名）
    public Customer findCustomerByUsername(String username) {
        List<Customer> customers = loadCustomersFromFile();
        return customers.stream()
                .filter(c -> c.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    // 更新用户
    public void updateCustomer(String username, Customer updatedCustomer) {
        List<Customer> customers = loadCustomersFromFile();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUsername().equals(username)) {
                customers.set(i, updatedCustomer);
                break;
            }
        }
        saveCustomersToFile(customers);
    }

    // 获取所有用户
    public List<Customer> getAllCustomers() {
        return loadCustomersFromFile();
    }
}
