<template>
    <NavBar />
    <router-view></router-view>
</template>

<script>
import $ from "jquery"
import { ref } from "vue"
import "bootstrap"
import "bootstrap/dist/css/bootstrap.min.css"

import NavBar from "@/components/NavBar.vue"

export default {
    name: "App",
    components: {
        NavBar,
    },
    setup() {
        let bot_name = ref("")
        let bot_rating = ref("")

        // $.ajax({
        //     url: "http://127.0.0.1:8090/user/account/register",
        //     type: "post",
        //     data: {
        //         username: "chenshone123",
        //         password: "123456",
        //         confirmedPassword: "123456",
        //     },
        //     success: (resp) => {
        //         console.log(resp)
        //         // bot_name.value = resp.name
        //         // bot_rating.value = resp.rating
        //     },
        //     error(resp) {
        //         console.log(resp)
        //     },
        // })
        return {
            bot_name,
            bot_rating,
        }
    },
}
</script>

<style lang="scss">
body {
    background-image: url("@/assets/img/bg.jpeg");
    background-size: 100%;
}
</style>
