import router from '@/router/index.js'

const duplicateCheckFileObject = {}

export default {
    testComponentList: [],
    componentTreeData: [],

    // IndexedDB 데이터베이스 열기
    async openDatabase() {
      return new Promise((resolve, reject) => {
        const request = window.indexedDB.open('ComponentTestDB', 1)

        request.onerror = event => {
          console.error('Failed to open database')
          reject(event.target.error)
        }

        request.onsuccess = event => {
          console.log('Database opened successfully')
          resolve(event.target.result)
        }

        // 업그레이드 되거나 최초 호출시에
        request.onupgradeneeded = event => {
          const db = event.target.result
          if (!db.objectStoreNames.contains('testComponentList')) {
            db.createObjectStore('testComponentList', { keyPath: 'index', autoIncrement: true })
          }

          if (!db.objectStoreNames.contains('componentTreeData')) {
            db.createObjectStore('componentTreeData', { keyPath: 'index', autoIncrement: true })
          }
        }
      })
    },

    async getTestComponentList() {
      const db = await this.openDatabase()
      return new Promise((resolve, reject) => {
        const transaction = db.transaction(['testComponentList'], 'readonly')
        const objectStore = transaction.objectStore('testComponentList')
        const request = objectStore.getAll()

        request.onerror = event => {
          console.error('Failed to fetch testComponentList')
          reject(event.target.error)
        }

        request.onsuccess = event => {
          console.log('testComponentList fetched successfully')
          resolve(event.target.result)
        }
      })
    },

    async getComponentTreeData() {
        const db = await this.openDatabase()
        return new Promise((resolve, reject) => {
          const transaction = db.transaction(['componentTreeData'], 'readonly')
          const objectStore = transaction.objectStore('componentTreeData')
          const request = objectStore.getAll()

          request.onerror = event => {
            console.error('Failed to fetch componentTreeData')
            reject(event.target.error)
          }

          request.onsuccess = event => {
            console.log('componentTreeData fetched successfully')
            resolve(event.target.result)
          }
        })
      },

    async initTestComponentList() {
        this.testComponentList = await this.getTestComponentList()
        if (this.testComponentList.length === 0) {
            const testRoutes = [...router.options.routes, ...router.options.routes2]
            for (const route of testRoutes) {
                await this.recursiveSetComponent(route.component)
                if (route.children) {
                    for (const child of route.children) {
                        const childComponent = await child.component()
                        await this.recursiveSetComponent(childComponent.default)
                    }
                }
            }
            this.testComponentList = await this.getTestComponentList()

            this.componentListConvertTreeData()
        }
    },

    async componentListConvertTreeData() {
        for (const testComponent of this.testComponentList) {
            await this.addComponentTreeData({
                componentPath: testComponent.__file,
                componentAlias: testComponent.__file.split('/').pop(),
                component: testComponent
            })
        }
    },

    async recursiveSetComponent(component) {
        if (!Object.hasOwnProperty.call(duplicateCheckFileObject, component.__file)) {
            duplicateCheckFileObject[component.__file] = component
            if (component.mixins) {
              const testComponent = component.mixins.find(mixin => mixin.__file === 'src/test/ComponentTesterMixins.vue')
              testComponent && await this.addTestComponentList(component)
            }

            if (component.components) {
                const componentKeyMap = Object.keys(component.components)
                for (const childComponent of componentKeyMap) {
                    await this.recursiveSetComponent(component.components[childComponent])
                }
            }
        }
    },

    async addComponentTreeData(treeData) {
        try {
            const db = await this.openDatabase()
            return new Promise((resolve, reject) => {
                const transaction = db.transaction(['componentTreeData'], 'readwrite')
                const objectStore = transaction.objectStore('componentTreeData')
                const request = objectStore.add({ value: treeData })

                request.onerror = event => {
                console.error('Failed to add treeData')
                reject(event.target.error)
                }

                request.onsuccess = event => {
                console.log('treeData added successfully')
                resolve()
                }
            })
        } catch (e) {
            console.error(e)
        }
    },

    async addTestComponentList(component) {
        try {
            const db = await this.openDatabase()
            return new Promise((resolve, reject) => {
                const transaction = db.transaction(['testComponentList'], 'readwrite')
                const objectStore = transaction.objectStore('testComponentList')
                const request = objectStore.add({ value: component })

                request.onerror = event => {
                    console.error('Failed to add todo')
                    reject(event.target.error)
                }

                request.onsuccess = event => {
                    console.log('Todo added successfully')
                    resolve()
                }
            })
        } catch (e) {
            console.error(e)
        }
    }
  }

