// --- TYPEOF OPERATOR ---
console.log("\n=== typeof Operator ===");
console.log("typeof 42:", typeof 42);              // number
console.log("typeof 'hello':", typeof 'hello');    // string
console.log("typeof true:", typeof true);          // boolean
console.log("typeof undefined:", typeof undefined); // undefined
console.log("typeof null:", typeof null);          // object ❗(known issue)
console.log("typeof {}:", typeof {});              // object
console.log("typeof Symbol():", typeof Symbol());  // symbol
console.log("typeof function() {}", typeof function () { }); // function
