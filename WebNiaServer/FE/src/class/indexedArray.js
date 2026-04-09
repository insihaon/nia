import { wait } from '@/utils'

export default class IndexedArray {
  constructor(request, keyName, defaultValues = []) {
    const delay = 0
    this.self = this
    this.request = request ?? (delay ? wait(delay) : Promise.resolve())
    this.keyName = keyName ?? 'id'
    this.rows = defaultValues
    this.rowsIndex = {}

    if (request) {
      this.response = this.request.then((data = []) => {
        this.onInit(data)
      })
    } else {
      this.buildIndex()
    }
  }

  static createAsync(request, keyName, defaultValues) {
    const instance = new IndexedArray(null, keyName, defaultValues)
    return (async() => {
      const data = await (request ?? Promise.resolve())
      instance.onInit(data)
      return instance
    })()
  }

  onInit(data = []) {
    if (data.length > 0) {
      if (this.rows > 0) {
        console.warn('초기값에 응답 데이터를 merge합니다. 무결성 데이터 사용은 createAsync 을 이용하세요')
        this.merge(data, true)
      } else {
        this.rows.push(...data)
        this.buildIndex()
      }
    }

    console.log(`데이터 요청에 대한 응답이 완료되었습니다. : ${data.length} 건`)
    this.response = null
  }

  clear() {
    this.rows.splice(0)
    this.rowsIndex = {}
    return this
  }

  normalize() {
    this.rows = this.toArray()
    this.buildIndex()
    return this
  }

  arrayFindIndexTest(key) {
    const { rows, keyName } = this
    return rows.findIndex(row => row[keyName] === key)
  }

  getKey(row) {
    const { keyName } = this
    return row && row[keyName]
  }

  print(description = '') {
    const { rows, rowsIndex } = this
    console.log(description, rows, rowsIndex)
  }

  buildIndex() {
    const { self, rows } = this

    for (let index = 0; index < rows.length; index++) {
      const row = rows[index]
      const key = self.getKey(row)
      key && (this.rowsIndex[key] = index)
    }
  }

  toArray(fn) {
    const { rows } = this
    return fn ? rows.filter(fn) : rows
  }

  // 성능을 높이려면 toArray 함수를 사용하세요
  async toArrayAsync() {
    this.response && await this.response

    const { rows } = this
    return rows.filter(row => row)
  }

  find(key) {
    const { rows, rowsIndex } = this
    if (!key) return
    return rows[rowsIndex[key]]
  }

  // 성능을 높이려면 find 함수를 사용하세요
  async findAsync(key) {
    const { rows, rowsIndex } = this
    if (!key) return
    return rows[rowsIndex[key]]
  }

  async insert(data = []) {
    this.response && await this.response

    const { self, rows, rowsIndex } = this
    data = Array.isArray(data) ? data : [data]
    data.forEach(async row => {
      const key = self.getKey(row)
      if (key === undefined) return

      if (!rowsIndex[key]) {
        const index = rows.push(row)
        rowsIndex[key] = index - 1
      }
      // self.print()
    })
  }

  async update(data = [], execInsert = true) {
    this.response && await this.response

    const { self, rows, rowsIndex } = this
    data = Array.isArray(data) ? data : [data]
    data.forEach(async row => {
      const key = self.getKey(row)
      if (key === undefined) { return }

      if (rowsIndex[key] !== undefined) {
        rows[rowsIndex[key]] = row
        // self.print()
      } else if (execInsert) {
        return await self.insert(row)
      }
    })
  }

  async merge(data = [], execInsert = true) {
    this.response && await this.response

    const { self, rows, rowsIndex } = this
    data = Array.isArray(data) ? data : [data]
    data.forEach(async row => {
      const key = self.getKey(row)
      if (key === undefined) { return }
      if (rowsIndex[key] !== undefined) {
        Object.assign(rows[rowsIndex[key]], row)
        // self.print()
      } else if (execInsert) {
        return await self.insert(row)
      }
    })
  }

  async delete(data = []) {
    this.response && await this.response

    const { self, rows, rowsIndex } = this
    data = Array.isArray(data) ? data : [data]
    data.forEach(async row => {
      const key = self.getKey(row)
      if (key === undefined) return

      if (rowsIndex[key] !== undefined) {
        rows[rowsIndex[key]] = undefined
        delete rowsIndex[key]
        // self.print()
      }
    })
  }
}

(async function performance(isTest = false) {
  if (isTest !== true) return

  const defaultValues = [/* { id: "dummy" } */]
  const memory = global.memory = new IndexedArray(null, undefined, defaultValues)
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
  const copy = memory.toArray()
  console.log('length=', copy.length)
  console.timeEnd(label)

  label = `order by desc`
  console.time(label)
  copy.sort((a, b) => (b.id).localeCompare(a.id, 'en', { numeric: false }))
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
  label = `### merge ${MAX} rows`
  console.time(label)
  for (let index = 0; index < MAX; index++) {
    await memory.merge({ id: `id_${index}`, merged: true })
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

  label = `### Response 받아 초기화 하기 전, merge 호출 시 데이터 출력 테스트`
  const delay = 1000
  const tickets = new IndexedArray(this.wait(delay).then(() => [{ ticketno: '1' }]), 'ticketno')
  tickets.merge({ ticketno: '1', updated: true }, false)
  await wait(delay)
  console.log(label, tickets)

  label = `### 두 개의 데이터를 merge 하는 방법`
  const tickets2 = await IndexedArray.createAsync(this.wait().then(() => [{ ticketno: '1' }]), 'ticketno')
  const tickets3 = [{ ticketno: '1', updated: true }]
  tickets2.merge(tickets3, false)
  console.log(label, tickets2)
})()
