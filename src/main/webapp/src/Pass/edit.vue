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
              <BreadcrumbItem>密码修改</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="6">&nbsp;</Col>
        <Col span="12">
        <Form :label-width="100" :model="pass"  ref="Form">
          <Form-item label="原密码"  prop="pass1Validate" required>
            <Input size="large" v-model="pass1" placeholder="请输入原密码" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="新密码" prop="pass2Validate" required>
            <Input size="large" v-model="pass2" placeholder="请输入新密码" style="width: 600px"></Input>
          </Form-item>
          <Form-item>
            <Button size="large" type="success" @click="goSave">保存</Button>
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
  import * as BASE from '../Common/Base.js'
  import MenuBar from '../Common/menubar.vue'
  import axios from 'axios'

  export default {
    name: 'edit',
    components: {MenuBar},
    data () {
      return {
        userName: '',
        LocationId: '',
        sys: false,
        active: 'pass',
        pass1: '',
        pass2: ''
      }
    },
    created: function () {
      this.getUser()
    },
    methods: {
      goSave () {
        this.$Loading.start()
        axios.get(API.Change, {
          params: {
            pass1: this.pass1,
            pass2: this.pass2
          }
        }).then(res => {
          if (res.data === 'OK') {
            this.$Loading.finish()
            this.$Message.success('重置密码成功!')
            this.$Notice.success({
              title: '操作完成!',
              desc: '请重新登录'
            })
            setTimeout(() => {
              window.location.href = BASE.base
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
            title: '服务器内部错误，无法设置新密码!'
          })
        })
      },
      goBack () {
        window.location.href = BASE.base + 'person'
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
  .layout {
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }
</style>
