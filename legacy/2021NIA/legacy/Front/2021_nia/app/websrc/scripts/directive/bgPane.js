//https://codepen.io/tmrDevelops/pen/FCfAk
let bgPane = function () {
    return {
        restrict: 'E',
        require: '^bgSplitter',
        replace: true,
        transclude: true,
        scope: {
            minSize: '='
        },
        template: '<div class="split-pane{{index}}" ng-transclude></div>',
        link: function(scope, element, attrs, bgSplitterCtrl) {
            scope.elem = element;
            scope.index = bgSplitterCtrl.addPane(scope);
        }
    };
};

export default [bgPane]