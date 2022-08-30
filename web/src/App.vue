<template>
    <NavBar />
    <router-view></router-view>
</template>

<script>
import "bootstrap"
import "bootstrap/dist/css/bootstrap.min.css"

import NavBar from "@/components/NavBar.vue"

export default {
    name: "App",
    components: {
        NavBar,
    },
    setup() {
        return {}
    },
}
</script>

<style lang="scss">
body {
    background-image: url("@/assets/img/bg.jpeg");
    background-size: 100%;
}
</style>
