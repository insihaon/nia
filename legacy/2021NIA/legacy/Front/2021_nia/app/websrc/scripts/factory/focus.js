var focus = function ($rootScope, $timeout) {
    return function (name) {
        $timeout(function () {
            $rootScope.$broadcast('focusOn', name);
        });
    }
};

export default ['$rootScope', '$timeout', focus]