import {IndexedDB} from "../../scripts/class/IndexedDB"
import * as Constants from "../class/Constants.js";

export class TicketIndexedDB extends IndexedDB {
    constructor($injector) {
        let tools = $injector.get('tools');
        let storeName = Constants.IndexedDB.TICKET.STORE;

        super($injector, Constants.IndexedDB.RCA_DB, tools.store.indexed_db_version.rca, 'data/ticket_sample.json', { name: storeName, option: { keyPath: "id" }, key: Constants.IndexedDB.TICKET.KEY },
            "ticket_id"
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
            return this.all('dateFilter').then((ticket) => {
                return filter(ticket);
            })
        }
    }

}
TicketIndexedDB.$inject = ['$injector'];


export function register(module) {
    module.service('TicketIndexedDB', TicketIndexedDB);        // baseController.js 를 통해 Singleton 처럼 사용한다
}

