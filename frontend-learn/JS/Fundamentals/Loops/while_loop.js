//While Loop
let iter = 3;
while (iter > 0) {
    console.log(iter)
    iter--;
}
console.log("=====Single_line_While_Loop========")
iter = 3; //re-value iter 
//single-line body
while (iter > 0) console.log(iter--);

//“do…while” loop
console.log("=====Do_While_Loop========")
iter = 0;
do {
    console.log(iter);
    iter++;
} while (iter < 3);

