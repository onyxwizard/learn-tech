//String type conversion
let bool_val = true;
console.log(typeof bool_val); //boolean

let string_conv = String(bool_val);
console.log(typeof string_conv); //string


// Alpha String cannot be converted to numbers
let age = Number("h");
console.log(age); //NaN


// Numeric conversion rules:
let undefine = Number(undefined);
console.log(undefine); //NaN

let Null = Number(null);
console.log(Null); // 0

let bool_true = Number(true);
console.log(bool_true); // 1

let bool_false = Number(false);
console.log(bool_false); // 0