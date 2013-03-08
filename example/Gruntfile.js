module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    dirs: {
      src: "src/main",
      dest: "target/scala-<%= pkg.scalaVersion %>/resource_managed/main"
    },
    artifactName: "<%= pkg.name %>-<%= pkg.version %>-<%= pkg.buildTime %>",
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
        eqnull: true,
        browser: true,
        globals: {
          jQuery: true,
          $: true,
          console: true,
          ko: true,
          '_': true,
          app: true,
          BsNotices: true
        }
      },
      build: {
        src: [
          "<%= dirs.src %>/javascript/*.js",
          "<%= dirs.src %>/javascript/views/**/*.js",
        ]
      }
    },
    concat: {
      options: {
        separator: ';'
      },
      scripts: {
        src: [
          "<%= dirs.src %>/javascript/libs/jquery-1.8.3.min.js",
          "<%= dirs.src %>/javascript/libs/jquery.dump.js",
          "<%= dirs.src %>/javascript/libs/bootstrap-2.2.1.min.js",
          "<%= dirs.src %>/javascript/libs/knockout-2.2.1.js",
          "<%= dirs.src %>/javascript/libs/underscore-1.4.4.min.js",
          "<%= dirs.src %>/javascript/libs/liftAjax.js",
          "<%= dirs.src %>/javascript/BsNotices.js",
          "<%= dirs.src %>/javascript/app.js",
          "<%= dirs.src %>/javascript/views/**/*.js"
        ],
        dest: "<%= dirs.dest %>/assets/<%= artifactName %>.js"
      }
    },
    uglify: {
      options: {
        banner: '/*! <%= pkg.name %>-<%= pkg.version %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
      },
      build: {
        src: "<%= dirs.dest %>/assets/<%= artifactName %>.js",
        dest: "<%= dirs.dest %>/assets/<%= artifactName %>.min.js"
      }
    },
    less: {
      compile: {
        files: {
          "<%= dirs.dest %>/assets/<%= artifactName %>.css": "<%= dirs.src %>/less/styles.less"
        }
      },
      compress: {
        options: {
          yuicompress: true
        },
        files: {
          "<%= dirs.dest %>/assets/<%= artifactName %>.min.css": "<%= dirs.src %>/less/styles.less"
        }
      }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-less');

  // Default task(s).
  grunt.registerTask('default', ['jshint', 'concat', 'less:compile']);
  grunt.registerTask('compress', ['jshint', 'concat', 'uglify', 'less:compile', 'less:compress']);
  grunt.registerTask('javascript', ['jshint', 'concat']);

};
