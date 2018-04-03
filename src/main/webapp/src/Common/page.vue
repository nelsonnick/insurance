<template>
  <div class="page">
    <Page :total="pageTotal" :current="pageCurrent" :page-size="pageSize" @on-page-size-change="sizeChange" @on-change="pageChange" show-sizer show-elevator show-total></Page>
  </div>
</template>
<script>
  export default {
    props: ['queryURL', 'totalURL', 'keyword'],
    data () {
      return {
        pageCurrent: 1,
        pageSize: 10,
        pageTotal: 0,
        pageList: []
      }
    },
    created: function () {
      this.getLists(this.queryURL, this.totalURL, this.keyword, this.pageCurrent, this.pageSize)
    },
    methods: {
      getLists (queryURL, totalURL, keyword, pageCurrent, pageSize) {
        this.$http.get(
          queryURL,
          { params: {
            keyword: keyword,
            pageCurrent: pageCurrent,
            pageSize: pageSize
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((res) => {
          if (res.body.toString() === 'illegal' || res.body.toString() === 'overdue') {
          } else {
            this.$http.get(
              totalURL,
              { params: {
                keyword: keyword
              } },
              { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
            ).then((response) => {
              if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
                this.$Notice.error({
                  title: '登录过期或非法操作!'
                })
                window.location.href = '/'
              } else {
                this.pageList = res.body
                this.pageTotal = response.body
                this.$emit('goList', this.pageList, this.pageTotal)
              }
            }, (response) => {
              this.$Notice.error({
                title: '服务器内部错误!'
              })
            })
          }
        }, (res) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      sizeChange (value) {
        this.pageSize = value
        this.pageCurrent = 1
        this.$emit('savePageCurrent', this.pageCurrent)
        this.getLists(this.queryURL, this.totalURL, this.keyword, this.pageCurrent, this.pageSize)
      },
      pageChange (value) {
        this.pageCurrent = value
        this.$emit('savePageCurrent', this.pageCurrent)
        this.getLists(this.queryURL, this.totalURL, this.keyword, this.pageCurrent, this.pageSize)
      },
      query (keyword) {
        this.keyword = keyword
        this.pageCurrent = 1
        this.$emit('savePageCurrentAndKeyword', this.keyword, this.pageCurrent)
        this.getLists(this.queryURL, this.totalURL, this.keyword, this.pageCurrent, this.pageSize)
      },
      queryNoChange (keyword) {
        this.keyword = keyword
        this.$emit('savePageCurrentAndKeyword', this.keyword, this.pageCurrent)
        this.getLists(this.queryURL, this.totalURL, this.keyword, this.pageCurrent, this.pageSize)
      }
    }
  }
</script>
<style>
  .page{
    float:right ;
    margin: 15px;
  }
</style>
