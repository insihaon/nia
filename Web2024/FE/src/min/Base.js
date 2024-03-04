import { AppOptions } from '@/class/appOptions'
import { Device } from '@/class/device'
import { ModalManager } from '@/class/modalManager'
import elAdaptiveTable from '@/directive/el-table' // base on element-ui
import variables from '@/styles/variables.scss'
import { assert, assertEquals, deepCloneFilter, getViewportArr, parseTime, simplize, toJson, wait, humanNumber } from '@/utils'
import _ from 'lodash'
import moment from 'moment'
import Vue from 'vue'
import { mapState } from 'vuex'
import XLSX from 'xlsx'
import xxtea from 'xxtea'
import CONSTANTS from './constants'

export const _var = { CONSTANTS, xxtea }

const Base = {
  filters: {
    ellipsisFilter(input, limit = 10) {
      return input.length > limit ? `${input.substring(0, limit)}...` : input
    },
  },
  directives: { elAdaptiveTable },
  data() {
    return {
      THIS: this,
      routeParams: null,
      activeQueryPannel: '1',
      scrollX: 0,
      scrollY: 0,
      viewRect: null,
      domElement: { labelWidth: '80px' },
      query: {},
      result: {},
      rules: {},
      CONSTANTS,
      loadingTarget: {},
      routeFrom: {},
      loading: {
        search: false,
        create: false,
      },
      limitedDev: false,
    }
  },
  computed: {
    innerHeight: () => {
      return window.innerHeight - this.$el.getBoundingClientRect().top
    },
    device: () => (AppOptions.instance.mobile ? 'desktop' : 'mobile'),
    ...mapState({
      screenDevice: (state) => state.app.screenDevice,
      viewport: (state) => state.app.viewport,
      windowSize: (state) => state.app.windowSize,
      openedViewData: (state) => state.app.openedViewData,
      visitedViews: (state) => state.tagsView.visitedViews,
      errorLog: (state) => state.errorLog.logs,
      serviceLog: (state) => state.serviceLog.logs,
      serverSetting: (state) => state.app.server,
      oasis: (state) => state.oasis,
      untact: (state) => state.untact,
      bcn: (state) => state.bcn,
      devt: (state) => state.devt,
      dataHub: (state) => state.dataHub,
      aam: (state) => state.aam,
      aamPersisted: (state) => state.aamPersisted,
      influenceCircitStore: (state) => state.influenceCircitStore,
      t3d: (state) => state.t3d,
      t3dPersisted: (state) => state.t3dPersisted,
      pmmlte: (state) => state.pmmlte
    }),
    webSocketManager() {
      return null
    },
    browserInfo() {
      return Device.instance.data
    },
    appOptions() {
      return AppOptions.instance
    },
    mobile() {
      return AppOptions.instance.mobile
    },
    desktop() {
      return !AppOptions.instance.mobile
    },
    uploadUrl() {
      return `${AppOptions.instance.baseURL}/uploadFile`
      // return `${AppOptions.instance.baseURL.replace(/\/v[0-9]+(\/){0,1}$/, '')}/uploadFile`
    },
    debug() {
      return AppOptions.instance.debug === true
    },
    dark() {
      return AppOptions.instance.dark === true
    },
    variables() {
      return variables
    },
    d() {
      return { ...this.$data }
    },
    typeOf() {
      return 'Base'
    },
    typeOfBase() {
      return true
    },
    routeTo() {
      let parent = this
      while (parent && parent !== this._routerRoot && parent.typeOf) {
        if (parent.name === this.$route.name) {
          break
        }
        parent = parent.$parent
      }
      return parent
    },
  },
  watch: {
    viewport(n, o) {
      if (n === 0) return
    },
    $route(to, from) {
      if (this.$route === to) {
        this.routeFrom = from
      }
    },
    screenDevice(n, o) {
      if (n === 0) return
      this.activeQueryPannel = this.screenDevice === 'mobile' ? '' : '1'
    },
  },
  created() {
    this.routeParams = this.$route.params
    window.addEventListener('scroll', this.handleScroll)
    this.$nextTick(() => {
      this.onInit()
      this.activeView('created')
      this.loadViewData()
    })
  },
  activated() {
    // this.trace(`Base activated ${this.name}`)
    this.activeView('activated')
  },
  deactivated() {
    this.onDeactiveView()
    // this.trace(`Base deactivated ${this.name}`)
  },
  beforeDestroy() {
    this.removeAllWsEventListener()
    window.removeEventListener('resize', this.onResize)
    window.removeEventListener('scroll', this.handleScroll)
    this.onDeactiveView()
  },
  beforeRouteEnter(to, from, next) {
    next()
  },
  beforeRouteLeave(to, from, next) {
    this.$store.dispatch('app/removeViewData', this.name)
    next()
  },
  mounted() {
    this.$nextTick(() => {
      window.addEventListener('resize', this.onResize)
      this.onResize()
    })
  },
  methods: {
    onInit(name = null) {
      var value = null
      switch (name) {
        case null:
          break
        default:
          break
      }
      return value
    },
    onResize() {
      const width = window.innerWidth
      const height = window.innerHeight
      if (this.viewRect && this.windowSize.width === width && this.windowSize.height === height) {
        return
      }

      this.$store.dispatch('app/setWindowSize', { width, height })
      const { clientWidth, clientHeight, clientLeft, clientTop } = this.$el
      this.viewRect = {
        left: clientLeft,
        top: clientTop,
        width: clientWidth,
        height: clientHeight,
      }
      this.onWindowResize(width, height)
    },
    onWindowResize(width, height) {
    },
    isViewport(operator, compare) {
      return getViewportArr(operator, compare).includes(this.viewport)
    },
    isViewwidth(operator, pixel) {
      if (['>', '<', '>=', '<=', '=='].includes(operator) === false) {
        throw new Error(CONSTANTS.message.isInvalidOperator)
      }

      const { _isNumber, windowSize } = this
      if (!_isNumber(pixel)) {
        throw new Error(CONSTANTS.message.isNotNumber)
      }

      const cmd = `${windowSize.width} ${operator} ${pixel}`
      // eslint-disable-next-line no-eval
      return eval(cmd)
    },
    async closeView(view) {
      view = view || this.$route
      await this.$store.dispatch('tagsView/delView', view)
      this.$router.back()
    },
    redirect({ path, name } = { name: 'ROOT' }, params = {}, query = {}) {
      if (path) {
        this.$router.push({
          path: '/redirect' + path,
          query: query,
        })
      } else {
        this.$router.push({
          name: name,
          query: query,
          params: params,
          props: true,
        })
      }
    },
    openTab(name = 'ROOT', params = {}, query = {}, newTab = true) {
      const routeData = this.$router.resolve({
        name: name,
        query: query,
        props: true,
      })
      const newWindow = window.open(routeData.href, newTab ? '_blank' : '_self')
      newWindow.routeParams = params || {
        name: 'dog',
        age: 4,
      }

      if (!newTab) {
        window.location.reload()
      }
    },
    printRoutes() {
      const array = []
      const printChildren = (parentPath = '', i) => {
        if (i.name) {
          const path = `${parentPath}/${i.path}`.replace(/\/\//gi, '/')
          if (!i.redirect) {
            array.push({ name: i.name, path: path, redirect: i.redirect })
          }
        }
        if (!i.children) return

        i.children.forEach((child) => {
          printChildren(i.path, child)
        })
      }

      const routers = []
        .concat(this.$router.options.routes)
        .concat(this.$router.options.routes2)
        .filter((i) => i.hidden !== true && (i.name || i.children))

      routers.forEach((i) => printChildren('', i))
      return simplize(array)
    },
    defaultPeriod(prevDay = -7, nextDay = 0) {
      return [Date.today(0).addDays(prevDay), Date.today().addDays(nextDay)]
    },
    getDayPeriod(offset = 0) {
      var day = new Date().addDays(parseInt(offset))
      return [day.dayStart(), day.dayEnd()]
    },
    getWeekPeriod(offset = 0) {
      const day1 = new Date(moment().day('Sunday')).addDays(7 * parseInt(offset))
      const day2 = new Date(moment().day('Saturday')).addDays(7 * parseInt(offset))
      return [day1.dayStart(), day2.dayEnd()]
    },
    getMonthPeriod(offset = 0) {
      var month = new Date().addMonth(parseInt(offset))
      return [month.monthStart(), month.monthEnd()]
    },
    handleScroll() {
      _.debounce(
        function () {
          this.scrollY = window.scrollY
          this.scrollX = window.scrollX
        }.bind(this),
        200
      )()
    },
    registerRefs(reset = false) {
      if (reset) window.ref = {}
      Object.assign(window.ref, { ...this.$refs })
      const refs = this.$refs
      for (const key in refs) {
        // eslint-disable-next-line no-prototype-builtins
        if (refs.hasOwnProperty(key)) {
          const child = refs[key]
          if (child && child.registerRefs) {
            child.registerRefs(false)
          }
        }
      }
    },
    addWsEventListener(channelName, eventHandler) {
    },
    removeWsEventListener(channelName, eventHandler) {
    },
    removeAllWsEventListener() {
    },
    async autoTest() {
      const { wait } = this
      await wait(1000)
    },
    toStringTime(time = new Date(), fmt = 'YYYY-MM-DD HH:mm:ss.SSS') {
      return moment(time).format(fmt)
    },
    toStringTime2(stringTime, srcFormat = 'YYYY-MM-DD HH:mm:ss', destFormat = 'YYYY/MM/DD HH:mm:ss') {
      return moment(stringTime, srcFormat).format(destFormat)
    },
    toDateTime(stringTime, fmt = 'YYYY-MM-DD HH:mm:ss') {
      return moment(stringTime, fmt).toDate()
    },
    moment,
    _isEqual: _.isEqual,
    _debounce: _.debounce,
    _throttle: _.throttle,
    _orderBy: _.orderBy,
    _cloneDeep: _.cloneDeep,
    _merge: _.merge,
    _isNumber: _.isNumber,
    assert: assert,
    assertEquals: assertEquals,
    onActiveView() { /* for Override */ },
    onDeactiveView() { /* for Override */ },
    activeView(when) {
      const self = this
      const { name, typeOf, _inactive } = self
      if (typeOf !== 'Base' || !name || _inactive === true) return
      if (typeOf === 'Base' && this.$route.name !== this.name) return
      if (ModalManager.instance.last()) return
      if (this.limitedDev) {
        this.onActiveView()
        return
      }

      this.debug && this.trace(`Base activeView: name=${this.name}, when=${when}, _inactive=${_inactive}`)

      if (window.routeParams) {
        Object.assign(this.routeParams, window.routeParams)
        delete window.routeParams
      }

      let v = (window.v = this)
      window.vd = { ...v.$data }
      window.vdq = { ...v.query }

      v = window.r = this.routeTo
      window.rd = { ...v.$data }
      window.rdq = { ...v.query }

      v = window.p = this.$parent && this.$parent.typeOf ? this.$parent : this
      window.pd = { ...v.$data }
      window.pdq = { ...v.query }

      this.registerRefs(true)
      this.onActiveView()
    },
    bringToFront(options) {
      ModalManager.instance.last() && ModalManager.instance.last().bringToFront(options)
      return options
    },
    prompt(message, title, options) {
      options = this.bringToFront(options)
      return this.$prompt(message, title, options)
    },
    alert(message, title, options) {
      options = this.bringToFront(options)
      return this.$alert(message, title, options)
    },
    confirm(message, title, options) {
      options = this.bringToFront(options)
      return this.$confirm(message, title, options)
    },
    msgbox(options, callback) {
      options = this.bringToFront(options)
      return this.$msgbox(options, callback)
    },
    errorMessage(options) {
      console.error(options)
      this.$message.error(options)
    },
    message(options) {
      console.log(options)
      this.$message.success(options)
    },
    notify(options) {
      console.log(options)
      if (typeof options === 'string') {
        options = {
          message: options,
        }
      }
      this.$notify(options)
    },
    getLoadingTarget(vue = 'body', selector) {
      if (vue instanceof Vue) {
        vue = selector ? vue.querySelector(selector) : vue.$el
      } else if (selector) {
        vue = document.querySelectorAll(`${vue} ${selector}`)
      }
      return { target: vue, id: `${vue && vue.__vue__ && vue.__vue__._uid}` }
    },
    openLoading(parent, myOptions = {}) {
      const { vue, selector } = parent || { vue: 'body' }
      const { target, id } = this.getLoadingTarget(vue, selector)
      if (this.loadingTarget[id]) {
        return
      }

      const isBody = target.tagName === 'BODY'
      const options = !isBody
        ? { target: target }
        : {
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)',
        }
      Object.assign(options, myOptions)

      const loading = this.$loading(options)

      this.loadingTarget[id] = loading
      setTimeout(() => {
        this.closeLoading(loading)
      }, 10 * 1000)
    },
    closeLoading(parent) {
      const { vue, selector } = parent || { vue: 'body' }
      const { id } = this.getLoadingTarget(vue, selector)
      const loading = this.loadingTarget[id] /* || this.$loading() */
      if (loading) {
        loading.close()
        delete this.loadingTarget[id]
      }
    },
    closeLoadingAll() {
      Object.values(this.loadingTarget).forEach((v) => {
        v.close()
      })
      this.loadingTarget.splice(0)
    },
    cloneFilter: deepCloneFilter,
    toJson: toJson,
    simplize: simplize,
    wait: wait,
    parseTime: parseTime,
    extend: Vue.util.extend,
    toggleOutline() {
      this.$store.dispatch('app/toggleOutline')
    },
    loadViewData() {
      if (!this.openedViewData) return
      const data = this.openedViewData[this.name]
      if (data) {
        Object.assign(this.$data, data)
      }
    },
    saveViewData() {
      this.$store.dispatch('app/putViewData', this.getViewData())
    },
    getViewData() {
      const { query, result } = this
      return { [this.name]: this._cloneDeep({ query, result }) }
    },
    async saveTimeCapsule() {
      const { action, value: description } = await this.prompt(CONSTANTS.message.pleaseInputName, 'SAVE', {
        confirmButtonText: CONSTANTS.message.save,
        cancelButtonText: CONSTANTS.message.cancel,
        inputValue: `${CONSTANTS.message.error}_${this.toStringTime()}`,
      })
      if (action !== 'confirm') return

      const viewData = {}
      const array = ModalManager.instance.findModals(this)
      array.forEach((dialog) => {
        _.merge(viewData, dialog.getViewData())
      })
      _.merge(viewData, this.getViewData())

      const capsule = {
        timestamp: new Date(),
        description,
        user: 'dimmby',
        name: this.$route.name,
        path: this.$route.path,
        data: this._cloneDeep({
          viewData,
          errorLog: this.errorLog.map((l) => {
            const { err, info, url } = l
            return {
              err: { message: err.message, stack: err.stack },
              info,
              url,
            }
          }),
          serviceLog: this.serviceLog,
        }),
      }
    },
    formatterUppercase(row, col, value, index) {
      return (value || '').toUpperCase()
    },
    formatterLowercase(row, col, value, index) {
      return (value || '').toLowerCase()
    },
    formatterNumber(row, col, value, index) {
      // 21,705,596 형식으로 렌더
      return Math.floor(value)
        .toString()
        .replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,')
    },
    formatterDateTime(row, col, value, index) {
      return this.parseTime(value, '{y}-{m}-{d} {h}:{i}')
    },
    formatterTimeStamp(time, formatStr = '') {
      if (time === null || formatStr.length === 0) {
        console.error('formatterDateTime ERROR')
        return
      }
      return moment(time).format(formatStr)
    },
    formatterLink(row, col, value, index) {
    },
    trace: !AppOptions.instance ? console.trace.bind(null, `called`) : () => { /* 빈 블록 사용 금지 */ },
    log: AppOptions.instance.debug === true ? console.log.bind(null) : () => { /* 빈 블록 사용 금지 */ },
    info: AppOptions.instance.debug === true ? console.info.bind(null) : () => { /* 빈 블록 사용 금지 */ },
    error: AppOptions.instance.debug === true ? console.error.bind(null) : () => { /* 빈 블록 사용 금지 */ },
    refreshAllView() {
      // In order to make the cached page re-rendered
      this.$store.dispatch('tagsView/delAllCachedViews', this.$route)

      const { fullPath } = this.$route

      this.$nextTick(() => {
        this.$router.replace({
          path: '/redirect' + fullPath,
        })
      })
    },
    exportExcel(data, prefix = this.name) {
      const dataWorkSheet = XLSX.utils.json_to_sheet(data)
      const workbook = XLSX.utils.book_new()
      XLSX.utils.book_append_sheet(workbook, dataWorkSheet, prefix)
      const formatted_date = new Date().toJSON().slice(0, 10).replace(/-/g, '/')
      XLSX.writeFile(workbook, `${prefix}_${formatted_date}.xlsx`)
    },
    importExcel(event) {
      return new Promise((resolve, reject) => {
        const file = event.target.files[0]
        const reader = new FileReader()
        reader.onload = (e) => {
          try {
            const data = reader.result
            const workbook = XLSX.read(data, { type: 'binary', codepage: 949 })
            const jsonData = XLSX.utils.sheet_to_json(workbook.Sheets[workbook.SheetNames[0]])
            resolve(jsonData)
          } catch (error) {
            console.log(error)
            reject()
          }
        }
        reader.readAsBinaryString(file)
      })
    },
    showObject(title, obj) {
      this.$message({
        showClose: true,
        dangerouslyUseHTMLString: true,
        message: `<strong>${title}</string><br><br>
          <div class="print-json">
            <pre>${JSON.stringify(obj, null, 4)}<pre>
          </div>
          `,
      })
    },
    addScript(scripts, async = true) {
      if (!scripts) return
      for (let i = 0, len = scripts.length; i < len; i++) {
        const script = scripts[i]
        if (document.querySelector(`script[src*="${script.replace('./', '')}"]`)) {
          continue
        }
        const scriptTag = document.createElement('script')
        scriptTag.setAttribute('type', 'text/javascript')
        scriptTag.setAttribute('src', script + (this.debug ? `?t=${new Date().getTime()}` : ''))
        scriptTag.async = async
        document.getElementsByTagName('body')[0].appendChild(scriptTag)
      }
    },
    addLink(links) {
      if (!links) return
      for (let i = 0, len = links.length; i < len; i++) {
        const link = links[i]
        if (document.querySelector(`link[href*="${link.replace('./', '')}"]`)) {
          continue
        }
        const linkTag = document.createElement('link')
        linkTag.setAttribute('rel', 'stylesheet')
        linkTag.setAttribute('href', link + (this.debug ? `?t=${new Date().getTime()}` : ''))
        linkTag.async = true
        document.getElementsByTagName('head')[0].appendChild(linkTag)
      }
    },
    objectToArray(obj) {
      return Object.entries(obj).map((i) => i[1])
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    eval(p) {
      // eslint-disable-next-line no-eval
      return eval('this.' + p)
    },
    eval2(p) {
      // eslint-disable-next-line no-eval
      return eval(p)
    },
    humanNumber(number) {
      return humanNumber(number)
    }
  },
}

export { Base }
