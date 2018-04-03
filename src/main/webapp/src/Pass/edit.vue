<template>
  <div>
    <Layout class="layout">
      <Header>
        <Menu mode="horizontal" theme="dark" active-name="pass" @on-select="MenuClick">
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
        <Form :label-width="100" :model="pass"  ref="Form" :rules="ruleValidate">
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
  export default {
    name: 'edit',
    data () {
      return {
        userName: window.userName,
        pass1: '',
        pass2: '',
        ruleValidate: {
          pass1Validate: [
            { required: true, message: '原密码不能为空', trigger: 'blur' }
          ],
          pass2Validate: [
            { required: true, message: '新密码不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      goSave () {
        this.$refs.Form.validate((valid) => {
          if (valid) {
            this.$Loading.start()
            this.$http.get(
              API.Change,
              { params: {
                pass1: this.pass1,
                pass2: this.pass2
              } },
              { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
            ).then((response) => {
              if (response.body === 'OK') {
                this.$Loading.finish()
                this.$Message.success('保存成功!')
                this.$Notice.success({
                  title: '操作完成!',
                  desc: '请重新登录'
                })
                window.location.href = '/'
              } else {
                this.$Loading.error()
                this.$Notice.error({
                  title: response.body
                })
              }
            }, (response) => {
              this.$Loading.error()
              this.$Notice.error({
                title: '服务器内部错误，无法设置新密码!'
              })
            })
          } else {
            this.$Loading.finish()
            this.$Message.error('请核实输入信息!')
          }
        })
      },
      goBack () {
        window.location.href = '/person'
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
