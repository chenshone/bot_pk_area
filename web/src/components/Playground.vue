<template>
    <div class="playground"><GameMap></GameMap></div>
</template>

<script>
import GameMap from "./GameMap.vue"
export default {
    setup() {
        return {}
    },
    components: { GameMap },
}
</script>

<style lang="scss" scoped>
.playground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
}
</style>
