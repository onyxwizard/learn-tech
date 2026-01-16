package Challenge;


import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * EmployeeDB - JDBC-based Data Access Object for Employee operations
 * Implements CRUD operations, batch processing, and transaction management
 *
 * @author onyxwizard
 * Date: 16-01-2026
 */
public class EmployeeDB {
    // Database configuration parameters
    private final String url = "jdbc:mysql://localhost:3306/staff";
    private final String user = "user";
    private final String pass = "password";

    // Database connection - marked volatile for thread safety in singleton pattern
    private Connection con = null;

    /**
     * Constructor - initializes database connection
     *
     * @throws SQLException if connection establishment fails
     */
    public EmployeeDB() throws SQLException {
        // Lazy initialization of connection
        if (con == null || con.isClosed()) {
            con = DbConnection.connect(url, user, pass);
            System.out.println("Database connection established successfully.");
        }
    }

    // ------------------------- TEST 1: BASIC CRUD OPERATIONS -------------------------

    /**
     * Retrieves all employees from the database
     *
     * @return List<Employee> containing all employee records
     * @throws SQLException if database access error occurs
     */
    public List<Employee> getAllEmployees() throws SQLException {
        String query = "SELECT * FROM employees";
        List<Employee> empList = new ArrayList<>();

        // Use try-with-resources for automatic resource management
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterate through result set and map to Employee objects
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String dept = rs.getString("department");
                Double salary = rs.getDouble("salary");
                Date date = rs.getDate("hire_date");

                // Create Employee object and add to list
                Employee employee = new Employee(id, name, dept, salary, date);
                empList.add(employee);

                // Log employee details for debugging
                System.out.printf("Retrieved: ID=%d, Name=%s, Dept=%s, Salary=%.2f, HireDate=%s%n",
                        id, name, dept, salary, date);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employees: " + e.getMessage());
            throw e; // Re-throw to maintain exception transparency
        }

        return empList;
    }

    /**
     * Adds a new employee to the database
     *
     * @param emp Employee object containing employee details
     * @return true if insertion successful, false otherwise
     * @throws SQLException if database access error occurs
     */
    public boolean addEmployee(Employee emp) throws SQLException {
        // Validate employee ID doesn't already exist
        if (checkEmployeeIdExist(emp.getId())) {
            System.out.println("Employee ID " + emp.getId() + " already exists!");
            return false;
        }

        // Use PreparedStatement to prevent SQL injection
        String query = "INSERT INTO employees (id, name, department, salary, hire_date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Bind parameters to PreparedStatement (1-indexed)
            ps.setInt(1, emp.getId());
            ps.setString(2, emp.getName());
            ps.setString(3, emp.getDepartment());
            ps.setDouble(4, emp.getSalary());

            // Convert java.util.Date to java.sql.Date
            if (emp.getHireDate() != null) {
                ps.setDate(5, new java.sql.Date(emp.getHireDate().getTime()));
            } else {
                ps.setNull(5, Types.DATE); // Handle null hire date
            }

            // Execute update and check affected rows
            int rowsAffected = ps.executeUpdate();
            boolean success = rowsAffected > 0;

            System.out.println("Insert " + (success ? "successful" : "failed") +
                    ". Rows affected: " + rowsAffected);
            return success;

        } catch (SQLException e) {
            // Handle specific SQL error codes
            if (e.getErrorCode() == 1062) { // MySQL duplicate entry error code
                System.out.println("Duplicate employee entry detected!");
            } else {
                System.err.println("SQL Error adding employee: " + e.getMessage());
            }
            throw e; // Re-throw for higher-level handling
        }
    }

    // ------------------------- TEST 2: ADVANCED OPERATIONS -------------------------

    /**
     * Retrieves employees filtered by department, ordered by salary descending
     *
     * @param dept Department name to filter by
     * @return List<Employee> filtered by specified department
     * @throws SQLException if database access error occurs
     */
    public List<Employee> getEmployeesByDepartment(String dept) throws SQLException {
        // FIXED: Removed quotes around parameter placeholder
        String query = "SELECT * FROM employees WHERE department = ? ORDER BY salary DESC";
        List<Employee> empList = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, dept); // Bind department parameter

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new Employee(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getDouble("salary"),
                            rs.getDate("hire_date")
                    );
                    empList.add(employee);

                    // Log retrieved employee
                    System.out.printf("Dept [%s]: ID=%d, Name=%s, Salary=%.2f%n",
                            dept, employee.getId(), employee.getName(), employee.getSalary());
                }
            }
        }

        System.out.println("Found " + empList.size() + " employees in department: " + dept);
        return empList;
    }

    /**
     * Updates an employee's salary
     *
     * @param employeeId ID of employee to update
     * @param newSalary  New salary value
     * @return true if update successful, false if employee not found
     * @throws SQLException if database access error occurs
     */
    public boolean updateSalary(int employeeId, double newSalary) throws SQLException {
        // Validate employee exists before update
        if (!checkEmployeeIdExist(employeeId)) {
            System.out.println("Cannot update: Employee ID " + employeeId + " does not exist");
            return false;
        }

        String query = "UPDATE employees SET salary = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setDouble(1, newSalary);
            ps.setInt(2, employeeId);

            int rowsUpdated = ps.executeUpdate();
            boolean success = rowsUpdated > 0;

            System.out.println("Salary update for ID " + employeeId +
                    (success ? " successful" : " failed"));
            return success;
        }
    }

    /**
     * Checks if an employee ID exists in the database
     *
     * @param id Employee ID to check
     * @return true if employee exists, false otherwise
     * @throws SQLException if database access error occurs
     */
    public boolean checkEmployeeIdExist(int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM employees WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // Simplified: returns true if count > 0
                }
            }
        }
        return false;
    }

    // ------------------------- TEST 3: TRANSACTION MANAGEMENT -------------------------

    /**
     * Transfers salary between two employees with transaction safety
     * Implements ACID properties: Atomicity, Consistency, Isolation, Durability
     *
     * @param fromEmpId Source employee ID
     * @param toEmpId   Destination employee ID
     * @param amount    Amount to transfer
     * @return true if transfer successful, false otherwise
     */
    public boolean transferSalary(int fromEmpId, int toEmpId, double amount) {
        // Pre-validation checks
        if (!validateTransferPreconditions(fromEmpId, toEmpId, amount)) {
            return false;
        }

        // Disable auto-commit to start transaction
        try {
            con.setAutoCommit(false);

            // Perform transfer operations
            boolean transferSuccessful = performSalaryTransfer(fromEmpId, toEmpId, amount);

            if (transferSuccessful) {
                con.commit(); // Commit transaction
                System.out.println("Salary transfer completed successfully.");
                return true;
            } else {
                con.rollback(); // Rollback on failure
                System.out.println("Salary transfer failed - transaction rolled back.");
                return false;
            }

        } catch (SQLException e) {
            // Rollback on any exception
            try {
                if (con != null) {
                    con.rollback();
                    System.out.println("Transaction rolled back due to error: " + e.getMessage());
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Critical: Rollback failed: " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            // Always restore auto-commit and NEVER close connection here
            try {
                if (con != null && !con.isClosed()) {
                    con.setAutoCommit(true); // Restore default behavior
                }
            } catch (SQLException e) {
                System.err.println("Warning: Could not restore auto-commit: " + e.getMessage());
            }
        }
    }

    /**
     * Validates preconditions for salary transfer
     *
     * @param fromEmpId Source employee ID
     * @param toEmpId   Destination employee ID
     * @param amount    Transfer amount
     * @return true if all preconditions satisfied
     */
    private boolean validateTransferPreconditions(int fromEmpId, int toEmpId, double amount) {
        try {
            // Check both employees exist
            if (!checkEmployeeIdExist(fromEmpId) || !checkEmployeeIdExist(toEmpId)) {
                System.out.println("Transfer failed: One or both employees not found");
                return false;
            }

            // Check sufficient balance
            if (!checkBalance(fromEmpId, amount)) {
                System.out.println("Transfer failed: Insufficient balance in source account");
                return false;
            }

            // Prevent transfer to same account
            if (fromEmpId == toEmpId) {
                System.out.println("Transfer failed: Cannot transfer to same account");
                return false;
            }

            // Validate amount is positive
            if (amount <= 0) {
                System.out.println("Transfer failed: Amount must be positive");
                return false;
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Validation error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Performs the actual salary transfer operations
     *
     * @param fromEmpId Source employee ID
     * @param toEmpId   Destination employee ID
     * @param amount    Transfer amount
     * @return true if both operations successful
     * @throws SQLException if database error occurs
     */
    private boolean performSalaryTransfer(int fromEmpId, int toEmpId, double amount) throws SQLException {
        // Deduct from source
        String deductSql = "UPDATE employees SET salary = salary - ? WHERE id = ?";
        try (PreparedStatement deductStmt = con.prepareStatement(deductSql)) {
            deductStmt.setDouble(1, amount);
            deductStmt.setInt(2, fromEmpId);
            int deductRows = deductStmt.executeUpdate();

            if (deductRows != 1) {
                System.out.println("Failed to deduct from source employee");
                return false;
            }
        }

        // Add to destination
        String addSql = "UPDATE employees SET salary = salary + ? WHERE id = ?";
        try (PreparedStatement addStmt = con.prepareStatement(addSql)) {
            addStmt.setDouble(1, amount);
            addStmt.setInt(2, toEmpId);
            int addRows = addStmt.executeUpdate();

            if (addRows != 1) {
                System.out.println("Failed to add to destination employee");
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if employee has sufficient balance for transfer
     *
     * @param id     Employee ID
     * @param amount Amount to check
     * @return true if sufficient balance
     * @throws SQLException if database error occurs
     */
    public boolean checkBalance(int id, double amount) throws SQLException {
        String query = "SELECT salary FROM employees WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double currentSalary = rs.getDouble(1);
                    boolean sufficient = currentSalary >= amount;

                    if (!sufficient) {
                        System.out.printf("Insufficient balance: Current=%.2f, Required=%.2f%n",
                                currentSalary, amount);
                    }
                    return sufficient;
                }
            }
        }
        System.out.println("Employee ID " + id + " not found for balance check");
        return false;
    }

    // ------------------------- TEST 4: BATCH PROCESSING -------------------------

    /**
     * Inserts multiple employees using JDBC batch processing
     * More efficient than individual inserts for bulk operations
     *
     * @param employees List of Employee objects to insert
     * @return int[] array of update counts for each statement
     */
    public int[] addMultipleEmployees(List<Employee> employees) {
        // Validate input
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees provided for batch insert");
            return new int[0];
        }

        String sql = "INSERT INTO employees (name, department, salary, hire_date) VALUES (?, ?, ?, ?)";

        // Note: Connection should not be closed in this method - managed by class lifecycle
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            // Disable auto-commit for transaction safety
            con.setAutoCommit(false);

            System.out.println("Starting batch insert for " + employees.size() + " employees...");

            // Add each employee to batch
            for (Employee emp : employees) {
                pstmt.setString(1, emp.getName());
                pstmt.setString(2, emp.getDepartment());
                pstmt.setDouble(3, emp.getSalary());

                // Handle date conversion safely
                if (emp.getHireDate() != null) {
                    pstmt.setDate(4, new java.sql.Date(emp.getHireDate().getTime()));
                } else {
                    pstmt.setNull(4, Types.DATE);
                }

                pstmt.addBatch(); // Add current statement to batch
            }

            // Execute batch and measure performance
            long startTime = System.currentTimeMillis();
            int[] updateCounts = pstmt.executeBatch();
            long endTime = System.currentTimeMillis();

            // Commit transaction
            con.commit();

            System.out.printf("Batch insert completed in %d ms%n", (endTime - startTime));
            printBatchResults(updateCounts);

            return updateCounts;

        } catch (SQLException e) {
            // Rollback on batch failure
            try {
                if (con != null) {
                    con.rollback();
                    System.out.println("Batch insert rolled back due to error");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Critical: Rollback failed during batch insert: " + rollbackEx.getMessage());
            }

            System.err.println("Batch insert failed: " + e.getMessage());
            return new int[0];
        } finally {
            // Always restore auto-commit
            try {
                if (con != null && !con.isClosed()) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Warning: Could not restore auto-commit after batch: " + e.getMessage());
            }
        }
    }

    /**
     * Prints detailed results of batch operation
     *
     * @param updateCounts Array of update counts from executeBatch()
     */
    private void printBatchResults(int[] updateCounts) {
        if (updateCounts.length == 0) {
            System.out.println("No batch results to display");
            return;
        }

        System.out.println("\nBatch Results Summary:");
        System.out.println("----------------------");

        int successCount = 0;
        int failedCount = 0;

        for (int i = 0; i < updateCounts.length; i++) {
            if (updateCounts[i] >= 0) {
                successCount++;
                System.out.printf("  Statement %d: SUCCESS (affected %d rows)%n", i + 1, updateCounts[i]);
            } else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                failedCount++;
                System.out.printf("  Statement %d: FAILED%n", i + 1);
            } else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
                successCount++;
                System.out.printf("  Statement %d: SUCCESS (no row count)%n", i + 1);
            }
        }

        System.out.printf("\nTotal: %d successful, %d failed%n", successCount, failedCount);
    }

    // ------------------------- UTILITY METHODS -------------------------

    /**
     * Safely closes the database connection
     * Should be called when EmployeeDB instance is no longer needed
     *
     * @throws SQLException if closing fails
     */
    public void closeConnection() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
            System.out.println("Database connection closed successfully.");
        } else {
            System.out.println("Connection already closed or was null.");
        }
    }

    /**
     * Checks if connection is still valid
     *
     * @return true if connection is open and valid
     */
    public boolean isConnectionValid() {
        try {
            return con != null && !con.isClosed() && con.isValid(2); // 2 second timeout
        } catch (SQLException e) {
            return false;
        }
    }

    // ------------------------- TEST DATA GENERATOR -------------------------

    /**
     * Creates test employee data for demonstration
     *
     * @return List of Employee objects
     */
    private static List<Employee> createTestEmployees() {
        List<Employee> employees = new ArrayList<>();

        // Note: ID should be 0 or null for auto-increment to work properly
        employees.add(new Employee(0, "Michael Scott", "Management", 85000.00,
                java.sql.Date.valueOf("2023-03-15")));
        employees.add(new Employee(0, "Pam Beesly", "Reception", 55000.00,
                java.sql.Date.valueOf("2023-04-20")));
        employees.add(new Employee(0, "Jim Halpert", "Sales", 72000.00,
                java.sql.Date.valueOf("2022-11-10")));
        employees.add(new Employee(0, "Dwight Schrute", "Sales", 68000.00,
                java.sql.Date.valueOf("2021-08-05")));
        employees.add(new Employee(0, "Angela Martin", "Accounting", 62000.00,
                java.sql.Date.valueOf("2020-12-01")));

        return employees;
    }

    // ------------------------- MAIN METHOD FOR TESTING -------------------------

    /**
     * Main method for testing EmployeeDB functionality
     * Demonstrates all major features with error handling
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        EmployeeDB db = null;

        try {
            // Initialize database connection
            db = new EmployeeDB();
            System.out.println("=== EmployeeDB Test Suite ===\n");

            // Test 1: Retrieve all employees
            System.out.println("1. Testing getAllEmployees():");
            List<Employee> allEmployees = db.getAllEmployees();
            System.out.println("Retrieved " + allEmployees.size() + " employees.\n");

            // Test 2: Batch insert
            System.out.println("2. Testing batch insert:");
            List<Employee> testEmployees = createTestEmployees();
            int[] batchResults = db.addMultipleEmployees(testEmployees);
            System.out.println("Batch insert completed with " + batchResults.length + " results.\n");

            // Test 3: Get by department
            System.out.println("3. Testing getEmployeesByDepartment('Sales'):");
            List<Employee> salesEmployees = db.getEmployeesByDepartment("Sales");
            System.out.println("Found " + salesEmployees.size() + " sales employees.\n");

            // Test 4: Update salary
            System.out.println("4. Testing updateSalary():");
            boolean updateSuccess = db.updateSalary(1, 80000.00);
            System.out.println("Salary update " + (updateSuccess ? "succeeded" : "failed") + "\n");

            // Test 5: Salary transfer (if sufficient data exists)
            System.out.println("5. Testing salary transfer:");
            if (allEmployees.size() >= 2) {
                boolean transferSuccess = db.transferSalary(1, 2, 5000.00);
                System.out.println("Salary transfer " + (transferSuccess ? "succeeded" : "failed"));
            } else {
                System.out.println("Skipped: Need at least 2 employees for transfer test");
            }

        } catch (SQLException e) {
            System.err.println("Database error in main test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure connection is closed
            if (db != null) {
                try {
                    db.closeConnection();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}