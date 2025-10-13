class IndexedDb {
    constructor(dbName, dbVersion, stores) {
        this.db;
        this.dbName = dbName;
        this.dbVersion = dbVersion;
        this.stores = stores;
        this.self = this;
    }

    openDB() {
        let { self } = this
        return new Promise(function (resolve, reject) {
            if (!window.indexedDB) {
                reject({ message: 'Unsupported indexedDB' });
            }
            let request = window.indexedDB.open(self.dbName, self.dbVersion);

            request.onsuccess = e => {
                self.db = request.result;
                resolve(self.db)
            };
            request.onerror = e => reject(e.target.error);
            request.onupgradeneeded = e => {
                self.db = e.target.result;
                self.db.onabort = e2 => reject(e2.target.error);
                self.db.error = e2 => reject(e2.target.error);
                self.stores.forEach((o) => {
                    if(self.db.objectStoreNames.contains(o.name) === false) {
                        self.db.createObjectStore(o.name, o.option);
                    }
                });
            };
        })
    }

    deleteDB() {
        let { self } = this
        if (window.indexedDB) {
            window.indexedDB.deleteDatabase(self.dbName);
        }
    }

    deleteStore(storeName) {
        let { self } = this
        return new Promise(function (resolve, reject) {
            if (!self.db) {
                reject({ message: 'indexedDB is null' })
            }
            self.db.deleteObjectStore();
            self.db.oncomplete = e => resolve(e.target.result);
            self.db.onabort = e => reject(e.target.error);
            self.db.error = e => reject(e.target.error);
        })
    }

    upsert(storeName, data) {
        let { self } = this
        return new Promise(function (resolve, reject) {
            if (!(self.db && data)) {
                reject({ message: 'indexedDB is null' })
            }
            let transaction = self.db.transaction([storeName], 'readwrite');
            transaction.onabort = te => reject(te.target.error);
            transaction.onerror = te => reject(te.target.error);

            let request = transaction.objectStore(storeName).put(data);
            request.onerror = e => reject(e.target.error);
            request.onsuccess = e => resolve(e.target.result);
        })
    }

    get(storeName, key) {
        let { self } = this
        return new Promise(function (resolve, reject) {
            if (!(self.db && key)) {
                reject({ message: 'indexedDB is null' })
            }
            
            let request = self.db.transaction([storeName]).objectStore().get(key)
            request.onerror = e => reject(e.target.error);
            request.onsuccess = e => resolve(e.target.result);
        })
    }

    getAll(storeName) {
        let { self } = this
        return new Promise(function (resolve, reject) {
            if (!self.db) {
                reject({ message: 'indexedDB is null' })
            }
            let request = self.db.transaction(storeName).objectStore(storeName).openCursor(null, IDBCursor.NEXT);
            let results = [];
            request.onsuccess = e => {
                let cursor = e.target.result;
                if (cursor) {
                    console.log("Key:" + cursor.key + " Value:" + cursor.value);
                    results.push({ [cursor.key]: cursor.value });
                    cursor.continue();
                } else {
                    resolve(results);
                }
            };
            request.onerror = e => reject(e.target.error);
        })
    }

    remove(storeName, key) {
        let { self } = this
        return new Promise(function (resolve, reject) {
            if (!self.db) {
                reject({ message: 'indexedDB is null' })
            }
            let request = self.db.transaction([storeName], 'readwrite').objectStore(storeName).delete(key);
            request.onerror = e => reject(e.target.error);
            request.onsuccess = e => resolve(e.target.result);
        })
    }

    clear(storeName) {
        let { self } = this
        return new Promise(function (resolve, reject) {
            if (!self.db) {
                reject({ message: 'indexedDB is null' })
            }
            let request = self.db.transaction([storeName], 'readwrite').objectStore(storeName).clear();
            request.onerror = e => reject(e.target.error);
            request.onsuccess = e => resolve(e.target.result);
        })
    }

    count(storeName) {
        let { self } = this
        return new Promise(function (resolve, reject) {
            if (!self.db) {
                reject({ message: 'indexedDB is null' })
            }
            let request = self.db.transaction([storeName]).objectStore(storeName).count();
            request.onerror = e => reject(e.target.error);
            request.onsuccess = e => resolve(e.target.result);
        })
    }
}

(async function test() {
    
    /* 
     * 속도가 매우 느려서 꼭 필요한 경우에만 사용을 권장한다. 
     * 주로, 데이터를 indexDB를 통해 새 창과 공유할 때 사용한다.
     */

    let storeName = 'storeName'
    let db = new IndexedDb("dbName", 1, [{ name: 'storeName', option: { keyPath: "id" } }])
    const MAX = 100

    let label = `### openDB`
    console.time(label)
    await db.openDB()
    console.timeEnd(label)

    label = `### insert ${MAX} rows`
    console.time(label)
    for (let index = 0; index < MAX; index++) {
        await db.upsert(storeName, { id: index, name: `NAME_${index}` })
    }
    console.timeEnd(label)

    label = `### delete ${MAX} rows`
    console.time(label)
    for (let index = 0; index < MAX; index++) {
        await db.remove(storeName, index)
    }
    console.timeEnd(label)

    label = `### clear store`
    console.time(label)
    await db.clear(storeName)
    console.timeEnd(label)
})()