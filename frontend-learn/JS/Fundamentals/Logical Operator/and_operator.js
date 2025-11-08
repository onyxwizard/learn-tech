// AND operator
// Both value needs to be true 
a = true
b = false
console.log(a && a); //True
console.log(a && b); //False
console.log(b && a); //False
console.log(b && b); //False


// Lets go further
console.log(undefined && NaN); //undefined
console.log(NaN && undefined); // NaN
// AND returns the first falsy value or the last value if none were found.
// Here the first unsuccessful value is returned
// But for or the last unsuccessful value is returned

