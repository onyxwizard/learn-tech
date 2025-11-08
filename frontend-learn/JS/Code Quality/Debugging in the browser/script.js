function hello(name) {
    let phrase = `Hello, ${name}!`;
    debugger; // by adding this statement can also works like a breakpoint
    say(phrase);
}

function say(phrase) {
    alert(`** ${phrase} **`);
}

hello("onyx");