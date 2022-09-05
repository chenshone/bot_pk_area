<template>
  <div class="playground">
    <GameMap></GameMap>
    <div class="player_container" v-if="!$store.state.record.is_record">
      <div class="player row justify-content-center">
        <span class="col-6">你当前的操作的角色颜色是</span>
        <div
          :class="
            $store.state.pk.aId == $store.state.user.id
              ? 'color_a col-6'
              : 'color_b col-6'
          "
        ></div>
      </div>
    </div>
  </div>
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
  .player_container {
    font-size: 30px;
    color: #8a1874;
    .player {
      div {
        width: 50px;
        height: 30px;
        margin-top: 8px;
      }
      .color_a {
        background: #5280f0;
      }
      .color_b {
        background: #fa5252;
      }
    }
  }
}
</style>
