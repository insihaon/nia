import 'angular'

let beforeUnload = function ($rootScope, $window) {

    $window.onbeforeunload = function (e) {
        var confirmation = {};
        var event = $rootScope.$broadcast('onBeforeUnload', confirmation);
        if (event.defaultPrevented) {
            return confirmation.message;
        }
    };

    $window.onunload = function () {
        $rootScope.$broadcast('onUnload');
    };

    return {};

};

export default ['$rootScope', '$window', beforeUnload]


