// OR operator
// If any one statement is true then it returns "true"
a = true
b = false
console.log(a || a); //True
console.log(a || b); //True
console.log(b || a); //True
console.log(b || b); //False


//lets go further
console.log(undefined || NaN); //NaN
console.log(1 || 0); //1
console.log(null || 1); //1
console.log(null || 0); //0
// Here we need to understand that if all are false value then it returns last value
console.log(0 || null); //null
console.log(NaN || undefined); //undefined