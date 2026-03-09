var popups = function() {
    return {
    	restrict: 'EA',
        replace: true,
        scope: { timeLabel: '@' },
        templateUrl: 'pages/directive/progress-popup.html',
        link: function($scope, element, attrs) {

        }
    }
};

export default [popups]
