//var FocusOn = function() {
//	  return function(scope, elem, attr) {
//	    scope.$on('focusOn', function(e, name) {
//	      if(name === attr.focusOn) {
//	        elem[0].focus();
//	      }
//	    });
//	  };
//};

let focusOn = function () {
    return {
        restrict: 'A',
        //replace: true,
        //template: '<div></div>',
        link: function (scope, element, attrs) {
            scope.$on('focusOn', function (e, name) {
                if (name === attrs.focusOn) {
                    element[0].focus();
                }
            });
        }
    }
};

export default [focusOn]