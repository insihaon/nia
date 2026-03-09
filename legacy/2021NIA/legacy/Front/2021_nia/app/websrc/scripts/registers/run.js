import Store from '../../scripts/class/SingletonStore'

function run() {
    this.run(['$rootScope', '$location', '$state', '$transitions',  function ($rootScope, $location, $state, $transitions) {


        // https://ui-router.github.io/guide/transitionhooks
        // 미인증 상태인 경우 로그인 페이지로 이동 하는 코드
        // 방법1.
        /*$transitions.onStart({}, function (transition) {

            if (Store.auth != true && transition.to().name != Store.login_page) {
                $state.go(Store.login_page);
                return false;
            }
        });*/

        // 방법2.
        $transitions.onBefore({}, function(transition) {
            // check if the state should be protected
            if (Store.auth != true && transition.to().name != Store.login_page) {
                // redirect to the 'login' state
                return transition.router.stateService.target(Store.login_page);
            }
        });

    }]);
}

export {run}
