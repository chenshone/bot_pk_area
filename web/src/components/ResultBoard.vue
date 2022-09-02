<template>
  <div class="result-board">
    <div class="result-board-text">
      {{ resultText }}
    </div>
    <div class="result-board-btn">
      <button @click="restart" type="button" class="btn btn-primary btn-lg">
        重新匹配
      </button>
    </div>
  </div>
</template>

<script>
import { ref } from "vue"
import { useStore } from "vuex"

export default {
  setup() {
    const store = useStore()
    let resultText = ref("")
    if (store.state.pk.loser === "all") {
      resultText.value = "平局"
    } else if (
      (store.state.pk.loser === "A" &&
        store.state.pk.aId == store.state.user.id) ||
      (store.state.pk.loser === "B" &&
        store.state.pk.bId == store.state.user.id)
    ) {
      resultText.value = "LOSE"
    } else {
      resultText.value = "WIN"
    }

    const restart = () => {
      store.commit("updateStatus", "matching")
      store.commit("updateLoser", "none")
      store.commit("updateOpponent", {
        username: "我的对手",
        avatar:
          "https://cdn.jsdelivr.net/gh/chenshone/myPictureHost@main/code_class/20220901211257.png",
      })
    }
    return {
      resultText,
      restart,
    }
  },
}
</script>

<style lang="scss" scoped>
.result-board {
  width: 30vw;
  height: 30vh;
  background-color: rgba($color: #f3a6b1, $alpha: 0.5);
  position: absolute;
  top: 30vh;
  left: 35vw;
  .result-board-text {
    font-size: 100px;
    font-weight: 666;
    font-style: italic;
    color: white;
    text-align: center;
    margin-top: 3vh;
  }
  .result-board-btn {
    text-align: center;
    margin-top: 3vh;
  }
}
</style>
