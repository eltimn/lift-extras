module.exports = function(grunt) {
  "use strict";

  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-jasmine');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-clean');

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    dirs: {
      src: "src/main",
      test: "src/test",
      target: "target/scala-<%= pkg.scalaVersion %>/resource_managed/main/assets"
    },
    meta: {
      artifactName: "<%= pkg.name %>-<%= pkg.version %>-<%= pkg.buildTime %>",
      concated: "<%= dirs.target %>/<%= meta.artifactName %>.js"
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
          jshintrc: '<%= dirs.src %>/javascript/.jshintrc'
        },
        src: [
          "<%= dirs.src %>/javascript/*.js",
          "<%= dirs.src %>/javascript/views/**/*.js"
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
          "<%= dirs.target %>/<%= meta.artifactName %>.css": "<%= dirs.src %>/less/styles.less"
        }
      },
      compress: {
        options: {
          yuicompress: true
        },
        files: {
          "<%= dirs.target %>/<%= meta.artifactName %>.min.css": "<%= dirs.src %>/less/styles.less"
        }
      }
    },
    concat: {
      options: {
        separator: ';'
      },
      scripts: {
        src: [
          "<%= dirs.src %>/javascript/libs/jquery-1.8.3.min.js",
          "<%= dirs.src %>/javascript/libs/bootstrap-2.2.1.min.js",
          "<%= dirs.src %>/javascript/libs/bootstrap-notify.js",
          "<%= dirs.src %>/javascript/libs/knockout-2.2.1.js",
          "<%= dirs.src %>/javascript/libs/underscore-1.4.4.min.js",
          "<%= dirs.src %>/javascript/libs/liftAjax.js",
          "<%= dirs.src %>/javascript/libs/jquery.bsAlerts.min.js",
          "<%= dirs.src %>/javascript/libs/jquery.bsFormAlerts.min.js",
          "<%= dirs.src %>/javascript/BsNotify.js",
          "<%= dirs.src %>/javascript/KoAlerts.js",
          "<%= dirs.src %>/javascript/App.js",
          "<%= dirs.src %>/javascript/utils/**/*.js",
          "<%= dirs.src %>/javascript/views/**/*.js"
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
        dest: "<%= dirs.target %>/<%= meta.artifactName %>.min.js"
      }
    },
    watch: {
      main: {
        files: '<%= jshint.main.src %>',
        tasks: ['clean', 'jshint', 'concat']
      },
      test: {
        files: '<%= jshint.test.src %>',
        tasks: ['test']
      },
      less: {
        files: ["<%= dirs.src %>/less/**/*.less"],
        tasks: ['clean', "less:compile"]
      },
      pkg: {
        files: 'package.json',
        tasks: ['compile']
      }
    },
    clean: {
      build: ["<%= dirs.target %>"]
    }
  });

  // Tasks
  grunt.registerTask('compile', ['clean', 'jshint', 'concat', 'less:compile']);
  grunt.registerTask('test', ['clean', 'jshint', 'concat', 'jasmine']);
  grunt.registerTask('compress', ['clean', 'jshint', 'concat', 'uglify', 'less:compile', 'less:compress']);

  // Default task
  grunt.registerTask('default', ['compile']);

};
