module.exports = function(grunt) {
  "use strict";

  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-jasmine');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-hash');

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    dirs: {
      main: "src/main",
      test: "src/test",
      build: "grunt-build",
      target: "<%= dirs.build %>/out",
      temp: "<%= dirs.build %>/temp"
    },
    meta: {
      artifact: "<%= pkg.name %>-<%= pkg.version %>",
      concated: "<%= dirs.target %>/assets/<%= meta.artifact %>.js"
    },
    jasmine: {
      src : '<%= meta.concated %>',
      options : {
        specs : '<%= dirs.test %>/javascript/**/*.js'
      }
    },
    jshint: {
      gruntfile: {
        options: {
          jshintrc: '.jshintrc'
        },
        src: 'Gruntfile.js'
      },
      main: {
        options: {
          jshintrc: '<%= dirs.main %>/javascript/.jshintrc'
        },
        src: [
          "<%= dirs.main %>/javascript/*.js",
          "<%= dirs.main %>/javascript/utils/**/*.js",
          "<%= dirs.main %>/javascript/apps/**/*.js",
          "<%= dirs.main %>/javascript/views/**/*.js"
        ]
      },
      test: {
        options: {
          jshintrc: '<%= dirs.test %>/javascript/.jshintrc'
        },
        src: ['<%= dirs.test %>/javascript/**/*.js']
      }
    },
    less: {
      compile: {
        files: {
          "<%= dirs.target %>/assets/<%= meta.artifact %>.css": "<%= dirs.main %>/less/styles.less"
        }
      },
      compress: {
        options: {
          yuicompress: true
        },
        files: {
          "<%= dirs.temp %>/<%= meta.artifact %>.min.css": "<%= dirs.main %>/less/styles.less"
        }
      }
    },
    concat: {
      options: {
        separator: ';'
      },
      scripts: {
        src: [
          "<%= dirs.main %>/javascript/libs/jquery-1.8.3.min.js",
          "<%= dirs.main %>/javascript/libs/bootstrap-2.2.1.min.js",
          "<%= dirs.main %>/javascript/libs/bootstrap-notify.js",
          "<%= dirs.main %>/javascript/libs/knockout-2.2.1.js",
          "<%= dirs.main %>/javascript/libs/underscore-1.4.4.min.js",
          "<%= dirs.main %>/javascript/libs/angular.min.js",
          "<%= dirs.main %>/javascript/libs/liftAjax.js",
          "<%= dirs.main %>/javascript/libs/jquery.bsAlerts.min.js",
          "<%= dirs.main %>/javascript/libs/jquery.bsFormAlerts.min.js",
          "<%= dirs.main %>/javascript/BsNotify.js",
          "<%= dirs.main %>/javascript/KoAlerts.js",
          "<%= dirs.main %>/javascript/App.js",
          "<%= dirs.main %>/javascript/utils/**/*.js",
          "<%= dirs.main %>/javascript/apps/**/*.js",
          "<%= dirs.main %>/javascript/views/**/*.js"
        ],
        dest: "<%= meta.concated %>"
      }
    },
    uglify: {
      options: {
        banner: '/*! <%= pkg.name %>-<%= pkg.version %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
      },
      build: {
        src: "<%= meta.concated %>",
        dest: "<%= dirs.temp %>/<%= meta.artifact %>.min.js"
      }
    },
    hash: {
      src: [
        "<%= dirs.temp %>/<%= meta.artifact %>.min.js",
        "<%= dirs.temp %>/<%= meta.artifact %>.min.css"
      ],
      mapping: '<%= dirs.build %>/hash/assets.json',
      dest: '<%= dirs.target %>/assets'
    },
    watch: {
      main: {
        files: '<%= jshint.main.src %>',
        tasks: ['jshint', 'concat']
      },
      test: {
        files: '<%= jshint.test.src %>',
        tasks: ['test']
      },
      less: {
        files: ["<%= dirs.main %>/less/**/*.less"],
        tasks: ["less:compile"]
      },
      pkg: {
        files: 'package.json',
        tasks: ['compile']
      }
    },
    clean: {
      build: ["<%= dirs.build %>"]
    }
  });

  // Tasks
  grunt.registerTask('compile', ['jshint', 'concat', 'less:compile']);
  grunt.registerTask('test', ['jshint', 'concat', 'jasmine']);
  grunt.registerTask('compress', ['jshint', 'concat', 'uglify', 'less:compile', 'less:compress', 'hash']);

  // Default task
  grunt.registerTask('default', ['compile']);

};
