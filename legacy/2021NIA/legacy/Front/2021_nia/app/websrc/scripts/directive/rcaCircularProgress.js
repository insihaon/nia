let rcaCircularProgress = function () {
    return  {
        restrict: 'E',
        scope: {
            size: '@',
            ngModel: '='
        },
        require: 'ngModel',
        template: '<div layout="row" md-theme="grey" layout-sm="column" ng-hide="!ngModel" layout-align="space-around">' +
            '          <md-progress-circular ng-disabled="!ngModel" md-diameter="{{size}}"></md-progress-circular>' +
            '      </div>',
        compile: function(element, attrs){
            const defaultSize = '40';
            if (attrs.size == undefined || isNaN(attrs.size)) { attrs.size = defaultSize; }

            let style = { 'position': 'absolute', 'z-index': '1000' };
            let halfSize = Math.floor(parseInt(attrs.size) / 2);

            if(!element.css('top') || element.css('top') == 'auto'){ style.top = `calc(50% - ${halfSize}px)` };
            if(!element.css('left') || element.css('left') == 'auto'){ style.left = `calc(50% - ${halfSize}px)` };
            element.css(style);

            return function($scope) {
                const defaultModel = false;
                if ($scope.ngModel == undefined || typeof($scope.ngModel) != 'boolean' ) { $scope.ngModel = defaultModel; }
            };
        }
    };
}

export default [rcaCircularProgress]
