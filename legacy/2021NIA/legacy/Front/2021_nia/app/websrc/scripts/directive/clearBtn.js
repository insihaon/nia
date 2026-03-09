let clearBtn = function ($timeout) {
    return {
        restrict: 'A',
        compile: function (element, attrs) {
            var action = attrs.ngModel + " = ''";
            element.after(
            '<i class="clear-btn fa fa-close fa-lg" ' +
            'ng-show="' + attrs.ngModel + '" ng-click="' + action + '"' +
            '></i>');
        }
    }
}

export default ['$timeout',clearBtn]

