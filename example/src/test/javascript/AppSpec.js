describe("App", function() {
  it("namespace creates a namespace properly", function() {
    var a = App.module;
    expect(a).toBeUndefined();
    App.namespace("module.mod2.mod3");
    a = App.module.mod2.mod3;
    var b = {};
    expect(a).toEqual(b);
  });
});
