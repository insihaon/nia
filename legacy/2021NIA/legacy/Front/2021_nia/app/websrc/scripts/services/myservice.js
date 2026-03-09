(function() {
    angular.module('myservice', []).factory('testService', ['$http', function ($http) {
        return {
            target: 'view',
            views: [
                {name: 'login', path: "login.html", controller: 'LoginCtrl'}
                , {name: 'page1', path: "view/page1.html", controller: 'ViewCtrl'}
                , {name: 'page2', path: "view/page2.html", controller: 'ViewCtrl'}
                , {name: 'page1.page1', path: "view/page3.html", controller: 'ViewCtrl2'}
                , {name: 'page1.page2', path: "view/page4.html", controller: 'ViewCtrl2'}
                , {name: 'page1.page1.page1', path: "view/page3.html", controller: 'ViewCtrl2'}
                , {name: 'page2.page1', path: "view/page3.html", controller: 'ViewCtrl2'}
                , {name: 'page2.page2', path: "view/page4.html", controller: 'ViewCtrl2'}
                , {name: 'page2.page1.page1', path: "view/page3.html", controller: 'ViewCtrl2'}
            ],
            getViews: function () {
                return views;
            },
            setViews: function (views) {
                this.views = views;
            }
        };
    }]);
})();