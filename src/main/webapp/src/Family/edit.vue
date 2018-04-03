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
          <Form-item label="证件号码"  prop="numberValidate" required>
            <Input size="large" v-model="number" placeholder="请输入身份证号码" style="width: 600px" maxlength="18"></Input>
          </Form-item>
          <Form-item label="人员姓名" prop="nameValidate" required>
            <Input size="large" v-model="name" placeholder="请输入姓名" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="联系电话" prop="phoneValidate" required>
            <Input size="large" v-model="phone" placeholder="请输入联系电话" style="width: 600px" maxlength="11"></Input>
          </Form-item>
          <Form-item size="large" label="婚姻状况" prop="marriage" required>
            <Radio-group v-model="marriage" size="large"  type="button">
              <Radio label="2">已婚</Radio>
              <Radio label="3">离异</Radio>
              <Radio label="1">未婚</Radio>
              <Radio label="4">丧偶</Radio>
            </Radio-group>
          </Form-item>
          <Form-item size="large" label="成员身份" prop="identity" required>
            <Radio-group v-model="identity" size="large"  type="button">
              <Radio label="1">夫</Radio>
              <Radio label="2">妻</Radio>
              <Radio label="3">子</Radio>
              <Radio label="4">女</Radio>
              <Radio label="5">父</Radio>
              <Radio label="6">母</Radio>
              <Radio label="7">兄弟</Radio>
              <Radio label="8">姐妹</Radio>
            </Radio-group>
          </Form-item>
          <Form-item label="备注信息" >
            <Input v-model="remark" type="textarea" :rows="4" placeholder="如有必要，请输入备注信息" style="width: 600px"></Input>
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
  export default {
    name: 'edit',
    data () {
      return {
        userName: window.userName,
        number: '',
        name: '',
        phone: '',
        marriage: '2',
        identity: '1',
        remark: ''
      }
    },
    created: function () {
      this.fetchData(this.$route.params.id)
    },
    watch: {
      // 如果路由有变化，会再次执行该方法
      '$route': 'fetchData'
    },
    methods: {
      goReset () {
        this.fetchData(this.$route.params.id)
      },
      goSave () {
        this.$refs.Form.validate((valid) => {
          if (valid) {
            this.$Loading.start()
            this.$http.get(
              API.Edit,
              { params: {
                id: this.$route.params.id,
                name: this.name,
                number: this.number,
                phone: this.phone,
                identity: this.identity,
                marriage: this.marriage,
                remark: this.remark
              } },
              { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
            ).then((response) => {
              if (response.body === 'OK') {
                this.$Loading.finish()
                this.$Message.success('保存成功!')
                this.$Notice.success({
                  title: '操作完成!',
                  desc: '家属：' + this.name + '已保存！'
                })
                setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
              } else {
                this.$Loading.error()
                this.$Notice.error({
                  title: response.body
                })
              }
            }, (response) => {
              this.$Loading.error()
              this.$Notice.error({
                title: '服务器内部错误!'
              })
            })
          } else {
            this.$Loading.finish()
            this.$Message.error('请核实输入信息!')
          }
        })
      },
      goBack () {
        this.$router.push({ path: '/list' })
      },
      fetchData (id) {
        this.$http.get(
          API.Get,
          { params: { id: id } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.number = response.body.number
          this.name = response.body.name
          this.phone = response.body.phone
          this.identity = response.body.identity
          this.marriage = response.body.marriage
          this.remark = response.body.remark
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      numberCheck (rule, value, callback) {
        if (!value) {
          callback()
        } else {
          this.$http.get(
            API.numberCheck,
            { params: {
              number: value
            } },
            { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
          ).then((response) => {
            if (response.body === 'OK') {
              callback()
            } else {
              callback(new Error(response.body.toString()))
            }
          }, (response) => {
            callback(new Error('无法执行后台验证，请重试'))
          })
        }
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
