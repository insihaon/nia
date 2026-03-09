import {Ticket} from "../../scripts/class/Ticket"
import * as Constants from "../class/Constants.js";

// not use
export class RcaTicket extends Ticket {
    constructor($injector) {

        super($injector, 'data/ticket_sample.json', "ticket_id",
        );
    }
}

RcaTicket.$inject = ['$injector'];

export class InspectorTicket extends Ticket {
    constructor($injector) {

        super($injector, 'data/inspector_sample.json', "inspector_seq",
        );
    }

    dateFilter(start, end, tickets = this.data) {
        tickets = Array.isArray(tickets) ? tickets : [].concat(tickets);

        return tickets.filter(ticket => {
            return (start ? start <= ticket.generation_time : true)
                && (end ? ticket.generation_time <= end : true);
        })
    }
}

InspectorTicket.$inject = ['$injector'];

export function register(module) {
    // module.service('RcaTicket', RcaTicket);
    module.service('InspectorTicket', InspectorTicket);
}

