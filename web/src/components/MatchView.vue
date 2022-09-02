<template>
  <div class="match-view">
    <div class="row">
      <div class="col-6">
        <div class="user-avatar">
          <img :src="$store.state.user.avatar" alt="我的头像" />
        </div>
        <div class="user-username">
          {{ $store.state.user.username }}
        </div>
      </div>
      <div class="col-6">
        <div class="user-avatar">
          <img :src="$store.state.pk.opponentAvatar" alt="我的头像" />
        </div>
        <div class="user-username">
          {{ $store.state.pk.opponentUsername }}
        </div>
      </div>
      <div class="col-12" style="text-align: center; padding-top: 12vh">
        <button
          type="button"
          class="btn btn-primary btn-lg"
          @click="matchBtnHandler"
        >
          {{ matchBtnMsg }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue"
import { useStore } from "vuex"

export default {
  setup() {
    const store = useStore()
    let matchBtnMsg = ref("开始匹配")

    const matchBtnHandler = () => {
      if (matchBtnMsg.value === "开始匹配") {
        matchBtnMsg.value = "取消匹配"
        store.state.pk.socket.send(
          JSON.stringify({
            event: "start-matching",
          })
        )
      } else if ((matchBtnMsg.value = "取消匹配")) {
        matchBtnMsg.value = "开始匹配"
        store.state.pk.socket.send(
          JSON.stringify({
            event: "stop-matching",
          })
        )
      }
    }
    return {
      matchBtnMsg,
      matchBtnHandler,
    }
  },
}
</script>

<style lang="scss" scoped>
.match-view {
  width: 60vw;
  height: 70vh;
  margin: 40px auto;
  padding-top: 100px;
  background-color: rgba($color: #f3a6b1, $alpha: 0.5);
  .user-avatar {
    text-align: center;
    img {
      width: 10vw;
      height: 17vh;
      border-radius: 50%;
    }
  }
  .user-username {
    text-align: center;
    font-size: 200%;
    font-weight: 666;
    padding-top: 20px;
  }
}
</style>
