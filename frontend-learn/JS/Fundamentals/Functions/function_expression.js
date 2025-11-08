//Function expressions
console.log("===Function expressions===");
let sayHi = function () {
    return "hello";
};

console.log(sayHi()); // Hello
//function call
sayHi(); // Nothing returns if needed then below method
x = sayHi();
console.log(x); 


// Copy Function
console.log("===Copy Function===");
function showMessage() {
    let message = "Hi! Onyx";
    console.log(message)
    return message;
}

let func = showMessage;
func(); //Hi! Onyx


// Debate: "Function Declaration" vs "Function Expression"
//when we need to declare a function, the first thing to consider is Function Declaration syntax
//When we need a conditional declaration then Function Expression should be used.