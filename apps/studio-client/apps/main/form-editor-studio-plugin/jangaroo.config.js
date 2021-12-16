const { jangarooConfig } = require("@jangaroo/core");

module.exports = jangarooConfig({
  type: "code",
  sencha: {
    name: "com.coremedia.blueprint__form-editor-studio-plugin",
    namespace: "com.tallence.formeditor.studio",
    studioPlugins: [
      {
        mainClass: "com.tallence.formeditor.studio.FormsStudioPlugin",
        name: "Formeditor Extension",
      },
    ],
  },
  command: { build: { ignoreTypeErrors: true } },
});
