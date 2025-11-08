//Switch Statement
let sum = 5;
console.log("===========Test Case 1================");
switch (sum) {
    case 1:
        console.log("Start");
        break;
    case 2:
        console.log("more than start");
        break;
    case 3:
        console.log("mid way");
        break;
    case 4:
        console.log("About to reach");
        break;
    case 5:
        console.log("reached");
        break;
}


// think of if no break is added

console.log("===========Test Case 2================");
switch (sum) {
    case 5:
        console.log("reached");
    case 4:
        console.log("About to reach");
    case 3:
        console.log("mid way");
    case 2:
        console.log("more than start");
    case 1:
        console.log("Start");
    default:
        console.log("If no test case pass then this runs!");
}

console.log("===========Test Case 3================");
let a = "2";
let b = 0;

switch (+a) { //increments to 1
    case b + 1: //b now is 1 so a == b => 1 = 1 so pass.
        console.log(a,b+1)
        console.log("this runs, because +a is 1, exactly equals b+1");
        break;

    default:
        console.log("this doesn't run");
}