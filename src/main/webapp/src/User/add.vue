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
              <BreadcrumbItem>用户管理</BreadcrumbItem>
              <BreadcrumbItem>新增</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="6">&nbsp;</Col>
        <Col span="12">
        <Form :label-width="100" :model="user"  ref="Form">
          <Form-item size="large" label="所属中心" required>
            <Select  size="large" v-model="lid" style="width: 600px" clearable="false">
              <Option v-for="item in location" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select >
          </Form-item>
          <Form-item label="用户姓名" required>
            <Input size="large" v-model="name" placeholder="请输入姓名" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="企业微信" required>
            <Input size="large" v-model="weixin" placeholder="请输入企业微信" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="登录名称" required>
            <Input size="large" v-model="login" placeholder="请输入登录名" style="width: 600px"></Input>
          </Form-item>
          <Form-item>
            <Button size="large" type="success" @click="goSave">保存</Button>
            <Button size="large" type="warning" style="margin-left: 8px" @click="goReset">重置</Button>
            <Button size="large" type="ghost" style="margin-left: 8px" @click="goBack">返回</Button>
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
    name: 'add',
    components: {MenuBar},
    data () {
      return {
        userName: window.userName,
        sys: window.sys,
        active: 'user',
        name: '',
        lid: '1',
        weixin: '',
        login: '',
        location: []
      }
    },
    created: function () {
      this.getLocation()
    },
    methods: {
      goReset () {
        this.name = ''
        this.weixin = ''
        this.login = ''
        this.lid = '1'
      },
      goSave() {
        this.$Loading.start()
        axios.get(API.Add, {
          params: {
            lid: this.lid,
            name: this.name,
            weixin: this.weixin,
            login: this.login
          }
        }).then(res => {
          if (res.data === 'OK') {
            this.$Loading.finish()
            this.$Message.success('新增成功!')
            this.$Notice.success({
              title: '操作完成!',
              desc: '用户：' + this.name + '已保存！'
            })
            setTimeout(() => {
              this.$router.push({path: '/list'})
            }, 1000)
          } else {
            this.$Loading.error()
            this.$Notice.error({
              title: res.data
            })
          }
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误，无法保存用户信息!'
          })
        })
      },
      goBack () {
        this.$router.push({ path: '/list' })
      },
      getLocation() {
        axios.get(API.getLocation).then(res => {
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
