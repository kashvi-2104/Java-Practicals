
import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class MiniBank {

    // 1. RECORDS AND ENUMS
    record BankInfo(String name, String branch) {}
    record Command(TransactionType type, String accountNumber, long amount) {}

    enum MenuOption {
        OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT
    }

    enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER
    }

    // 2. CUSTOM ANNOTATIONS
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Id {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Positive {
        String message() default "must be greater than zero";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface MaxLength {
        int value();
    }

    // 3. INTERFACES
    interface Transactable {
        void deposit(long amount) throws BankException;
        boolean withdraw(long amount) throws BankException;
    }

    interface InterestBearing {
        double interestRate();

        default double yearlyInterest(long balance) {
            return balance * interestRate() / 100.0;
        }
    }

    interface Premium {}

    @FunctionalInterface
    interface WithdrawRule {
        boolean allow(Account account, long amount);
    }

    // 4. CUSTOM EXCEPTIONS
    static class BankException extends Exception {
        BankException(String message) {
            super(message);
        }
    }

    static class InsufficientFundsException extends BankException {
        InsufficientFundsException(long shortfall) {
            super("Insufficient funds. Short by " + shortfall);
        }
    }

    static class AccountNotFoundException extends BankException {
        AccountNotFoundException(String id) {
            super("Account not found: " + id);
        }
    }

    static class InvalidAmountException extends BankException {
        InvalidAmountException() {
            super("Amount must be positive.");
        }
    }

    // 5. CUSTOMER AND NESTED ADDRESS CLASS
    static class Customer implements Serializable, Cloneable {
        private static final long serialVersionUID = 1L;
        private static long counter = 100;

        @MaxLength(60)
        private final String name;

        private final String email;
        private final String mobile;
        private final String customerId;
        private final Address address;

        static class Address implements Serializable {
            private static final long serialVersionUID = 1L;

            private final String line;
            private final String city;
            private final String pincode;

            Address(String line, String city, String pincode) {
                this.line = line;
                this.city = city;
                this.pincode = pincode;
            }

            public String getLine() {
                return line;
            }

            public String getCity() {
                return city;
            }

            public String getPincode() {
                return pincode;
            }
        }

        Customer(String name, String email, String mobile, Address address) {
            this.name = name;
            this.email = email;
            this.mobile = mobile;
            this.address = address;
            this.customerId = "CUST" + (++counter);
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getMobile() {
            return mobile;
        }

        public String getCustomerId() {
            return customerId;
        }

        public Address getAddress() {
            return address;
        }

        @Override
        public Customer clone() {
            try {
                return (Customer) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }

    // 6. ABSTRACT ACCOUNT CLASS
    static abstract class Account implements
            Transactable, InterestBearing, Serializable, Comparable<Account> {

        private static final long serialVersionUID = 1L;
        private static long counter = 0;

        @Id
        private final String accountNumber;

        @MaxLength(60)
        private final String ownerName;

        @Positive
        private long balance;

        private boolean active = true;

        Account(String ownerName, long openingBalance) {
            this.accountNumber = String.format("AC%04d", ++counter);
            this.ownerName = ownerName;
            this.balance = openingBalance;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public synchronized long getBalance() {
            return balance;
        }

        public boolean isActive() {
            return active;
        }

        public synchronized void close() {
            active = false;
        }

        protected synchronized void changeBalance(long amount) {
            balance += amount;
        }

        public synchronized void deposit(long amount) throws BankException {
            if (amount <= 0) {
                throw new InvalidAmountException();
            }

            if (!active) {
                throw new BankException("Account is inactive.");
            }

            changeBalance(amount);
        }

        public synchronized boolean withdraw(long amount)
                throws BankException {

            if (amount <= 0) {
                throw new InvalidAmountException();
            }

            if (!active) {
                throw new BankException("Account is inactive.");
            }

            if (!canWithdraw(amount)) {
                throw new InsufficientFundsException(
                        Math.max(0, amount - balance));
            }

            changeBalance(-amount);
            return true;
        }

        abstract boolean canWithdraw(long amount);

        @Override
        public int compareTo(Account other) {
            return accountNumber.compareTo(other.accountNumber);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Account other
                    && accountNumber.equals(other.accountNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountNumber);
        }

        @Override
        public String toString() {
            return accountNumber + " | " + ownerName
                    + " | Balance: " + balance
                    + " | Active: " + active
                    + " | Interest: " + interestRate() + "%";
        }
    }

    // 7. INHERITANCE: SAVINGS ACCOUNT
    static class SavingsAccount extends Account {
        private static final long serialVersionUID = 1L;
        private final long minBalance;

        SavingsAccount(String name, long balance, long minBalance) {
            super(name, balance);
            this.minBalance = minBalance;
        }

        @Override
        public double interestRate() {
            return 4.0;
        }

        @Override
        boolean canWithdraw(long amount) {
            return getBalance() - amount >= minBalance;
        }
    }

    // 8. INHERITANCE: CURRENT ACCOUNT
    static class CurrentAccount extends Account implements Premium {
        private static final long serialVersionUID = 1L;
        private final long overdraftLimit;

        CurrentAccount(String name, long balance, long overdraftLimit) {
            super(name, balance);
            this.overdraftLimit = overdraftLimit;
        }

        @Override
        public double interestRate() {
            return 0.0;
        }

        @Override
        boolean canWithdraw(long amount) {
            return getBalance() - amount >= -overdraftLimit;
        }

        @Override
        public synchronized boolean withdraw(long amount)
                throws BankException {

            if (amount <= 0) {
                throw new InvalidAmountException();
            }

            if (!isActive()) {
                throw new BankException("Account is inactive.");
            }

            if (!canWithdraw(amount)) {
                throw new InsufficientFundsException(
                        Math.max(0, amount - getBalance()));
            }

            changeBalance(-amount);
            return true;
        }
    }

    // 9. INHERITANCE: FIXED DEPOSIT ACCOUNT
    static class FixedDepositAccount extends Account {
        private static final long serialVersionUID = 1L;

        FixedDepositAccount(String name, long balance) {
            super(name, balance);
        }

        @Override
        public double interestRate() {
            return 7.0;
        }

        @Override
        boolean canWithdraw(long amount) {
            return false;
        }
    }

    // 10. REGULAR EXPRESSION VALIDATION
    static class Validator {
        static boolean isValidMobile(String s) {
            return s != null && s.matches("[6-9][0-9]{9}");
        }

        static boolean isValidEmail(String s) {
            return s != null && Pattern.matches(
                    "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", s);
        }

        static boolean isValidPan(String s) {
            return s != null && s.matches("[A-Z]{5}[0-9]{4}[A-Z]");
        }

        static boolean isValidIfsc(String s) {
            return s != null && s.matches("[A-Z]{4}0[A-Z0-9]{6}");
        }
    }

    // 11. COMMAND PARSER
    static class CommandParser {
        static Command parse(String line) {
            String[] parts = line.trim().split("\\s+");

            if (parts.length != 3) {
                throw new IllegalArgumentException(
                        "Use: TYPE ACCOUNT AMOUNT");
            }

            return new Command(
                    TransactionType.valueOf(parts[0].toUpperCase()),
                    parts[1],
                    Long.parseLong(parts[2]));
        }
    }

    // 12. STRINGBUILDER AND STATEMENT GENERATION
    static class StatementFormatter {
        static String buildStatement(Account account) {
            return new StringBuilder()
                    .append("\n========== MINIBANK STATEMENT ==========\n")
                    .append("Account: ").append(account.getAccountNumber())
                    .append("\nOwner: ").append(account.getOwnerName())
                    .append("\nBalance: Rs. ").append(account.getBalance())
                    .append("\nInterest rate: ").append(account.interestRate())
                    .append("%\nYearly interest estimate: Rs. ")
                    .append(String.format("%.2f",
                            account.yearlyInterest(account.getBalance())))
                    .append("\nAccount active: ").append(account.isActive())
                    .append("\n========================================\n")
                    .toString();
        }
    }

    // 13. REFLECTION AND ANNOTATION VALIDATION
    static class AnnotationValidator {
        static String[] validate(Object object) {
            List<String> errors = new ArrayList<>();

            for (Field field : object.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(object);

                    if (field.isAnnotationPresent(Positive.class)
                            && value instanceof Number number
                            && number.longValue() <= 0) {

                        errors.add(field.getName() + ": "
                                + field.getAnnotation(Positive.class).message());
                    }

                    if (field.isAnnotationPresent(MaxLength.class)
                            && value instanceof String text
                            && text.length()
                            > field.getAnnotation(MaxLength.class).value()) {

                        errors.add(field.getName() + ": text is too long.");
                    }
                } catch (ReflectiveOperationException e) {
                    errors.add(e.getMessage());
                }
            }

            return errors.toArray(new String[0]);
        }
    }

    // 14. GENERICS AND REPOSITORY
    static class Repository<T> {
        private final HashMap<String, T> data = new HashMap<>();

        private String idOf(T object) {
            try {
                Class<?> type = object.getClass();

                while (type != null) {
                    for (Field field : type.getDeclaredFields()) {
                        if (field.isAnnotationPresent(Id.class)) {
                            field.setAccessible(true);
                            return String.valueOf(field.get(object));
                        }
                    }

                    type = type.getSuperclass();
                }

                throw new IllegalArgumentException(
                        "No @Id field found.");
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }

        T save(T object) {
            data.put(idOf(object), object);
            return object;
        }

        T findById(String id) {
            return data.get(id);
        }

        ArrayList<T> findAll() {
            return new ArrayList<>(data.values());
        }

        boolean delete(String id) {
            return data.remove(id) != null;
        }

        @SafeVarargs
        final void saveAll(T... objects) {
            for (T object : objects) {
                save(object);
            }
        }

        static <N extends Number> double average(N[] numbers) {
            return Arrays.stream(numbers)
                    .mapToDouble(Number::doubleValue)
                    .average()
                    .orElse(0.0);
        }
    }

    // 15. CONCURRENT COLLECTIONS AND TRANSACTION HISTORY
    static class AccountStore {
        private final ConcurrentHashMap<String, Account> accounts =
                new ConcurrentHashMap<>();

        private final List<String> history =
                Collections.synchronizedList(new ArrayList<>());

        void add(Account account) {
            accounts.put(account.getAccountNumber(), account);
        }

        Account get(String id) {
            return accounts.get(id);
        }

        Collection<Account> all() {
            return accounts.values();
        }

        ArrayList<Account> sortedByBalance() {
            ArrayList<Account> list = new ArrayList<>(accounts.values());

            list.sort(Comparator.comparingLong(Account::getBalance));
            return list;
        }

        void log(String message) {
            history.add(LocalDateTime.now() + " | " + message);
        }

        List<String> history() {
            synchronized (history) {
                return new ArrayList<>(history);
            }
        }
    }

    // 16. MULTITHREADING: RUNNABLE WORKER
    static class AccountWorker implements Runnable {
        private final Account account;
        private final int times;
        private final long amount;

        AccountWorker(Account account, int times, long amount) {
            this.account = account;
            this.times = times;
            this.amount = amount;
        }

        @Override
        public void run() {
            for (int i = 0; i < times; i++) {
                try {
                    account.deposit(amount);
                } catch (BankException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    // 17. EXECUTOR SERVICE AND AUTO CLOSEABLE
    static class TransactionProcessor implements AutoCloseable {
        private final ExecutorService pool =
                Executors.newFixedThreadPool(3);

        void deposit(Account account, long amount) {
            pool.submit(() -> {
                try {
                    account.deposit(amount);
                } catch (BankException e) {
                    System.out.println(e.getMessage());
                }
            });
        }

        void process(List<Runnable> jobs) {
            for (Runnable job : jobs) {
                pool.submit(job);
            }

            pool.shutdown();

            try {
                if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                }
            } catch (InterruptedException e) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        @Override
        public void close() {
            pool.shutdown();
        }
    }

    // 18. SERIALIZATION: SAVE AND LOAD
    static class StatePersister {
        static void save(Account[] accounts, Path path)
                throws IOException {

            try (ObjectOutputStream out = new ObjectOutputStream(
                    Files.newOutputStream(path))) {
                out.writeObject(accounts);
            }
        }

        static Account[] load(Path path)
                throws IOException, ClassNotFoundException {

            if (!Files.exists(path)) {
                return new Account[0];
            }

            try (ObjectInputStream in = new ObjectInputStream(
                    Files.newInputStream(path))) {
                return (Account[]) in.readObject();
            }
        }
    }

    // 19. FILE HANDLING: TRANSACTION LOG
    static class TransactionLog {
        static void append(Path path, String message)
                throws IOException {

            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            Files.writeString(
                    path,
                    LocalDateTime.now() + " | " + message
                            + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        }
    }

    // 20. REPORT GENERATION
    static class ReportGenerator {
        static void report(Path directory) throws IOException {
            if (!Files.exists(directory)) {
                System.out.println("No reports yet.");
                return;
            }

            try (DirectoryStream<Path> stream =
                         Files.newDirectoryStream(directory)) {

                for (Path path : stream) {
                    if (Files.isRegularFile(path)) {
                        System.out.println(
                                "\n--- " + path.getFileName()
                                        + " (" + Files.size(path)
                                        + " bytes) ---");

                        try (BufferedReader reader =
                                     Files.newBufferedReader(path)) {
                            reader.lines().forEach(System.out::println);
                        }
                    }
                }
            }
        }
    }

    // 21. BANK SERVICE: MAIN BUSINESS LOGIC
    static class BankService {
        private final Repository<Account> repository = new Repository<>();
        private final AccountStore store = new AccountStore();

        private final Path state = Path.of("accounts.dat");
        private final Path log = Path.of("logs", "transactions.log");

        BankService() {
            try {
                for (Account account : StatePersister.load(state)) {
                    repository.save(account);
                    store.add(account);
                }
            } catch (Exception e) {
                System.out.println(
                        "Starting with empty data: " + e.getMessage());
            }
        }

        Account openAccount(String name, long opening, int type)
                throws BankException {

            if (name == null || name.isBlank() || name.length() > 60) {
                throw new BankException(
                        "Owner name must contain 1 to 60 characters.");
            }

            if (opening < 0) {
                throw new BankException(
                        "Opening balance cannot be negative.");
            }

            Account account = switch (type) {
                case 1 -> new SavingsAccount(name, opening, 500);
                case 2 -> new CurrentAccount(name, opening, 5000);
                case 3 -> new FixedDepositAccount(name, opening);
                default -> throw new BankException("Invalid account type.");
            };

            repository.save(account);
            store.add(account);

            record("OPEN " + account.getAccountNumber()
                    + " " + opening);

            return account;
        }

        Account find(String id) throws AccountNotFoundException {
            Account account = repository.findById(id);

            if (account == null) {
                throw new AccountNotFoundException(id);
            }

            return account;
        }

        void deposit(String id, long amount) throws BankException {
            Account account = find(id);
            account.deposit(amount);

            record("DEPOSIT " + id + " " + amount);
        }

        void withdraw(String id, long amount) throws BankException {
            Account account = find(id);
            account.withdraw(amount);

            record("WITHDRAW " + id + " " + amount);
        }

        void transfer(String from, String to, long amount)
                throws BankException {

            if (amount <= 0) {
                throw new InvalidAmountException();
            }

            Account source = find(from);
            Account destination = find(to);

            if (source.equals(destination)) {
                throw new BankException(
                        "Choose two different accounts.");
            }

            // Withdraw first, then deposit. Restore the source if deposit fails.
            source.withdraw(amount);

            try {
                destination.deposit(amount);
            } catch (BankException e) {
                try {
                    source.deposit(amount);
                } catch (BankException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }

            record("TRANSFER " + from + " -> " + to + " " + amount);
        }

        void statement(String id) throws BankException {
            System.out.println(
                    StatementFormatter.buildStatement(find(id)));
        }

        void listAccounts() {
            ArrayList<Account> accounts = store.sortedByBalance();

            if (accounts.isEmpty()) {
                System.out.println("No accounts available.");
                return;
            }

            for (Account account : accounts) {
                System.out.println(account);
            }
        }

        void showHistory() {
            List<String> history = store.history();

            if (history.isEmpty()) {
                System.out.println("No transactions in this session.");
                return;
            }

            for (String entry : history) {
                System.out.println(entry);
            }
        }

        void report() {
            try {
                ReportGenerator.report(Path.of("logs"));
            } catch (IOException e) {
                System.out.println(
                        "Report error: " + e.getMessage());
            }
        }

        void batch() {
            List<Runnable> jobs = new ArrayList<>();

            for (Account account : store.all()) {
                jobs.add(() -> {
                    try {
                        account.deposit(1);
                        record("BATCH DEPOSIT "
                                + account.getAccountNumber() + " 1");
                    } catch (BankException ignored) {
                        // Inactive accounts are skipped.
                    }
                });
            }

            try (TransactionProcessor processor =
                         new TransactionProcessor()) {
                processor.process(jobs);
            }

            System.out.println("Batch processing completed.");
        }

        void save() {
            try {
                StatePersister.save(
                        store.all().toArray(new Account[0]), state);
            } catch (IOException e) {
                System.out.println(
                        "Could not save accounts: " + e.getMessage());
            }
        }

        private void record(String message) {
            store.log(message);

            try {
                TransactionLog.append(log, message);
            } catch (IOException e) {
                System.out.println(
                        "Log warning: " + e.getMessage());
            }
        }
    }

    // 22. MAIN MENU
    public static void main(String[] args) {
        System.out.println(
                new BankInfo("MiniBank", "Charusat Branch"));

        BankService bank = new BankService();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("\n========== MINIBANK MENU ==========");
            System.out.println("1.  Open account");
            System.out.println("2.  Deposit");
            System.out.println("3.  Withdraw");
            System.out.println("4.  Transfer money");
            System.out.println("5.  View statement");
            System.out.println("6.  List accounts");
            System.out.println("7.  View reports");
            System.out.println("8.  Batch deposit");
            System.out.println("9.  Transaction history");
            System.out.println("10. Validate email/mobile/PAN/IFSC");
            System.out.println("11. Parse transaction command");
            System.out.println("12. Run worker-thread demonstration");
            System.out.println("13. Exit and save");
            System.out.print("Choice: ");

            String input = scanner.nextLine().trim();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid menu number.");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Owner name: ");
                        String name = scanner.nextLine();

                        System.out.print("Opening balance: ");
                        long balance =
                                Long.parseLong(scanner.nextLine());

                        System.out.println(
                                "1. Savings  2. Current  3. Fixed Deposit");
                        System.out.print("Account type: ");
                        int type = Integer.parseInt(scanner.nextLine());

                        Account account =
                                bank.openAccount(name, balance, type);

                        System.out.println("Account created:");
                        System.out.println(account);
                    }

                    case 2 -> {
                        System.out.print("Account number: ");
                        String id = scanner.nextLine();

                        System.out.print("Amount: ");
                        long amount =
                                Long.parseLong(scanner.nextLine());

                        bank.deposit(id, amount);
                        System.out.println("Deposit successful.");
                    }

                    case 3 -> {
                        System.out.print("Account number: ");
                        String id = scanner.nextLine();

                        System.out.print("Amount: ");
                        long amount =
                                Long.parseLong(scanner.nextLine());

                        bank.withdraw(id, amount);
                        System.out.println("Withdrawal successful.");
                    }

                    case 4 -> {
                        System.out.print("From account: ");
                        String from = scanner.nextLine();

                        System.out.print("To account: ");
                        String to = scanner.nextLine();

                        System.out.print("Amount: ");
                        long amount =
                                Long.parseLong(scanner.nextLine());

                        bank.transfer(from, to, amount);
                        System.out.println("Transfer successful.");
                    }

                    case 5 -> {
                        System.out.print("Account number: ");
                        bank.statement(scanner.nextLine());
                    }

                    case 6 -> bank.listAccounts();

                    case 7 -> bank.report();

                    case 8 -> bank.batch();

                    case 9 -> bank.showHistory();

                    case 10 -> {
                        System.out.print("Email: ");
                        String email = scanner.nextLine();

                        System.out.print("Mobile: ");
                        String mobile = scanner.nextLine();

                        System.out.print("PAN: ");
                        String pan = scanner.nextLine();

                        System.out.print("IFSC: ");
                        String ifsc = scanner.nextLine();

                        System.out.println(
                                "Email valid: "
                                        + Validator.isValidEmail(email));
                        System.out.println(
                                "Mobile valid: "
                                        + Validator.isValidMobile(mobile));
                        System.out.println(
                                "PAN valid: "
                                        + Validator.isValidPan(pan));
                        System.out.println(
                                "IFSC valid: "
                                        + Validator.isValidIfsc(ifsc));
                    }

                    case 11 -> {
                        System.out.println(
                                "Enter command: TYPE ACCOUNT AMOUNT");
                        System.out.println(
                                "Example: DEPOSIT AC0001 500");

                        Command command =
                                CommandParser.parse(scanner.nextLine());

                        System.out.println("Parsed command: " + command);
                    }

                    case 12 -> {
                        System.out.print("Account number: ");
                        Account account = bank.find(scanner.nextLine());

                        System.out.print("Number of deposits: ");
                        int times = Integer.parseInt(scanner.nextLine());

                        System.out.print("Amount per deposit: ");
                        long amount =
                                Long.parseLong(scanner.nextLine());

                        if (times < 0 || amount <= 0) {
                            throw new IllegalArgumentException(
                                    "Count must be nonnegative and amount positive.");
                        }

                        Thread worker = new Thread(
                                new AccountWorker(account, times, amount));

                        worker.start();
                        worker.join();

                        System.out.println(
                                "Worker finished. Current balance: "
                                        + account.getBalance());
                    }

                    case 13 -> {
                        bank.save();
                        running = false;
                        System.out.println("Accounts saved. Goodbye!");
                    }

                    default -> System.out.println("Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            } catch (BankException e) {
                System.out.println(
                        "Operation failed: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(
                        "Invalid input: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Operation interrupted.");
            } finally {
                System.out.println("Operation handled.");
            }
        }

        bank.save();
        scanner.close();
    }
}
