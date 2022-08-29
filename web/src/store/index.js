import { createStore } from "vuex"
import UserStore from "./user"
export default createStore({
    state: {},
    getters: {},
    mutations: {},
    actions: {},
    modules: {
        user: UserStore,
    },
})
