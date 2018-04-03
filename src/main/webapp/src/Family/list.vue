<template>
  <div>
    <Layout class="layout">
      <Header>
        <Menu mode="horizontal" theme="dark" active-name="family" @on-select="MenuClick">
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
                <BreadcrumbItem>家庭成员</BreadcrumbItem>
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
    components: { Search, Page, Options },
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
            title: '姓名',
            key: 'name',
            sortable: true
          },
          {
            title: '身份证号码',
            key: 'number',
            sortable: true
          },
          {
            title: '联系电话',
            key: 'phone',
            sortable: true
          },
          {
            title: '身份',
            key: 'identity',
            sortable: true
          },
          {
            title: '婚姻',
            key: 'marriage',
            sortable: true
          },
          {
            title: '关联申请人',
            key: 'pname',
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
            key: 'state',
            align: 'center',
            width: 400,
            render: (h, params) => {
              const operate = []
              if (params.row.state.toString() === '1') {
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
              if (params.row.state.toString() === '2') {
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
                  }, '删除')
                )
              }
              return h('div', operate)
            }
          }
        ]
      }
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
  .layout-nav {
    width: 1000px;
    margin: 0 auto;
    margin-right: 20px;
  }
</style>
