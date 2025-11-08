/**
 * Write the code, one line for each action:

    Create an empty object user.
    Add the property name with the value John.
    Add the property surname with the value Smith.
    Change the value of the name to Pete.
    Remove the property name from the object.

*/
let user = {};
console.log(user); //{}
user.name = 'John';
console.log(user); //{ name: 'John' }
user.surname = 'Smith';
console.log(user); //{ name: 'John', surname: 'Smith' }
user.name = 'Pete';
console.log(user); //{ name: 'Pete', surname: 'Smith' }
delete user.name;
console.log(user); //{ surname: 'Smith' }
