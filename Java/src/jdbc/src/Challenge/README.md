## 🎯 **JDBC PRACTICAL TEST**

### **SETUP: Database Schema**
We'll use this simple schema:
```sql
CREATE TABLE employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    salary DECIMAL(10, 2),
    hire_date DATE
);

INSERT INTO employees (name, department, salary, hire_date) VALUES
('John Doe', 'IT', 75000, '2022-01-15'),
('Jane Smith', 'HR', 65000, '2021-03-22'),
('Bob Johnson', 'IT', 82000, '2020-06-10'),
('Alice Brown', 'Finance', 70000, '2023-02-28');
```

---

## **TEST 1: Basic CRUD Operations**

### **Question 1:** Write a method to get ALL employees from the database using raw JDBC

**Signature:** `List<Employee> getAllEmployees()`

**Requirements:**
- Use PreparedStatement or Statement
- Handle all exceptions
- Close all resources properly
- Map ResultSet to Employee objects

**Employee class is given:**
```java
class Employee {
    private int id;
    private String name;
    private String department;
    private double salary;
    private Date hireDate;
    // getters/setters
}
```

**Try writing the code before looking at solution!**

---

### **Question 2:** Write a method to add a new employee

**Signature:** `boolean addEmployee(Employee emp)`

**Requirements:**
- Use PreparedStatement to prevent SQL injection
- Return true if insertion successful
- Handle duplicate entries gracefully

---

## **TEST 2: Advanced Operations**

### **Question 3:** Write a method to update an employee's salary

**Signature:** `boolean updateSalary(int employeeId, double newSalary)`

**Requirements:**
- Use PreparedStatement
- Return true if update affected at least 1 row
- Handle case where employee doesn't exist

---

### **Question 4:** Write a method to get employees by department

**Signature:** `List<Employee> getEmployeesByDepartment(String dept)`

**Requirements:**
- Use PreparedStatement with parameter
- Order by salary descending

---

## **TEST 3: Transaction Management**

### **Question 5:** Transfer salary between two employees

**Scenario:** Company wants to transfer $5000 from Employee A to Employee B

**Signature:** `boolean transferSalary(int fromEmpId, int toEmpId, double amount)`

**Requirements:**
- Use transactions
- Both updates must succeed or both fail
- Check if fromEmp has sufficient salary
- Return true if transfer successful

---

## **TEST 4: Batch Processing**

### **Question 6:** Insert multiple employees at once

**Signature:** `int[] addMultipleEmployees(List<Employee> employees)`

**Requirements:**
- Use batch processing
- Return array of update counts
- Use try-with-resources

---

## **TEST 5: Connection Management**

### **Question 7:** Write a proper DatabaseConnection class

**Requirements:**
- Singleton pattern for Connection
- Connection pooling basics
- Proper cleanup method
- Configurable parameters

---


## 📊 **SELF-ASSESSMENT CHECKLIST**

Score yourself (1-5 for each):

| Skill | Score | Notes |
|-------|-------|-------|
| **Connection Management** | ⬜⬜⬜⬜⬜ | Opening/closing connections properly |
| **PreparedStatement Usage** | ⬜⬜⬜⬜⬜ | Preventing SQL injection |
| **ResultSet Handling** | ⬜⬜⬜⬜⬜ | Processing query results |
| **Transaction Management** | ⬜⬜⬜⬜⬜ | commit(), rollback(), setAutoCommit() |
| **Exception Handling** | ⬜⬜⬜⬜⬜ | Try-catch-finally, specific exceptions |
| **Resource Cleanup** | ⬜⬜⬜⬜⬜ | Closing in finally block |
| **Batch Processing** | ⬜⬜⬜⬜⬜ | addBatch(), executeBatch() |
| **Connection Pooling** | ⬜⬜⬜⬜⬜ | Understanding pooling concepts |

### **Scoring:**
- **25-30**: Ready for SQL + Spring Boot!
- **20-24**: Good, review weak areas
- **15-19**: Needs more JDBC practice
- **Below 15**: Revisit JDBC fundamentals



## 🚀 **NEXT STEPS BASED ON YOUR SCORE:**

### **If score ≥ 20:**
✅ **You're ready for SQL!** Focus on:
1. **Complex Joins** (INNER, LEFT, RIGHT, FULL)
2. **Subqueries** (correlated, non-correlated)
3. **Indexes & Optimization** (EXPLAIN command)
4. **Stored Procedures**

### **If score 15-19:**
🔄 **Spend 2 more days on JDBC:**
1. Build a complete project with all 7 solutions above
2. Add logging (Log4j/SLF4j)
3. Add proper error messages
4. Then move to SQL

### **If score < 15:**
📚 **Revisit fundamentals:**
1. Complete [Oracle JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)
2. Build 3 small projects:
    - Student Management System
    - Library Book Tracker
    - Bank Account System
3. Then retest



## 💡 **QUICK DIAGNOSTIC:**

**Answer these quickly:**

1. What's the order to close Connection, Statement, ResultSet?
2. Why use PreparedStatement instead of Statement?
3. What does `executeUpdate()` return?
4. When should you use `setAutoCommit(false)`?
5. What's the difference between `execute()` and `executeQuery()`?

**Answers:**
1. ResultSet → Statement → Connection (reverse order)
2. Prevents SQL injection, better performance
3. Number of rows affected (int)
4. When doing multiple operations as single transaction
5. `executeQuery()` for SELECT, `execute()` for any SQL



## 🎯 **FINAL VERDICT:**

Based on how you solved these problems:

**If you could write Solutions 1-4 without help →** You're ready for SQL!
**If you struggled with Solutions 5-7 →** That's normal! Those are advanced.

**My recommendation:** Complete Solutions 1-4 confidently, then start SQL while occasionally practicing 5-7.
