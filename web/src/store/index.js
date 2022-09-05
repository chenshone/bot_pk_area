import { createStore } from "vuex"
import UserStore from "./user"
import PkStore from "./pk"
import recordStore from "./record"
export default createStore({
  state: {},
  getters: {},
  mutations: {},
  actions: {},
  modules: {
    user: UserStore,
    pk: PkStore,
    record: recordStore,
  },
})
