/**
 * Object property can be represent as either string type or symbol type
    - What is Symbol? => A “symbol” represents a unique identifier.
    - Symbols are guaranteed to be unique
*/
// Inside Symbol('Enter name for description')
let id = Symbol(); //
console.log(id); // Symbol()
console.log(typeof id); //Symbol

let id_user1 = Symbol("123");
let id_user2 = Symbol("123");

console.log(id_user1); //Symbol(123)
console.log(id_user2); // Symbol(123)

console.log(id_user1 == id_user2); // false
console.log(id_user1 === id_user2); // false

// Note : symbol is a “primitive unique value” with an optional description
console.log(id_user1.description); // 123

/**
 * Symbolic properties do not participate in for loop and "Object.keys(user)" also ignores them.
    - So, “hiding symbolic properties” principle
    - But  Object.assign copies both string and symbol properties
*/

/** Global symbols -  To create one
 * Symbols inside the registry are called global symbols
*/

// read from the global registry
let ids = Symbol.for("id"); // if the symbol did not exist, it is created

// read it again (maybe from another part of the code)
let idAgain = Symbol.for("id");

// the same symbol
console.log(ids === idAgain); // true

/**
 * Symbol.keyFor() [Global Scope]

    We have seen that for global symbols, Symbol.for(key) returns a symbol by name.
    - To do the opposite – return a name by global symbol – we can use: Symbol.keyFor(sym):
 */
// get symbol by name
let sym = Symbol.for("name");
let sym2 = Symbol.for("id");

// get name by symbol
console.log( Symbol.keyFor(sym) ); // name
console.log(Symbol.keyFor(sym2)); // id

// Global vs local scope
let globalSymbol = Symbol.for("name");
let localSymbol = Symbol("name");

alert( Symbol.keyFor(globalSymbol) ); // name, global symbol
alert(Symbol.keyFor(localSymbol)); // undefined, not global

/**
 * There exist many “system” symbols that JavaScript uses internally,
    - Symbol.hasInstance
    - Symbol.isConcatSpreadable
    - Symbol.iterator
    - Symbol.toPrimitive
 */