var service = function($injector){
    var service = {
        error: function() {
            self.type = 'error';
            log.apply(self, arguments);
        },
        warn: function() {
            self.type = 'warn';
            log.apply(self, arguments);
        },
        info: function() {
            self.type = 'info';
            log.apply(self, arguments);
        },
        log: function() {
            self.type = 'log';
            log.apply(self, arguments);
        },
        debug: function() {
            self.type = 'debug';
            log.apply(self, arguments);
        },
        enabled: false,
        logs: []
    };

    var log = function() {

        var args = [];
        if (typeof arguments === 'object') {
            for(var i = 0; i < arguments.length; i++ ) {
                var arg = arguments[i];
                if (arg.stack == null) {
                    args.push(arg);
                } else {
                    var exception = {};
                    exception.message = arg.message;
                    exception.stack = arg.stack;
                    args.push(JSON.stringify(exception));
                }
            }
        }

        //var eventLogDateTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
        var eventLogDateTime = moment(new Date()).format('HH:mm:ss');
        var logItem = {
            time: eventLogDateTime,
            message: args.join('\n'),
            type: type
        };


        console.log('[' + logItem.time + '] ' + logItem.message.toString());
        service.logs.push(logItem);
    };

    return service;
};

export default ['$injector', service]