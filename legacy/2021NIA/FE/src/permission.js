import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { AppOptions } from '@/class/appOptions'
import router from './router'
import store from './store'
import { findIntersection } from '@/utils'

const { debug, urlParam, project } = AppOptions.instance

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect', '/404', '/401', '/responsive'] // no redirect whitelist
const anonymousMode = JSON.parse(process.env.VUE_APP_ANONYMOUS_MODE || 'true')
const isOnlyFront = JSON.parse(process.env.VUE_APP_ONLY_FE || 'false')
const useErrorPage = JSON.parse(process.env.VUE_APP_ERROR_PAGE || 'true')
const onlyIframe = JSON.parse(process.env.VUE_APP_ONLY_IFRAME || 'false')
export const _ = { anonymousMode, env: process.env, store }

const getAuthToken = () => getToken()
const isWhiteList = (path) => whiteList.indexOf(path) !== -1
router.beforeEach(onBeforeEach)
router.afterEach((to, from, next) => {
  NProgress.done()
})

const newSession = JSON.parse(process.env.VUE_APP_USE_SESSION_SIGNIN || 'false')
if (anonymousMode && newSession) {
  store.dispatch('user/resetToken')
  store.dispatch('user/resetInfo')
}

async function onBeforeEach(to, from, next) {
  NProgress.start()

  document.title = getPageTitle(to.meta.title)

  if (to.path === '/login') {
    next()
    return
  } else if (isWhiteList(to.path)) {
    if (useErrorPage) {
      next()
    } else {
      next('/')
    }
    return
  }

  if (!debug) {
    if (project === 'datahub' && location.host.includes('ktnms.kt.com')) {
      console.log = () => {}
      if (onlyIframe && window.top === window.self) {
        next(useErrorPage ? `/401` : '/')
        return
      }
    }
  }

  let hasToken = getAuthToken()
  let newToken = false
  if (!hasToken) {
    if (anonymousMode) {
      const retval = await store.dispatch('user/login')
      if (retval !== true) {
        next(`/401`)
        return
      }
      hasToken = getAuthToken()
      newToken = true
    }
    if (isOnlyFront) {
      hasToken = getAuthToken()
      newToken = true
    }
  }

  if (hasToken) {
    const hasRoles = store.getters.roles && store.getters.roles.length > 0
    if (!newToken && hasRoles) {
      const menuRoles = to?.meta?.grant ?? ['ROLE_ADMIN', 'ROLE_USER']
      const myRoles = store.getters.roles
      const intersection = findIntersection(menuRoles, myRoles)
      if (intersection.length === 0) {
        next(from?.path ?? `/401`)
      } else {
        next()
      }
    } else {
      try {
        await store.dispatch('app/getServer')
        const { roles } = await store.dispatch('user/getInfo')
        const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
        router.addRoutes(accessRoutes)
        next({ ...to, replace: true })
      } catch (error) {
        await store.dispatch('user/resetToken')
        await store.dispatch('user/resetInfo')
        Message.error(error || 'Has Error')
        next(`/login?redirect=${to.path}`)
      }
    }
  } else {
    if (isWhiteList(to.path) || isOnlyFront) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
    NProgress.done()
  }
}

