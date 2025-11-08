// Short Hand Property
// if the name of the key and value is same then

//Approach [old]
function makeName(name, age) {
    const student = {
        name: name,
        age: age
    };
    return student
}

function createName(name, age) {
    const student = {
        name, // Shorthand property
        age // shorthand property
    };
    return student
}
let user = makeName("Onyx", 27);
let user1 = createName("Onyxwizard", 26);
console.log(user.name, user.age);
console.log(user1.name,user1.age);