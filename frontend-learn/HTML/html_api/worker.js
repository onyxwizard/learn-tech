let count = 0;

for (let i = 0; i <= 1_000_000_000; i++) {
    count = i;
}

postMessage(count);