/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2020 Adobe Systems Incorporated
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

const path = require('path');

const BUILD_DIR = path.join(__dirname, 'dist');

const CLIENTLIB_DIR = path.join(
  __dirname,
  '..',
  'ui.apps',
  'src',
  'main',
  'content',
  'jcr_root',
  'apps',
  'sony',
  'clientlibs'
);

const libsBaseConfig = {
  allowProxy: true,
  serializationFormat: 'xml',
  cssProcessor: ['default:none', 'min:none'],
  jsProcessor: ['default:none', 'min:none']
};

// Config for `aem-clientlib-generator`
module.exports = {
  context: BUILD_DIR,
  clientLibRoot: CLIENTLIB_DIR,

  libs: [
    // ✅ Dependencies (no change)
    {
      ...libsBaseConfig,
      name: 'clientlib-dependencies',
      categories: ['sony.dependencies'],
      assets: {
        js: {
          cwd: 'clientlib-dependencies',
          files: ['**/*.js'],
          flatten: false
        },
        css: {
          cwd: 'clientlib-dependencies',
          files: ['**/*.css'],
          flatten: false
        }
      }
    },

    // ✅ Updated from clientlib-site → clientlib-sony
    {
      ...libsBaseConfig,
      name: 'clientlib-sony',
      categories: ['sony.clientlibs'],   // 👉 you can rename this if needed
      dependencies: ['sony.dependencies'],

      assets: {
        js: {
          cwd: 'clientlib-sony',
          files: ['**/*.js'],
          flatten: false
        },
        css: {
          cwd: 'clientlib-sony',
          files: ['**/*.css'],
          flatten: false
        },

        // resources (images/fonts etc.)
        resources: {
          cwd: 'clientlib-sony',
          files: ['**/*.*'],
          flatten: false,
          ignore: ['**/*.js', '**/*.css']
        }
      }
    }
  ]
};