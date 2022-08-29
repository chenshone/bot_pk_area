import { createRouter, createWebHistory } from "vue-router"

import NotFound from "../views/error/NotFound"
import PKIndexView from "../views/pk/PKIndexView"
import RankListIndexView from "../views/rank_list/RankListIndexView"
import RecordIndexView from "../views/record/RecordIndexView"
import UserBotIndexView from "../views/user/bot/UserBotIndexView"
import UserAccountLoginView from "../views/user/account/UserAccountLoginView"
import UserAccountRegisterView from "../views/user/account/UserAccountRegisterView"

const routes = [
    {
        path: "/",
        name: "home",
        redirect: "/pk",
    },
    {
        path: "/pk",
        name: "pk_index",
        component: PKIndexView,
    },
    {
        path: "/rank-list",
        name: "rank_list_index",
        component: RankListIndexView,
    },
    {
        path: "/record",
        name: "record_index",
        component: RecordIndexView,
    },
    {
        path: "/user/bot",
        name: "user_bot_index",
        component: UserBotIndexView,
    },
    {
        path: "/user/account/login",
        name: "user_account_login",
        component: UserAccountLoginView,
    },
    {
        path: "/user/account/register",
        name: "user_account_register",
        component: UserAccountRegisterView,
    },
    {
        path: "/404",
        name: "404",
        component: NotFound,
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

export default router
