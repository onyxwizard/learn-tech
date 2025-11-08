/**
 * Create a calculator
importance: 5

    Create an object calculator with three methods:

    - read() prompts for two values and saves them as object properties with names a and b respectively.
    - sum() returns the sum of saved values.
    - mul() multiplies saved values and returns the result.

 */

let calculator = {
    sum: () => {
        return this.a + this.b;
    },
    mul: () => {
        return this.a * this.b;
    },
    read : () => {
        this.a = +prompt("Enter value for a", 0); // need to define +prompt() for integer
        this.b = +prompt("Enter value for b", 0);
    },
};

calculator.read();
console.log(calculator.sum());
console.log(calculator.mul());