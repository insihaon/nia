import StoreService from '../factory/storeService';

let config = function($logProvider, $provide) {
    $provide.factory('storeService', StoreService);
    let storeService = StoreService[StoreService.length - 1]();

    $logProvider.debugEnabled(storeService.store.debug != null && storeService.store.debug);

    $provide.decorator('$log', ['$delegate', function ($delegate) {
        let originalFns = {};

        // Store the original log functions
        angular.forEach($delegate, function (originalFunction, functionName) {
            originalFns[functionName] = originalFunction;
        });

        let functionsToDecorate = ['debug', 'warn', 'info'];

        // Apply the decorations
        angular.forEach(functionsToDecorate, function (functionName) {
            $delegate[functionName] = logDecorator(originalFns[functionName]);
        });

        return $delegate;
    }]);

    function logDecorator(fn) {
        return function () {
            if (!$logProvider.debugEnabled()) {
                return;
            }

            let args = [].slice.call(arguments);

            // Insert a separator between the existing log message(s) and what we're adding.
            //args.push(' - ');

            // Use (instance of Error)'s stack to get the current line.
            let stack = (new Error()).stack.split('\n').slice(1);

            // Throw away the first item because it is the `$log.fn()` function,
            // but we want the code that called `$log.fn()`.
            stack.shift();

            // We only want the top line, thanks.
            //stack = stack.slice(1, 2);

            // Put it on the args stack.
            //args.push(stack);

            for(let i = 0; i < stack.length; i++) {
                let value = stack[i];
                if(value.indexOf('/jspm_packages/') === -1) {
                    args.push('\n' + stack[i]);
                }
            }

            // Call the original function with the new args.
            fn.apply(fn, args);
        };
    }

    // $provide.decorator('$log', ['$delegate', function ($delegate) {
    //     //Original methods
    //     let origInfo = $delegate.info;
    //     let origLog = $delegate.log;
    //     let origDebug = $delegate.debug;
    //     let origError = $delegate.error;
    //
    //     //Override the default behavior
    //     $delegate.info = function () {
    //         if ($logProvider.debugEnabled())
    //             origInfo.apply(null, arguments)
    //     };
    //
    //     // //Override the default behavior
    //     // $delegate.log = function () {
    //     //     //if ($logProvider.debugEnabled())
    //     //         origLog.apply(null, arguments)
    //     // };
    //
    //     // //Override the default behavior
    //     // $delegate.debug = function () {
    //     //     if ($logProvider.debugEnabled())
    //     //         origDebug.apply(null, arguments)
    //     // };
    //
    //     // //Override the default behavior
    //     // $delegate.error = function () {
    //     //     //if ($logProvider.debugEnabled())
    //     //     origError.apply(null, arguments)
    //     // };
    //
    //     return $delegate;
    // }]);

    // $provide.decorator('$log', ['$delegate', 'Logging', function($delegate, Logging) {
    //     Logging.enabled = true;
    //     let methods = {
    //         error: function() {
    //             if (Logging.enabled) {
    //                 $delegate.error.apply($delegate, arguments);
    //                 //Logging.error.apply(null, arguments);
    //             }
    //         },
    //         log: function() {
    //             if (Logging.enabled) {
    //                 //$delegate.log.apply($delegate, arguments);
    //                 Logging.log.apply(null, arguments);
    //             }
    //         },
    //         info: function() {
    //             if (Logging.enabled) {
    //                 //$delegate.info.apply($delegate, arguments);
    //                 Logging.info.apply(null, arguments);
    //             }
    //         },
    //         warn: function() {
    //             if (Logging.enabled) {
    //                 //$delegate.warn.apply($delegate, arguments);
    //                 Logging.warn.apply(null, arguments);
    //             }
    //         },
    //         debug: function() {
    //             if (Logging.enabled) {
    //                 //$delegate.debug.apply($delegate, arguments);
    //                 Logging.debug.apply(null, arguments);
    //             }
    //         }
    //     };
    //     return methods;
    // }]);

    // $provide.decorator('mdDatepickerDirective', [
    //     '$delegate',
    //
    //     function mdDatePickerDecorator($delegate) {
    //         let directive = $delegate[0];
    //         let compile = directive.compile;
    //
    //         directive.compile = function(tElement) {
    //             let link = compile.apply(this, arguments);
    //             tElement.find("input")[0].setAttribute("readonly","true");
    //             tElement.find("input").bind('click', function(){
    //                 tElement.find("button")[0].click();
    //             });
    //         };
    //
    //         return $delegate;
    //     }
    // ]);
};

export default ['$logProvider', '$provide', config]