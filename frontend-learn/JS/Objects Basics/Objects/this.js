/* Method
A function that is a property of an object

let object1 = {
    name: "onyx",
    id: 1,
    func: function () {
        console.log("Function inside object");
    }
}*/
//#--------------------- OR -------------------------#
// Also called method shorthand
let object1 = {
	name: "onyx",
	id: 1,
	func() {
		console.log("Function inside object");
	},
};

/* This Keyword
    To access the value of object

    eg : let user = {
                    name: "John",
                    age: 30,
                    sayHi() {
                        alert(user.name); // "user" instead of "this"
                    }
        Why => Because when user is changed/reassigned to null then user.name doest work so
        Try => this.name which picks the current object variable "name".

};
*/
let user = {
	name: "Onyx",
	age: 25,
	sayHi() {
		//console.log(user.name); // leads to an error
		console.log(this.name); // Fix
	},
};

let admin = user;
user = null; // overwrite to make things obvious

admin.sayHi(); // TypeError: Cannot read property 'name' of null

console.log(this); //{}

//"this" is undefined in strict mode
//non-strict mode the value of "this" in such case will be the global object (window)
function sayHi() {
	console.log(this);
}
sayHi();

// Arrow function with "this" keyword
//  If we reference "this" from such a function, it’s taken from the outer “normal” function.
let user1 = {
	firstName: "Onyx wizard",
	sayHi() {
		let arrow = () => console.log(this.firstName);
		arrow();
	},
};

user1.sayHi(); // Onyx wizard

let user2 = {
    firstName: "Onyx wizard",
    f(){
        let x = ()=> {
            let arrow = () => console.log(this.firstName);
            arrow();
        };
        x();
    }
};

user2.f(); // Onyx wizard