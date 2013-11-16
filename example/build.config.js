/**
 * This file/module contains all configuration for the build process.
 */
module.exports = {

  dirs: {
    src: "src/main/webapp",
    vendor: "src/main/webapp/vendor",
    target: "target/grunt",
    dist: "<%= dirs.target %>/dist",
    build: "<%= dirs.target %>/build",
    resources: "<%= dirs.target %>/resources"
  },

  /**
   * This is a collection of file patterns that refer to our app code (the
   * stuff in `src/main/webapp/app`). These file paths are used in the configuration of
   * build tasks. `js` is all project angular javascript, less tests. `jsjq` is
   * jquery javascript. `tpl` contains our app's template HTML files, `less`
   * is our main stylesheet, and `jsspec` contains our app's tests.
   */
  app_files: {
    js: [
      "<%= dirs.src %>/app/App.js",
      "<%= dirs.src %>/app/**/*.js",
      "!<%= dirs.src %>/app/**/*.spec.js"
    ],
    jsspec: ['<%= dirs.src %>/app/**/*.spec.js'],
    less: '<%= dirs.src %>/less/styles.less'
  },

  /**
   * This is a collection of files used during testing only.
   */
  test_files: {
    js: []
  },

  /**
   * This is the same as `app_files`, except it contains patterns that
   * reference vendor code (`components/`) that we need to place into the build
   * process somewhere. While the `app_files` property ensures all
   * standardized files are collected for compilation, it is the user's job
   * to ensure non-standardized (i.e. vendor-related) files are handled
   * appropriately in `vendor_files.js`.
   *
   * The `vendor_files.js` property holds files to be automatically
   * concatenated and minified with our project source files.
   *
   * The `vendor_files.css` property holds any CSS files to be automatically
   * included in our app.
   *
   * The `vendor_files.assets` property holds any assets to be copied along
   * with our app's assets. This structure is flattened, so it is not
   * recommended that you use wildcards.
   */
  vendor_files: {
    js: [
      "<%= dirs.vendor %>/jquery-1.10.2.min.js",
      "<%= dirs.vendor %>/bootstrap-3.0.2.min.js",
      "<%= dirs.vendor %>/bootstrap-notify.js",
      "<%= dirs.vendor %>/knockout-2.2.1.js",
      "<%= dirs.vendor %>/underscore-1.4.4.min.js",
      "<%= dirs.vendor %>/angular.min.js",
      "<%= dirs.vendor %>/liftAjax.js",
      "<%= dirs.vendor %>/jquery.bsAlerts.min.js",
      "<%= dirs.vendor %>/jquery.bsFormAlerts.min.js"
    ],
    css: [
    ],
    assets: [
    ]
  },
};
