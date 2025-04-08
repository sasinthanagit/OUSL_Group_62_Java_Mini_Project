import java.io.BufferedReader; //දත්ත පේළියෙන් පේළියට කියවීම සඳහා
import java.io.FileReader; // Character file දත්ත කියවීම සඳහා
import java.io.IOException; //Input Output වලදී සිදුවිය හැකි දෝෂ (exceptions) හැසිරවීම සඳහා
import java.nio.file.*; // ගොනු සහ නාමාවලි මෙහෙයුම් සඳහා
import java.util.*; // util පැකේජයේ සියලුම Classes import කරයි

class Product {
    int id; //භාණ්ඩයේ හැඳුනුම් අංකය ගබඩා කිරීම සඳහා
    String name; // භාණ්ඩයේ නම ගබඩා කිරීම සඳහා
    double price; //භාණ්ඩයේ මිල ගබඩා කිරීම සඳහා

    public Product(int id, String name, double price) {
        this.id = id; // constructor ලබාදුන් අගය සමඟ 'id' ක්ෂේත්‍රය ආරම්භ කරයි
        this.name = name; //constructor ලබාදුන් අගය සමඟ 'name' ක්ෂේත්‍රය ආරම්භ කරයි.
        this.price = price; //constructor ලබාදුන් අගය සමඟ 'price' ක්ෂේත්‍රය ආරම්භ කරයි.
    }
}

class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product; //constructor ලබාදුන් Product වස්තුව සමඟ 'product' ක්ෂේත්‍රය ආරම්භ
        this.quantity = quantity; //constructor ලබාදුන් අගය සමඟ 'quantity' ක්ෂේත්‍රය ආරම්භ කරයි
    }

    public double getTotal() {
        return product.price * quantity; // CartItem අයිතමයේ මුළු මිල (මිල ගුණ කිරීම ප්‍රමාණය) ගණනය කර ආපසු ලබා දෙයි.

    }
}

class Shop {
    private final List<Product> products = new ArrayList<>(); // Product වස්තු ගබඩා කිරීම සඳහා 'products' නම් පුද්ගලික සහ අවසාන (private final) ArrayList එකක් සාදයි.
    private final List<CartItem> cart = new ArrayList<>(); // CartItem වස්තු ගබඩා කිරීම සඳහා 'cart' නම් පුද්ගලික සහ අවසාන ArrayList එකක් සාදයි.

    public void loadFromCSV(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) { // 'filePath' මඟින් නිශ්චිතව දක්වා ඇති ගොනුවෙන් කියවීම සඳහා BufferedReader එකක් සාදයි. try-with-resources ප්‍රකාශය මඟින් කියවනය ස්වයංක්‍රීයව වසා දැමීම සහතික කරයි.
            String line; //ගොනුවෙන් කියවන සෑම පේළියක්ම ගබඩා කිරීම සඳහා 'line' නම් String Variable ප්‍රකාශ කරයි.
            reader.readLine(); // skip header of the database.csv file

            while ((line = reader.readLine()) != null) { // ගොනුවෙන් ඊළඟ පේළිය කියවා එය 'line' වෙත පවරයි(Assign). කියවීමට පේළි ඇති තාක් කල් Loop දිගටම ක්‍රියාත්මක වේ.
                line = line.trim(); // වත්මන් පේළියේ ඉදිරිපස සහ පසුපස හිස්තැන් ඉවත් කරයි.
                if (line.isEmpty()) //පේළිය "හිස් නම්"
                    continue;

                String[] data = line.split(","); // කොමාව (,) බෙදුම්කරුවෙකු ලෙස භාවිතා කර වත්මන් පේළිය String ('data') බෙදයි
                if (data.length != 3)
                    continue; //'data' Array හරියටම මූලද්‍රව්‍ය 3ක් (ID, Name, Price) නොමැති නම්.


                try {
                    int id = Integer.parseInt(data[0].trim()); // හිස්තැන් ඉවත් කිරීමෙන් පසු 'data' හි පළමු ID Whole number convert කරයි.
                    String name = data[1].trim(); // හිස්තැන් ඉවත් කිරීමෙන් පසු 'data' හි second item (product name) 'name' විචල්‍යයට පවරයි.
                    double price = Double.parseDouble(data[2].trim()); //// හිස්තැන් ඉවත් කිරීමෙන් පසු 'data' හි තුන්වන මූලද්‍රව්‍යය (Product Price) Double Datatype assign කරයි.
                    products.add(new Product(id, name, price)); // පරිවර්තනය කළ ProductID,Name,Price සමඟ නව Product Object සාදා එය 'products' ලැයිස්තුවට එකතු කරයි.
                } catch (NumberFormatException e) {
                    // skip invalid row (උදාහරණයක් ලෙස, ProductID, Price වලංගු අංකයක් නොවේ නම්) මෙම catch කොටස ක්‍රියාත්මක වන අතර වත්මන් පේළිය මඟ හරිනු ලැබේ.
                }
            }
        } catch (IOException e) { //CSV ගොනුව කියවීමට නොහැකි විය
            System.out.println("Unable to read the Database: " + e.getMessage());
        }
    }

    public void showProducts() {
        if (products.isEmpty()) { // 'products' ලැයිස්තුව හිස් දැයි පරීක්ෂා කරයි.
            System.out.println("No products available.");
            return;
        }

        System.out.println("\nAvailable Products in the Shop Inventory:\n"); //Heading Title
        System.out.printf("%-5s %-25s %-10s\n", "ID", "Name", "Price"); //Column headers with specific formatting columns are aligned, making the data easier to read for customer. i.e. "ID" has some trailing spaces to fill the 5-character width, "Name" has more trailing spaces to fill the 25-character width, and "Price" has trailing spaces to fill the 10-character width. The left-justification ensures that the text starts at the beginning of its allocated space.
        for (Product p : products) { // 'products' ලැයිස්තුවේ ඇති සෑම Product වස්තුවක් සඳහාම ('p') Loop කරයි.
            System.out.printf("%-5d %-25s %-10.2f\n", p.id, p.name, p.price);
        }
    }/* Above Loop iterates through each Product in the products list and prints its ID (left-justified in a 5-character width), its name (left-justified in a 25-character width), and its price (left-justified in a 10-character width with two decimal places), each on a new line. */

    public Product getProductById(int id) {
        for (Product p : products) {
            if (p.id == id) return p;// වත්මන් භාණ්ඩයේ ('p') id ලබාදුන් 'id' ට ගැලපේ නම්, p return දෙයි.
        }
        return null; //කිසිදු භාණ්ඩයක් සොයාගත නොහැකි නම්
    }

    public void addItemToCart(int id, int qty) {
        Product prod = getProductById(id); // ලබාදුන් 'id' සමඟ Product object ලබා ගැනීමට
        if (prod != null && qty > 0) { // භාණ්ඩයක් හමු වූයේ නම් (not null) and ප්‍රමාණය 0 ට වඩා වැඩි නම්
            cart.add(new CartItem(prod, qty)); //create new ItemObject - ලැයිස්තුවට එකතු කරයි.
            System.out.println("Added to cart: " + qty + " x " + prod.name);
        } else {
            System.out.println("Invalid product ID or quantity.");
        }
    }

    public void printReceipt() {
        if (cart.isEmpty()) { //Cart List හිස් දැයි පරීක්ෂා කරයි.
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("\nReceipt:"); //රිසිට්පත සඳහා title මුද්‍රණය කරයි
        System.out.printf("%-25s %-10s %-10s\n", "Item", "Qty", "Total");
        double total = 0; // Cart ඇති භාණ්ඩවල මුළු මුදල ගබඩා කිරීම සඳහා intialize a varible called total

        for (CartItem item : cart) {
            double itemTotal = item.getTotal(); // current CartItem හි මුළු මිල ලබා ගැනීමට calling getTotal() method
            System.out.printf("%-25s %-10d %.2f\n", item.product.name, item.quantity, itemTotal);
            total += itemTotal; // සමස්ත 'total' වෙත 'itemTotal' එකතු කරයි.
        }

        System.out.println("--------------------------------------"); // වෙන් කිරීමේ රේඛාවක්
        System.out.printf("Total Amount: Rs.%.2f\n", total); // 'total' මුදල two decimal point සමඟ මුද්‍රණය කරයි.
        cart.clear();//රිසිට්පත මුද්‍රණය කිරීමෙන් පසු 'cart' ලැයිස්තුවේ ඇති සියලුම item ඉවත් කරයි.
    }
}

public class Retailshop {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); //Scanner Object called input
        Shop shop = new Shop(); // New instance of a shop class

        Path source = Paths.get("database.csv"); // source object "database.csv" project source ඇතැයි උපකල්පනය කෙරේ
        Path target = Paths.get(System.getProperty("user.dir"), "database.csv"); // Path object

        try {
            if (!Files.exists(target)) { //Source file database.csv දැනටමත් පවතිනවාදැයි පරීක්ෂා කරයි.
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING); //database.csv නොපවතී නම්, Source file target ස්ථානයට copy කරයි
                System.out.println("Database copied to working directory.");
            } else {
                System.out.println("Database Integration Complete");
            }
        } catch (IOException e) {
            System.out.println("Database copying failed: " + e.getMessage());
        }

        shop.loadFromCSV(target.toString());// Copy කළ ගොනුවෙන් නිෂ්පාදන දත්ත පැටවීම සඳහා 'shop' object calls loadFromCSV method.
        shop.showProducts(); // පවතින නිෂ්පාදන ප්‍රදර්ශනය කිරීම සඳහා

        while (true) { // Never ending loop
            System.out.println("\nOptions:");
            System.out.println("1. Add Product to Cart");
            System.out.println("2. Print Bill and Exit");
            System.out.print("Enter your choice: ");
            String choice = input.nextLine().trim();// User ගේ Input කියවා, .trim()= ඉදිරිපස/පසුපස හිස්තැන් ඉවත් කර , එය 'choice' variable යේ ගබඩා කරයි.

            if (choice.equals("1")) { //User ගේ තේරීම "1" දැයි පරීක්ෂා කරයි.
                System.out.print("Enter Product ID: ");
                String idStr = input.nextLine().trim();// Ask Product id to assign isStr
                System.out.print("Enter Quantity: ");
                String qtyStr = input.nextLine().trim();// Ask for quantity to assign qtyStr

                try {
                    int id = Integer.parseInt(idStr); //Id පූර්ණ සංඛ්‍යාවකට convert කරයි.
                    int qty = Integer.parseInt(qtyStr);
                    shop.addItemToCart(id, qty);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter valid numbers.");
                }

            } else if (choice.equals("2")) { //User ගේ තේරීම "2" දැයි පරීක්ෂා කරයි.
                shop.printReceipt();
                System.out.println("Transaction Successful!");
                break;
            } else {
                System.out.println("Invalid selection.");
            }
        }

        input.close(); //Close the Scanner Object
    }
}
