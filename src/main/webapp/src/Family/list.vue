<template>
  <div>
    <Layout class="layout">
      <Header>
        <MenuBar :sys="sys" :active="active" :userName="userName"></MenuBar>
      </Header>
        <Row>
          <Col>
            <div class="left">
              <Breadcrumb :style="{margin: '20px 15px 0px 15px'}">
                <BreadcrumbItem>槐荫区就业困难人员管理</BreadcrumbItem>
                <BreadcrumbItem>家庭成员</BreadcrumbItem>
                <BreadcrumbItem>列表</BreadcrumbItem>
              </Breadcrumb>
            </div>
            <div class="right">
              <Button type="ghost" @click="goDown"><Icon type="ios-cloud-download"></Icon>下载</Button>
              <Search @goQuery="getQuery"></Search>
            </div>
          </Col>
        </Row>
        <Row>
          <Col>
          <div style="min-height: 200px;" class="layout-content">
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
      <Row>
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
    </Layout>
  </div>
</template>
<script>
  import Search from '../Common/search.vue'
  import Page from '../Common/page.vue'
  import Options from '../Common/options.vue'
  import * as API from './API.js'
  import * as BASE from '../Common/Base.js'
  import MenuBar from '../Common/menubar.vue'
  import axios from 'axios'

  export default {
    name: 'list',
    components: { Search, Page, Options, MenuBar },
    data () {
      return {
        userName: '',
        LocationId: '',
        sys: false,
        active: 'family',
        query: API.Query,
        total: API.Total,
        keyword: '',
        pageList: [],
        pageTotal: '',
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
            width: 80,
            render: (h, params) => {
              return h('div', params.index + 1)
            }
          },
          {
            title: '困难人员',
            sortable: true,
            render: (h, params) => {
              return h('div', params.row.pnumber + '' + params.row.pname)
            }
          },
          {
            title: '身份',
            key: 'identity',
            width: 80,
            sortable: true
          },
          {
            title: '证件号码',
            key: 'number',
            sortable: true
          },
          {
            title: '姓名',
            key: 'name',
            sortable: true
          },
          {
            title: '联系电话',
            key: 'phone',
            sortable: true
          },
          {
            title: '婚姻',
            key: 'marriage',
            width: 80,
            sortable: true
          },
          {
            title: '所属中心',
            key: 'location',
            sortable: true
          },
          {
            title: '核查状态',
            key: 'state',
            sortable: true
          },
          {
            title: '操作',
            key: 'state',
            align: 'center',
            width: 300,
            render: (h, params) => {
              const operate = []
              if ((params.row.lid.toString() === this.LocationId.toString() && params.row.sid.toString() === '1') || this.LocationId.toString() === '1') {
                operate.push(
                  h('Button', {
                    props: {
                      type: 'info',
                      size: 'small'
                    },
                    on: {
                      click: () => {
                        this.goEdit(params.index)
                      }
                    }
                  }, '修改')
                )
              }
              if (params.row.lid.toString() === this.LocationId.toString() && params.row.sid.toString() === '1') {
                operate.push(
                  h('Button', {
                    props: {
                      type: 'error',
                      size: 'small'
                    },
                    on: {
                      click: () => {
                        this.goDel(params.index)
                      }
                    }
                  }, '停止核查')
                )
              }
              if (params.row.lid.toString() === this.LocationId.toString() && params.row.sid.toString() === '0' && params.row.psid.toString() === '1') {
                operate.push(
                  h('Button', {
                    props: {
                      type: 'success',
                      size: 'small'
                    },
                    on: {
                      click: () => {
                        this.goActive(params.index)
                      }
                    }
                  }, '开展核查')
                )
              }
              if (params.row.lid.toString() === this.LocationId.toString() && params.row.sid.toString() === '1' && params.row.psid.toString() === '1' && params.row.check.toString() === '1') {
                operate.push(
                  h('Button', {
                    props: {
                      type: 'warning',
                      size: 'small'
                    },
                    on: {
                      click: () => {
                        this.goClose(params.index)
                      }
                    }
                  }, '关闭自动核查')
                )
              }
              if (params.row.lid.toString() === this.LocationId.toString() && params.row.sid.toString() === '1' && params.row.psid.toString() === '1' && params.row.check.toString() === '0') {
                operate.push(
                  h('Button', {
                    props: {
                      size: 'small'
                    },
                    on: {
                      click: () => {
                        this.goOpen(params.index)
                      }
                    }
                  }, '开启自动核查')
                )
              }
              return h('div', operate)
            }
          }
        ]
      }
    },
    created: function () {
      this.getUser()
    },
    methods: {
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
      goEdit (index) {
        this.$router.push({ path: '/edit/' + this.pageList[index].id })
      },
      goDel (index) {
        this.$router.push({path: '/del/' + this.pageList[index].id})
      },
      goActive (index) {
        this.$router.push({path: '/active/' + this.pageList[index].id})
      },
      goOpen (index) {
        axios.get(API.Open, {
          params: { id: this.pageList[index].id }
        }).then(res => {
          if (res.data === 'OK') {
            this.$Message.success('开启自动核查成功!')
            this.$Notice.success({
              title: '操作完成!',
              desc: '家庭成员：' + this.pageList[index].name + '已开启自动核查！'
            })
            this.getQuery(this.keyword)
          } else {
            this.$Notice.error({
              title: res.data
            })
          }
        }).catch(res => {
          this.$Notice.error({
            title: '服务器内部错误，无法开启自动核查!'
          })
        })
      },
      goClose (index) {
        axios.get(API.Close, {
          params: { id: this.pageList[index].id }
        }).then(res => {
          if (res.data === 'OK') {
            this.$Message.success('关闭自动核查成功!')
            this.$Notice.success({
              title: '操作完成!',
              desc: '家庭成员：' + this.pageList[index].name + '已关闭自动核查！'
            })
            this.getQuery(this.keyword)
          } else {
            this.$Notice.error({
              title: res.data
            })
          }
        }).catch(res => {
          this.$Notice.error({
            title: '服务器内部错误，无法关闭自动核查!'
          })
        })
      },
      goDown () {
        window.location.href = BASE.base + 'family/export?keyword=' + this.keyword
      },
      getUser () {
        axios.get(API.GetUser).then(res => {
          if (res.data.lid.toString() === '1') {
            this.addPerson = false
            this.sys = true
            window.sys = true
          }
          window.LocationId = res.data.lid
          window.userName = res.data.name
          this.LocationId = res.data.lid
          this.userName = res.data.name
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误，无法获取当前用户信息!'
          })
        })
      }
    }
  }
</script>
<style scoped>
  .layout-content{
    margin:0px 15px 0px 15px;
    overflow: hidden;
    background: #fff;
    border-radius: 4px;
  }
  .left{
    margin: 15px;
    border-radius: 4px;
  }
  .right{
    margin: 15px;
    border-radius: 4px;
    float: right;
  }
</style>
<style scoped>
  .layout-content {
    margin: 0px 15px 0px 15px;
    overflow: hidden;
    background: #fff;
    border-radius: 4px;
  }
  .left {
    margin: 15px;
    border-radius: 4px;
  }
  .right {
    margin: 15px;
    border-radius: 4px;
    float: right;
  }
  .layout {
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }
</style>
