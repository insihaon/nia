import {Storage} from "../../scripts/class/Storage"
import * as Constants from "../class/Constants.js";

/**
 * Fake REST Services (Contacts, Folders, Messages) used in the mymessages submodule.
 *
 * Each of these APIs have:
 *
 * .all()
 * .search(exampleItem)
 * .get(id)
 * .save(item)
 * .post(item)
 * .put(item)
 * .remove(item)
 *
 * See ../util/sessionStorage.js for more details, if curious
 */

/** A fake Contacts REST client API */

// for TEST
export class CurrentTimeStorage extends Storage {
    constructor($injector) {

        super($injector, sessionStorage, "time", {
            service: 'rca',
            action: 'SELECT_CURRENT_TIME',
        });
    }
}
CurrentTimeStorage.$inject = ['$injector'];

export class TicketStorage extends Storage {
    constructor($injector) {

        // super($injector, sessionStorage, "Ticket", {
        //         service: Constants.Service.rca,
        //         //action: Constants.Action.SELECT_TICKET_CUR_LIST,
        //         action: Constants.Action.GET_TICKET_ALL,
        //         // FROM: from,
        //         callback: function (resp) {
        //             let tools = $injector.get('tools');
        //             tools.store.last_event_sequence = resp.data.RESULT1.last_sequence || '';
        //             tools.store.isTicketsLoaded = true;
        //             return resp.data.RESULT2;
        //         },
        //         query: function () {
        //             let filterStorage = $injector.get('FilterStorage');
        //             return filterStorage.loadFilter().then((ticketFilter) => {
        //                 let from = moment(new Date().setHours(0, 0, 0, 0) - new Date(1000 * 60 * 60 * 24 * ticketFilter.maxDays)).format('YYYY-MM-DD HH:mm:ss');
        //                 // return {FROM : from};
        //                 return {MAX_DAYS: ticketFilter.maxDays};
        //             })
        //         }
        //     }, undefined,
        //     "ticket_id" //,(l, r) => l[this.ticket_id] === r[this.ticket_id]
        // );
        super($injector, sessionStorage, "Ticket", undefined, undefined,
            "ticket_id" //,(l, r) => l[this.ticket_id] === r[this.ticket_id]
        );
    }

    dateFilter(start, end, tickets) {
        let filter = (arr) => {
            let result;

            if (!start && end) {
                result = arr.filter((item) => {
                    return item.ticket_generation_time <= end;
                });
            } else if (start && !end) {
                result = arr.filter((item) => {
                    return start <= item.ticket_generation_time;
                });
            } else if (start && end) {
                result = arr.filter((item) => {
                    return start <= item.ticket_generation_time && item.ticket_generation_time <= end;
                });
            } else {
                result = arr;
            }
            return result;
        };

        if (tickets) {
            if (tickets.constructor == Object) {
                let arr = [];
                arr.push(tickets);
                return filter(arr);
            } else if (tickets.constructor == Array) {
                return this.$timeout(() => {
                    return filter(tickets);
                });
            }
        } else {
            return this.all((ticket) => {
                return filter(ticket);
            })
        }
    }
}

TicketStorage.$inject = ['$injector'];

export class FilterStorage extends Storage {
    constructor($injector) {

        let initValue = {
            date: {
                start: '',
                end: ''
            },
            maxDays: 14,
            ticketFilter1: {
                state: [
                    {display: Constants.TicketStatus.INIT.text, value: Constants.TicketStatus.INIT.code, selected: true},
                    {display: Constants.TicketStatus.ACK.text, value: Constants.TicketStatus.ACK.code, selected: true},
                    {display: Constants.TicketStatus.FIN.text, value: Constants.TicketStatus.FIN.code, selected: true},
                    {display: Constants.TicketStatus.AUTO_FIN.text, value: Constants.TicketStatus.AUTO_FIN.code, selected: true}
                ],
            }
        }

        super($injector, localStorage, "Filter", undefined,  initValue,
            undefined //,(l, r) => l[this.ticket_id] === r[this.ticket_id]
        );

        this.initValue = initValue;
        this.loadFilter().then((function (filter) {
            this.filter = filter;
        }).bind(this));
    }

    loadFilter(isChangeMaxDay = false) {
        return this.all(items => {
            //if(items < 1)
            //    return;

            var userFilter = items[0];
            var isSetFilter = false;

            var newFilter = angular.copy(userFilter);
            var ticketMaxTerm = new Date().setHours(0, 0, 0, 0) - new Date(1000 * 60 * 60 * 24 * userFilter.maxDays);

            if (newFilter.date.start < ticketMaxTerm || isChangeMaxDay || newFilter.date.start == undefined) {
                newFilter.date.start = ticketMaxTerm;
                isSetFilter = true;
            }
            if (newFilter.date.end != '' && newFilter.date.end < ticketMaxTerm) {
                newFilter.date.end = '';

                isSetFilter = true;
            }
            if (isSetFilter) {
                this.setData(newFilter);
                return newFilter;
            }
            return userFilter;
        })
    }

    saveFilter(filter) {
        // if (filter.date.end) {
        //     filter.date.end = new Date(filter.date.end).setHours(0, 0, 0, 0) + (1000 * 60 * 60 * 24) - 1000;
        // }

        this.setData(JSON.parse(JSON.stringify(filter, (key, value) => (key === "$$hashKey" ? undefined : value))));
    }

}
FilterStorage.$inject = ['$injector'];

export class SqlStorage extends Storage {
    constructor($injector) {

        let initValue = [];

        super($injector, sessionStorage, "Sql", undefined,  initValue,
            undefined //,(l, r) => l[this.ticket_id] === r[this.ticket_id]
        );
    }
}
SqlStorage.$inject = ['$injector'];

export function register(module) {
    module.service('CurrentTimeStorage', CurrentTimeStorage);
    module.service('TicketStorage', TicketStorage);        // baseController.js 를 통해 Singleton 처럼 사용한다
    module.service('FilterStorage', FilterStorage);        // baseController.js 를 통해 Singleton 처럼 사용한다
    module.service('SqlStorage', SqlStorage);
}

