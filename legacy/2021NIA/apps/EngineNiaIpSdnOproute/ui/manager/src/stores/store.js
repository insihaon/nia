import { createStore } from "vuex";

const store = createStore({
    state: {
        token: localStorage.getItem('jwtToken'),
    },
    getters: {
        isLogin(state) {
            return state.token == null ? false : true;
        }
    },
    mutations: {
        setToken(state, value) {
            state.token = value;
        }
    },
    actions: {
        setToken:({commit}, value) => {
            commit('setToken', value);
            localStorage.setItem('jwtToken', value);
        }
    }
});
export default store;