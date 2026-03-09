import {pushToArr, setProp} from "./util";

export class IndexedDB {

    constructor($injector, dbName, dbVersion = 1, sourceUrl, store, idProp = '_id', eqFn) {
        this.db = null;
        Object.assign(this, {
            $injector, dbName, dbVersion, sourceUrl, store,
            $http: $injector.get('$http'),
            $timeout: $injector.get('$timeout'),
            $q: $injector.get('$q'),
            _data: undefined,
            _idProp: idProp, // For each data object, the _idProp defines which property has that object's unique identifier
            _eqFn: eqFn ? eqFn : (l, r) => l[this._idProp] === r[this._idProp],  // A basic triple-equals equality checker for two values
        });
        this.self = this;
    }

    openDB() {

        if (this.db) {
            return this.$q.resolve(this.db);
        }

        return this.$q(((resolve, reject) => {
            let {db, dbName, dbVersion, store} = this;

            if (!window.indexedDB) {
                reject('Unsupported indexedDB');
            }
            let request = window.indexedDB.open(dbName, dbVersion);

            request.onsuccess = (e => {
                this.db = e.target.result;
                resolve(e.target.result);
            }).bind(this);

            request.onerror = e => {
                reject(e.target.error);
            }

            request.onupgradeneeded = (e => {
                this.db = request.result;
                this.db.error = e2 => reject(e2.target.error);
                this.db.createObjectStore(store.name, store.option);
            }).bind(this);

        }).bind(this));
    }

    deleteDB() {
        return this.$q(((resolve, reject) => {
            let {db, dbName, dbVersion, store} = this;

            if (window.indexedDB) {
                window.indexedDB.deleteDatabase(dbName);
            }
        }).bind(this));
    }

    deleteStore(storeName) {
        return this.$q(((resolve, reject) => {
            let {db, dbName, dbVersion, store} = this;

            if (db) {
                storeName = storeName || store.name;
                db.deleteObjectStore(storeName);
                db.oncomplete = e => resolve(e.target.result);
                db.onabort = e => reject(e.target.error);
                db.error = e => reject(e.target.error);
            }
        }).bind(this));
    }

    commit(storeName, data) {       // data = {id: v.ticket_id, value: v}
        return this.$q(((resolve, reject) => {
            let {db, dbName, dbVersion, store} = this;

            if (db && data) {
                storeName = storeName || store.name;
                let transaction = db.transaction([storeName], 'readwrite');
                transaction.onabort = te => reject(te.target.error);
                transaction.onerror = te => reject(te.target.error);

                if (Array.isArray(data)) {
                    for (var i = 0; i < data.length; i++) {
                        let request = transaction.objectStore(storeName).put(data[i])
                    }
                    transaction.oncomplete = e => resolve(data.map((v) => {
                        return v.value
                    }));
                } else if (typeof data === 'object') {
                    let request = transaction.objectStore(storeName).put(data);
                    request.onerror = e => reject(e.target.error);
                    request.onsuccess = e => resolve(data.value);
                }
            }
        }).bind(this));
    }

     get(storeName, key) {

        return this.$q((async (resolve, reject) => {

            if (!this.db) {
                await this.load();
            }

            let {db, self, dbName, dbVersion, store} = this;

            if (storeName && key) {
                storeName = storeName || store.name;
                var request = db.transaction([storeName]).objectStore(storeName).get(key)
                request.onerror = e => reject(e.target.error);
                // request.onsuccess = e => resolve(e.target.result.value);
                request.onsuccess = (e => {
                    // debugger;
                    // target.result.value.value.value.value.value
                    if(e.target.result && e.target.result.value) {
                        resolve(e.target.result.value);
                    } else {
                        reject();
                    }
                });
            }
        }).bind(this));
    }

    getAll(storeName) {
        return this.$q(((resolve, reject) => {
            let {db, dbName, dbVersion, store} = this;

            if (db) {
                storeName = storeName || store.name;
                let request = db.transaction(storeName).objectStore(storeName).openCursor(null, IDBCursor.NEXT);
                let results = [];
                request.onsuccess = (e => {
                    let cursor = e.target.result;
                    if (cursor) {
                        // console.log("Key:" + cursor.key + " Value:" + cursor.value.value);
                        results.push(cursor.value.value);
                        cursor.continue();
                    } else {
                        resolve(results);
                    }
                }).bind(this);
                request.onerror = e => reject(e.target.error);
            }
        }).bind(this));

    }

    remove(storeName, key) {
        return this.$q(((resolve, reject) => {
            let {db, dbName, dbVersion, store} = this;

            if (db) {
                storeName = storeName || store.name;
                let request = db.transaction([storeName], 'readwrite').objectStore(storeName).delete(key);
                request.onerror = e => reject(e.target.error);
                request.onsuccess = e => resolve(e.target.result);
            }
        }).bind(this));
    }

    clear(storeName) {
        return this.$q(((resolve, reject) => {
            let {db, dbName, dbVersion, store} = this;

            if (db) {
                storeName = storeName || store.name;
                let request = db.transaction([storeName], 'readwrite').objectStore(storeName).clear();
                request.onerror = e => reject(e.target.error);
                request.onsuccess = e => resolve(e.target.result);
            }
        }).bind(this));
    }

    count(storeName) {
        return this.$q(((resolve, reject) => {
            let {db, dbName, dbVersion, store} = this;

            if (db) {
                storeName = storeName || store.name;
                let request = db.transaction([storeName]).objectStore(storeName).count();
                request.onerror = e => reject(e.target.error);
                request.onsuccess = e => resolve(e.target.result);
            } else {
                reject();
            }

        }).bind(this));
    }

    async load(caller = 'load') {

        let {sourceUrl, $http, self} = this;

        console.log(`indexdb load caller=${caller}`);

        this.isLoadding = true;
        await this.openDB();
        if (sourceUrl && sourceUrl.requestSkip) {
            await self.getAll().then(d => {
                self._data = d;
            });
        } else {
            await this.clear(this.store.name);
            self._data = await request();
        }
        await self.commit(self.store.name, self._data.map((v) => {
            return {id: v.ticket_id, value: v}
        }));

        this.isLoadding = false;
        return self._data;

        async function queryAndPostRequest() {
            if (sourceUrl.query) {
                await sourceUrl.query().then(param => {
                    Object.assign(sourceUrl, param);
                });
            }

            return $http.post(sourceUrl.path || 'service', sourceUrl)
                .then(
                    resp => {
                        if ((resp.data || {}).error) {
                            throw "데이터조회실패"
                        }
                        if (sourceUrl.callback) {
                            return sourceUrl.callback.call(this, resp);
                        }
                        return resp.data;
                    })
        }

        function requestGet() {
            return $http.get(sourceUrl)
                .then(resp => {
                    if (!((resp || {}).data)) throw "데이터조회실패";
                    return resp.data
                }).catch();
        }

        function request() {
            if (sourceUrl && typeof sourceUrl === 'object') {
                sourceUrl.caller = caller;
            }
            return (sourceUrl !== null && typeof sourceUrl === 'object') ?
                queryAndPostRequest()
                : requestGet();
        }

    }

    async reload(caller = 'reload') {
        return this.load(caller);
    }

    async upsert(newItem, allowUpdate = false, allowMerge = false, extend = null, eqFn = this._eqFn) {

        if (!newItem) return this.$q.reject('param is null or undefined');

        if (Array.isArray(newItem)) {
            if (newItem.length == 0) {
                return this.$q.resolve();
            }
            return this.all('upsert1').then((items) => {
                let upserted = [];
                newItem.forEach((v, i, a) => {

                    if (extend) {
                        Object.assign(v, extend);
                    }

                    let idx = items.findIndex(eqFn.bind(null, v));
                    if (idx === -1) {
                        items = pushToArr(items, v);
                        upserted.push(v);
                    } else if (allowUpdate) {
                        items[idx] = Object.assign(items.find(eqFn.bind(null, v)), v);
                        upserted.push(items[idx]);
                    }
                });
                this._data = items;
                this.commit(this.store.name, upserted.map((v) => {
                    return {id: v.ticket_id, value: v}
                }));
                return upserted;
            });
        } else if (typeof newItem === 'object') {
            if (extend) {
                Object.assign(newItem, extend);
            }
            return this.all('upsert2').then((items) => {
                let idx = items.findIndex(eqFn.bind(null, newItem));
                if (idx === -1) {
                    idx = items.length;
                    items = pushToArr(items, newItem);
                } else if (allowUpdate) {
                    items[idx] = Object.assign(items.find(eqFn.bind(null, newItem)), newItem);
                }
                this._data = items;
                this.commit(this.store.name, {id: items[idx].ticket_id, value: items[idx]});
                return items[idx];
            });
        }
    }

    /*async delete(deleteItem, allowUpdate = false, allowMerge = false, extend = null, eqFn = this._eqFn) {

        if (!deleteItem) return this.$q.reject('param is null or undefined');

        if (Array.isArray(deleteItem)) {
            if (deleteItem.length == 0) {
                return this.$q.resolve();
            }
            return this.all().then((items) => {
                let upserted = [];
                deleteItem.forEach((v, i, a) => {

                    if (extend) {
                        Object.assign(v, extend);
                    }

                    let idx = items.findIndex(eqFn.bind(null, v));
                    if (idx === -1) {
                        items = pushToArr(items, v);
                        upserted.push(v);
                    } else if (allowUpdate) {
                        items[idx] = Object.assign(items.find(eqFn.bind(null, v)), v);
                        upserted.push(items[idx]);
                    }
                });
                this._data = items;
                this.commit(this.store.name, upserted.map((v) => {
                    return {id: v.ticket_id, value: v}
                }));
                return upserted;
            });
        } else if (typeof deleteItem === 'object') {
            if (extend) {
                Object.assign(deleteItem, extend);
            }
            return this.all().then((items) => {
                let idx = items.findIndex(eqFn.bind(null, deleteItem));
                if (idx === -1) {
                    items = pushToArr(items, deleteItem);
                } else if (allowUpdate) {
                    items[idx] = Object.assign(items.find(eqFn.bind(null, deleteItem)), deleteItem);
                }
                this._data = items;
                this.commit(this.store.name, {id: items[idx].ticket_id, value: items[idx]});
                return (idx !== -1) ? items[idx] : deleteItem;
            });
        }
    }*/

    async all(caller = 'all') {
        if (!this._data) {
            if (this.isLoadding) {
                return this.$timeout(() => this.all(), 50);
            }
            await this.load(caller);
        }
        return this.$timeout(() => this._data);
    }

    async search(id) {
        let searchObject = {};
        searchObject['ticket_id'] = id;
        let fn_compare = (search, original) => search == original;
        let matchesExample = (example, item) =>
            Object.keys(example).reduce((memo, key) => memo && fn_compare(example[key], item[key]), true);
        // let result = await this.all().then(items => {
        //     return items.filter(matchesExample.bind(null, searchObject));
        // });
        // return result;
        return this._data.filter(matchesExample.bind(null, searchObject));
    }

}
