package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CustomerShoppingCart {
    private Map<String, Integer> shoppingCart; // 商品集
    private ProductDatabase productDatabase;
    private static final String FILE_NAME = "shoppingCart.txt"; // 文件名

    Scanner scanner = new Scanner(System.in);

    public CustomerShoppingCart(Customer customer) {
        this.shoppingCart = new HashMap<>();
        this.productDatabase = new ProductDatabase();
        loadCartFromFile(); // 从文件中加载购物车内容
    }

    // 将购物车内容保存到文件
    private void saveCartToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(shoppingCart);
        } catch (IOException e) {
            System.out.println("Error saving cart to file: " + e.getMessage());
        }
    }

    // 从文件加载购物车内容
    private void loadCartFromFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                shoppingCart = (Map<String, Integer>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading cart from file: " + e.getMessage());
            }
        }
    }

    // 加入购物车
    public void addToCart() {
        System.out.println("请输入商品ID：");
        String productId = scanner.nextLine();
        System.out.println("请输入数量：");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 消耗换行
        if (productDatabase.findProductById(Integer.parseInt(productId)) != null) {
            shoppingCart.put(productId, shoppingCart.getOrDefault(productId, 0) + quantity);
            productDatabase.updateProductQuantity(Integer.parseInt(productId), quantity);
            saveCartToFile(); // 保存购物车内容
        } else {
            System.out.println("Product ID " + productId + " not found in the database.");
        }
    }

    // 从购物车删除
    public void removeFromCart() {
        System.out.println("请输入商品ID：");
        String productId = scanner.nextLine();
        System.out.println("请输入数量：");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 消耗换行
        if (shoppingCart.containsKey(productId)) {
            int currentQuantity = shoppingCart.get(productId);
            if (currentQuantity > quantity) {
                shoppingCart.put(productId, currentQuantity - quantity);
            } else {
                shoppingCart.remove(productId);
            }
            productDatabase.increaseProductQuantity(Integer.parseInt(productId), quantity);
            saveCartToFile(); // 保存购物车内容
        } else {
            System.out.println("Product ID " + productId + " not found in the cart.");
        }
    }

    // 更新购物车商品数量
    public void updateCartItemQuantity() {
        System.out.println("请输入商品ID：");
        String productId = scanner.nextLine();
        System.out.println("请输入数量：");
        int newQuantity = scanner.nextInt();
        scanner.nextLine();//消耗换行
        if (shoppingCart.containsKey(productId)) {
            int currentQuantity = shoppingCart.get(productId);
            int quantityChange = newQuantity - currentQuantity;
            shoppingCart.put(productId, newQuantity);
            productDatabase.updateProductQuantity(Integer.parseInt(productId), quantityChange);
            saveCartToFile(); // 保存购物车内容
        } else {
            System.out.println("Product ID " + productId + " not found in the cart.");
        }
    }

    // 获取购物车历史内容
    public void getShoppingCartHistory() {
        StringBuilder cart = new StringBuilder();
        for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
            cart.append("Product ID: ").append(entry.getKey())
                .append(", Quantity: ").append(entry.getValue())
                .append("\n");
        }
        System.out.println(cart.toString());
    }

    // 结账
    public void checkout() {
        System.out.println("请选择支付方式：");
        System.out.println("1. 支付宝");
        System.out.println("2. 微信");
        System.out.println("3. 银行卡");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println("使用支付宝支付");
                break;
            case 2:
                System.out.println("使用微信支付");
                break;
            case 3:
                System.out.println("使用银行卡支付");
                break;
            default:
                System.out.println("无效的支付方式");
        }
    }
}
