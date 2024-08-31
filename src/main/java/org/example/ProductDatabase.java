package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDatabase {
    private static final String PRODUCT_FILE = "products.txt";

    // 保存商品列表到文件
    private static void saveProductsToFile(List<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE))) {
            for (Product product : products) {
                writer.write(product.getProductId() + ";" + product.getProductName() + ";" + product.getManufacturer() + ";" +
                        product.getModel() + ";" + product.getPurchasePrice() + ";" + product.getRetailPrice() + ";" + product.getNums());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("保存商品到文件时出错: " + e.getMessage());
        }
    }

    // 从文件加载商品列表
    private static List<Product> loadProductsFromFile() {
        List<Product> products = new ArrayList<>();
        File file = new File(PRODUCT_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 7) {
                        Product product = new Product(
                                Integer.parseInt(parts[0]),
                                parts[1],
                                parts[2],
                                parts[3],
                                Double.parseDouble(parts[4]),
                                Double.parseDouble(parts[5]),
                                Integer.parseInt(parts[6])
                        );
                        products.add(product);
                    }
                }
            } catch (IOException e) {
                System.err.println("加载商品文件时出错: " + e.getMessage());
            }
        }
        return products;
    }

    // 增加商品
    public static void addProduct(Product product) {
        List<Product> products = loadProductsFromFile();
        products.add(product);
        saveProductsToFile(products);
        System.out.println("商品已成功添加: " + product.getProductName());
    }

    // 删除商品
    public static void deleteProduct(int productId) {
        List<Product> products = loadProductsFromFile();
        boolean removed = products.removeIf(p -> p.getProductId() == productId);
        saveProductsToFile(products);
        if (removed) {
            System.out.println("商品已成功删除，商品ID: " + productId);
        } else {
            System.out.println("未找到对应商品，商品ID: " + productId);
        }
    }

    // 查找商品（通过ID）
    public static Product findProductById(int productId) {
        List<Product> products = loadProductsFromFile();
        Product product = products.stream()
                .filter(p -> p.getProductId() == productId)
                .findFirst()
                .orElse(null);
        if (product != null) {
            System.out.println("找到商品，商品ID: " + productId + "，商品名称: " + product.getProductName());
        } else {
            System.out.println("未找到对应商品，商品ID: " + productId);
        }
        return product;
    }

    // 更新商品
    public static void updateProduct(int productId, Product updatedProduct) {
        List<Product> products = loadProductsFromFile();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId() == productId) {
                products.set(i, updatedProduct);
                break;
            }
        }
        saveProductsToFile(products);
        System.out.println("商品已成功更新，商品ID: " + productId);
    }

    // 获取所有商品
    public static List<Product> getAllProducts() {
        System.out.println("正在加载所有商品...");
        return loadProductsFromFile();
    }

    // 更新商品库存数量
    public static void updateProductQuantity(int productId, int quantity) {
        Product product = findProductById(productId);
        if (product != null) {
            int newQuantity = product.getNums() - quantity;
            if (newQuantity > 0) {
                product.setNums(newQuantity);
                updateProduct(productId, product);
                System.out.println("商品库存已更新，商品ID: " + productId + "，剩余库存: " + newQuantity);
            } else if (newQuantity <= 0) {
                // 删除商品，如果数量小于或等于0
                deleteProduct(productId);
               
            }
        } else {
            System.out.println("未找到对应商品，商品ID: " + productId);
        }
    }
}
