import { createStore } from "vuex"
import UserStore from "./user"
import PkStore from "./pk"
export default createStore({
  state: {},
  getters: {},
  mutations: {},
  actions: {},
  modules: {
    user: UserStore,
    pk: PkStore,
  },
})
