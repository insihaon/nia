/**
 * Ticket Wrapper Class
 */
export class Ticket {
    /**
     * Creates a new Ticket object
     *
     * @param $http Pass in the $http service
     * @param sourceUrl The url that contains the initial data.
     */
    constructor($injector, sourceUrl, idProp = '_id', eqFn) {
        let _data = [];

        Object.assign(this, {
            sourceUrl,
            defaultValue: [],
            $http: $injector.get('$http'),
            _idProp: idProp, // For each data object, the _idProp defines which property has that object's unique identifier
            _eqFn: eqFn ? eqFn : (l, r) => l[this._idProp] === r[this._idProp],    // A basic triple-equals equality checker for two values
            getData: function () {
                return _data;
            },
            setData: function (value) {
                if(!(value instanceof Array)) {
                    console.error('value is not Array');
                    return;
                }
                if(!value.every(ticket => ticket[this._idProp])) {
                    console.error('value is wrong');
                    return;
                }
                _data = value;
            }
        });
    }

    get data() {
        return this.getData();
    }
    set data(value) {
        this.setData(value);
    }

    async load(isClear = false){

        let {$http, sourceUrl} = this;

        this.isLoadding = true;

        if(isClear){
            this.clear();
        }

        this.data = await request();

        this.isLoadding = false;

        function queryAndPostRequest() {
            return (sourceUrl.query ? sourceUrl.query().then(param => {
                    Object.assign(sourceUrl, param);
                    return requestPost()
                })
                : requestPost());
        }

        function requestPost() {

            return $http.post(sourceUrl.path || 'service', sourceUrl)
                .then(
                    resp => {
                        if ((resp.data || {}).error) {
                            throw "데이터조회실패";
                        }
                        if (sourceUrl.callback) {
                            sourceUrl.callback.call(this, resp);
                        }
                        return resp.data.result != null && Array.isArray(resp.data.result) ? resp.data.result : resp.data
                    });
        }

        function requestGet() {
            return $http.get(sourceUrl)
                .then(resp => {
                    if (!((resp || {}).data)) throw "데이터조회실패";
                    return resp.data
                }).catch();
        }

        function request() {
            return (sourceUrl !== null && typeof sourceUrl === 'object') ?
                queryAndPostRequest()
                : requestGet();
        }

    }

    getAll() {
        return this.data;
    }

    clear()
    {
        this.data = this.defaultValue;
    }

    async reload() {
        return this.load();
    }

    log() {

        return this.all( items => {
            console.log(items);
            debugger;
            return items;
        })
    }

    /**
     * Returns a promise for the item with the given identifier
     * _id 값으로 데이터를 조회한다.
     **/
    get(id) {
        return this.data.find(ticket => ticket[this._idProp] == id);
    }

    /**
     *
     * @param newItem
     * @param allowMerge TRUE: Merge  FALSE: overwrite
     * @param eqFn
     * @return 티켓 data
     */
    upsert(newItem, allowMerge = false, eqFn = this._eqFn) {
        let {sourceUrl} = this;
        if (!newItem) {
            console.error('param is null or undefined');
            return;
        }

        if (Array.isArray(newItem)) {
            newItem.forEach((ticket, index, array) => {
                if(sourceUrl.parse) {
                    sourceUrl.parse(ticket);
                }
                let idx = this.data.findIndex(eqFn.bind(null, ticket));
                if (idx === -1) {
                    this.data.push(ticket);
                } else {
                    if (allowMerge) {
                        this.data[idx] = Object.assign(this.data.find(eqFn.bind(null, ticket)), ticket);
                    } else {
                        this.data[idx] = ticket;
                    }
                }
            });
        } else if (typeof newItem === 'object') {
            if(sourceUrl.parse) {
                sourceUrl.parse(newItem);
            }
            let idx = this.data.findIndex(eqFn.bind(null, newItem));
            if (idx === -1) {
                this.data.push(newItem);
            } else {
                if (allowMerge) {
                    this.data[idx] = Object.assign(this.data.find(eqFn.bind(null, newItem)), newItem);
                } else {
                    this.data[idx] = newItem;
                }
            }
        }

        return this.data;
    }

    /**
     * Returns a promise to remove (DELETE) an item.
     *  _id 혹은 eqFn 으로 찾은 데이터를 삭제한다.
     **/
    delete(item, eqFn = this._eqFn) {
        let idx = this.data.findIndex(eqFn.bind(null, item));
        if (idx === -1) { console.error(`${item} not found in ${this}`); return; }
        this.data.splice(idx, 1);
        return this.data;
    }


}
Ticket.$inject = ['$http', 'storage', 'storageKey', 'sourceUrl'];
