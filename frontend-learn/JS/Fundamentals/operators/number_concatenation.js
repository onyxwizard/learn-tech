//Number Concatenate
let num = 1;
console.log(num); //1


//binary form has two operand

let binary = 1 + 2;
console.log(binary); //3

// unary operator and operand
// using single operand and operator

let unary = -num;
console.log(unary); //-1
unary = + num;
console.log(unary) // 1

// lets apply unary to non numeric
let non_number = "";
console.log(-non_number); //-0
console.log(+non_number); //0


//Apllied for logical
console.log(+true);//1
console.log(-true);//-1
console.log(-false);//-0

console.log(-undefined); //NaN
console.log(+undefined);// NaN

console.log(+NaN); //NaN


// Unary plus can be used to convert string to number
let val1, val2;
val1 = "10";
val2 = "5";

console.log(+val1 / +val2); //2
//there is a concept called operator precedence go and learn that so you know the the operator order works

let a = (1 + 2, 3 + 4);// Always the last value is picked
console.log(a); //7