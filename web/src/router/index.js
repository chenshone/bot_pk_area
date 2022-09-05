import { createRouter, createWebHistory } from "vue-router"
import store from "@/store"

import NotFound from "../views/error/NotFound"
import PKIndexView from "../views/pk/PKIndexView"
import RankListIndexView from "../views/rank_list/RankListIndexView"
import RecordIndexView from "../views/record/RecordIndexView"
import RecordContentView from "../views/record/RecordContentVIew.vue"
import UserBotIndexView from "../views/user/bot/UserBotIndexView"
import UserAccountLoginView from "../views/user/account/UserAccountLoginView"
import UserAccountRegisterView from "../views/user/account/UserAccountRegisterView"

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk",
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/pk",
    name: "pk_index",
    component: PKIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/rank-list",
    name: "rank_list_index",
    component: RankListIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/record",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/record/:recordId",
    name: "record_content",
    component: RecordContentView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/user/bot",
    name: "user_bot_index",
    component: UserBotIndexView,
    meta: {
      requestAuth: true,
    },
  },
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: "/404",
    name: "404",
    component: NotFound,
    meta: {
      requestAuth: false,
    },
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404",
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  if (to.meta.requestAuth && !store.state.user.is_login) {
    const token = localStorage.getItem("jwt_token")

    if (token && token.length) {
      store.commit("updateToken", token)
      store.dispatch("getInfo", {
        success(resp) {
          next()
        },
        error(resp) {
          next({ name: "user_account_login" })
        },
      })
    } else next({ name: "user_account_login" })
  } else {
    next()
  }
})

export default router
