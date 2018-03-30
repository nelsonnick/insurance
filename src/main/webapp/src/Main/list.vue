<template>
  <div class="layout">
    <Row>
      <Col>
        <Menu mode="horizontal" theme="dark">
          <div class="layout-nav"></div>
        </Menu>
      </Col>
    </Row>
    <Row>
      <Breadcrumb>
        <Breadcrumb-item>&nbsp;&nbsp;&nbsp;列表</Breadcrumb-item>
      </Breadcrumb>
    </Row>
    <Row>
      <Col>
        <div>
          <div class="left"><Button type="info" size="large" @click="goAdd" >新增</Button></div>
          <div class="right"><Search @goQuery="getQuery" ></Search></div>
        </div>
      </Col>
    </Row>
    <Row>
      <Col>
        <div class="layout-content">
          <Table
            highlight-row
            :height="height"
            :context="self"
            :border="border"
            :stripe="stripe"
            :size="size"
            :columns="columns"
            :data="pageList">
          </Table>
        </div>
      </Col>
    </Row>
    <Row >
      <Col>
      <div>
        <div class="left">
          <Options
            @showBorder="getBorder"
            @showStripe="getStripe"
            @tableSize="getSize"
          >
          </Options>
        </div>
        <div class="right">
          <Page
            ref="pages"
            @goList="getList"
            @savePageCurrent="saveCurrent"
            @savePageCurrentAndKeyword="CurrentAndKeyword"
            :queryURL="query"
            :totalURL="total"
            :keyword="keyword"
          >
          </Page>
        </div>
      </div>
      </Col>
    </Row>
    <Modal v-model="del" width="360" :styles="{top: '40px'}">
      <p slot="header" style="color:#FF0000;text-align:center">
        <Icon type="information-circled"></Icon>
        <span>删除确认</span>
      </p>
      <div style="text-align:center">
        <p>即将要删除菜谱（{{name}}）</p>
        <p>该功能必须慎用！</p>
        <p>是否继续删除？</p>
      </div>
      <div slot="footer">
        <Button type="error" size="large" long @click="goDelete">删除</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
  import Search from '../Common/search.vue'
  import Page from '../Common/page.vue'
  import Options from '../Common/options.vue'
  import * as API from './API.js'
  export default {
    name: 'list',
    components: { Search, Page, Options },
    data () {
      return {
        name: '',
        query: API.query,
        total: API.total,
        keyword: '',
        pageList: [],
        pageTotal: '',
        del: false,
        index: '',
        border: false,
        stripe: false,
        size: 'small',
        height: 450,
        self: this,
        columns: [
          {
            title: '序号',
            key: 'id',
            sortable: true,
            render (row, column, index) {
              return `${index + 1}`
            }
          },
          {
            title: '菜名',
            key: 'name',
            sortable: true
          },
          {
            title: '类型',
            key: 'type',
            sortable: true
          },
          {
            title: '使用时间',
            key: 'time',
            sortable: true
          },
          {
            title: '食用季节',
            key: 'season',
            sortable: true
          },
          {
            title: '操作',
            key: 'state',
            align: 'center',
            width: 400,
            render (row, column, index) {
              return `
              <i-button type="primary" @click="goEdit(${index})">修改</i-button>
              <i-button type="success" @click="goLook(${index})">网站链接</i-button>
              <i-button type="error" @click="showDelete(${index})">删除</i-button>
              `
            }
          }
        ]
      }
    },

    methods: {
      showDelete (index) {
        this.del = true
        this.index = index
        this.name = this.pageList[index].name
      },
      getQuery (keyword) {
        this.keyword = keyword
        this.$refs.pages.query(keyword)
      },
      getQueryNoChange (keyword) {
        this.keyword = keyword
        this.$refs.pages.queryNoChange(keyword)
      },
      getBorder (border) {
        this.border = border
      },
      getStripe (stripe) {
        this.stripe = stripe
      },
      getSize (tableSize) {
        if (tableSize.toString() === 'true') {
          this.height = 665
          this.size = 'large'
        } else {
          this.height = 450
          this.size = 'small'
        }
      },
      getList (pageList) {
        this.pageList = pageList
      },
      saveCurrent (pageCurrent) {
        this.$store.commit('save', {
          pageCurrent: pageCurrent
        })
      },
      CurrentAndKeyword (keyword, pageCurrent) {
        this.$store.commit('save', {
          keyword: keyword,
          pageCurrent: pageCurrent
        })
      },
      goAdd () {
        this.$router.push({ path: '/add' })
      },
      goLook (index) {
        window.open(this.pageList[index].url)
      },
      goEdit (index) {
        this.$router.push({ path: '/edit/' + this.pageList[index].id })
      },
      goDelete () {
        this.$Loading.start()
        this.$Message.info('正在进行删除操作，请稍后...')
        this.del = false
        this.$http.get(
          API.del,
          { params: {
            id: this.pageList[this.index].id
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body.toString() === 'OK') {
            this.getQueryNoChange(this.keyword)
            this.$Notice.success({
              title: '操作完成!',
              desc: '菜谱：' + this.name + '已删除！'
            })
            this.$Loading.finish()
          } else if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
            this.$Notice.error({
              title: '登录过期或非法操作!'
            })
            this.$Loading.error()
          } else {
            this.$Notice.error({
              title: response.body
            })
            this.$Loading.error()
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
          this.$Loading.error()
        })
      }
    }
  }
</script>
<style scoped>
  .layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
  }
  .layout-nav{
    width: 420px;
    margin: 0 auto;
  }
  .layout-assistant{
    width: 300px;
    margin: 0 auto;
    height: inherit;
  }
  .layout-content{
    margin:0px 15px 0px 15px;
    overflow: hidden;
    background: #fff;
    border-radius: 4px;
  }
  .layout-content-main{
    padding: 5px;
  }
  .left{
    margin: 15px;
    border-radius: 4px;
    float: left;
  }
  .right{
    margin: 15px;
    border-radius: 4px;
    float: right;
  }
</style>
