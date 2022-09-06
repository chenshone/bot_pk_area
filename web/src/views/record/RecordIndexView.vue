<template>
  <ContentField
    ><table class="table table-striped table-hover">
      <thead>
        <tr>
          <th>玩家A</th>
          <th>玩家B</th>
          <th>PK结果</th>
          <th>PK时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="record in records" :key="record.record.id">
          <th>
            <img class="record-avatar" :src="record.a_avatar" />
            &nbsp;
            <span class="record-username">{{ record.a_username }}</span>
          </th>
          <th>
            <img class="record-avatar" :src="record.b_avatar" />
            &nbsp;
            <span class="record-username">{{ record.b_username }}</span>
          </th>
          <th>{{ record.result }}</th>
          <th>{{ record.record.createTime }}</th>
          <th>
            <button
              @click="openRecordContent(record.record.id)"
              type="button"
              class="btn btn-primary"
            >
              查看对局
            </button>
          </th>
        </tr>
      </tbody>
    </table>
    <nav style="float: right" aria-label="Page navigation example">
      <ul class="pagination">
        <li
          :class="{ 'page-item': true, disabled: currentPage - 1 < 1 }"
          @click="getRecordList(currentPage - 1)"
        >
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li
          v-for="page in pages"
          :class="{ 'page-item': true, active: currentPage === page }"
          :key="page"
          @click="getRecordList(page)"
        >
          <a href="#" class="page-link">{{ page }}</a>
        </li>
        <li
          :class="{ 'page-item': true, disabled: currentPage + 1 > maxPages }"
          @click="getRecordList(currentPage + 1)"
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
import { useRouter } from "vue-router"
export default {
  setup() {
    const store = useStore()
    const router = useRouter()
    let records = ref([])
    let currentPage = ref(1)
    let totalRecords = ref(0)
    let maxPages = ref(0)
    let pages = ref([])

    const updatePages = () => {
      maxPages.value = parseInt(Math.ceil(totalRecords.value / 10))
      let newPages = []
      for (let i = currentPage.value - 2; i <= currentPage.value + 2; i++) {
        if (i >= 1 && i <= maxPages.value) {
          newPages.push(i)
        }
      }
      pages.value = newPages
    }

    const getRecordList = (page) => {
      $.ajax({
        url: "http://bpa.chenshone.top/api/record/getlist",
        type: "get",
        data: {
          page,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          records.value = resp.records
          totalRecords.value = resp.records_count
          currentPage.value = page
          updatePages()
          console.log(resp)
        },
      })
    }

    const strTo2D = (map) => {
      let g = []
      for (let i = 0, k = 0; i < 13; i++) {
        let line = []
        for (let j = 0; j < 14; j++, k++) {
          if (map[k] === "0") line.push(0)
          else line.push(1)
        }
        g.push(line)
      }
      return g
    }

    const openRecordContent = (recordId) => {
      for (const record of records.value) {
        if (record.record.id === recordId) {
          store.commit("updateIsRecord", true)
          store.commit("updateGame", {
            map: strTo2D(record.record.map),
            aId: record.record.aid,
            aSx: record.record.asx,
            aSy: record.record.asy,
            bId: record.record.bid,
            bSx: record.record.bsx,
            bSy: record.record.bsy,
          })
          store.commit("updateSteps", {
            a_steps: record.record.asteps,
            b_steps: record.record.bsteps,
          })
          store.commit("updateRecordLoser", record.record.loser)
          router.push({
            name: "record_content",
            params: {
              recordId,
            },
          })
          break
        }
      }
    }

    getRecordList(1)

    return {
      records,
      currentPage,
      totalRecords,
      pages,
      maxPages,
      getRecordList,
      openRecordContent,
    }
  },
  components: { ContentField },
}
</script>

<style lang="scss" scoped>
.record-avatar {
  width: 5vw;
  border-radius: 50%;
}
.record-username {
}
</style>
