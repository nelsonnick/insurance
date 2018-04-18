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
              <BreadcrumbItem>家庭成员变更记录</BreadcrumbItem>
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
  import MenuBar from '../Common/menubar.vue'
  import * as API from './API.js'
  import * as BASE from '../Common/Base.js'
  import axios from 'axios'

  export default {
    name: 'list',
    components: {Search, Page, Options, MenuBar},
    data () {
      return {
        userName: '',
        LocationId: '',
        sys: false,
        active: 'family',
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
        addPerson: true,
        columns: [
          {
            title: '序号',
            key: 'id',
            width: 80,
            sortable: true,
            render: (h, params) => {
              return h('div', params.index + 1)
            }
          },
          {
            title: '困难人员姓名',
            key: 'pnumber',
            sortable: true
          },
          {
            title: '困难人员证件号码',
            key: 'pname',
            sortable: true
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
            title: '操作人员',
            key: 'user',
            sortable: true
          },
          {
            title: '变更类别',
            key: 'type',
            sortable: true
          },
          {
            title: '变更时间',
            key: 'time',
            sortable: true
          },
          {
            title: '变更原因',
            key: 'reason',
            sortable: true
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
      goDown () {
        window.location.href = BASE.base + 'familyChange/export?keyword=' + this.keyword
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
