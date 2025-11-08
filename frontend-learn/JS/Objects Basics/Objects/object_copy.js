// Copy objects [Independent reference]
// Note : when copy nested object inside object try using strucutreClone()
const object1 = {
    name: "onyx",
    title: "hacker",
    size: {
        name: 'ak'
    }
};

let object2 = object1;
console.log(object2);
console.log(object2 === object1); //True

delete object2
// But try this Independent reference copy
object2 = {};
for (let key in object1) {
    object2[key] = object1[key];
}
console.log(object2);
console.log(object2 === object1); //False
console.log(object2.size === object1.size); //true

// Use built-in
let object3 = {}
Object.assign(object3, object1);
console.log(object3);
console.log(object3 === object1); //False
console.log(object3.size === object1.size); //True


// Strucute clone
let object4 = structuredClone(object1);
console.log(object4 === object1); //False
console.log(object4.size === object1.size); //False
