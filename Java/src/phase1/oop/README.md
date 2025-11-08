### 🛠️ Mini-Project: **"Vehicle Fleet Manager" (Console-Based)**

You’ll model real-world vehicles using OOP principles. Start simple, then layer in inheritance, interfaces, and packages.



## ✅ Step-by-Step Learning Tasks

### 🔹 **1. Create a `Vehicle` Class (Objects & Classes)**
- **Goal**: Understand **state (fields)** and **behavior (methods)**.
- Create a class `Vehicle` with:
    - Fields: `brand` (String), `speed` (int), `isRunning` (boolean)
    - Methods:
        - `start()` → sets `isRunning = true` and prints `"Engine started."`
        - `stop()` → sets `isRunning = false` and prints `"Engine stopped."`
        - `accelerate(int increment)` → increases speed if running
        - `getStatus()` → returns a string like `"Toyota running at 30 km/h"`

> 💡 **Key concept**: A class is a blueprint. Each `Vehicle` object has its own state.



### 🔹 **2. Create a Demo Class with `main()`**
- Create `VehicleDemo.java`
- In `main()`:
    - Create two `Vehicle` objects: `car1` (brand: "Toyota"), `car2` (brand: "Ford")
    - Call methods on them: start, accelerate, print status
- Run it using:
  ```bash
  java VehicleDemo.java   # (single-file mode!)
  ```

> ✅ You’re now using **objects**, **classes**, and the **main method**—just like the `BicycleDemo` example.



### 🔹 **3. Add Inheritance: Create `Car` and `Bicycle` Subclasses**
- Make `Vehicle` a **superclass**
- Create:
    - `class Car extends Vehicle` → add field `numDoors`
    - `class Bicycle extends Vehicle` → add field `numGears`
- Override `getStatus()` in each to include their unique fields.

> 🎯 **Why?** To see how **inheritance** lets you **reuse common behavior** (`start`, `accelerate`) while adding **specialized features**.



### 🔹 **4. Define an Interface: `Drivable`**
- Create an interface:
  ```java
  interface Drivable {
      void start();
      void stop();
      void accelerate(int increment);
  }
  ```
- Make your `Vehicle` class **implement `Drivable`**:
  ```java
  class Vehicle implements Drivable { ... }
  ```
- Notice: the compiler now **forces** you to implement all methods.

> 💡 This teaches you:
> - **Interfaces = contracts**
> - They enable **polymorphism** (e.g., a method that accepts any `Drivable`)



### 🔹 **5. Organize into Packages**
- Create folder structure:
  ```
  fleet/
    ├── model/
    │   ├── Vehicle.java
    │   ├── Car.java
    │   ├── Bicycle.java
    │   └── Drivable.java
    └── demo/
        └── VehicleDemo.java
  ```
- Add `package fleet.model;` at the top of `Vehicle.java`, etc.
- In `VehicleDemo.java`, add:
  ```java
  package fleet.demo;
  import fleet.model.*;
  ```
- Compile and run:
  ```bash
  javac fleet/demo/VehicleDemo.java
  java fleet.demo.VehicleDemo
  ```

> 📦 This teaches **packages** = logical grouping + avoiding naming conflicts.



## 🧠 What You’ll Learn by Doing This

| Concept | How You Practice It |
|-------|---------------------|
| **Object** | `new Car("Honda")` → has its own state |
| **Class** | `Vehicle.java` = blueprint |
| **Inheritance** | `Car extends Vehicle` → reuses code |
| **Interface** | `Drivable` → enforces method contract |
| **Package** | `fleet.model` → organizes code |

