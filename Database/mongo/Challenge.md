# MongoDB Shell Challenge: Library Management System

This challenge is designed to test everything you’ve learned about the MongoDB shell.  
You’ll create a small database for a library, insert sample data, and run a series of queries and updates – all using the `mongosh` commands.


## 🎯 Objective

Build and manipulate a **library database** with two collections:
- `books` – information about each book
- `members` – library members and their borrowing history

Then complete the tasks below to practice **CRUD**, **query operators**, **sorting/limiting**, **indexes**, and **collection management**.

---

## 📦 Setup

1. **Start your MongoDB server** (if not already running).
2. Open `mongosh` (MongoDB Shell).
3. Switch to (or create) a new database called `library`:
   ```javascript
   use library
   ```
4. Insert the following sample data.

### 📚 Books Collection
```javascript
db.books.insertMany([
  {
    title: "The Hobbit",
    author: "J.R.R. Tolkien",
    genre: "Fantasy",
    publishedYear: 1937,
    copiesAvailable: 4,
    totalCopies: 7,
    location: "Section A, Shelf 3"
  },
  {
    title: "1984",
    author: "George Orwell",
    genre: "Dystopian",
    publishedYear: 1949,
    copiesAvailable: 2,
    totalCopies: 5,
    location: "Section B, Shelf 1"
  },
  {
    title: "To Kill a Mockingbird",
    author: "Harper Lee",
    genre: "Classic",
    publishedYear: 1960,
    copiesAvailable: 0,
    totalCopies: 3,
    location: "Section A, Shelf 2"
  },
  {
    title: "Dune",
    author: "Frank Herbert",
    genre: "Sci-Fi",
    publishedYear: 1965,
    copiesAvailable: 3,
    totalCopies: 6,
    location: "Section C, Shelf 4"
  },
  {
    title: "The Great Gatsby",
    author: "F. Scott Fitzgerald",
    genre: "Classic",
    publishedYear: 1925,
    copiesAvailable: 1,
    totalCopies: 2,
    location: "Section A, Shelf 2"
  },
  {
    title: "Neuromancer",
    author: "William Gibson",
    genre: "Sci-Fi",
    publishedYear: 1984,
    copiesAvailable: 0,
    totalCopies: 1,
    location: "Section C, Shelf 5"
  }
])
```

### 👥 Members Collection
```javascript
db.members.insertMany([
  {
    name: "Alice Johnson",
    email: "alice@email.com",
    joined: new Date("2023-01-15"),
    active: true,
    borrowedBooks: [
      { title: "The Hobbit", borrowed: new Date("2025-02-01"), due: new Date("2025-02-15") }
    ]
  },
  {
    name: "Bob Smith",
    email: "bob@email.com",
    joined: new Date("2023-03-20"),
    active: true,
    borrowedBooks: []
  },
  {
    name: "Carol White",
    email: "carol@email.com",
    joined: new Date("2024-06-10"),
    active: false,
    borrowedBooks: [
      { title: "Dune", borrowed: new Date("2025-01-20"), due: new Date("2025-02-03") },
      { title: "1984", borrowed: new Date("2025-01-25"), due: new Date("2025-02-08") }
    ]
  },
  {
    name: "David Brown",
    email: "david@email.com",
    joined: new Date("2024-09-05"),
    active: true,
    borrowedBooks: []
  }
])
```

---

## 🧪 Challenge Tasks

Complete each task using **MongoDB shell commands**.  
You can check your results by running `db.books.find().pretty()` or `db.members.find().pretty()` after each step.

### 1. Database & Collection Basics
- [ ] Show all databases (verify `library` appears – if not, you may need to insert data first).
- [ ] Show all collections in the `library` database.
- [ ] Create a new collection named `staff` (even if empty) and then drop it.

### 2. Inserting Documents
- [ ] Insert a new book:  
  *Title:* "The Martian", *Author:* "Andy Weir", *Genre:* "Sci-Fi", *Year:* 2011,  
  *copiesAvailable:* 2, *totalCopies:* 3, *location:* "Section C, Shelf 6".  
- [ ] Insert a new member:  
  *Name:* "Eve Adams", *Email:* "eve@email.com", *Joined:* today's date, *active:* true, *borrowedBooks:* [].

### 3. Finding Documents (with filters, projections)
- [ ] Find all books that are **Sci-Fi**.
- [ ] Find all books published **after 1950**.
- [ ] Find all books with **zero copies available** (out of stock).
- [ ] Find members who **joined in 2024** (hint: use `$gte` and `$lt` with dates).
- [ ] Find all books, but **show only title, author, and publishedYear** (exclude `_id`).
- [ ] Find members who are **active** and have **borrowed at least one book** (hint: `borrowedBooks` array not empty → `$ne: []` or `$exists` and `$gt: {$size: 0}`).

### 4. Comparison & Logical Operators
- [ ] Find books where **copiesAvailable is less than or equal to 1**.
- [ ] Find books that are **either "Classic" or "Fantasy"** (use `$in`).
- [ ] Find books that are **not "Dystopian"** (use `$ne`).
- [ ] Find members who **joined before 2024-01-01 OR are not active** (use `$or`).
- [ ] Find members who are **active AND have borrowed at least one book** (use `$and` with array conditions).

### 5. Sorting & Limiting
- [ ] List all books **sorted by publishedYear (oldest first)**.
- [ ] Get the **3 most recently published books** (sort descending, limit 3).
- [ ] Find the **member who joined most recently** (use sort and limit).

### 6. Updating Documents
- [ ] **Update one book:** Increase `copiesAvailable` by 1 for "The Martian" (you added it earlier).  
  *Hint:* Use `$inc`.
- [ ] **Add a new field** `pages` to all books (set a default value, e.g., 300) using `updateMany`.
- [ ] **Mark a member as inactive** (set `active: false`) for the member with email "bob@email.com".
- [ ] **Add a new book to a member's borrowedBooks** (e.g., let Alice borrow "The Great Gatsby" – add a subdocument with today's date and due date 14 days later).
- [ ] **Remove a field** from all members, e.g., remove the `joined` field temporarily (then add it back if you want). Use `$unset`.

### 7. Deleting Documents
- [ ] **Delete a single book** – for example, remove "Neuromancer" (it has only 1 copy and is out of stock).
- [ ] **Delete all members who are not active** (but maybe first check how many there are; only Carol is inactive). Use `deleteMany`.
- [ ] **Drop the entire `staff` collection** if you still have it.

### 8. Indexes
- [ ] **Create an index** on the `author` field in the `books` collection.
- [ ] **View all indexes** on the `books` collection.
- [ ] **Explain the execution** of a query that finds books by a specific author (e.g., "Frank Herbert") both **before and after** creating the index.  
  Use `.explain("executionStats")` and note the difference in `totalDocsExamined`.
- [ ] **Drop the index** on `author`.

### 9. Bonus / Advanced
- [ ] Use `$regex` to find books whose title contains "the" (case‑insensitive).
- [ ] Count the total number of books in each genre using aggregation (optional – if you want to peek ahead).  
  *Example:*  
  ```javascript
  db.books.aggregate([ { $group: { _id: "$genre", count: { $sum: 1 } } } ])
  ```
- [ ] Create a **capped collection** named `logs` with a maximum size of 1MB and max 100 documents. Insert a few log entries and observe that old ones are overwritten when full.

---

## ✅ Verify Your Work

You can always run `db.books.find().pretty()` and `db.members.find().pretty()` to see the current state.  
For specific checks, use the queries you just wrote – they should return the expected documents.

If you ever want to reset the data, you can:
```javascript
use library
db.books.drop()
db.members.drop()
```
Then re‑insert the sample data from the Setup section.

---

## 📘 Notes

- All commands are run inside `mongosh`.
- Remember to use `pretty()` for readable output, e.g., `db.books.find().pretty()`.
- Dates in MongoDB are stored as ISODate. When querying, use `new Date("YYYY-MM-DD")` or `ISODate("...")`.
- For array queries, remember `$size`, `$elemMatch`, etc., if needed.

---

Good luck, and have fun mastering MongoDB! 🚀