//if else statement

let age = 100;

if (30 > age && age < 50) {
    console.log("Mid Age")
} else if (age >= 100) {
    console.log("Old Age")
} else {
    console.log("Young Age")
}


// Ternary or conditional operator
let student_mark = 80;

result = (student_mark >= 90) ? "Exceptional Student" : "Poor Student";
console.log(result)

// Chain nesting by
result = (student_mark >= 90) ? "Exceptional Student" : (student_mark>69 &&student_mark<90)?"Mid Student":"Poor Student";
console.log(result)