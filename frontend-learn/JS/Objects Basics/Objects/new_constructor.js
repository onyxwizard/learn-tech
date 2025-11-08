// Constructor
// Naming should start with Capital letter
// Constructor doesn't  return anything

// using this we can create multiple users using objects

function User(name) {
    this.name = name;
    this.admin = false;
}

// create instance of constructor
let user = new User('Onyx');
let user1 = new User('Wizard');
console.log(user);
console.log(user1);

// Constructor mode test: new.target
function Constructor() {
    console.log(new.target);
}
Constructor(); //undefined

new Constructor(); // function Constructor()
