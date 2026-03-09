import 'angular'

let container = function ($http, $mdDialog, cipher, $state, $mdSidenav, $log, storeService, $timeout) {

    let factory = {};

    // factory.extendState  = function () {
    //     $log.error('fake extendState');
    // };
    //
    // factory.layoutManager = {
    //
    //     eventHub : {
    //         on : function () { $log.error('fake layoutManager.eventHub.on'); },


    //         emit : function ()  { $log.error('fake layoutManager.eventHub.emit'); },
    //     }
    // };

    factory.layout = function (content) {
        // GoldenLayout이 아닌 독립창으로도 사용가능하게 하려고 했는데,
        // html을 읽어오는 부분을 templateCache로 처리해야 할 것같다
    };

    return factory;
};

export default ['$http', '$mdDialog', 'cipher', '$state', '$mdSidenav', '$log', 'storeService', '$timeout', container]


