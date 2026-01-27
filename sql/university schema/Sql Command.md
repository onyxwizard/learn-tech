# Lets Start Setting up the DB
## Step 1: Create the Database

```sql
-- Create the University database
CREATE DATABASE IF NOT EXISTS University;
USE University;
```

## Step 2: Create Tables with Proper Relationships

### Department Table
```sql
CREATE TABLE IF NOT EXISTS Department (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    department_name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Subject Table (Separate table for better normalization)
```sql
CREATE TABLE IF NOT EXISTS Subject (
    subject_id INT PRIMARY KEY AUTO_INCREMENT,
    subject_name VARCHAR(100) NOT NULL,
    department_id INT,
    credits INT DEFAULT 3,
    FOREIGN KEY (department_id) REFERENCES Department(department_id) ON DELETE SET NULL,
    UNIQUE KEY unique_subject_dept (subject_name, department_id)
);
```

### Student Table
```sql
CREATE TABLE IF NOT EXISTS Student (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    id_number VARCHAR(20) UNIQUE NOT NULL, -- University ID number
    name VARCHAR(100) NOT NULL,
    age INT CHECK (age >= 16 AND age <= 70),
    course_id INT, -- Will reference subject_id
    state VARCHAR(50),
    city VARCHAR(50),
    country VARCHAR(50) DEFAULT 'India',
    phone_number VARCHAR(15),
    email VARCHAR(100) UNIQUE,
    enrollment_date DATE,
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES Department(department_id) ON DELETE SET NULL,
    INDEX idx_student_name (name),
    INDEX idx_student_city (city)
);
```

### Professor Table
```sql
CREATE TABLE IF NOT EXISTS Professor (
    professor_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    department_id INT,
    subject_id INT,
    email VARCHAR(100) UNIQUE,
    phone_number VARCHAR(15),
    hire_date DATE,
    FOREIGN KEY (department_id) REFERENCES Department(department_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id) ON DELETE SET NULL,
    INDEX idx_prof_name (name)
);
```

### Attendance Table
```sql
CREATE TABLE IF NOT EXISTS Attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    date DATE NOT NULL,
    subject_id INT NOT NULL,
    status ENUM('Present', 'Absent', 'Late', 'Excused') DEFAULT 'Absent',
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES Student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES Subject(subject_id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (student_id, date, subject_id),
    INDEX idx_attendance_date (date),
    INDEX idx_attendance_status (status)
);
```

## Step 3: Insert Sample Data
### Note {before Deleting any table}
```sql
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM Department;
ALTER TABLE department auto_increment=1;
SET FOREIGN_KEY_CHECKS = 1;
```

### Insert Departments
```sql
INSERT INTO Department (department_name) VALUES
('Computer Science'),
('Mathematics'),
('Physics'),
('Chemistry'),
('Electrical Engineering');
```
### Insert Subjects
```sql
-- Insert engineering departments
INSERT INTO Department (department_name) VALUES
('CSE'),
('IT'),
('ECE'),
('MECH'),
('CIVIL'),
('EEE');
```

## Step 2: Insert Semester-wise Subjects for Each Department
### For Computer Science and Engineering (CSE)

```sql
-- Get CSE department ID
SET @cse_dept_id = (SELECT department_id FROM Department WHERE department_name = 'CSE');
-- Semester 1 (Common for all engineering)
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Engineering Mathematics - I', @cse_dept_id, 4),
('Engineering Physics', @cse_dept_id, 4),
('Engineering Chemistry', @cse_dept_id, 4),
('Programming in C', @cse_dept_id, 4),
('Engineering Graphics', @cse_dept_id, 3),
('Basic Electrical Engineering', @cse_dept_id, 3);

-- Semester 2
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Engineering Mathematics - II', @cse_dept_id, 4),
('Data Structures', @cse_dept_id, 4),
('Digital Logic Design', @cse_dept_id, 4),
('Object Oriented Programming', @cse_dept_id, 4),
('Discrete Mathematics', @cse_dept_id, 3),
('Environmental Science', @cse_dept_id, 2);

-- Semester 3
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Database Management Systems', @cse_dept_id, 4),
('Computer Organization', @cse_dept_id, 4),
('Operating Systems', @cse_dept_id, 4),
('Theory of Computation', @cse_dept_id, 3),
('Software Engineering', @cse_dept_id, 3),
('Python Programming', @cse_dept_id, 3);

-- Semester 4
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Design and Analysis of Algorithms', @cse_dept_id, 4),
('Computer Networks', @cse_dept_id, 4),
('Microprocessors', @cse_dept_id, 4),
('Web Technologies', @cse_dept_id, 3),
('Machine Learning Fundamentals', @cse_dept_id, 3),
('Java Programming', @cse_dept_id, 3);
```

### For Information Technology (IT)
```sql
-- Get IT department ID
SET @it_dept_id = (SELECT department_id FROM Department WHERE department_name = 'IT');

-- Semester 1
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Engineering Mathematics - I', @it_dept_id, 4),
('Engineering Physics', @it_dept_id, 4),
('Fundamentals of IT', @it_dept_id, 4),
('Programming Fundamentals', @it_dept_id, 4),
('Digital Electronics', @it_dept_id, 3),
('Communication Skills', @it_dept_id, 2);

-- Semester 2
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Engineering Mathematics - II', @it_dept_id, 4),
('Data Structures and Algorithms', @it_dept_id, 4),
('Web Design and Development', @it_dept_id, 4),
('Object Oriented Programming with Java', @it_dept_id, 4),
('Discrete Structures', @it_dept_id, 3),
('Database Concepts', @it_dept_id, 3);

-- Semester 3
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Database Management Systems', @it_dept_id, 4),
('Computer Networks', @it_dept_id, 4),
('Operating Systems', @it_dept_id, 4),
('Software Engineering', @it_dept_id, 3),
('Probability and Statistics', @it_dept_id, 3),
('Python for Data Science', @it_dept_id, 3);

-- Semester 4
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Information Security', @it_dept_id, 4),
('Cloud Computing', @it_dept_id, 4),
('Mobile Application Development', @it_dept_id, 4),
('Data Analytics', @it_dept_id, 3),
('Internet of Things', @it_dept_id, 3),
('Cyber Security Fundamentals', @it_dept_id, 3);
```

### For Electronics and Communication Engineering (ECE)

```sql
-- Get ECE department ID
SET @ece_dept_id = (SELECT department_id FROM Department WHERE department_name = 'ECE');

-- Semester 1
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Engineering Mathematics - I', @ece_dept_id, 4),
('Engineering Physics', @ece_dept_id, 4),
('Engineering Chemistry', @ece_dept_id, 4),
('Basics of Electronics', @ece_dept_id, 4),
('Engineering Graphics', @ece_dept_id, 3),
('Workshop Practice', @ece_dept_id, 2);

-- Semester 2
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Engineering Mathematics - II', @ece_dept_id, 4),
('Network Analysis', @ece_dept_id, 4),
('Electronic Devices', @ece_dept_id, 4),
('Digital Electronics', @ece_dept_id, 4),
('Signals and Systems', @ece_dept_id, 3),
('C Programming for ECE', @ece_dept_id, 3);

-- Semester 3
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Analog Circuits', @ece_dept_id, 4),
('Electromagnetic Theory', @ece_dept_id, 4),
('Communication Systems', @ece_dept_id, 4),
('Microprocessors and Microcontrollers', @ece_dept_id, 3),
('Control Systems', @ece_dept_id, 3),
('Electronic Measurements', @ece_dept_id, 3);

-- Semester 4
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Digital Communication', @ece_dept_id, 4),
('VLSI Design', @ece_dept_id, 4),
('Antenna and Wave Propagation', @ece_dept_id, 4),
('Digital Signal Processing', @ece_dept_id, 3),
('Optical Communication', @ece_dept_id, 3),
('Embedded Systems', @ece_dept_id, 3);
```
### For Mechanical Engineering

```sql
-- Get Mechanical department ID
SET @mech_dept_id = (SELECT department_id FROM Department WHERE department_name = 'MECH');

-- Semester 1
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Engineering Mathematics - I', @mech_dept_id, 4),
('Engineering Physics', @mech_dept_id, 4),
('Engineering Chemistry', @mech_dept_id, 4),
('Engineering Mechanics', @mech_dept_id, 4),
('Engineering Graphics', @mech_dept_id, 3),
('Workshop Technology', @mech_dept_id, 2);

-- Semester 2
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Engineering Mathematics - II', @mech_dept_id, 4),
('Engineering Thermodynamics', @mech_dept_id, 4),
('Mechanics of Solids', @mech_dept_id, 4),
('Manufacturing Processes', @mech_dept_id, 4),
('Fluid Mechanics', @mech_dept_id, 3),
('Material Science', @mech_dept_id, 3);

-- Semester 3
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Heat Transfer', @mech_dept_id, 4),
('Theory of Machines', @mech_dept_id, 4),
('Machine Drawing', @mech_dept_id, 4),
('Kinematics of Machinery', @mech_dept_id, 3),
('Engineering Materials', @mech_dept_id, 3),
('Metrology and Measurements', @mech_dept_id, 3);

-- Semester 4
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Dynamics of Machinery', @mech_dept_id, 4),
('Machine Design', @mech_dept_id, 4),
('CAD/CAM', @mech_dept_id, 4),
('Automobile Engineering', @mech_dept_id, 3),
('Refrigeration and Air Conditioning', @mech_dept_id, 3),
('Industrial Engineering', @mech_dept_id, 3);
```
### For Civil

```sql
-- Add subjects for Civil Engineering
SET @civil_dept_id = (SELECT department_id FROM Department WHERE department_name = 'CIVIL');

-- Civil Engineering subjects (4 semesters)
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Engineering Mechanics', @civil_dept_id, 4),
('Building Materials', @civil_dept_id, 3),
('Surveying', @civil_dept_id, 4),
('Structural Analysis', @civil_dept_id, 4),
('Geotechnical Engineering', @civil_dept_id, 4),
('Transportation Engineering', @civil_dept_id, 4),
('Environmental Engineering', @civil_dept_id, 3),
('Construction Management', @civil_dept_id, 3),
('Hydraulics', @civil_dept_id, 4),
('Concrete Technology', @civil_dept_id, 3),
('Design of Structures', @civil_dept_id, 4),
('Foundation Engineering', @civil_dept_id, 3);
```
### For EEE
```sql
-- Add subjects for Electrical Engineering
SET @electrical_dept_id = (SELECT department_id FROM Department WHERE department_name = 'EEE');

-- Electrical Engineering subjects (4 semesters)
INSERT INTO Subject (subject_name, department_id, credits) VALUES
('Circuit Theory', @electrical_dept_id, 4),
('Electromagnetic Fields', @electrical_dept_id, 4),
('Electrical Machines', @electrical_dept_id, 4),
('Power Systems', @electrical_dept_id, 4),
('Control Systems', @electrical_dept_id, 4),
('Power Electronics', @electrical_dept_id, 4),
('Digital Signal Processing', @electrical_dept_id, 3),
('Renewable Energy Systems', @electrical_dept_id, 3),
('Microprocessors', @electrical_dept_id, 4),
('High Voltage Engineering', @electrical_dept_id, 3),
('Smart Grid Technology', @electrical_dept_id, 3),
('Electric Drives', @electrical_dept_id, 3);
```

## Step 3: Insert Professor-wise Subjects for Each Department

```sql
-- SET IT
SET @cse_dept_id = (SELECT department_id FROM Department WHERE department_name = 'CSE');
SET @it_dept_id = (SELECT department_id FROM Department WHERE department_name = 'IT');
SET @ece_dept_id = (SELECT department_id FROM Department WHERE department_name = 'ECE');
SET @mech_dept_id = (SELECT department_id FROM Department WHERE department_name = 'MECH');
SET @civil_dept_id = (SELECT department_id FROM Department WHERE department_name = 'CIVIL');
SET @electrical_dept_id = (SELECT department_id FROM Department WHERE department_name = 'EEE');


-- Create function to generate random phone numbers
DELIMITER $$
CREATE FUNCTION generate_phone() RETURNS VARCHAR(15)
DETERMINISTIC
BEGIN
    RETURN CONCAT('+91-', LPAD(FLOOR(RAND() * 9000000000) + 1000000000, 10, '0'));
END$$
DELIMITER ;

-- Create function to generate random dates
DELIMITER $$
CREATE FUNCTION random_date(start_date DATE, end_date DATE) RETURNS DATE
DETERMINISTIC
BEGIN
    RETURN DATE_ADD(start_date, INTERVAL FLOOR(RAND() * DATEDIFF(end_date, start_date)) DAY);
END$$
DELIMITER ;


-- Professor Table Data Insertion

-- Insert CSE Department Professors (15 professors)
INSERT INTO Professor (name, department_id, subject_id, email, phone_number, hire_date) VALUES
-- HOD and Senior Professors
('Dr. Rajesh Kumar', @cse_dept_id, 1, 'rajesh.kumar@university.edu', generate_phone(), random_date('2005-01-01', '2010-12-31')),
('Dr. Meena Sharma', @cse_dept_id, 2, 'meena.sharma@university.edu', generate_phone(), random_date('2006-06-15', '2011-06-30')),
('Dr. Arun Patel', @cse_dept_id, 4, 'arun.patel@university.edu', generate_phone(), random_date('2007-03-10', '2012-03-31')),
('Dr. Priya Singh', @cse_dept_id, 8, 'priya.singh@university.edu', generate_phone(), random_date('2008-08-20', '2013-08-31')),

-- Associate Professors
('Prof. Vikram Reddy', @cse_dept_id, 13, 'vikram.reddy@university.edu', generate_phone(), random_date('2012-01-01', '2015-12-31')),
('Prof. Anjali Gupta', @cse_dept_id, 14, 'anjali.gupta@university.edu', generate_phone(), random_date('2013-06-15', '2016-06-30')),
('Prof. Sanjay Verma', @cse_dept_id, 15, 'sanjay.verma@university.edu', generate_phone(), random_date('2014-03-10', '2017-03-31')),
('Prof. Neha Joshi', @cse_dept_id, 19, 'neha.joshi@university.edu', generate_phone(), random_date('2015-08-20', '2018-08-31')),

-- Assistant Professors
('Mr. Rahul Desai', @cse_dept_id, 20, 'rahul.desai@university.edu', generate_phone(), random_date('2018-01-01', '2020-12-31')),
('Ms. Sneha Kapoor', @cse_dept_id, 21, 'sneha.kapoor@university.edu', generate_phone(), random_date('2019-06-15', '2021-06-30')),
('Mr. Amit Trivedi', @cse_dept_id, 3, 'amit.trivedi@university.edu', generate_phone(), random_date('2020-03-10', '2022-03-31')),
('Ms. Pooja Nair', @cse_dept_id, 7, 'pooja.nair@university.edu', generate_phone(), random_date('2021-08-20', '2023-08-31')),

-- Visiting Faculty
('Dr. Suresh Iyer', @cse_dept_id, 10, 'suresh.iyer@university.edu', generate_phone(), random_date('2016-01-01', '2019-12-31')),
('Dr. Kavita Menon', @cse_dept_id, 16, 'kavita.menon@university.edu', generate_phone(), random_date('2017-06-15', '2020-06-30')),
('Mr. Deepak Rao', @cse_dept_id, 22, 'deepak.rao@university.edu', generate_phone(), random_date('2022-01-01', '2023-12-31'));

-- Insert IT Department Professors (12 professors)
INSERT INTO Professor (name, department_id, subject_id, email, phone_number, hire_date) VALUES
-- HOD and Senior Professors
('Dr. Sunil Nair', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id AND subject_name LIKE '%Database%' LIMIT 1), 
 'sunil.nair@university.edu', generate_phone(), random_date('2006-01-01', '2011-12-31')),
('Dr. Anitha Pillai', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id AND subject_name LIKE '%Networks%' LIMIT 1), 
 'anitha.pillai@university.edu', generate_phone(), random_date('2007-06-15', '2012-06-30')),

-- Associate Professors
('Prof. Manoj Kumar', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id AND subject_name LIKE '%Security%' LIMIT 1), 
 'manoj.kumar@university.edu', generate_phone(), random_date('2013-01-01', '2016-12-31')),
('Prof. Shweta Das', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id AND subject_name LIKE '%Cloud%' LIMIT 1), 
 'shweta.das@university.edu', generate_phone(), random_date('2014-06-15', '2017-06-30')),

-- Assistant Professors
('Ms. Ritu Sharma', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id AND subject_name LIKE '%Mobile%' LIMIT 1), 
 'ritu.sharma@university.edu', generate_phone(), random_date('2019-01-01', '2021-12-31')),
('Mr. Karthik Reddy', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id AND subject_name LIKE '%Python%' LIMIT 1), 
 'karthik.reddy@university.edu', generate_phone(), random_date('2020-06-15', '2022-06-30')),

-- More IT Professors
('Dr. Venkatesh Iyer', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id ORDER BY RAND() LIMIT 1), 
 'venkatesh.iyer@university.edu', generate_phone(), random_date('2010-01-01', '2015-12-31')),
('Prof. Lakshmi Menon', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id ORDER BY RAND() LIMIT 1), 
 'lakshmi.menon@university.edu', generate_phone(), random_date('2015-01-01', '2018-12-31')),
('Mr. Rohit Gupta', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id ORDER BY RAND() LIMIT 1), 
 'rohit.gupta@university.edu', generate_phone(), random_date('2021-01-01', '2023-12-31')),
('Ms. Anjali Chatterjee', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id ORDER BY RAND() LIMIT 1), 
 'anjali.chatterjee@university.edu', generate_phone(), random_date('2018-01-01', '2020-12-31')),
('Dr. Prakash Singh', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id ORDER BY RAND() LIMIT 1), 
 'prakash.singh@university.edu', generate_phone(), random_date('2009-01-01', '2014-12-31')),
('Prof. Meera Patel', @it_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @it_dept_id ORDER BY RAND() LIMIT 1), 
 'meera.patel@university.edu', generate_phone(), random_date('2016-01-01', '2019-12-31'));

-- Insert ECE Department Professors (12 professors)
INSERT INTO Professor (name, department_id, subject_id, email, phone_number, hire_date) VALUES
-- HOD and Senior Professors
('Dr. Gopalakrishnan Nair', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id AND subject_name LIKE '%Communication%' LIMIT 1), 
 'gopalakrishnan.nair@university.edu', generate_phone(), random_date('2004-01-01', '2009-12-31')),
('Dr. Sharmila Banerjee', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id AND subject_name LIKE '%VLSI%' LIMIT 1), 
 'sharmila.banerjee@university.edu', generate_phone(), random_date('2005-06-15', '2010-06-30')),

-- Associate Professors
('Prof. Ravi Shankar', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id AND subject_name LIKE '%Signal%' LIMIT 1), 
 'ravi.shankar@university.edu', generate_phone(), random_date('2011-01-01', '2014-12-31')),
('Prof. Sunita Rao', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id AND subject_name LIKE '%Embedded%' LIMIT 1), 
 'sunita.rao@university.edu', generate_phone(), random_date('2012-06-15', '2015-06-30')),

-- Assistant Professors (8 more)
('Mr. Aditya Joshi', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id ORDER BY RAND() LIMIT 1), 
 'aditya.joshi@university.edu', generate_phone(), random_date('2017-01-01', '2019-12-31')),
('Ms. Priyanka Reddy', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id ORDER BY RAND() LIMIT 1), 
 'priyanka.reddy@university.edu', generate_phone(), random_date('2018-06-15', '2020-06-30')),
('Dr. K. S. Nambiar', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id ORDER BY RAND() LIMIT 1), 
 'ks.nambiar@university.edu', generate_phone(), random_date('2008-01-01', '2013-12-31')),
('Prof. Arjun Menon', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id ORDER BY RAND() LIMIT 1), 
 'arjun.menon@university.edu', generate_phone(), random_date('2013-01-01', '2016-12-31')),
('Ms. Divya Sharma', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id ORDER BY RAND() LIMIT 1), 
 'divya.sharma@university.edu', generate_phone(), random_date('2019-01-01', '2021-12-31')),
('Mr. Varun Kumar', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id ORDER BY RAND() LIMIT 1), 
 'varun.kumar@university.edu', generate_phone(), random_date('2020-06-15', '2022-06-30')),
('Dr. Malini Sundaram', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id ORDER BY RAND() LIMIT 1), 
 'malini.sundaram@university.edu', generate_phone(), random_date('2007-01-01', '2012-12-31')),
('Prof. Suresh Iyengar', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id ORDER BY RAND() LIMIT 1), 
 'suresh.iyengar@university.edu', generate_phone(), random_date('2014-01-01', '2017-12-31')),
('Mr. Rajeev Nair', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id ORDER BY RAND() LIMIT 1), 
 'rajeev.nair@university.edu', generate_phone(), random_date('2021-01-01', '2023-12-31')),
('Ms. Ananya Das', @ece_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @ece_dept_id ORDER BY RAND() LIMIT 1), 
 'ananya.das@university.edu', generate_phone(), random_date('2016-01-01', '2018-12-31'));

-- Insert Mechanical Engineering Professors (10 professors)
INSERT INTO Professor (name, department_id, subject_id, email, phone_number, hire_date) VALUES
-- HOD and Senior Professors
('Dr. R. K. Sharma', @mech_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @mech_dept_id AND subject_name LIKE '%Thermodynamics%' LIMIT 1), 
 'rk.sharma@university.edu', generate_phone(), random_date('2003-01-01', '2008-12-31')),
('Dr. S. P. Singh', @mech_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @mech_dept_id AND subject_name LIKE '%Machine%Design%' LIMIT 1), 
 'sp.singh@university.edu', generate_phone(), random_date('2004-06-15', '2009-06-30')),

-- Associate and Assistant Professors (8 more)
('Prof. Vijay Kumar', @mech_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @mech_dept_id ORDER BY RAND() LIMIT 1), 
 'vijay.kumar@university.edu', generate_phone(), random_date('2010-01-01', '2015-12-31')),
('Prof. Geeta Verma', @mech_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @mech_dept_id ORDER BY RAND() LIMIT 1), 
 'geeta.verma@university.edu', generate_phone(), random_date('2011-06-15', '2016-06-30')),
('Mr. Alok Patel', @mech_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @mech_dept_id ORDER BY RAND() LIMIT 1), 
 'alok.patel@university.edu', generate_phone(), random_date('2017-01-01', '2019-12-31')),
('Ms. Nisha Reddy', @mech_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @mech_dept_id ORDER BY RAND() LIMIT 1), 
 'nisha.reddy@university.edu', generate_phone(), random_date('2018-06-15', '2020-06-30')),
('Dr. Ashok Joshi', @mech_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @mech_dept_id ORDER BY RAND() LIMIT 1), 
 'ashok.joshi@university.edu', generate_phone(), random_date('2006-01-01', '2011-12-31')),
('Prof. Kavita Singh', @mech_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @mech_dept_id ORDER BY RAND() LIMIT 1), 
 'kavita.singh@university.edu', generate_phone(), random_date('2012-01-01', '2015-12-31')),
('Mr. Ramesh Gupta', @mech_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @mech_dept_id ORDER BY RAND() LIMIT 1), 
 'ramesh.gupta@university.edu', generate_phone(), random_date('2019-01-01', '2021-12-31')),
('Ms. Swati Nair', @mech_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @mech_dept_id ORDER BY RAND() LIMIT 1), 
 'swati.nair@university.edu', generate_phone(), random_date('2020-06-15', '2022-06-30')),
('Dr. P. S. Namboothiri', @mech_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @mech_dept_id ORDER BY RAND() LIMIT 1), 
 'ps.namboothiri@university.edu', generate_phone(), random_date('2007-01-01', '2012-12-31'));

-- Insert Civil Engineering Professors (10 professors)
INSERT INTO Professor (name, department_id, subject_id, email, phone_number, hire_date) VALUES
('Dr. S. Ramanathan', @civil_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @civil_dept_id ORDER BY RAND() LIMIT 1), 
 's.ramanathan@university.edu', generate_phone(), random_date('2005-01-01', '2010-12-31')),
('Dr. Anjali Deshpande', @civil_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @civil_dept_id ORDER BY RAND() LIMIT 1), 
 'anjali.deshpande@university.edu', generate_phone(), random_date('2006-06-15', '2011-06-30')),
('Prof. Rajiv Kapoor', @civil_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @civil_dept_id ORDER BY RAND() LIMIT 1), 
 'rajiv.kapoor@university.edu', generate_phone(), random_date('2012-01-01', '2015-12-31')),
('Prof. Meenakshi Iyer', @civil_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @civil_dept_id ORDER BY RAND() LIMIT 1), 
 'meenakshi.iyer@university.edu', generate_phone(), random_date('2013-06-15', '2016-06-30')),
('Mr. Vikas Sharma', @civil_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @civil_dept_id ORDER BY RAND() LIMIT 1), 
 'vikas.sharma@university.edu', generate_phone(), random_date('2018-01-01', '2020-12-31')),
('Ms. Radhika Menon', @civil_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @civil_dept_id ORDER BY RAND() LIMIT 1), 
 'radhika.menon@university.edu', generate_phone(), random_date('2019-06-15', '2021-06-30')),
('Dr. K. Venkataraman', @civil_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @civil_dept_id ORDER BY RAND() LIMIT 1), 
 'k.venkataraman@university.edu', generate_phone(), random_date('2008-01-01', '2013-12-31')),
('Prof. Shalini Gupta', @civil_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @civil_dept_id ORDER BY RAND() LIMIT 1), 
 'shalini.gupta@university.edu', generate_phone(), random_date('2014-01-01', '2017-12-31')),
('Mr. Sameer Joshi', @civil_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @civil_dept_id ORDER BY RAND() LIMIT 1), 
 'sameer.joshi@university.edu', generate_phone(), random_date('2021-01-01', '2023-12-31')),
('Ms. Anuradha Rao', @civil_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @civil_dept_id ORDER BY RAND() LIMIT 1), 
 'anuradha.rao@university.edu', generate_phone(), random_date('2017-01-01', '2019-12-31'));

-- Insert Electrical Engineering Professors (11 professors)
INSERT INTO Professor (name, department_id, subject_id, email, phone_number, hire_date) VALUES
('Dr. M. S. Reddy', @electrical_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @electrical_dept_id AND subject_name LIKE '%Power%Systems%' LIMIT 1), 
 'ms.reddy@university.edu', generate_phone(), random_date('2004-01-01', '2009-12-31')),
('Dr. Latha Nair', @electrical_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @electrical_dept_id AND subject_name LIKE '%Electrical%Machines%' LIMIT 1), 
 'latha.nair@university.edu', generate_phone(), random_date('2005-06-15', '2010-06-30')),
('Prof. Sanjay Verma', @electrical_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @electrical_dept_id ORDER BY RAND() LIMIT 1), 
 'sanjay.verma@university.edu', generate_phone(), random_date('2011-01-01', '2014-12-31')),
('Prof. Anjali Kapoor', @electrical_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @electrical_dept_id ORDER BY RAND() LIMIT 1), 
 'anjali.kapoor@university.edu', generate_phone(), random_date('2012-06-15', '2015-06-30')),
('Mr. Rakesh Kumar', @electrical_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @electrical_dept_id ORDER BY RAND() LIMIT 1), 
 'rakesh.kumar@university.edu', generate_phone(), random_date('2017-01-01', '2019-12-31')),
('Ms. Shruti Sharma', @electrical_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @electrical_dept_id ORDER BY RAND() LIMIT 1), 
 'shruti.sharma@university.edu', generate_phone(), random_date('2018-06-15', '2020-06-30')),
('Dr. P. K. Menon', @electrical_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @electrical_dept_id ORDER BY RAND() LIMIT 1), 
 'pk.menon@university.edu', generate_phone(), random_date('2007-01-01', '2012-12-31')),
('Prof. Ritu Das', @electrical_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @electrical_dept_id ORDER BY RAND() LIMIT 1), 
 'ritu.das@university.edu', generate_phone(), random_date('2013-01-01', '2016-12-31')),
('Mr. Deepak Singh', @electrical_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @electrical_dept_id ORDER BY RAND() LIMIT 1), 
 'deepak.singh@university.edu', generate_phone(), random_date('2020-01-01', '2022-12-31')),
('Ms. Preeti Nair', @electrical_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @electrical_dept_id ORDER BY RAND() LIMIT 1), 
 'preeti.nair@university.edu', generate_phone(), random_date('2019-01-01', '2021-12-31')),
('Dr. S. Venkateswaran', @electrical_dept_id, (SELECT subject_id FROM Subject WHERE department_id = @electrical_dept_id ORDER BY RAND() LIMIT 1), 
 's.venkateswaran@university.edu', generate_phone(), random_date('2006-01-01', '2011-12-31'));
```

## Step 4: Insert Student Data

## Step 1: Modify Student Table Structure

```sql
-- First, let's check the current structure of the Student table
DESCRIBE Student;

-- Add gender column to the Student table
ALTER TABLE Student 
ADD COLUMN gender ENUM('Male', 'Female', 'Other') DEFAULT 'Male' AFTER age;

-- Let's also add some additional useful columns
ALTER TABLE Student 
ADD COLUMN date_of_birth DATE AFTER age,
ADD COLUMN semester INT DEFAULT 1 AFTER course_id,
ADD COLUMN academic_year VARCHAR(10) AFTER semester,
ADD COLUMN enrollment_id VARCHAR(20) UNIQUE AFTER id_number;

-- Update existing students with random genders
UPDATE Student 
SET gender = CASE 
    WHEN RAND() < 0.5 THEN 'Male' 
    ELSE 'Female' 
END;

-- View updated structure
DESCRIBE Student;
```

## Step 2: Create Functions for Random Data Generation

```sql
-- Function to generate random names

DELIMITER $$
CREATE FUNCTION generate_student_name(gender VARCHAR(10)) RETURNS VARCHAR(100)
DETERMINISTIC
BEGIN
    DECLARE first_name VARCHAR(50);
    DECLARE last_name VARCHAR(50);
    
    -- Indian first names based on gender
    IF gender = 'Male' THEN
        SET first_name = ELT(FLOOR(1 + RAND() * 20), 
            'Aarav', 'Vivaan', 'Aditya', 'Vihaan', 'Arjun', 'Sai', 'Reyansh', 'Atharv', 
            'Dhruv', 'Krishna', 'Kian', 'Kabir', 'Rudra', 'Veer', 'Aryan', 'Mohammed',
            'Rohan', 'Siddharth', 'Yash', 'Pranav'
        );
    ELSEIF gender = 'Female' THEN
        SET first_name = ELT(FLOOR(1 + RAND() * 20), 
            'Saanvi', 'Ananya', 'Diya', 'Aadhya', 'Ishita', 'Myra', 'Avni', 'Anika', 
            'Pari', 'Navya', 'Riya', 'Sara', 'Aarohi', 'Anvi', 'Kiara', 'Prisha',
            'Zara', 'Sara', 'Anaya', 'Advika'
        );
    ELSE
        SET first_name = ELT(FLOOR(1 + RAND() * 20), 
            'Alex', 'Taylor', 'Jordan', 'Riley', 'Casey', 'Jamie', 'Morgan', 'Quinn',
            'Dakota', 'Skyler', 'Rowan', 'Avery', 'Cameron', 'Peyton', 'Blake', 'Drew',
            'Finley', 'Harley', 'Kai', 'Marley'
        );
    END IF;
    
    -- Indian last names
    SET last_name = ELT(FLOOR(1 + RAND() * 25), 
        'Sharma', 'Verma', 'Gupta', 'Singh', 'Kumar', 'Reddy', 'Patel', 'Mishra', 
        'Jain', 'Nair', 'Menon', 'Pillai', 'Chatterjee', 'Banerjee', 'Mukherjee', 
        'Das', 'Roy', 'Yadav', 'Jha', 'Pandey', 'Thakur', 'Choudhary', 'Rao', 
        'Naidu', 'Iyer'
    );
    
    RETURN CONCAT(first_name, ' ', last_name);
END$$
DELIMITER ;

-- Function to generate random Indian phone numbers
DELIMITER $$
CREATE FUNCTION generate_indian_phone() RETURNS VARCHAR(15)
DETERMINISTIC
BEGIN
    DECLARE prefix VARCHAR(5);
    DECLARE number_part VARCHAR(10);
    
    SET prefix = ELT(FLOOR(1 + RAND() * 5), '+91-7', '+91-8', '+91-9', '+91-6', '+91-7');
    SET number_part = LPAD(FLOOR(RAND() * 1000000000), 9, '0');
    
    RETURN CONCAT(prefix, number_part);
END$$
DELIMITER ;

-- Function to generate random email
DELIMITER $$
CREATE FUNCTION generate_email(first_name VARCHAR(50), last_name VARCHAR(50), year INT) 
RETURNS VARCHAR(100)
DETERMINISTIC
BEGIN
    DECLARE email VARCHAR(100);
    DECLARE random_num INT;
    
    SET random_num = FLOOR(RAND() * 1000);
    SET email = LOWER(CONCAT(
        SUBSTRING(first_name, 1, 1),
        last_name,
        year,
        random_num,
        '@university.edu'
    ));
    
    RETURN email;
END$$
DELIMITER ;

-- Function to generate random date of birth for college students (18-25 years old)
DELIMITER $$
CREATE FUNCTION generate_date_of_birth() RETURNS DATE
DETERMINISTIC
BEGIN
    DECLARE random_year INT;
    DECLARE random_month INT;
    DECLARE random_day INT;
    
    -- Generate birth years between 1998 and 2005 (18-25 years old)
    SET random_year = 1998 + FLOOR(RAND() * 8);
    SET random_month = 1 + FLOOR(RAND() * 12);
    
    -- Handle different days per month
    IF random_month IN (1,3,5,7,8,10,12) THEN
        SET random_day = 1 + FLOOR(RAND() * 31);
    ELSEIF random_month IN (4,6,9,11) THEN
        SET random_day = 1 + FLOOR(RAND() * 30);
    ELSE
        -- February
        SET random_day = 1 + FLOOR(RAND() * 28);
    END IF;
    
    RETURN CONCAT(random_year, '-', LPAD(random_month, 2, '0'), '-', LPAD(random_day, 2, '0'));
END$$
DELIMITER ;

-- Function to calculate age from date of birth
DELIMITER $$
CREATE FUNCTION calculate_age(dob DATE) RETURNS INT
DETERMINISTIC
BEGIN
    RETURN TIMESTAMPDIFF(YEAR, dob, CURDATE());
END$$
DELIMITER ;

```

## Step 3: Generate 100 Random Students

```sql
-- Create a temporary table to store Indian cities and states
CREATE TEMPORARY TABLE IF NOT EXISTS indian_cities (
    city VARCHAR(50),
    state VARCHAR(50),
    country VARCHAR(50) DEFAULT 'India'
);

INSERT INTO indian_cities (city, state) VALUES
('Mumbai', 'Maharashtra'),
('Delhi', 'Delhi'),
('Bangalore', 'Karnataka'),
('Hyderabad', 'Telangana'),
('Chennai', 'Tamil Nadu'),
('Kolkata', 'West Bengal'),
('Pune', 'Maharashtra'),
('Ahmedabad', 'Gujarat'),
('Jaipur', 'Rajasthan'),
('Lucknow', 'Uttar Pradesh'),
('Chandigarh', 'Punjab'),
('Bhopal', 'Madhya Pradesh'),
('Patna', 'Bihar'),
('Bhubaneswar', 'Odisha'),
('Guwahati', 'Assam'),
('Thiruvananthapuram', 'Kerala'),
('Dehradun', 'Uttarakhand'),
('Ranchi', 'Jharkhand'),
('Raipur', 'Chhattisgarh'),
('Gandhinagar', 'Gujarat'),
('Panaji', 'Goa'),
('Shimla', 'Himachal Pradesh'),
('Srinagar', 'Jammu and Kashmir'),
('Kochi', 'Kerala'),
('Coimbatore', 'Tamil Nadu'),
('Visakhapatnam', 'Andhra Pradesh'),
('Nagpur', 'Maharashtra'),
('Indore', 'Madhya Pradesh'),
('Vadodara', 'Gujarat'),
('Surat', 'Gujarat'),
('Nashik', 'Maharashtra'),
('Ludhiana', 'Punjab'),
('Agra', 'Uttar Pradesh'),
('Varanasi', 'Uttar Pradesh'),
('Allahabad', 'Uttar Pradesh'),
('Amritsar', 'Punjab'),
('Jodhpur', 'Rajasthan'),
('Udaipur', 'Rajasthan'),
('Jabalpur', 'Madhya Pradesh'),
('Gwalior', 'Madhya Pradesh');

-- Procedure to insert 100 random students
DELIMITER $$
CREATE PROCEDURE generate_100_students()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE dept_count INT;
    DECLARE dept_id INT;
    DECLARE subject_count INT;
    DECLARE subject_id INT;
    DECLARE gender_val ENUM('Male', 'Female', 'Other');
    DECLARE first_name_part VARCHAR(50);
    DECLARE last_name_part VARCHAR(50);
    DECLARE full_name VARCHAR(100);
    DECLARE dob DATE;
    DECLARE age_val INT;
    DECLARE enroll_year INT;
    DECLARE city_state_record VARCHAR(200);
    DECLARE city_val VARCHAR(50);
    DECLARE state_val VARCHAR(50);
    DECLARE email_val VARCHAR(100);
    
    -- Get department count
    SELECT COUNT(*) INTO dept_count FROM Department;
    
    WHILE i <= 100 DO
        -- Random gender (70% Male, 29% Female, 1% Other)
        SET gender_val = CASE 
            WHEN RAND() < 0.7 THEN 'Male'
            WHEN RAND() < 0.99 THEN 'Female'
            ELSE 'Other'
        END;
        
        -- Generate name
        IF gender_val = 'Male' THEN
            SET first_name_part = ELT(FLOOR(1 + RAND() * 20), 
                'Aarav', 'Vivaan', 'Aditya', 'Vihaan', 'Arjun', 'Sai', 'Reyansh', 'Atharv', 
                'Dhruv', 'Krishna', 'Kian', 'Kabir', 'Rudra', 'Veer', 'Aryan', 'Mohammed',
                'Rohan', 'Siddharth', 'Yash', 'Pranav');
        ELSEIF gender_val = 'Female' THEN
            SET first_name_part = ELT(FLOOR(1 + RAND() * 20), 
                'Saanvi', 'Ananya', 'Diya', 'Aadhya', 'Ishita', 'Myra', 'Avni', 'Anika', 
                'Pari', 'Navya', 'Riya', 'Sara', 'Aarohi', 'Anvi', 'Kiara', 'Prisha',
                'Zara', 'Sara', 'Anaya', 'Advika');
        ELSE
            SET first_name_part = ELT(FLOOR(1 + RAND() * 15),
                'Alex', 'Taylor', 'Jordan', 'Riley', 'Casey', 'Jamie', 'Morgan', 'Quinn',
                'Dakota', 'Skyler', 'Rowan', 'Avery', 'Cameron', 'Peyton', 'Blake');
        END IF;
        
        SET last_name_part = ELT(FLOOR(1 + RAND() * 25), 
            'Sharma', 'Verma', 'Gupta', 'Singh', 'Kumar', 'Reddy', 'Patel', 'Mishra', 
            'Jain', 'Nair', 'Menon', 'Pillai', 'Chatterjee', 'Banerjee', 'Mukherjee', 
            'Das', 'Roy', 'Yadav', 'Jha', 'Pandey', 'Thakur', 'Choudhary', 'Rao', 
            'Naidu', 'Iyer');
        
        SET full_name = CONCAT(first_name_part, ' ', last_name_part);
        
        -- Generate date of birth and calculate age
        SET dob = generate_date_of_birth();
        SET age_val = calculate_age(dob);
        
        -- Random department (ensure we have subjects in that department)
        SET dept_id = FLOOR(1 + RAND() * dept_count);
        
        -- Get random subject from the selected department
        SELECT COUNT(*) INTO subject_count 
        FROM Subject 
        WHERE department_id = dept_id;
        
        IF subject_count > 0 THEN
            SELECT s.subject_id INTO subject_id 
            FROM Subject s 
            WHERE s.department_id = dept_id 
            ORDER BY RAND() 
            LIMIT 1;
        ELSE
            SET subject_id = NULL;
        END IF;
        
        -- Random city and state
        SELECT CONCAT(city, '|', state) INTO city_state_record 
        FROM indian_cities 
        ORDER BY RAND() 
        LIMIT 1;
        
        SET city_val = SUBSTRING_INDEX(city_state_record, '|', 1);
        SET state_val = SUBSTRING_INDEX(city_state_record, '|', -1);
        
        -- Enrollment year (2020-2023)
        SET enroll_year = 2020 + FLOOR(RAND() * 4);
        
        -- Generate email
        SET email_val = LOWER(CONCAT(
            SUBSTRING(first_name_part, 1, 1),
            last_name_part,
            enroll_year,
            LPAD(FLOOR(RAND() * 1000), 3, '0'),
            '@university.edu'
        ));
        
        -- Insert the student
        INSERT INTO Student (
            id_number,
            enrollment_id,
            name,
            gender,
            date_of_birth,
            age,
            course_id,
            semester,
            academic_year,
            department_id,
            state,
            city,
            country,
            phone_number,
            email,
            enrollment_date
        ) VALUES (
            CONCAT('UNIV', LPAD(i, 6, '0')),
            CONCAT('EN', enroll_year, 'CS', LPAD(i, 4, '0')),
            full_name,
            gender_val,
            dob,
            age_val,
            subject_id,
            1 + FLOOR(RAND() * 8), -- Random semester 1-8
            CONCAT(enroll_year, '-', enroll_year+1),
            dept_id,
            state_val,
            city_val,
            'India',
            generate_indian_phone(),
            email_val,
            CONCAT(enroll_year, '-08-', LPAD(1 + FLOOR(RAND() * 15), 2, '0'))
        );
        
        SET i = i + 1;
    END WHILE;
    
    SELECT '100 students generated successfully!' as message;
END$$
DELIMITER ;

-- Execute the procedure to generate 100 students
CALL generate_100_students();
```

## Step 4: Verify and View Generated Data

```sql
-- Count total students
SELECT COUNT(*) as total_students FROM Student;

-- View sample of generated students
SELECT 
    enrollment_id,
    name,
    gender,
    age,
    date_of_birth,
    d.department_name,
    city,
    state,
    email,
    phone_number,
    semester,
    academic_year
FROM Student s
JOIN Department d ON s.department_id = d.department_id
LIMIT 20;

-- Gender distribution
SELECT 
    gender,
    COUNT(*) as count,
    CONCAT(ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM Student), 2), '%') as percentage
FROM Student
GROUP BY gender
ORDER BY count DESC;

-- Age distribution
SELECT 
    CASE 
        WHEN age BETWEEN 17 AND 19 THEN '17-19'
        WHEN age BETWEEN 20 AND 22 THEN '20-22'
        WHEN age BETWEEN 23 AND 25 THEN '23-25'
        ELSE '26+'
    END as age_group,
    COUNT(*) as count,
    CONCAT(ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM Student), 2), '%') as percentage
FROM Student
GROUP BY age_group
ORDER BY age_group;

-- Department-wise distribution
SELECT 
    d.department_name,
    COUNT(s.student_id) as student_count,
    CONCAT(ROUND(COUNT(s.student_id) * 100.0 / (SELECT COUNT(*) FROM Student), 2), '%') as percentage,
    GROUP_CONCAT(DISTINCT s.gender ORDER BY s.gender SEPARATOR ', ') as genders_present
FROM Department d
LEFT JOIN Student s ON d.department_id = s.department_id
GROUP BY d.department_name
ORDER BY student_count DESC;

-- State-wise distribution
SELECT 
    state,
    COUNT(*) as student_count,
    GROUP_CONCAT(DISTINCT city SEPARATOR ', ') as cities
FROM Student
GROUP BY state
ORDER BY student_count DESC
LIMIT 10;

-- Semester distribution
SELECT 
    semester,
    COUNT(*) as student_count,
    MIN(age) as youngest,
    MAX(age) as oldest,
    ROUND(AVG(age), 2) as average_age
FROM Student
GROUP BY semester
ORDER BY semester;

-- Students by enrollment year
SELECT 
    YEAR(enrollment_date) as enrollment_year,
    COUNT(*) as student_count,
    academic_year
FROM Student
GROUP BY YEAR(enrollment_date), academic_year
ORDER BY enrollment_year;
```

