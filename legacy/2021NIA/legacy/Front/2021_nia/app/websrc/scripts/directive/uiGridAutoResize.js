let uiGridAutoResize = function ($timeout, gridUtil) {
    return {
        require: 'uiGrid',
        scope: false,
        link: function ($scope, $elm, $attrs, uiGridCtrl) {
            var prevGridWidth, prevGridHeight;

            function getDimensions() {
                prevGridHeight = gridUtil.elementHeight($elm);
                prevGridWidth = gridUtil.elementWidth($elm);
            }

            // Initialize the dimensions
            getDimensions();

            var resizeTimeoutId;
            function startTimeout() {
                clearTimeout(resizeTimeoutId);

                resizeTimeoutId = setTimeout(function () {
                    var elemVisible = $elm.is(":visible");
                    var newGridHeight = elemVisible ? gridUtil.elementHeight($elm) : 0;
                    var newGridWidth = elemVisible ? gridUtil.elementWidth($elm) : 0;

                    if (elemVisible && (newGridHeight !== prevGridHeight || newGridWidth !== prevGridWidth)) {
                        uiGridCtrl.grid.gridHeight = newGridHeight;
                        uiGridCtrl.grid.gridWidth = newGridWidth;

                        $scope.$apply(function () {
                            uiGridCtrl.grid.refresh()
                                .then(function () {
                                    getDimensions();

                                    startTimeout();
                                });
                        });
                    }
                    else {
                        startTimeout();
                    }
                }, 250);
            }

            startTimeout();

            $scope.$on('$destroy', function() {
                clearTimeout(resizeTimeoutId);
            });
        }
    };
}

export default ['$timeout', 'gridUtil',uiGridAutoResize]
