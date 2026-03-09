let mdSelectAutoHide = function ($mdSelect) {
    return {
        restrict: 'A',
        link: function (scope, elem, attrs) {
            angular.element(document).ready(() => {
                elem.on('click', function (e) {
                    if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                        $mdSelect.hide();
                    }
                });
            });
        }
    };
}

export default ['$mdSelect', mdSelectAutoHide]

