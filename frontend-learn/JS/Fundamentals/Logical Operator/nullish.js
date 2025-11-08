//Nullish operator
// This is a new operator

//------------------------------------------------------------------------
// lets intro to "polyfills" and "transpiler"
//transpiler - translate source code to another source code
// polyfills  - not only translates code but also built-in function as well
//------------------------------------------------------------------------

// returns the first argument if it’s not null/undefined
let user = "Hi"
console.log(user ?? "Anonymous"); // Hi
console.log(null ?? user); // Hi

// under the hood Nullish syntax is : (a !== null && a !== undefined) ? a : b;


//example
let height = 0;

alert(height || 100); // 100
alert(height ?? 100); // 0