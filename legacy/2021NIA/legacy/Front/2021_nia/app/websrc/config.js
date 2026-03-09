System.config({
  baseURL: "./",
  defaultJSExtensions: true,
  transpiler: "babel",
  babelOptions: {
    "optional": [
      "runtime",
      "optimisation.modules.system"
    ]
  },
  paths: {
    "github:*": "jspm_packages/github/*",
    "npm:*": "jspm_packages/npm/*",
    "bower:*": "jspm_packages/bower/*"
  },

  map: {
    "@uirouter/core": "npm:@uirouter/core@5.0.19",
    "angular": "github:angular/bower-angular@1.7.5",
    "angular-animate": "github:angular/bower-angular-animate@1.7.0",
    "angular-aria": "npm:angular-aria@1.7.3",
    "angular-bind-html-compile": "npm:angular-bind-html-compile@1.4.1",
    "angular-ivh-treeview": "npm:angular-ivh-treeview@1.1.0",
    "angular-material": "github:angular/bower-material@1.1.5",
    "angular-messages": "github:angular/bower-angular-messages@1.7.5",
    "angular-sanitize": "npm:angular-sanitize@1.7.5",
    "angular-slimscroll": "bower:angular-slimscroll@1.1.5",
    "angular-touch": "npm:angular-touch@1.7.3",
    "angular-ui-grid": "npm:angular-ui-grid@4.6.3",
    "angular-ui-router": "github:angular-ui/angular-ui-router-bower@1.0.18",
    "angular-ui-router-css": "npm:angular-ui-router-css@0.0.0",
    "angular-ui-router-styles": "npm:angular-ui-router-styles@0.0.6",
    "babel": "npm:babel-core@5.8.38",
    "babel-runtime": "npm:babel-runtime@5.8.38",
    "bootstrap-material-design": "npm:bootstrap-material-design@4.1.1",
    "castillo-io/angular-css": "github:castillo-io/angular-css@1.0.8",
    "chart.js": "npm:chart.js@2.7.2",
    "core-js": "npm:core-js@1.2.7",
    "css": "github:systemjs/plugin-css@0.1.37",
    "fullcalendar": "npm:fullcalendar@3.9.0",
    "jquery": "npm:jquery@3.3.1",
    "moment": "npm:moment@2.22.1",
    "ng-csv": "npm:ng-csv@0.3.6",
    "ng-material-datetimepicker": "npm:ng-material-datetimepicker@1.15.1",
    "rx-angular": "npm:rx-angular@1.1.3",
    "slimScroll": "bower:slimScroll@1.3.8",
    "bower:angular-slimscroll@1.1.5": {
      "jquery": "bower:jquery@3.3.1",
      "slimScroll": "bower:slimScroll@1.3.8"
    },
    "github:angular/bower-angular-animate@1.7.0": {
      "angular": "github:angular/bower-angular@1.7.5"
    },
    "github:angular/bower-angular-aria@1.7.0": {
      "angular": "github:angular/bower-angular@1.7.5"
    },
    "github:angular/bower-angular-messages@1.7.5": {
      "angular": "github:angular/bower-angular@1.7.5"
    },
    "github:angular/bower-material@1.1.5": {
      "angular": "github:angular/bower-angular@1.7.5",
      "angular-animate": "github:angular/bower-angular-animate@1.7.0",
      "angular-aria": "github:angular/bower-angular-aria@1.7.0",
      "css": "github:systemjs/plugin-css@0.1.37"
    },
    "github:jspm/nodelibs-assert@0.1.0": {
      "assert": "npm:assert@1.4.1"
    },
    "github:jspm/nodelibs-buffer@0.1.1": {
      "buffer": "npm:buffer@5.2.1"
    },
    "github:jspm/nodelibs-path@0.1.0": {
      "path-browserify": "npm:path-browserify@0.0.0"
    },
    "github:jspm/nodelibs-process@0.1.2": {
      "process": "npm:process@0.11.10"
    },
    "github:jspm/nodelibs-util@0.1.0": {
      "util": "npm:util@0.10.3"
    },
    "github:jspm/nodelibs-vm@0.1.0": {
      "vm-browserify": "npm:vm-browserify@0.0.4"
    },
    "npm:@uirouter/core@5.0.19": {
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:angular-ivh-treeview@1.1.0": {
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:angular-material@1.1.9": {
      "angular": "github:angular/bower-angular@1.7.5",
      "angular-animate": "github:angular/bower-angular-animate@1.7.0",
      "angular-aria": "github:angular/bower-angular-aria@1.7.0",
      "angular-messages": "github:angular/bower-angular-messages@1.7.5",
      "css": "github:systemjs/plugin-css@0.1.37"
    },
    "npm:angular-ui-grid@4.6.3": {
      "angular": "npm:angular@1.7.3",
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:angular-ui-router-css@0.0.0": {
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:assert@1.4.1": {
      "assert": "github:jspm/nodelibs-assert@0.1.0",
      "buffer": "github:jspm/nodelibs-buffer@0.1.1",
      "process": "github:jspm/nodelibs-process@0.1.2",
      "util": "npm:util@0.10.3"
    },
    "npm:babel-runtime@5.8.38": {
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:buffer@5.2.1": {
      "base64-js": "npm:base64-js@1.3.0",
      "ieee754": "npm:ieee754@1.1.12"
    },
    "npm:chart.js@2.7.2": {
      "buffer": "github:jspm/nodelibs-buffer@0.1.1",
      "chartjs-color": "npm:chartjs-color@2.2.0",
      "fs": "github:jspm/nodelibs-fs@0.1.2",
      "moment": "npm:moment@2.22.1",
      "path": "github:jspm/nodelibs-path@0.1.0",
      "process": "github:jspm/nodelibs-process@0.1.2",
      "systemjs-json": "github:systemjs/plugin-json@0.1.2"
    },
    "npm:chartjs-color-string@0.5.0": {
      "color-name": "npm:color-name@1.1.3"
    },
    "npm:chartjs-color@2.2.0": {
      "buffer": "github:jspm/nodelibs-buffer@0.1.1",
      "chartjs-color-string": "npm:chartjs-color-string@0.5.0",
      "color-convert": "npm:color-convert@0.5.3"
    },
    "npm:color-name@1.1.3": {
      "assert": "github:jspm/nodelibs-assert@0.1.0"
    },
    "npm:core-js@1.2.7": {
      "fs": "github:jspm/nodelibs-fs@0.1.2",
      "path": "github:jspm/nodelibs-path@0.1.0",
      "process": "github:jspm/nodelibs-process@0.1.2",
      "systemjs-json": "github:systemjs/plugin-json@0.1.2"
    },
    "npm:fullcalendar@3.9.0": {
      "jquery": "npm:jquery@3.3.1",
      "moment": "npm:moment@2.22.1",
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:inherits@2.0.1": {
      "util": "github:jspm/nodelibs-util@0.1.0"
    },
    "npm:ng-material-datetimepicker@1.15.1": {
      "angular": "npm:angular@1.7.3",
      "angular-animate": "npm:angular-animate@1.7.0",
      "angular-aria": "npm:angular-aria@1.7.3",
      "angular-material": "npm:angular-material@1.1.9",
      "angular-messages": "npm:angular-messages@1.7.0",
      "moment": "npm:moment@2.22.1"
    },
    "npm:path-browserify@0.0.0": {
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:process@0.11.10": {
      "assert": "github:jspm/nodelibs-assert@0.1.0",
      "fs": "github:jspm/nodelibs-fs@0.1.2",
      "vm": "github:jspm/nodelibs-vm@0.1.0"
    },
    "npm:rx-angular@1.1.3": {
      "rx": "npm:rx@4.1.0"
    },
    "npm:rx@4.1.0": {
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:util@0.10.3": {
      "inherits": "npm:inherits@2.0.1",
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:vm-browserify@0.0.4": {
      "indexof": "npm:indexof@0.0.1"
    }
  }
});
