import { AppOptions } from '@/class/appOptions'
// import { TempStore } from '@/class/tempStore'
import { ModalManager } from '@/class/modalManager'
import http from '@/min/http.min'
import { exportServicelist, getLastSql, getServicelist, getApilist, resendService } from '@/min/route'
import router from '@/router'
import $store from '@/store'
import { assert, assertEquals, clone, deepClone, deepCloneFilter, toJson, wait } from '@/utils'
import clip from '@/utils/clipboard'
import hotkeys from 'hotkeys-js'
import _ from 'lodash'
import Vue from 'vue'
;(function (global) {
  console.log(1)
  const helper = {
    Vue,
    $store: $store,
    router: router,
    state: $store.state,
    getters: $store.getters,
    actions: $store._actions,
    mutations: $store._mutations,
    options: AppOptions.instance,
    saveOptions: AppOptions.instance.update.bind(AppOptions.instance),
    baseUrl: (index) => {
      try {
        const url = AppOptions.instance.setBaseURLIndex(index)
        if (url) {
          http.defaults.baseURL = url
        }
      } catch (error) {
        console.error(error)
      }

      console.debug(http.defaults.baseURL)
    },
    s: {
      list: getServicelist,
      save: exportServicelist,
      sql: getLastSql,
      re: resendService,
      clear: () => {
        $store.dispatch('serviceLog/clearServiceLog')
      },
      api: getApilist,
      // mock: () => TempStore.instance.mockGetter.get,
      get l() {
        return getServicelist.call(this)
      },
      get a() {
        return getApilist.call(this)
      },
      get e() {
        return getServicelist.call(this, `"result":false`)
      },
      get s() {
        return exportServicelist.call(this)
      },
      get se() {
        return exportServicelist.call(this, `"result":false`)
      },
      get q() {
        return getLastSql.call(this)
      },
      get r() {
        return resendService.call(this)
      },
      get c() {
        return $store.dispatch('serviceLog/clearServiceLog')
      },
      // get m() {
      //   return TempStore.instance.mockGetter.get()
      // },
    },
    get fn() {
      return this
    },
    e: {
      get l() {
        return global.$0 && global.$0.closest('[__vue__*=".vue"')
      },
      get v() {
        let n = global.e.l
        let index = 0
        while (n && n instanceof Element) {
          try {
            const vm = n.__vue__
            if (vm) {
              // const v = n.getAttribute('__vue__')
              // const src = (v && v.match(/src\/.*vue/ig) || [])[0]
              const src = vm != null ? vm.src : undefined
              if (src) {
                // eslint-disable-next-line no-eval
                eval(`window.v${index} = vm`)
                console.log(`v${index}=`, vm.$el, vm, `webpack:///${src}`)
                index++ === 0 && clip(src)
              }
            }
          } finally {
            n = n.parentNode
          }
        }
        return global.v0
      },
    },
    toJson: toJson,
    cloneIf: deepCloneFilter,
    tj(obj, filter) {
      // eslint-disable-next-line no-undef
      return toJson(obj, filter)
    },
    tj0(obj, filter) {
      // eslint-disable-next-line no-undef
      return toJson(obj, filter, 0)
    },
    clone: clone.bind(this),
    _,
    qs(selector) {
      return document.querySelector(selector)
    },
    qsa(selector) {
      return document.querySelectorAll(selector)
    },
    testResponsiveView() {
      const { v } = global
      helper.saveViewData(20 * 1000)
      const routeData = v.$router.resolve({ name: 'Responsive', query: { path: v.$route.path } })
      // eslint-disable-next-line no-unused-vars
      const newWindow = window.open(routeData.href, '_blank')
    },
    saveViewData(expire) {
      const { v } = global
      const array = ModalManager.instance.findModals(v)
      array.forEach((dialog) => {
        dialog.saveViewData()
      })
      v.saveViewData()
      helper.$store.dispatch('app/saveViewDataInStorage', expire)
    },
    reload() {
      helper.saveViewData()
      location.reload()
    },
    toggleOutline() {
      global.v.toggleOutline()
    },
    toggleShowWindowSize() {
      helper.$store.dispatch('app/toggleShowWindowSize')
    },
    assert: assert,
    assertEquals: assertEquals,
    async autoTest() {
      const { ws } = global
      const label = `### websocket 테스트`

      const fn = (event) => {
        ws.removeEventListener('UNKNOWN', fn)
        console.timeEnd(label)
        assert(event.data.message === `### websocket 테스트`)
      }
      console.time(label)
      ws.addEventListener('UNKNOWN', fn)
      ws.sendMessage('UNKNOWN', label)

      // console.log('\n')
      // label = `### 화면별 테스트`
      // const views = global.v.printRoutes()
      // for (let index = 0; index < views.length; index++) {
      //   const view = views[index]
      //   // if (view.name !== 'Members') {
      //   //   continue
      //   // }

      //   global.v.redirect({ name: view.name })
      //   await wait(1000)
      //   const label = `${view.name} autoTest`
      //   const { v } = global
      //   console.time(label)
      //   await v.autoTest()
      //   console.timeEnd(label)
      //   await wait(1000)
      // }
    },
    copySrc() {
      const src = global.v.src
      if (src) {
        console.log(src)
        const path = (/src\/.*vue/.exec(src) || [''])[0]
        clip(path)
      }
    },
    device() {
      const device = global.v.browserInfo
      alert(helper.tj(device))
      console.log(device)
    },
    deleteHotkey() {
      hotkeys.deleteScope('debug')
    },
    help() {
      if (['production', 'staging'].includes(process.env.NODE_ENV) === false) {
        console.table({
          'alt+p': '접속된 서버 프로젝트로 변경',
          'qs(selector) / qsa(selector)': 'document.querySelectorAll(...)',
          's.list(filter) / s.l / f2': '전체 서비스 로그 보기(필터가능)',
          's.save(filter) / s.s': '전체 서비스 로그 다운로드(필터가능)',
          's.sql(filter) / f4': '전체 서비스 최근 3개 sql(필터가능)',
          's.clear() / f9': '전체 서비스 로그 삭제',
          's.re(index) / s.r': 'vc.list() 를 기준으로 인덱스에 해당하는 서비스 다시 요청',
          'e.l / e.v': '선택한 dom element의 VUE를 반환',
          'v.src / f6': '현재 화면 view 소스 링크 보기',
          'v.$data / vd, v.d / f7': '현재 화면 DATA 보기',
          'v.query / f8': '현재 화면 조회 조건 보기',
          'c, c.src, cd, cdq': '현재 화면 컴포넌트 및 데이터 v대신 c를 사용함, debugComponentName 설정해야 함 ',
          ref: '컨포넌트 refs에 접근 ',
          'vs.list(filter) / vs.l / f3': '현재 화면 서비스 로그 보기(필터가능)',
          'vs.save(filter) / vs.s / f10': '현재 화면 서비스 로그 다운로드(필터가능)',
          'vs.sql(filter) / f4': '현재 화면 최근 3개 sql(필터가능)',
          ws: 'WebSocket Instance',
          appOptions: 'appOptions Instance',
          modalManager: 'modalManager Instance',
          'baseUrl(1) / alt+2': 'baseUrl 설정 변경',
          'saveOptions({project:"demo"}) / appOptions.project="oasis"': '옵션변경 (dark,project,baseURL)',
          'toJson(v.$router.options.routes2, "name,path") / tj()': 'json formatter 로 로깅, 필터는 생략 가능',
          'cloneIf({a:1,b:1,c1:1}, /[^(c1|b)]/)': '객체 복사, key를 filter 할 때 사용 ',
          'clone(v.viewRect)': '객체 복사, Observer 를 제거 할 때 유용하다',
          'toggleShowWindowSize() / alt+3': '화면크기를 우측하단에 표시한다',
          'toggleOutline() / alt+4': '레이아웃 테스트를 위한 outline을 보이게 한다.',
          'testResponsiveView() / alt+5': '스크린 사이즈에 따른 반응형 웹 테스트',
          'reload() / alt+r': '화면조회 조건이나 다이얼로그를 유지한 새로고침',
          'copySrc() / alt+c': '화면 소스 파일 경로를 클립보드에 복사',
          'mobile ⇄ device 전환 / alt+m, shift+alt+m': '모바일 모드 / 디바이스 모드 전환, shift 사용 시 rerouting',
          'alt+h': 'Header 감추기/보이기',
          'alt+b': 'Bottombar 감추기/보이기',
          'appOptions.project="untact" / alt+1': 'project 변경',
          'device()': '디바이스 확인',
          'autoTest()': '자동 테스트',
          'alt+,': '설정 보이기/감추기',
          'alt+.': '태그뷰 보이기/감추기',
          'help() or fn': '기능 리스트 출력',
        })
      }
    },
  }

  function registHotkey() {
    hotkeys.setScope('debug')
    if (process.env.NODE_ENV === 'staging') {
      hotkeys.deleteScope('debug')
    }

    hotkeys(`alt+,`, 'debug', function (event, handler) {
      event.preventDefault()
      helper.$store.dispatch('settings/changeSetting', {
        key: 'showSettings',
        value: !helper.$store.state.settings.showSettings,
      })
    })

    hotkeys(`alt+.`, 'debug', function (event, handler) {
      event.preventDefault()
      helper.$store.dispatch('settings/changeSetting', {
        key: 'tagsView',
        value: !helper.$store.state.settings.tagsView,
      })
    })

    hotkeys(`f1`, 'debug', function (event, handler) {
      event.preventDefault()
      global.help()
    })

    hotkeys(`f2`, 'debug', function (event, handler) {
      event.preventDefault()
      console.debug('\n\n')
      console.debug(...global.s.l)
      console.debug('#전체 서비스 로그 s.list(filter) / s.l ')
    })

    hotkeys(`f3`, 'debug', function (event, handler) {
      event.preventDefault()
      console.debug('\n\n')
      console.debug(...global.vs.l)
      console.debug('현재 화면 서비스 로그 보기 vs.list(filter) ')
    })

    hotkeys(`f4`, 'debug', function (event, handler) {
      event.preventDefault()
      console.debug('\n\n')
      global.vs.sql()
      console.debug('현재 화면 최근 3개 sql vs.sql(filter)')
    })

    hotkeys(`f6`, 'debug', function (event, handler) {
      event.preventDefault()
      console.debug('\n\n')
      console.debug(global.v.src)
      console.debug('현재 화면 view 소스 링크 보기 v.src')
    })

    hotkeys(`f7`, 'debug', function (event, handler) {
      event.preventDefault()
      console.debug('\n\n')
      console.debug(deepClone(global.v.$data))
      console.debug('현재 화면 DATA 보기 v.$data / vd')
    })

    hotkeys(`f8`, 'debug', function (event, handler) {
      event.preventDefault()
      console.debug('\n\n')
      console.debug(deepClone(global.v.query))
      console.debug('현재 화면 조회 조건 보기 v.query ')
    })

    hotkeys(`f9`, 'debug', function (event, handler) {
      event.preventDefault()
      global.s.clear()
      console.debug('전체 서비스 로그 삭제 s.clear()')
    })

    hotkeys(`f10`, 'debug', function (event, handler) {
      event.preventDefault()
      global.vs.save()
      console.debug('#현재 화면 서비스 로그 다운로드 vs.save(filter) / vs.s')
    })

    hotkeys(`alt+s`, 'debug', function (event, handler) {
      event.preventDefault()
      debugger
      // alt+s 만 작동이 안 되네..
    })

    hotkeys(`alt+c`, 'debug', function (event, handler) {
      event.preventDefault()
      helper.copySrc()
    })

    hotkeys(`alt+r`, 'debug', function (event, handler) {
      event.preventDefault()
      helper.reload()
    })

    hotkeys(`alt+m`, 'debug', function (event, handler) {
      event.preventDefault()
      AppOptions.instance.mobile = !AppOptions.instance.mobile
      const message = `mobile: ${AppOptions.instance.mobile}`
      console.debug(message)
      global.v.message(message)
      global.v.refreshAllView()
    })

    hotkeys(`shift+alt+m`, 'debug', async function (event, handler) {
      event.preventDefault()
      AppOptions.instance.mobile = !AppOptions.instance.mobile
      const message = `mobile: ${AppOptions.instance.mobile}`
      console.debug(message)
      global.v.message(message)
      await wait(1000)
      location.reload()
    })

    hotkeys(`alt+3`, 'debug', function (event, handler) {
      event.preventDefault()
      global.toggleShowWindowSize()
    })

    hotkeys(`alt+4`, 'debug', function (event, handler) {
      event.preventDefault()
      global.toggleOutline()
    })

    hotkeys(`alt+5`, 'debug', function (event, handler) {
      event.preventDefault()
      global.testResponsiveView()
    })

    // for (let index = 0; index < 3; index++) {
    //   hotkeys(`alt+${index}`, 'debug', function(event, handler) {
    //     event.preventDefault()
    //     global.baseUrl(index)
    //   })
    // }

    hotkeys(`alt+1`, 'debug', async function (event, handler) {
      event.preventDefault()

      global.changeProject = (index) => {
        try {
          if (index !== current && index < projectList.length && index >= 0) {
            global.saveOptions({ project: `${projectList[index]}` })
          }
        } catch (error) {
          // 예외 처리
        }
      }

      const { projectList, project } = AppOptions.instance
      const current = projectList.indexOf(project) || 0
      const html = projectList.reduce((acc, cur, i) => {
        if (acc) acc += '<br>'
        acc += `${i}: <a href="javascript:changeProject(${i})" ${i === current ? 'style="color:red"' : ''}>${cur}</a>`
        return acc
      }, '')

      const result = await global.v.prompt(html, 'Select Project', {
        confirmButtonText: 'OK',
        dangerouslyUseHTMLString: true, // message 에 HTML 사용 여부
      })

      if (result.action === 'confirm' && result.value) {
        const index = Number(result.value)
        global.changeProject(index)
      }
    })

    hotkeys(`alt+2`, 'debug', async function (event, handler) {
      event.preventDefault()

      const { baseURLs, baseURLIndex } = AppOptions.instance
      const html = baseURLs.reduce((acc, cur, i) => {
        if (acc) acc += '<br>'
        acc += `${i}: <a href="javascript:baseUrl(${i})" ${i === baseURLIndex ? 'style="color:red"' : ''}">${cur}</a>`
        return acc
      }, '')

      const result = await global.v.prompt(html, 'Select baseURL', {
        confirmButtonText: 'OK',
        dangerouslyUseHTMLString: true, // message 에 HTML 사용 여부
      })

      if (result.action === 'confirm' && result.value) {
        try {
          const index = Number(result.value)
          if (index !== baseURLIndex && index < baseURLs.length && index >= 0) {
            global.baseUrl(index)
          }
        } catch (error) {
          // 예외 처리
        }
      }
    })

    hotkeys(`alt+9`, 'debug', function (event, handler) {
      event.preventDefault()
      console.debug({ ...AppOptions.instance.baseURLs })
    })
  }

  if (AppOptions.instance.debug) {
    Object.assign(global, helper)
    Object.defineProperty(global, 'gv', {
      get: function () {
        return global.e.v
      },
      configurable: true,
    })
    Object.defineProperty(global, 'gl', {
      get: function () {
        return global.e.l
      },
      configurable: true,
    })
    global.helper = helper
    registHotkey()

    setTimeout(() => {
      global.help()
    }, 800)
  }

  global.Vue = global.Vue || Vue
})(typeof exports !== 'undefined' ? exports : window)
