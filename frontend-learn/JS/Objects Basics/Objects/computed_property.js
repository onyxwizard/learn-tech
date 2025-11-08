/**
 * Computed properties
 * need to use square bracket and inside it is literal.
 **/

let fruit = prompt("Which fruit to buy?");
let bag = {
	[fruit]: 5, // the name of the property is taken from the variable fruit
};
alert(bag[fruit]); // 5 if fruit="apple" or can be any user input

//Alternative
fruit = "apple";
let basket = {
	[`${fruit}` + "Computers"]: 5, // basket.appleComputers = 5
};
console.log(basket.appleComputers); //5

/**
 * Property value shorthand
 *
 * Old way
 * function makeUser(name, age) {
 * return {
 *  name: name,
 *  age: age,
 *  // ...other properties
 *  };
 * }
**/
// New way both are same

function makeUser(name, age) {
	return {
		name, // same as name: name
		age, // same as age: age
		// ...
	};
}

let user = makeUser("John", 30);
console.log(user.name); // John

/**
 * Property names limitations
 * object property, there’s no such restriction: they can use reserved words as ;literals
 * let obj = {
 * for: 1,
 * let: 2,
 * return: 3
 * };
*/
