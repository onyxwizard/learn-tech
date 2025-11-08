describe("sum", function () {
	it("should return 5 when adding 2 and 3", function () {
		assert.equal(sum(2, 3), 5);
	});

	it("should return 0 when adding -1 and 1", function () {
		assert.equal(sum(-1, 1), 0);
	});
});

describe("Adding two Numbers", function () {
    it("Need to return 5", function () {
        assert.equal(sum(3, 2), 5);
    });
    it("Need to return 10", function () {
        assert.equal(sum(5, 6), 10); //failure condition
    });
});