<template>
  <div>
    <Layout class="layout">
      <Header>
        <MenuBar :sys="sys" :active="active" :userName="userName"></MenuBar>
      </Header>
      <Row>
        <Col>
          <div>
            <Breadcrumb :style="{margin: '20px 15px 0px 15px'}">
              <BreadcrumbItem>槐荫区就业困难人员管理</BreadcrumbItem>
              <BreadcrumbItem>家庭成员</BreadcrumbItem>
              <BreadcrumbItem>修改</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="6">&nbsp;</Col>
        <Col span="12">
        <Form :label-width="100" :model="family"  ref="Form">
          <Form-item size="large" label="所属中心" required>
            <Select  size="large" v-model="lid" style="width: 600px">
              <Option v-for="item in location" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select >
          </Form-item>
          <Form-item label="人员姓名" required>
            <Input size="large" v-model="name" placeholder="请输入姓名" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="企业微信" required>
            <Input size="large" v-model="weixin" placeholder="请输入企业微信" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="登录名称" required>
            <Input size="large" v-model="login" placeholder="请输入登录名" style="width: 600px"></Input>
          </Form-item>
          <Form-item>
            <Button size="large" type="success" @click="goSave" :disabled="dis">保存</Button>
            <Button size="large" type="warning" style="margin-left: 8px" @click="goReset" :disabled="dis">重置</Button>
            <Button size="large" type="ghost" style="margin-left: 8px" @click="goBack" :disabled="dis">返回</Button>
          </Form-item>
        </Form>
        </Col>
        <Col span="6">&nbsp;</Col>
      </Row>
    </Layout>
  </div>
</template>
<script>
  import * as API from './API.js'
  import MenuBar from '../Common/menubar.vue'
  import axios from 'axios'

  export default {
    name: 'edit',
    components: {MenuBar},
    data () {
      return {
        userName: window.userName,
        sys: window.sys,
        active: 'user',
        dis: false,
        name: '',
        lid: '',
        weixin: '',
        login: '',
        location: []
      }
    },
    created: function () {
      this.getLocation()
      this.fetchData(this.$route.params.id)
    },
    watch: {
      // 如果路由有变化，会再次执行该方法
      '$route': 'fetchData'
    },
    methods: {
      goReset () {
        this.getLocation()
        this.fetchData(this.$route.params.id)
      },
      goSave() {
        this.dis = true
        this.$Loading.start()
        axios.get(API.Edit, {
          params: {
            lid: this.lid,
            name: this.name,
            weixin: this.weixin,
            login: this.login
          }
        }).then(res => {
          if (res.data === 'OK') {
            this.$Loading.finish()
            this.$Message.success('修改成功!')
            this.$Notice.success({
              title: '操作完成!',
              desc: '用户：' + this.name + '已修改！'
            })
            setTimeout(() => {
              this.$router.push({path: '/list'})
            }, 1000)
          } else {
            this.dis = false
            this.$Loading.error()
            this.$Notice.error({
              title: res.data
            })
          }
        }).catch(res => {
          this.dis = false
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误，无法修改用户信息!'
          })
        })
      },
      goBack () {
        this.$router.push({ path: '/list' })
      },
      fetchData (id) {
        axios.get(API.Get,
          { params: { id: id } }
        ).then(res => {
          this.name = res.data.name
          this.lid = res.data.lid.toString()
          this.weixin = res.data.weixin
          this.login = res.data.login
        }).catch(res => {
          this.$Notice.error({
            title: '服务器内部错误，无法获取用户信息!'
          })
        })
      },
      getLocation() {
        axios.get(API.GetLocation).then(res => {
          this.location = eval('(' + res.data + ')')
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      }
    }
  }
</script>
<style scoped>
  .layout {
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }
</style>
