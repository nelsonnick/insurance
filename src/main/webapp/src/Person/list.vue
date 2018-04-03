<template>
  <div>
    <Layout class="layout">
      <Header>
        <Menu mode="horizontal" theme="dark" active-name="person" @on-select="MenuClick">
          <div class="layout-nav">
            <MenuItem name="0">
              <Icon type="user"></Icon>
              当前用户：{{ userName }}
            </MenuItem>
            <MenuItem name="person" >
              <Icon type="android-person"></Icon>
              困难人员
            </MenuItem>
            <MenuItem name="family">
              <Icon type="android-people"></Icon>
              家庭成员
            </MenuItem>
            <MenuItem name="pass">
              <Icon type="android-settings"></Icon>
              修改密码
            </MenuItem>
            <MenuItem name="logout">
              <Icon type="android-close"></Icon>
              退出系统
            </MenuItem>
          </div>
        </Menu>
      </Header>
      <Row>
        <Col>
          <div class="left">
            <Breadcrumb :style="{margin: '20px 15px 0px 15px'}">
              <BreadcrumbItem>槐荫区就业困难人员管理</BreadcrumbItem>
              <BreadcrumbItem>困难人员</BreadcrumbItem>
              <BreadcrumbItem>列表</BreadcrumbItem>
            </Breadcrumb>
          </div>
          <div class="right">
            <Button type="info" @click="goAdd" v-if="lid">新增</Button>
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

  export default {
    name: 'list',
    components: {Search, Page, Options},
    data () {
      return {
        userName: window.userName,
        name: '',
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
        lid: true,
        columns: [
          {
            title: '序号',
            key: 'id',
            sortable: true,
            render: (h, params) => {
              return h('div', params.index + 1)
            }
          },
          {
            title: '身份证号码',
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
            title: '类别',
            key: 'type',
            sortable: true
          },
          {
            title: '所属中心',
            key: 'location',
            sortable: true
          },
          {
            title: '状态',
            key: 'state',
            sortable: true
          },
          {
            title: '操作',
            key: 'operate',
            align: 'center',
            width: 400,
            render: (h, params) => {
              const operate = []
              if ((params.row.lid.toString() === window.LocationId && params.row.sid.toString() === '1') || window.LocationId.toString() === '1') {
                operate.push(
                  h('Button', {
                    props: {
                      type: 'primary',
                      size: 'small'
                    },
                    on: {
                      click: () => {
                        this.goSave(params.index)
                      }
                    }
                  }, '添加家庭成员')
                )
              }
              if ((params.row.lid.toString() === window.LocationId && params.row.sid.toString() === '1') || window.LocationId.toString() === '1') {
                operate.push(
                  h('Button', {
                    props: {
                      type: 'warning',
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
              if ((params.row.lid.toString() === window.LocationId && params.row.sid.toString() === '1') || (window.LocationId.toString() === '1' && params.row.sid.toString() === '1')) {
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
                  }, '注销')
                )
              }
              if ((params.row.lid.toString() === window.LocationId && params.row.sid.toString() === '0') || (window.LocationId.toString() === '1' && params.row.sid.toString() === '0')) {
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
                  }, '激活')
                )
              }
              return h('div', operate)
            }
          }
        ]
      }
    },
    created: function () {
      this.$http.get(
        API.GetLocation,
        {headers: {'X-Requested-With': 'XMLHttpRequest'}}
      ).then((response) => {
        window.LocationId = response.body
        if (response.body === '1') {
          this.lid = false
        }
      }, (response) => {
        this.$Notice.error({
          title: '服务器内部错误，无法获取当前用户所属中心!'
        })
      })
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
      goAdd () {
        this.$router.push({path: '/add'})
      },
      goEdit (index) {
        this.$router.push({path: '/edit/' + this.pageList[index].id})
      },
      goSave (index) {
        this.$router.push({path: '/save/' + this.pageList[index].id})
      },
      goDel (index) {
        this.$http.get(
          API.Del,
          { params: {
            id: this.pageList[index].id
          } },
          {headers: {'X-Requested-With': 'XMLHttpRequest'}}
        ).then((response) => {
          if (response.body === 'OK') {
            this.$Loading.finish()
            this.$Message.success('注销成功!')
            this.getQuery(this.keyword)
          } else {
            this.$Loading.error()
            this.$Notice.error({
              title: response.body
            })
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误，无法注销!'
          })
        })
      },
      goActive (index) {
        this.$http.get(
          API.Active,
          { params: {
            id: this.pageList[index].id
          } },
          {headers: {'X-Requested-With': 'XMLHttpRequest'}}
        ).then((response) => {
          if (response.body === 'OK') {
            this.$Loading.finish()
            this.$Message.success('激活成功!')
            this.getQuery(this.keyword)
          } else {
            this.$Loading.error()
            this.$Notice.error({
              title: response.body
            })
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误，无法注销!'
          })
        })
      },
      getUser () {
        this.$http.get(
          API.GetUser,
          {headers: {'X-Requested-With': 'XMLHttpRequest'}}
        ).then((response) => {
          this.userName = response.body
          window.userName = this.userName
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误，无法获取当前用户姓名!'
          })
        })
      },
      MenuClick (name) {
        if (name.toString() === 'person') {
          window.location.href = '/person'
        } else if (name.toString() === 'family') {
          window.location.href = '/family'
        } else if (name.toString() === 'pass') {
          window.location.href = '/user/pass'
        } else if (name.toString() === 'logout') {
          window.location.href = '/logout'
        } else {
          this.getUser()
        }
      }
    }
  }
</script>
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
  .layout-nav {
    width: 1000px;
    margin: 0 auto;
    margin-right: 20px;
  }
</style>
