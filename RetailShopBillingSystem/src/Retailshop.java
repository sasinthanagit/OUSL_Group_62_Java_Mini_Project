import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

class Product {
    int id;
    String name;
    double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double get_total() {
        return product.price * quantity;
    }
}

class RetailSystem {
    private final List<Product> products = new ArrayList<>();
    private final List<CartItem> cart = new ArrayList<>();

    public void load_from_csv(String file_path) {
        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        String name = data[1].trim();
                        double price = Double.parseDouble(data[2].trim());
                        add_product(id, name, price);
                    } catch (NumberFormatException e) {
                        // Skip lines that don't match expected format
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read the database: " + e.getMessage());
        }
    }

    public void add_product(int id, String name, double price) {
        Product product = new Product(id, name, price);
        products.add(product);
    }

    public void show_products() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("\nAvailable Products:");
        System.out.printf("%-5s %-25s %-10s\n", "ID", "Name", "Price");
        for (Product p : products) {
            System.out.printf("%-5d %-25s %-10.2f\n", p.id, p.name, p.price);
        }
    }

    public void add_item_to_cart(int id, int quantity) {
        Product product = get_product_by_id(id);
        if (product != null && quantity > 0) {
            cart.add(new CartItem(product, quantity));
            System.out.println("Added " + quantity + " x " + product.name + " to the cart.");
        } else {
            System.out.println("Invalid product ID or quantity.");
        }
    }

    public double calculate_total() {
        double total = 0;
        for (CartItem item : cart) {
            total += item.get_total();
        }
        return total;
    }

    public void generate_bill() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. No bill to generate.");
            return;
        }

        System.out.println("\nBill Summary:");
        System.out.printf("%-25s %-10s %-10s\n", "Item", "Qty", "Total");
        for (CartItem item : cart) {
            System.out.printf("%-25s %-10d %.2f\n", item.product.name, item.quantity, item.get_total());
        }

        double total_amount = calculate_total();
        System.out.println("--------------------------------------");
        System.out.printf("Total Amount: Rs. %.2f\n", total_amount);
    }

    public void clear_cart() {
        cart.clear();
        System.out.println("Shopping cart has been cleared.");
    }

    public void find_product_by_id(int id) {
        Product product = get_product_by_id(id);
        if (product != null) {
            System.out.printf("\nFound Product: ID = %d, Name = %s, Price = %.2f\n", product.id, product.name, product.price);
        } else {
            System.out.println("Product not found with ID: " + id);
        }
    }

    private Product get_product_by_id(int id) {
        for (Product p : products) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
    }

    public static class RetailShop {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            RetailSystem retail_system = new RetailSystem();

            Path csv_path = Paths.get(System.getProperty("user.dir"), "database.csv");
            retail_system.load_from_csv(csv_path.toString());

            System.out.println("Welcome to Retail Shop Billing System");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            while (!authenticate(username, password)) {
                System.out.println("Invalid username or password. Please try again.");
                System.out.print("Enter username: ");
                username = scanner.nextLine();
                System.out.print("Enter password: ");
                password = scanner.nextLine();
            }
            System.out.println("Login successful! Welcome, " + username + "!");

            while (true) {
                System.out.println("\n--- Retail Billing System Menu ---");
                System.out.println("1. Show Products");
                System.out.println("2. Add Product to Cart");
                System.out.println("3. Generate Bill");
                System.out.println("4. Clear Cart");
                System.out.println("5. Find Product by ID");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        retail_system.show_products();
                        break;

                    case 2:
                        System.out.print("Enter Product ID: ");
                        int id = scanner.nextInt();
                        System.out.print("Enter Quantity: ");
                        int quantity = scanner.nextInt();
                        retail_system.add_item_to_cart(id, quantity);
                        break;

                    case 3:
                        retail_system.generate_bill();
                        break;

                    case 4:
                        retail_system.clear_cart();
                        break;

                    case 5:
                        System.out.print("Enter Product ID to find: ");
                        int search_id = scanner.nextInt();
                        retail_system.find_product_by_id(search_id);
                        break;

                    case 6:
                        System.out.println("Transaction complete, Thank you for shopping! Goodbye.");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        }

        private static boolean authenticate(String username, String password) {
            String valid_username = "admin";
            String valid_password = "admin";
            return username.equals(valid_username) && password.equals(valid_password);
        }
    }
}                                                                               