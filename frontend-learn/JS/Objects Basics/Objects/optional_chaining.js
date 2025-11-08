//Optional chaining '?.'
/**
 * The optional chaining ?. is a safe way to access nested object properties, even if an intermediate property doesn’t exist.
 */

// Use only to check if object doesn't exist
let user = {}; // a user without "address" property

 // console.log(user.address); // Error!
console.log(user.address ? user.address.street : undefined); // undefined

/**
 * Note : As you can see, property names are still duplicated in the code. E.g. in the code above, user.address appears three times.
    - That’s why the optional chaining ?. was added to the language. To solve this problem once and for all!
 */

let user1 = {}; // user has no address

console.log(user1?.address?.street); // undefined (no error)

let user2 = {

};
console.log(user2.name?.address); //null

// We can use optional chaining with function and square bracket =>    "?.()", "?.[]"