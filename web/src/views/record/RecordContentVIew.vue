<template>
  <Playground />
</template>

<script>
import Playground from "@/components/Playground.vue"
import { useStore } from "vuex"
export default {
  components: {
    Playground,
  },
  setup() {
    const store = useStore()

    return {}
  },
}
</script>

<style lang="scss" scoped></style>
