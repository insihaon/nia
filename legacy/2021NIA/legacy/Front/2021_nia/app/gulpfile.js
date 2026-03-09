//npm install --save-dev gulp gulp-concat gulp-connect gulp-sass gulp-sass-themes gulp-sourcemaps gulp-uglify gulp-util gulp-watch gulp-jspm gulp-babel babel-preset-env
var gulp_jspm = require('gulp-jspm'); // npm install gulp-jspm
var gulp = require('gulp');
var gutil = require('gulp-util');
let babel = require('gulp-babel');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var sourcemaps = require('gulp-sourcemaps');
var sass = require('gulp-sass');
var connect = require('gulp-connect');
var watch = require('gulp-watch');

var main = 'app';
var mainsrc = 'websrc/' + main + '.js';

//mangle: false 옵션을 사용하면 constructor Name을 minify 에서 제외한다.
var jspmOption = {fileName: main, inject: true, minify: false, mangle: false, verbose: false};

gulp.task('build_common', function () {

    gulp.src(['websrc/*.html', 'websrc/*.ico', 'websrc/*.js'])
        .pipe(gulp.dest('build/'));

    gulp.src('websrc/config.js')
        .pipe(gulp.dest('build/'));

    gulp.src('websrc/lib/**/*')
        .pipe(gulp.dest('build/lib'));

    gulp.src('websrc/other/**/*')
        .pipe(gulp.dest('build/other'));

    gulp.src('websrc/data/**/*')
        .pipe(gulp.dest('build/data'));

    gulp.src('websrc/json/**/*')
        .pipe(gulp.dest('build/json'));

    gulp.src('websrc/jspm_packages/system.js*')
        .pipe(gulp.dest('build/jspm_packages/'));

    gulp.src('websrc/jspm_packages/system-polyfills.js*')
        .pipe(gulp.dest('build/jspm_packages/'));

    gulp.src('websrc/assets/styles/*.css')
        .pipe(gulp.dest('build/assets/styles/'));

    gulp.src('websrc/assets/fonts/**/*.*')
        .pipe(gulp.dest('build/assets/fonts/'));

    gulp.src('websrc/assets/audio/**/*.*')
        .pipe(gulp.dest('build/assets/audio/'));

    gulp.src('websrc/assets/video/*.*')
        .pipe(gulp.dest('build/assets/video/'));

    gulp.src('websrc/css/**/*.css')
        .pipe(gulp.dest('build/css/'));

    gulp.src('websrc/images/**/*.*')
        .pipe(gulp.dest('build/images/'));

    gulp.src('websrc/jspm_packages/github/angular/bower-material@1.1.5/angular-material.css')
        .pipe(gulp.dest('build/jspm_packages/github/angular/bower-material@1.1.5/'));

    //gulp.src('websrc/demo/**/*.*')
    //    .pipe(gulp.dest('build/demo/'));
    //
    //gulp.src('websrc/views/**/*.*')
    //    .pipe(gulp.dest('build/views/'));
    //

    gulp.src('websrc/pages/**/*.*')
        .pipe(gulp.dest('build/pages/'));
});

gulp.task('build_main', function () {
    return gulp.src(mainsrc)
        .pipe(babel({presets: ['es2015']}))
        .pipe(gulp_jspm(jspmOption))
        .pipe(gulp.dest('build/'));
});

gulp.task('sourcemap_main', function () {
    gulp.src('websrc/jspm_packages/github/angular/bower-material@1.1.5/angular-material.css')
        .pipe(gulp.dest('build/jspm_packages/github/angular/bower-material@1.1.5/'));

    var sourcemaps = require('gulp-sourcemaps');
    return gulp.src(mainsrc)
        .pipe(babel({presets: ['es2015']}))
        .pipe(sourcemaps.init())
        .pipe(gulp_jspm(jspmOption))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest('build/'));
});

gulp.task('build', ['build_common', 'build_main']);
gulp.task('sourcemap', ['build_common', 'sourcemap_main']);

var paths = {
    jspm_packages: 'websrc/jspm_packages/**/*.*',
    lib: 'websrc/lib/**/*',
    other: 'websrc/other/**/*',
    data: 'websrc/data/**/*',
    scripts: 'websrc/scripts/**/*.js',
    css: 'websrc/css/**/*.css',
    styles_scss: 'websrc/assets/styles2/themes/*.scss',
    styles: 'websrc/assets/styles/*.css',
    fonts: 'websrc/assets/fonts/**/*.*',
    audio: 'websrc/assets/audio/**/*.*',
    video: 'websrc/assets/video/*.*',
};

gulp.task('server', function () {
    connect.server({
        livereload: true,
        port: 8888
    });
});

gulp.task('combine-scripts', function () {
    gulp.src(paths.scripts)
        .pipe(babel({presets: ['es2015']}))
        .pipe(uglify())
        .pipe(concat('all.js'))
        .pipe(gulp.dest('dist/scripts'));
});

gulp.task('move-jspm_packages', function () {
    gulp.src(paths.jspm_packages)
        .pipe(gulp.dest('dist/jspm_packages'));
});

gulp.task('move-lib', function () {
    gulp.src(paths.lib)
        .pipe(gulp.dest('dist/lib'));
});

gulp.task('move-other', function () {
    gulp.src(paths.other)
        .pipe(gulp.dest('dist/other'));
});

gulp.task('move-data', function () {
    gulp.src(paths.data)
        .pipe(gulp.dest('dist/data'));
});

gulp.task('move-scripts', function () {
    gulp.src(paths.scripts)
        .pipe(gulp.dest('dist/scripts'));
});

gulp.task('move-css', function () {
    gulp.src(paths.css)
        .pipe(gulp.dest('dist/css'));
});

gulp.task('compile-styles', function () {
    gulp.src(paths.styles_scss)
        .pipe(sourcemaps.init())
        .pipe(sass()).on('error', sass.logError)
        .pipe(sourcemaps.write())
        .pipe(gulp.dest('websrc/assets/styles'));
});

gulp.task('move-styles', function () {
    gulp.src(paths.styles)
        .pipe(gulp.dest('dist/assets/styles'));
});

gulp.task('move-root-html', function () {
    gulp.src(['websrc/*.html', 'websrc/*.ico', 'websrc/*.js'])
        .pipe(gulp.dest('dist'));
});

gulp.task('move-demo-html', function () {
    gulp.src('websrc/demo/**/*.*')
        .pipe(gulp.dest('dist/demo'));
});

// gulp.task('move-views-html', function () {
//     gulp.src('websrc/views/**/*.*')
//         .pipe(gulp.dest('dist/views'));
// });

gulp.task('move-pages-html', function () {
    gulp.src('websrc/pages/**/*.*')
        .pipe(gulp.dest('dist/pages'));
});

gulp.task('move-images', function () {
    gulp.src('websrc/images/**/*')
        .pipe(gulp.dest('dist/images'));
});

gulp.task('move-fonts', function () {
    gulp.src('websrc/assets/fonts/**/*')
        .pipe(gulp.dest('dist/assets/fonts'));
});

gulp.task('move-audio', function () {
    gulp.src(paths.styles)
        .pipe(gulp.dest('dist/assets/audio'));
});

gulp.task('move-video', function () {
    gulp.src(paths.styles)
        .pipe(gulp.dest('dist/assets/video'));
});

gulp.task('livereload', function () {
    gulp.src(['dist/**/*'])
        .pipe(watch('dist/**/*'))
        .pipe(connect.reload());
});

gulp.task('watch', function () {
    //livereload.listen();
    gulp.watch('websrc/*.{html,ico,js}', ['move-root-html']);
    gulp.watch('websrc/demo/**/*.*', ['move-demo-html']);
    // gulp.watch('websrc/views/**/*.*', ['move-views-html']);
    gulp.watch('websrc/pages/**/*.*', ['move-pages-html']);
    gulp.watch(paths.scripts, ['combine-scripts']);
    gulp.watch(paths.jspm_packages, ['move-jspm_packages']);
    gulp.watch(paths.lib, ['move-lib']);
    gulp.watch(paths.other, ['move-other']);
    gulp.watch(paths.data, ['move-data']);
    gulp.watch(paths.scripts, ['move-scripts']);
    gulp.watch(paths.css, ['move-css']);
    gulp.watch('websrc/assets/images/**/*', ['move-images']);
    gulp.watch('websrc/assets/fonts/**/*', ['move-fonts']);
    gulp.watch('websrc/assets/audio/**/*', ['move-audio']);
    gulp.watch('websrc/assets/video/*', ['move-video']);
    gulp.watch('websrc/assets/styles2/**/*.scss', ['compile-styles']);
    gulp.watch('websrc/assets/styles/**/*.css', ['move-styles']);
    gulp.watch('websrc/assets/fonts/**/*', ['move-fonts']);
    //gulp.watch('dist/**').on('change', livereload.changed);
    gulp.src(['*',])
});

gulp.task('default',
    [
        'combine-scripts',
        'move-jspm_packages',
        'move-lib',
        'move-other',
        'move-data',
        'move-scripts',
        'move-css',
        'compile-styles',
        'move-styles',
        'move-fonts',
        'move-root-html',
        //'move-demo-html',
        //'move-views-html',
        'move-pages-html',
        'move-images',
        'watch'
    ]);

gulp.task('serve',
    ['server',
        'default',
        //'livereload',
    ]);
