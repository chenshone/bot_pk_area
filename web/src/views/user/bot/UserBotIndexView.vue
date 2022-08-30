<template>
    <ContentField><div>我的bot</div></ContentField>
</template>

<script>
import $ from "jquery"

import ContentField from "@/components/ContentField.vue"
import { useStore } from "vuex"
export default {
    components: { ContentField },
    setup() {
        const store = useStore()
        // $.ajax({
        //     url: "http://localhost:8090/user/bot/add",
        //     type: "post",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     data: {
        //         title: "bot1",
        //         description: "",
        //         content: "print('hello world!')",
        //     },
        //     success(resp) {
        //         console.log(resp)
        //     },
        //     error(resp) {
        //         console.log(resp)
        //     },
        // })

        // $.ajax({
        //     url: "http://localhost:8090/user/bot/remove",
        //     type: "post",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     data: {
        //         id: "2",
        //     },
        //     success(resp) {
        //         console.log(resp)
        //     },
        //     error(resp) {
        //         console.log(resp)
        //     },
        // })

        // $.ajax({
        //     url: "http://localhost:8090/user/bot/update",
        //     type: "post",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     data: {
        //         id: "3",
        //         title: "bot11",
        //         content: "print('hello')",
        //     },
        //     success(resp) {
        //         console.log(resp)
        //     },
        //     error(resp) {
        //         console.log(resp)
        //     },
        // })

        $.ajax({
            url: "http://localhost:8090/user/bot/get-list",
            type: "get",
            headers: {
                Authorization: "Bearer " + store.state.user.token,
            },
            success(resp) {
                console.log(resp)
            },
            error(resp) {
                console.log(resp)
            },
        })

        return {}
    },
}
</script>

<style lang="scss" scoped></style>
