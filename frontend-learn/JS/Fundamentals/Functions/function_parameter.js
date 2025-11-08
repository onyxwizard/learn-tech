// Function Parameters
console.log("====Function Parameter====");
function showMessage(gesture,name) {
    console.log(gesture+" "+"my name is: " + name);
}
showMessage("Hi", "Onyx");

//Default values
console.log("====Function Default values====");
function printMessage(gesture,name="Onyx") {
    console.log(gesture+" "+"my name is: " + name);
}
printMessage("Hello!");
