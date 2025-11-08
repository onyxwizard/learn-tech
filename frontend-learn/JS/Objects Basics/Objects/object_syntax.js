//Objects

//creation of Empty Object syntax 1:
console.log("========Creation Empty Object========");
let obj = new Object();
console.log(obj); //{}

//creation of Empty Object syntax 2:
console.log("========Creation Empty Object========");
let object = {};
console.log(object); //{}

// Object Adding Literals and properties
console.log("========Add Key value pair========");
let user = {     // an object
  name: "John",  // by key "name" store value "John"
  age: 30        // by key "age" store value 30
};
console.log(user); // { name: 'John', age: 30 }

//calling key value pairs
console.log("========Call Key value pair========");
console.log(user.name); // John
console.log(user['age']); // 30

// Add value to object
console.log("========Insert Additional value========");
user.isAdmin = true;
console.log(user); // { name: 'John', age: 30, isAdmin: true }

//To remove a property
console.log("========Remove Key value pair========");
delete user.age;
console.log(user); // { name: 'John', isAdmin: true }

// Add multiword but need to inside quotes
console.log("========Multi Word========");
user["Credit Score"] = 800;
console.log(user); // { name: 'John', isAdmin: true, 'Credit Score': 800 }