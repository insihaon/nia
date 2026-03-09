export function RcaAlarmFilter() {
    return function (alarms, param) {
        return alarms.filter(function (alarm) {

            try {

                // var regExp = [new RegExp('Geumsan_GR1-BS2\\(1505Bay\\).+-3-.+\\(OUT\\)', 'g'),
                //     new RegExp('Jangsu_GR1-BS2\\(801\\).+-3-.+\\(IN\\)', 'g')];

                // var regExp = [null, new RegExp('Jangsu_GR1-BS2\\(801\\).+-3-.+\\(IN\\)', 'g')];
                // var regExp = [new RegExp('Geumsan_GR1-BS2\\(1505Bay\\).+-3-.+\\(OUT\\)', 'g'), null];

                var regExp = param;

                if( regExp[0] && regExp[1] ){ // 둘다 존재할경우
                    return regExp[0].test(alarm.monitored_object) || regExp[1].test(alarm.monitored_object);
                } else if(!regExp[0] && !regExp[1]) { // 둘다 존재하지 않을 경우
                    return false;
                } else {
                    return ( regExp[0] || regExp[1] ).test(alarm.monitored_object);
                }
            } catch (e) {
                return false;
            }
        });
    };
}
export function RcaAlarmTimeFilter() {
    return function (alarms, param) {
        let startTime = (param.fault_time - (1000 * 60 * 60 * 3));
        let endTime = param.fault_time;
        return alarms.filter(function (alarm) {

            try {
                // param.fault_time
                return startTime <= alarm.end_time && alarm.end_time <= endTime;
                /*var a=0;
                return true;*/
            } catch (e) {
                return false;
            }
        });
    };
}

export function NiaAlarmFilter() {
    return function (alarms, param) {
        return alarms.filter(function (alarm) {

            try {
                return (alarm.sysname == param.root_cause_sysnamea);
            } catch (e) {
                return false;
            }
        });
    };
}

export function NiaPfFilter() {
    return function (tickets, param) {
        return tickets.filter(function (ticket) {

            try {
                if(ticket.ticket_type != 'PF') {
                    return true;
                }
                else {
                    return param == true ? true : false;
                }
            } catch (e) {
                return false;
            }
        });
    };
}
