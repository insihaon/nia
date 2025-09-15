import { niaRoute } from '@/router/nia/index'

export function getHiddenParameter(routerPath, popupDialogName, action) {
    return `<span style="display:none">[path]:${routerPath}, [popup]:${popupDialogName}, [action]:${action}</span>`
}

export function getNiaRouterPathByName(routerName, path = '', routes = niaRoute) {
    for (const route of routes) {
        const slashRoutePath = route.path.startsWith('/') ? route.path : '/' + route.path

        if (route.name === routerName) {
            return path + slashRoutePath
        } else {
            if (route.children) {
                const routerPath = getNiaRouterPathByName(routerName, path + slashRoutePath, route.children)
                if (routerPath) {
                    return routerPath
                }
            }
        }
    }

    return null
}

export function getNiaRouteNameByPath(path, routes = niaRoute, prefix = '') {
    for (const route of routes) {
        // 1. 현재 라우트의 path와 일치하는지 확인
        if (prefix + route.path === path) {
            return route.name
        }

        // 2. children이 있는지 확인하고 재귀적으로 탐색
        if (route.children) {
            const foundName = getNiaRouteNameByPath(path, route.children, route.path + '/')
            // 자식 라우트에서 이름이 발견되면 즉시 반환
            if (foundName) {
                return foundName
            }
        }
    }

    // 모든 라우트를 탐색했지만 일치하는 것을 찾지 못한 경우
    return null
}

export function getNiaRouteTitleByPath(path, routes = niaRoute, prefix = '') {
    for (const route of routes) {
        // 1. 현재 라우트의 path와 일치하는지 확인
        if (prefix + route.path === path) {
            return route.meta.title
        }

        // 2. children이 있는지 확인하고 재귀적으로 탐색
        if (route.children) {
            const foundTitle = getNiaRouteTitleByPath(path, route.children, route.path + '/')
            // 자식 라우트에서 이름이 발견되면 즉시 반환
            if (foundTitle) {
                return foundTitle
            }
        }
    }

    // 모든 라우트를 탐색했지만 일치하는 것을 찾지 못한 경우
    return null
}
