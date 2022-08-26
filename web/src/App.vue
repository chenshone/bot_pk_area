<template>
  <div>
    <div>bot昵称: {{ bot_name }}</div>
    <div>bot rating: {{ bot_rating }}</div>
  </div>
</template>

<script>
import $ from "jquery";
import { ref } from "vue";

export default {
  name: "App",
  setup() {
    let bot_name = ref("");
    let bot_rating = ref("");
    $.ajax({
      url: "http://127.0.0.1:8090/pk/botinfo",
      type: "get",
      success: (resp) => {
        console.log(resp);
        bot_name.value = resp.name;
        bot_rating.value = resp.rating;
      },
    });
    return {
      bot_name,
      bot_rating,
    };
  },
};
</script>

<style lang="scss">
body {
  background-image: url("@/assets/bg.jpeg");
  background-size: 100%;
}
</style>
