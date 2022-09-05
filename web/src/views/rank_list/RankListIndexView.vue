<template>
  <ContentField
    ><table class="table table-striped table-hover">
      <thead>
        <tr>
          <th>玩家</th>
          <th>rating</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="users.id">
          <th>
            <img class="user-avatar" :src="user.avatar" />
            &nbsp;
            <span class="user-username">{{ user.username }}</span>
          </th>
          <th>
            <span>{{ user.rating }}</span>
          </th>
        </tr>
      </tbody>
    </table>
    <nav style="float: right" aria-label="Page navigation example">
      <ul class="pagination">
        <li
          :class="{ 'page-item': true, disabled: currentPage - 1 < 1 }"
          @click="getUserList(currentPage - 1)"
        >
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li
          v-for="page in pages"
          :class="{ 'page-item': true, active: currentPage === page }"
          :key="page"
          @click="getUserList(page)"
        >
          <a href="#" class="page-link">{{ page }}</a>
        </li>
        <li
          :class="{ 'page-item': true, disabled: currentPage + 1 > maxPages }"
          @click="getUserList(currentPage + 1)"
        >
          <a href="#" class="page-link" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>

<script>
import $ from "jquery"
import ContentField from "@/components/ContentField.vue"
import { useStore } from "vuex"
import { ref } from "vue"
export default {
  setup() {
    const store = useStore()
    let users = ref([])
    let currentPage = ref(1)
    let totalUsers = ref(0)
    let maxPages = ref(0)
    let pages = ref([])

    const updatePages = () => {
      maxPages.value = parseInt(Math.ceil(totalUsers.value / 3))
      let newPages = []
      for (let i = currentPage.value - 2; i <= currentPage.value + 2; i++) {
        if (i >= 1 && i <= maxPages.value) {
          newPages.push(i)
        }
      }
      pages.value = newPages
    }

    const getUserList = (page) => {
      $.ajax({
        url: "http://127.0.0.1:8090/ranklist/getlist",
        type: "get",
        data: {
          page,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          users.value = resp.users
          totalUsers.value = resp.users_count
          currentPage.value = page
          updatePages()
          console.log(resp)
        },
      })
    }

    getUserList(1)

    return {
      users,
      currentPage,
      totalUsers,
      pages,
      maxPages,
      getUserList,
    }
  },
  components: { ContentField },
}
</script>

<style lang="scss" scoped>
.user-avatar {
  width: 5vw;
  border-radius: 50%;
}
.user-username {
}
</style>
