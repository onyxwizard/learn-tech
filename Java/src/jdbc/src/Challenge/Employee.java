package Challenge;

import java.util.Date;

/**
 * @author onyxwizard
 * Date : 16-01-2026
 */

class Employee {
    private int id;
    private String name;
    private String department;
    private double salary;
    private Date hireDate;

    public Employee(int id, String name, String department, double salary, Date hireDate) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
}