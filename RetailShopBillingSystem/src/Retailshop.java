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

    public double getTotal() {
        return product.price * quantity;
    }
}

class RetailSystem {
    private final List<Product> products = new ArrayList<>();
    private final List<CartItem> cart = new ArrayList<>();

    public void loadFromCSV(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] data = line.split(","); // කොමාව (,) බෙදුම්කරුවෙකු ලෙස භාවිතා කර වත්මන් පේළිය String ('data') බෙදයි
                if (data.length != 3)
                    continue;


                try {
                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    double price = Double.parseDouble(data[2].trim());
                    products.add(new Product(id, name, price));
                } catch (NumberFormatException e) {
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read the Database: " + e.getMessage());
        }
    }

    // Function 1: Add a product to the inventory
    public void addProduct(int id, String name, double price) {
        Product product = new Product(id, name, price);
        products.add(product);
    }

    // Function 2: Show available products
    public void showProducts() {
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

    // Function 3: Add a product to the shopping cart
    public void addItemToCart(int id, int quantity) {
        Product product = getProductById(id);
        if (product != null && quantity > 0) {
            cart.add(new CartItem(product, quantity));
            System.out.println("Added " + quantity + " x " + product.name + " to the cart.");
        } else {
            System.out.println("Invalid product ID or quantity.");
        }
    }

    // Function 4: Calculate total amount for items in the cart
    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cart) {
            total += item.getTotal();
        }
        return total;
    }

    // Function 5: Generate and display the bill
    public void generateBill() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. No bill to generate.");
            return;
        }

        System.out.println("\nBill Summary:");
        System.out.printf("%-25s %-10s %-10s\n", "Item", "Qty", "Total");
        for (CartItem item : cart) {
            System.out.printf("%-25s %-10d %.2f\n", item.product.name, item.quantity, item.getTotal());
        }

        double totalAmount = calculateTotal();
        System.out.println("--------------------------------------");
        System.out.printf("Total Amount: %.2f\n", totalAmount);
    }

    // Function 6: Clear the shopping cart
    public void clearCart() {
        cart.clear();
        System.out.println("Shopping cart has been cleared.");
    }

    // Function 7: Find a product by ID
    public void findProductById(int id) {
        Product product = getProductById(id);
        if (product != null) {
            System.out.printf("\nFound Product: ID = %d, Name = %s, Price = %.2f\n", product.id, product.name, product.price);
        } else {
            System.out.println("Product not found with ID: " + id);
        }
    }

    // Helper function to get a product by ID
    private Product getProductById(int id) {
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
            RetailSystem retailSystem = new RetailSystem();

            Path source = Paths.get("database.csv");
            Path target = Paths.get(System.getProperty("user.dir"), "database.csv");

            try {
                if (!Files.exists(target)) {
                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Database copied to working directory.");
                } else {
                    System.out.println("Database Integration Complete");
                }
            } catch (IOException e) {
                System.out.println("Database copying failed: " + e.getMessage());
            }

            retailSystem.loadFromCSV(String.valueOf(target));
            retailSystem.showProducts();

            // Authentication section
            System.out.println("Welcome to the Retail System!");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Simple Authentication check (you can expand this to a more complex system)
            if (!authenticate(username, password)) {
                System.out.println("Invalid username or password.");
                return; // End the program if authentication fails
            }

            // User interaction after authentication
            while (true) {
                System.out.println("\n--- Retail Shop System ---");
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
                        retailSystem.showProducts();
                        break;

                    case 2:
                        System.out.print("Enter Product ID: ");
                        int id = scanner.nextInt();
                        System.out.print("Enter Quantity: ");
                        int quantity = scanner.nextInt();
                        retailSystem.addItemToCart(id, quantity);
                        break;

                    case 3:
                        retailSystem.generateBill();
                        break;

                    case 4:
                        retailSystem.clearCart();
                        break;

                    case 5:
                        System.out.print("Enter Product ID to find: ");
                        int searchId = scanner.nextInt();
                        retailSystem.findProductById(searchId);
                        break;

                    case 6:
                        System.out.println("Thank you for shopping! Goodbye.");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        }

        // Simple authentication method
        private static boolean authenticate(String username, String password) {
            // Here you can implement more complex authentication logic or use a database
            String validUsername = "admin";
            String validPassword = "admin";
            return username.equals(validUsername) && password.equals(validPassword);
        }
    }
}
