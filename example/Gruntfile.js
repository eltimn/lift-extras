module.exports = function(grunt) {
  "use strict";

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    dirs: {
      src: "src/main",
      dest: "target/scala-<%= pkg.scalaVersion %>/resource_managed/main/assets"
    },
    meta: {
      artifactName: "<%= pkg.name %>-<%= pkg.version %>-<%= pkg.buildTime %>",
      concated: "<%= dirs.dest %>/<%= meta.artifactName %>.js"
    },
    jasmine: {
      src : '<%= meta.concated %>',
      options : {
        specs : 'src/test/javascript/**/*.js'
      }
    },
    jshint: {
      options: {
        jshintrc: '.jshintrc'
      },
      main: [
        "<%= dirs.src %>/javascript/*.js",
        "<%= dirs.src %>/javascript/views/**/*.js"
      ],
      test: []
    },
    less: {
      compile: {
        files: {
          "<%= dirs.dest %>/<%= meta.artifactName %>.css": "<%= dirs.src %>/less/styles.less"
        }
      },
      compress: {
        options: {
          yuicompress: true
        },
        files: {
          "<%= dirs.dest %>/<%= meta.artifactName %>.min.css": "<%= dirs.src %>/less/styles.less"
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
        dest: "<%= dirs.dest %>/<%= meta.artifactName %>.min.js"
      }
    },
    watch: {
      main: {
        files: '<%= jshint.main %>',
        tasks: ['jshint', 'concat']
      },
      test: {
        files: 'src/test/javascript/**/*.js',
        tasks: ['test']
      },
      less: {
        files: ["<%= dirs.src %>/less/**/*.less"],
        tasks: "less:compile"
      }
    },
    clean: {
      build: ["<%= dirs.dest %>"]
    }
  });

  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-jasmine');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-clean');

  // Tasks
  grunt.registerTask('compile', ['clean:build', 'jshint', 'concat', 'less:compile']);
  grunt.registerTask('test', ['clean:build', 'jshint', 'concat', 'jasmine']);
  grunt.registerTask('compress', ['clean:build', 'jshint', 'concat', 'uglify', 'less:compile', 'less:compress']);

  // Default task
  grunt.registerTask('default', ['compile']);

};
