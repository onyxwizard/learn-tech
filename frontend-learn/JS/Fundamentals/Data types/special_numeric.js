console.log("===Special Number ===");

let value1, value2;
value1 = 1;
value2 = 0;

console.log(value1 / value2);  // Infinity

// NaN represents a computational error
console.log(value1 / "hi"); // NaN

// Anything with NaN is NaN
// But Except
console.log(NaN ** 0); // 1
console.log(NaN + 1); // NaN


// BigInt
// integer values larger than (2^53-1) (that’s 9007199254740991), or less than -(2^53-1) for negatives.
console.log(9007199254740991 + 1); // 9007199254740992
console.log(9007199254740991 + 2); // 9007199254740992

console.log("\n=== Big Int ===");
console.log(9007199254740991n + 1n); // 9007199254740992n
console.log(9007199254740991n + 2n); // 9007199254740993n

