<template>
  <div>
    <Layout>
        <Row>
          <Col>
            <div>
              <Breadcrumb :style="{margin: '20px 15px 0px 15px'}">
                <BreadcrumbItem>槐荫区就业困难人员管理</BreadcrumbItem>
                <BreadcrumbItem>享受人员</BreadcrumbItem>
                <BreadcrumbItem>列表</BreadcrumbItem>
              </Breadcrumb>
            </div>
            <div class="left"><Button type="primary" @click="goFamily">切换到家庭人员</Button><Button type="info" @click="goAdd"  v-if="lid">新增</Button><Button type="text" @click="goAdd" >修改密码</Button></div>
            <div class="right"><Search @goQuery="getQuery" ></Search></div>
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
        lid: '',
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
              if (params.row.state.toString() === window.LocationId) {
                operate.push(
                  h('Button', {
                    props: {
                      type: 'info',
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
              if (params.row.state.toString() === window.LocationId) {
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
              return h('div', operate)
            }
          }
        ]
      }
    },
    created: function () {
      this.$http.get(
        API.GetLocation,
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        window.LocationId = response.toString().trim()
        if (response.toString().trim() === '1') {
          this.lid = false
        } else {
          this.lid = true
        }
      }, (response) => {
        this.$Notice.error({
          title: '服务器内部错误，无法获取当前用户所属中心!'
        })
      })
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
        this.$router.push({ path: '/add' })
      },
      goEdit (index) {
        this.$router.push({ path: '/edit/' + this.pageList[index].id })
      },
      goSave (index) {
        this.$router.push({ path: '/save/' + this.pageList[index].id })
      },
      goFamily () {
        window.location.href = '/family'
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
