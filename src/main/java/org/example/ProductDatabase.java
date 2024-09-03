package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductDatabase {
    private static final String PRODUCT_FILE = "products.txt";
    private static List<Product> productList;
   
    public ProductDatabase(List<Product> productList){
        this.productList=productList;
    }
    // 保存商品列表到文件（退出时调用）
    public static void saveProductsToFile(List<Product> products) {
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

    // 从文件加载商品列表（启动时调用）
    public static List<Product> loadProductsFromFile() {
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
        productList.add(product);
        System.out.println("商品已成功添加: " + product.getProductName());
    }

    // 删除商品
    public static void deleteProduct(int productId) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("您确定要删除商品 " +productId + " 吗？该操作不可撤销。 (y/n)");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("y")) {
            System.out.println("删除操作已取消。");
            return; // 取消删除操作
        }

        boolean removed = productList.removeIf(p -> p.getProductId() == productId);
        if (removed) {
            System.out.println("商品已成功删除，商品ID: " + productId);
        } else {
            System.out.println("未找到对应商品，商品ID: " + productId);
        }
    }

    // 查找商品（通过ID）
    public static Product findProductById(int productId) {
        Product product=productList.stream().filter(p -> p.getProductId() == productId).findFirst().orElse(null);
        if (product != null) {
            System.out.println("找到商品，商品ID: " + productId + "，商品名称: " + product.getProductName());
        } else {
            System.out.println("未找到对应商品，商品ID: " + productId);
        }
        return product;
    }

    // 更新商品
    public static void updateProduct(int productId, Product updatedProduct) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getProductId() == productId) {
                productList.set(i, updatedProduct);
                System.out.println("商品已成功更新，商品ID: " + productId);
                break;
            }
        }
    }

    // 获取所有商品
    public static List<Product> getAllProducts() {
       return productList;
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
    // public List<Product> getProducts() {
        
    //     throw new UnsupportedOperationException("Unimplemented method 'getProducts'");
    // }
}
