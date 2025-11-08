//Function Return Value
function sum(a, b) {
	return a + b;
}
let result = sum(1, 2);
console.log(result); // 3

// return without a value
function checkCondition(bool) {
    if (bool) return console.log(1); //to see output console.log(1);
    else return console.log(0);
}

checkCondition(true); //1
checkCondition(false); //0