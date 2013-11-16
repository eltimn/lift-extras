module.exports = function(grunt) {
  "use strict";

  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-jasmine');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-hash');

  /**
   * Load in our build configuration file.
   */
  var userConfig = require( './build.config.js' );
  /**
   * This is the configuration object Grunt uses to give each plugin its
   * instructions.
   */
  var taskConfig = {
    /**
     * We read in our `package.json` file so we can access the package name and
     * version. It's already there, so we don't repeat ourselves here.
     */
    pkg: grunt.file.readJSON('package.json'),

    meta: {
      artifact: "<%= pkg.name %>-<%= pkg.version %>"
    },

    jshint: {
      options: {
        curly: true,
        eqeqeq: true,
        immed: true,
        latedef: true,
        newcap: true,
        noarg: true,
        sub: true,
        undef: true,
        unused: true,
        boss: true,
        eqnull: true,
        browser: true,
        evil: true,
        globals: {
          "jQuery": false,
          "$": false,
          "angular": false,
          "_": false,
          "ko": false,
          "App": true
        }
      },
      src: [
        '<%= app_files.js %>'
      ],
      test: {
        options: {
          globals: {
            "jQuery": false,
            "$": false,
            "angular": false,
            "_": false,
            "ko": false,
            "App": true,
            "it": false,
            "xit": false,
            "describe": false,
            "xdescribe": false,
            "beforeEach": false,
            "afterEach": false,
            "expect": false,
            "spyOn": false,
            "module": false,
            "inject": false
          }
        },
        files: {
          src: ['<%= app_files.jsspec %>']
        }
      },
      gruntfile: {
        options: {
          globals: {
            "module": false,
            "require": false
          }
        },
        files: {
          src: ['Gruntfile.js']
        }
      }
    },

    jasmine: {
      src: [
        '<%= vendor_files.js %>',
        '<%= app_files.js %>',
        '<%= test_files.js %>'
      ],
      options: {
        specs: '<%= app_files.jsspec %>'
      }
    },

    less: {
      compile: {
        options: {
          sourceMap: true,
          sourceMapFilename: "<%= dirs.build %>/css/assets/app_styles.css.map",
          sourceMapBasepath: "<%= dirs.build %>/css",
          sourceMapRootpath: "/"
        },
        files: {
          "<%= dirs.build %>/css/assets/app_styles.css": "<%= app_files.less %>"
        }
      },
      compress: {
        options: {
          compress: true,
          cleancss: true,
          report: 'min'
        },
        files: {
          "<%= dirs.build %>/app_styles.min.css": "<%= app_files.less %>"
        }
      }
    },

    concat: {
      /**
       * The `vendor_css` target concatenates vendor CSS.
       */
      vendor_css: {
        src: ['<%= vendor_files.css %>'],
        dest: '<%= dirs.build %>/vendor_styles.css'
      },
      /**
       * The `build_css` target concatenates compiled CSS and vendor CSS
       * together.
       */
      build_css: {
        src: [
          '<%= dirs.build %>/app_styles.min.css',
          '<%= dirs.build %>/vendor_styles.min.css'
        ],
        dest: '<%= dirs.build %>/<%= meta.artifact %>.min.css'
      },
      /**
       * The `compile_js` target is the concatenation of our application source
       * code and all specified vendor source code into a single file.
       */
      compile_js: {
        /*options: {
          separator: ';'
        },*/
        src: [
          '<%= vendor_files.js %>',
          '<%= app_files.js %>'
        ],
        dest: "<%= dirs.build %>/<%= meta.artifact %>.js"
      }
    },

    uglify: {
      options: {
        banner: '/*! <%= pkg.name %>-<%= pkg.version %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
      },
      build: {
        src: "<%= concat.compile_js.dest %>",
        dest: "<%= dirs.build %>/<%= meta.artifact %>.min.js"
      }
    },

    hash: {
      options: {
        mapping: '<%= dirs.resources %>/assets.json',
        srcBasePath: '<%= dirs.build %>',
        destBasePath: '<%= dirs.dist %>/assets'
      },
      js: {
        src: '<%= uglify.build.dest %>',
        dest: '<%= dirs.dist %>/assets'
      },
      css: {
        src: '<%= dirs.build %>/<%= meta.artifact %>.min.css',
        dest: '<%= dirs.dist %>/assets'
      }
    },

    gensourceslist: {
      sources_js: {
        options: {
          srcBasePath: '<%= dirs.src %>', // the base Path you want to remove from the input
          destBasePath: '' // the base Path you want to prepend to output
        },
        files: [
          {
            src: [
              '<%= app_files.js %>'
            ],
            dest: '<%= dirs.resources %>/source_scripts.txt'
          }
        ]
      },
      vendor_js: {
        options: {
          srcBasePath: '<%= dirs.src %>'
        },
        files: [
          {
            src: '<%= vendor_files.js %>',
            dest: '<%= dirs.resources %>/vendor_scripts.txt'
          }

        ]
      },
      vendor_css: {
        options: {
          srcBasePath: '<%= dirs.src %>'
        },
        files: [
          {
            src: '<%= vendor_files.css %>',
            dest: '<%= dirs.resources %>/vendor_styles.txt'
          }

        ]
      }
    },

    copy: {
      vendor_assets: {
        files: [
          {
            src: [ '<%= vendor_files.assets %>' ],
            dest: '<%= dirs.dist %>/',
            cwd: '<%= dirs.src %>',
            expand: true
          }
       ]
      }
    },

    delta: {
      /**
       * By default, we want the Live Reload to work for all tasks; this is
       * overridden in some tasks (like this file) where browser resources are
       * unaffected. It runs by default on port 35729, which your browser
       * plugin should auto-detect.
       */
      options: {
        livereload: true
      },

      /**
       * When the Gruntfile changes, we just want to lint it.
       */
      gruntfile: {
        files: 'Gruntfile.js',
        tasks: [ 'jshint:gruntfile' ]
      },

      /**
       * When our JavaScript source files change, we want to lint them,
       * run our unit tests, and live reload.
       */
      jssrc: {
        files: [
          '<%= app_files.js %>'
        ],
        tasks: ['jshint:src']
      },

      /**
       * When a JavaScript test file changes, we only want to lint it and
       * run the unit tests. We don't want to do any live reloading.
       */
      jstest: {
        files: '<%= app_files.jsspec %>',
        tasks: [ 'jshint:test', 'jasmine'],
        options: {
          livereload: false
        }
      },

      less: {
        files: ["<%= dirs.src %>/less/**/*.less"],
        tasks: ["less:compile"]
      },
      pkg: {
        files: 'package.json',
        tasks: ['build']
      }
    },
    clean: {
      build: ["<%= dirs.target %>"]
    }
  };

  // configure grunt
  grunt.initConfig( grunt.util._.extend( taskConfig, userConfig ) );

  /**
   * The `build` task gets your app ready to run for development and testing.
   */
  grunt.registerTask('build', ['clean', 'gensourceslist', 'jshint', 'less:compile']);

  /**
   * The `test` task runs your tests.
   */
  grunt.registerTask('test', ['jasmine']);

  /**
   * The `compress` task gets your app ready for deployment by concatenating and
   * minifying your code.
   */
  grunt.registerTask('compress', [
    'concat:compile_js', 'uglify', 'less:compress', 'concat:vendor_css', 'concat:build_css', 'hash', 'copy:vendor_assets'
  ]);

  /**
   * The default task is to build, test, and compress.
   */
  grunt.registerTask('default', ['build', 'test', 'compress']);

  /**
   * In order to make it safe to just compile or copy *only* what was changed,
   * we need to ensure we are starting from a clean, fresh build. So we rename
   * the `watch` task to `delta` (that's why the configuration var above is
   * `delta`) and then add a new task called `watch` that does a clean build
   * before watching for changes.
   */
  grunt.renameTask('watch', 'delta');
  grunt.registerTask('watch', ['build', 'delta']);

  grunt.registerMultiTask('gensourceslist', 'Generate a list of sources.', function() {
    var options = this.options(),
        destBasePath = options.destBasePath || '';

    this.files.forEach(function(fs) {
      var contents = "";
      fs.src.forEach(function(file) {
        var out = destBasePath + file.replace(options.srcBasePath, "");
        contents = contents + out + "\n";
      });

      grunt.file.write(fs.dest, contents);
      grunt.log.writeln("File created: " + fs.dest);
    });
  });
};
