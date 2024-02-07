const duplicateCheckFileObject = {}
import router from '@/router/index.js'

const state = {
    testComponentList: [],
    componentTreeData: [],
    defaultComponentTreeKey: ''
}

const mutations = {
    PUSH_TEST_COMPONENT_LIST: (state, component) => {
        state.testComponentList.push(component)
    },

    COMPONENT_LIST_CONVERT_TREE_DATA(state) {
        state.testComponentList.forEach((testComponent) => {
            state.componentTreeData.push({
                componentPath: testComponent.__file,
                componentAlias: testComponent.__file.split('/').pop(),
                component: testComponent
            })
        })
    },

    SET_DEFAULT_COMPONENT_TREE_KEY(state, componentPath) {
        state.defaultComponentTreeKey = componentPath
    }
}

const actions = {
    async initTestComponentList({ dispatch, commit, state }) {
        if (state.testComponentList.length === 0) {
            await dispatch('setComponentList')
            await commit('COMPONENT_LIST_CONVERT_TREE_DATA')
        }
    },

    async setComponentList({ dispatch, state }) {
        const testRoutes = [...router.options.routes, ...router.options.routes2]
        for (const route of testRoutes) {
            dispatch('recursiveSetComponent', route.component)
            if (route.children) {
                for (const child of route.children) {
                    if (child.component) {
                        const childComponent = await child.component()
                        dispatch('recursiveSetComponent', childComponent.default)
                    }
                }
            }
        }
    },

    recursiveSetComponent({ dispatch, commit, state }, component) {
        if (!Object.hasOwnProperty.call(duplicateCheckFileObject, component.__file)) {
            duplicateCheckFileObject[component.__file] = component
            if (component.mixins) {
                const testComponent = component.mixins.find(mixin => mixin.__file === 'src/test/ComponentTesterMixins.vue')
                testComponent && commit('PUSH_TEST_COMPONENT_LIST', component)
            }

            if (component.components) {
                Object.keys(component.components).forEach((childComponent) => {
                    dispatch('recursiveSetComponent', component.components[childComponent])
                })
            }
        }
    },

}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
