const { jangarooConfig } = require("@jangaroo/core");

module.exports = jangarooConfig({
  type: "code",
  autoLoad: [
    "./src/init",
  ],
});
