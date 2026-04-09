; (function (global) {

    class IndexedArray {
        constructor(request, keyName, defaultValues = []) {
            this.self = this
            this.request = request || new Promise(resolve => setTimeout(() => { resolve() }, 3000))
            this.keyName = keyName || 'id'
            this.rows = defaultValues
            this.rowsIndex = {}
            console.log(`request and wait ... 3000 ms`)
            
            this.response = this.request.then((data = []) => {
                this.rows.push(...data)
                this.buildIndex()
                this.print('response data:')
            })
        }

        clear() {
            let { rows, rowsIndex } = this
            rows.splice(0)
            rowsIndex = {}
            return this
        }

        normalize() {
            this.rows = this.toArray()
            this.buildIndex()
            return this
        }

        arrayFindIndexTest(key) {
            let { rows, keyName } = this
            return rows.findIndex(row => row[keyName] === key)
        }

        getKey(row) {
            let { keyName } = this
            return row && row[keyName]
        }

        print(description = '') {
            let { rows, rowsIndex } = this
            console.log(description, rows, rowsIndex)
        }

        buildIndex() {
            let { self, rows, rowsIndex } = this

            for (let index = 0; index < rows.length; index++) {
                let row = rows[index]
                let key = self.getKey.call(self, row)
                key && (rowsIndex[key] = index)
            }
        }

        toArray(fn = row => row) {
            let { rows } = this
            return rows.filter(fn)
        }

        // 성능을 높이려면 toArray 함수를 사용하세요
        async toArrayAsync() {
            await this.response;
            let { rows } = this
            return rows.filter(row => row)
        }

        find(key) {
            let { rows, rowsIndex } = this
            if (!key) return;
            return rows[rowsIndex[key]]
        }

        // 성능을 높이려면 find 함수를 사용하세요
        async findAsync(key) {
            await this.response;
            let { rows, rowsIndex } = this
            if (!key) return;
            return rows[rowsIndex[key]]
        }

        async insert(row) {
            await this.response;
            let { self, rows, rowsIndex } = this
            let key = self.getKey(row)
            if (key === undefined) return;

            if (!rowsIndex[key]) {
                let index = rows.push(row)
                rowsIndex[key] = index - 1
            }
            // self.print()
        }

        async update(row) {
            await this.response;
            let { self, rows, rowsIndex } = this
            let key = self.getKey(row)
            if (key === undefined) return;

            if (rowsIndex[key] !== undefined) {
                rows[rowsIndex[key]] = row
                // self.print()
            }
        }

        async delete(row) {
            await this.response;
            let { self, rows, rowsIndex } = this
            let key = self.getKey(row)
            if (key === undefined) return;

            if (rowsIndex[key] !== undefined) {
                rows[rowsIndex[key]] = null
                delete rowsIndex[key]
                // self.print()
            }
        }
    }

    (async function performance() {
        let defaultValues = [/* { id: "dummy" } */]
        let memory = global.m = new IndexedArray(null, undefined, defaultValues)
        const MAX = 100000

        let label = `### insert ${MAX} rows`
        console.time(label)
        for (let index = 0; index < MAX; index++) {
            await memory.insert({ id: `id_${index}` })
        }
        console.log('\n')
        console.timeEnd(label)

        label = `toArray`
        console.time(label)
        let copy = window.copy = memory.toArray()
        console.log('length=', copy.length)
        console.timeEnd(label)

        label = `order by desc`
        console.time(label)
        copy.sort((a, b) => (b.id).localeCompare(a.id));
        console.timeEnd(label)

        label = `find index=${MAX - 1}`
        console.time(label)
        memory.find(`id_${MAX - 1}`)
        console.timeEnd(label)

        label = `Array.findIndex index=${0}`
        console.time(label)
        memory.arrayFindIndexTest(`id_${0}`)
        console.timeEnd(label)

        label = `Array.findIndex index=${MAX - 1}`
        console.time(label)
        memory.arrayFindIndexTest(`id_${MAX - 1}`)
        console.timeEnd(label)

        console.log('\n')
        label = `### update ${MAX} rows`
        console.time(label)
        for (let index = 0; index < MAX; index++) {
            await memory.update({ id: `id_${index}`, updated: true })
        }
        console.timeEnd(label)
        console.log('length=', memory.toArray().length)

        console.log('\n')
        label = `### delete ${MAX} rows`
        console.time(label)
        for (let index = 0; index < MAX; index++) {
            await memory.delete({ id: `id_${index}` })
        }
        console.timeEnd(label)

        label = `toArray before normalize`
        console.time(label)
        console.log('length=', memory.toArray().length)
        console.timeEnd(label)

        label = `normalize`
        console.time(label)
        memory.normalize()
        console.timeEnd(label)

        label = `toArray after normalize`
        console.time(label)
        memory.toArray()
        console.timeEnd(label)
    })()

})(typeof exports !== 'undefined' ? exports : this);
