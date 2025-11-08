//String concatenation with binary +

console.log("=== Basic String Concatenation ===");
console.log("Sample of string concatenate: ", 'my' + 'string'); // mystring
console.log("Sample of string concatenate: ", 'Hello' + ' World'); // Hello World

console.log("\n=== Mixing Strings and Numbers ===");
console.log("Mixin of string but order matters\nString + number:", '1' + 2); // "12"
console.log("Mixin of string but order matters\nString + number:", 2 + '1'); // "21"

console.log("\n=== Order Matters ===");
console.log("Mixin of string but order matters\Number + Number + String:", 2 + 2 + '1'); // "41"
console.log("Mixin of string but order matters\Number + Number + String:", '1' + 2 - 2); // "122"
