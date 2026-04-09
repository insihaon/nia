@REM npm install -g uglify-js
@REM uglify-js 3.14.3

@REM window
uglifyjs ../js/d3.v4.js ^
         ../js/d3-tip.js ^
         ../js/dat.gui.min.js ^
         ../js/d3-context-menu.js ^
         ../global.js ^
         ../index_dev.js ^
         -o map2d.min.js ^
         --source-map ^
         url=map2d.min.js.map ^
         -c -m 
@REM mac os
uglifyjs ../js/d3.v4.js \
         ../js/d3-tip.js \
         ../js/dat.gui.min.js \
         ../js/d3-context-menu.js \
         ../global.js \
         ../index_dev.js \
         -o map2d.min.js \
         --source-map \
         url=map2d.min.js.map \
         -c -m 

@REM type map2d.min.js | find "sourceMappingURL" 

