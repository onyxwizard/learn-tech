//For Loop
/* Syntax
for (begin; condition; step) {
    ... loop body ...
}
*/
for (let iter = 3; iter > 0; iter--){
    console.log(iter);
}


//We define variable outside for because we try to access the varibale outside the function.
//If declared inside for loop then its inline function and cannot be accessed outside.
let i = 3;
for (i; i > 0; i--){
    console.log(i);
}
console.log(i);


// Infinite loop
/* for (;;) {
    repeats without limits
} */


// we can use "break/continue" to exit and skip an iteration but we cannot use it with ternary operator "?"
//(i > 5) ? alert(i) : continue; // continue isn't allowed here