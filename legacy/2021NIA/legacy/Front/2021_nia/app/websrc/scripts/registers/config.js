import Theme from '../config/theme';
import StateProvider from '../config/stateProvider';
import Icons from '../config/icons';
import DateLocale from '../config/dateLocale';
import LogProvider from '../config/logProvider';
import InterceptorHttp from '../factory/interceptorHttp';

function config() {
    this.config(Theme);
    this.config(StateProvider);
    this.config(Icons);
    this.config(LogProvider);
    this.config(DateLocale);

    this.config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push(InterceptorHttp);
    }]);

    this.config(['$compileProvider', function ($compileProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|sms|tel):/);
    }]);

    this.config(['$qProvider', function ($qProvider) {
        $qProvider.errorOnUnhandledRejections(false); //  Possibly unhandled rejection 에러에 대한 예외 처리
    }]);

    this.config(['$provide', function($provide){
        $provide.decorator('uiGridAutoResizeDirective', ['$delegate', function($delegate) {
            //$delegate is array of all ui-grid-auto-resize directive
            //in this case frist one is angular buildin ui-grid-auto-resize
            //so we remove it.
            $delegate.shift();
            return $delegate;
        }]);
    }]);

    //ng-include
    // this.config(['$sceDelegateProvider', function($sceDelegateProvider) {
    //     $sceDelegateProvider.resourceUrlWhitelist([
    //         //'http://**',
    //         //'https://**',
    //         'http://localhost:8080/**',
    //     ]);
    // }]);
}

export {config}
