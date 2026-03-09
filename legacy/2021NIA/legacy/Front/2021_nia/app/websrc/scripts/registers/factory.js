import Container from '../factory/container';
import State from '../factory/state';
import Event from '../factory/event';
import Tools from '../factory/tools';
import Cipher from '../factory/cipher';
import Focus from '../factory/focus';
import Logging from '../services/logging';
import StoreService from '../factory/storeService';
import * as DataSource from '../services/storage.service.js';
import * as IndexedDBSource from '../services/indexedDB.service';
import * as TicketSource from '../services/ticket.service';


function factory() {
    this.factory('container', Container);
    this.factory('state', State);
    this.factory('event', Event);
    this.factory('tools', Tools);
    this.factory('cipher', Cipher);
    this.factory('focus', Focus);
    //this.factory('beforeUnload', BeforeUnload);
    this.service('Logging', Logging);
    this.service('storeService', StoreService);

    //this.run(function (beforeUnload) {
    //    // Must invoke the service at least once
    //});

    DataSource.register(this);
    IndexedDBSource.register(this);
    TicketSource.register(this);
}

export {factory}
