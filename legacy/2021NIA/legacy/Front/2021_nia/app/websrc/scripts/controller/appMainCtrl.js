import Base from '../controller/base'

class appMainCtrl extends Base {
    constructor($rootScope, $scope, $state, tools, $log) {
    	super($scope, tools);

        tools.$debug('appMainCtrl');
    	
        tools.injection($scope);
    }
}

export default ['$rootScope', '$scope', '$state', 'tools', '$log', appMainCtrl];

//appMainCtrl.$inject = ['$rootScope', '$scope', '$state'];
//export default appMainCtrl;


//function appMainCtrl($rootScope, $scope, $state) {
//	console.log('appMainCtrl');
//	
//	$scope.logout = function() {
//		location.replace('/logout');	
//	}
//	
//	$scope.statego = function(page) {
//		$state.go('view_' + page);	
//	}
//}
//
//export default ['$rootScope', '$scope', '$state', appMainCtrl]
