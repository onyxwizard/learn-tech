//Function Declaration
console.log("======Function Declaration==========");
function showMessage() {
    console.log("Hello Onyx!")
}

//Function call
showMessage();

//Local variables
console.log("======Local variables==========");
function count() {
    let iter = 5;
    console.log(iter);
}
count();
/* console.log(iter); //is not defined */


//Global or outer variables
console.log("======Global variables==========");
let g_variable = "HI";
function printMessage() {
    g_variable = "BYE";
    console.log(g_variable);
}

printMessage();  // BYE
//global variable call
console.log(g_variable); // BYE

// Function has full access to global variable and can also modify it
