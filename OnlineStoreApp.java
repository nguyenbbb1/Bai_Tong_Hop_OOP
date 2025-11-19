import java.util.*;

// Exception
class DuplicateIdException extends Exception {
    public DuplicateIdException(String message) {
        super(message);
    }
}

class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }
}

class InvalidPriceException extends Exception {
    public InvalidPriceException(String message) {
        super(message);
    }
}

class NonRefundableException extends Exception {
    public NonRefundableException(String message) {
        super(message);
    }
}

// Product & Subclasses
abstract class Product {
    protected String id;
    protected String name;
    protected double price;

    public Product(String id, String name, double price) throws InvalidPriceException {
        if(price < 0) throw new InvalidPriceException("Price cannot be negative");
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', price=" + price + "}";
    }
}

class Book extends Product implements Deliverable, Refundable {
    private String author;

    public Book(String id, String name, double price, String author) throws InvalidPriceException {
        super(id, name, price);
        this.author = author;
    }

    public String getAuthor() { return author; }

    @Override
    public void deliver() {
        System.out.println("Delivering book: " + name + " by " + author);
    }

    @Override
    public void refund() {
        System.out.println("Refunding book: " + name);
    }

    @Override
    public String toString() {
        return super.toString() + ", author='" + author + "'";
    }
}

class Phone extends Product implements Deliverable, Refundable {
    private String brand;

    public Phone(String id, String name, double price, String brand) throws InvalidPriceException {
        super(id, name, price);
        this.brand = brand;
    }

    public String getBrand() { return brand; }

    @Override
    public void deliver() {
        System.out.println("Delivering phone: " + brand + " " + name);
    }

    @Override
    public void refund() {
        System.out.println("Refunding phone: " + name);
    }

    @Override
    public String toString() {
        return super.toString() + ", brand='" + brand + "'";
    }
}

class Laptop extends Product implements Deliverable, Refundable {
    private String brand;

    public Laptop(String id, String name, double price, String brand) throws InvalidPriceException {
        super(id, name, price);
        this.brand = brand;
    }

    public String getBrand() { return brand; }

    @Override
    public void deliver() {
        System.out.println("Delivering laptop: " + brand + " " + name);
    }

    @Override
    public void refund() throws NonRefundableException {
        throw new NonRefundableException("Laptop " + name + " cannot be refunded!");
    }

    @Override
    public String toString() {
        return super.toString() + ", brand='" + brand + "'";
    }
}

// Interface
interface Deliverable {
    void deliver();
}

interface Refundable {
    void refund() throws NonRefundableException;
}

interface Payment {
    void pay(Order order);
}

// Payment Implementations
class CreditCardPayment implements Payment {
    @Override
    public void pay(Order order) {
        System.out.println("Paid " + order.getTotal() + " using Credit Card");
    }
}

class PaypalPayment implements Payment {
    @Override
    public void pay(Order order) {
        System.out.println("Paid " + order.getTotal() + " using PayPal");
    }
}

class CashPayment implements Payment {
    @Override
    public void pay(Order order) {
        System.out.println("Paid " + order.getTotal() + " in cash");
    }
}

// Customer & Order
class Customer {
    private String id;
    private String name;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Customer{id='" + id + "', name='" + name + "'}";
    }
}

class Order {
    private String id;
    private Customer customer;
    private List<Product> products = new ArrayList<>();

    public Order(String id, Customer customer) {
        this.id = id;
        this.customer = customer;
    }

    public void addProduct(Product p) {
        products.add(p);
    }

    public double getTotal() {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }

    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public List<Product> getProducts() { return products; }

    public void deliverAll() {
        for (Product p : products) {
            if (p instanceof Deliverable) ((Deliverable)p).deliver();
        }
    }

    public void refundAll() {
        for (Product p : products) {
            if (p instanceof Refundable) {
                try {
                    ((Refundable)p).refund();
                } catch (NonRefundableException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Order{id='" + id + "', customer=" + customer.getName() + ", total=" + getTotal() + "}";
    }
}

// Generic Repository
interface Repository<T> {
    void add(T item) throws DuplicateIdException;
    void update(T item) throws NotFoundException;
    void delete(String id) throws NotFoundException;
    List<T> findAll();
}

class ProductRepository implements Repository<Product> {
    private Map<String, Product> store = new HashMap<>();

    @Override
    public void add(Product item) throws DuplicateIdException {
        if(store.containsKey(item.getId())) throw new DuplicateIdException("Product ID already exists: " + item.getId());
        store.put(item.getId(), item);
    }

    @Override
    public void update(Product item) throws NotFoundException {
        if(!store.containsKey(item.getId())) throw new NotFoundException("Product not found: " + item.getId());
        store.put(item.getId(), item);
    }

    @Override
    public void delete(String id) throws NotFoundException {
        if(!store.containsKey(id)) throw new NotFoundException("Product not found: " + id);
        store.remove(id);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }
}

class CustomerRepository implements Repository<Customer> {
    private Map<String, Customer> store = new HashMap<>();

    @Override
    public void add(Customer item) throws DuplicateIdException {
        if(store.containsKey(item.getId())) throw new DuplicateIdException("Customer ID already exists: " + item.getId());
        store.put(item.getId(), item);
    }

    @Override
    public void update(Customer item) throws NotFoundException {
        if(!store.containsKey(item.getId())) throw new NotFoundException("Customer not found: " + item.getId());
        store.put(item.getId(), item);
    }

    @Override
    public void delete(String id) throws NotFoundException {
        if(!store.containsKey(id)) throw new NotFoundException("Customer not found: " + id);
        store.remove(id);
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(store.values());
    }
}

class OrderRepository implements Repository<Order> {
    private Map<String, Order> store = new HashMap<>();

    @Override
    public void add(Order item) throws DuplicateIdException {
        if(store.containsKey(item.getId())) throw new DuplicateIdException("Order ID already exists: " + item.getId());
        store.put(item.getId(), item);
    }

    @Override
    public void update(Order item) throws NotFoundException {
        if(!store.containsKey(item.getId())) throw new NotFoundException("Order not found: " + item.getId());
        store.put(item.getId(), item);
    }

    @Override
    public void delete(String id) throws NotFoundException {
        if(!store.containsKey(id)) throw new NotFoundException("Order not found: " + id);
        store.remove(id);
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(store.values());
    }
}

// Main/Test
public class OnlineStoreApp {
    public static void main(String[] args) {
        ProductRepository productRepo = new ProductRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        OrderRepository orderRepo = new OrderRepository();

        try {
            // Add products
            Book book1 = new Book("b1", "Java Basics", 200, "Nguyen");
            Phone phone1 = new Phone("p1", "iPhone 15", 2500, "Apple");
            Laptop laptop1 = new Laptop("l1", "ThinkPad X1", 3000, "Lenovo");

            productRepo.add(book1);
            productRepo.add(phone1);
            productRepo.add(laptop1);

            // List products
            System.out.println("=== Products ===");
            productRepo.findAll().forEach(System.out::println);

            // Add customers
            Customer c1 = new Customer("c1", "Alice");
            customerRepo.add(c1);

            // Create order
            Order order1 = new Order("o1", c1);
            order1.addProduct(book1);
            order1.addProduct(phone1);
            order1.addProduct(laptop1);
            orderRepo.add(order1);

            System.out.println("\n=== Order Details ===");
            System.out.println(order1);

            // Deliver products
            System.out.println("\n=== Delivering Products ===");
            order1.deliverAll();

            // Refund products
            System.out.println("\n=== Refunding Products ===");
            order1.refundAll();

            // Payment
            System.out.println("\n=== Payment ===");
            Payment payment1 = new CreditCardPayment();
            Payment payment2 = new PaypalPayment();
            Payment payment3 = new CashPayment();

            payment1.pay(order1);
            payment2.pay(order1);
            payment3.pay(order1);

            // Exception testing
            System.out.println("\n=== Exception Testing ===");
            try {
                productRepo.add(book1); // Duplicate
            } catch (DuplicateIdException e) {
                System.out.println(e.getMessage());
            }

            try {
                Laptop laptop2 = new Laptop("l2", "XPS 13", -100, "Dell"); // Invalid price
            } catch (InvalidPriceException e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
