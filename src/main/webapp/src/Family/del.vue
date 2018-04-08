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
              <BreadcrumbItem>注销</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="6">&nbsp;</Col>
        <Col span="12">
        <Form :label-width="100" :model="family"  ref="Form">
          <Form-item label="证件号码"  prop="numberValidate" required>
            <Input size="large" v-model="number" placeholder="请输入身份证号码" style="width: 600px" maxlength="18" disabled></Input>
          </Form-item>
          <Form-item label="人员姓名" prop="nameValidate" required>
            <Input size="large" v-model="name" placeholder="请输入姓名" style="width: 600px" disabled></Input>
          </Form-item>
          <Form-item label="联系电话" prop="phoneValidate" required>
            <Input size="large" v-model="phone" placeholder="请输入联系电话" style="width: 600px" maxlength="11" disabled></Input>
          </Form-item>
          <Form-item size="large" label="婚姻状况" prop="marriage" required>
            <Radio-group v-model="marriage" size="large"  type="button" disabled>
              <Radio label="2">已婚</Radio>
              <Radio label="3">离异</Radio>
              <Radio label="1">未婚</Radio>
              <Radio label="4">丧偶</Radio>
            </Radio-group>
          </Form-item>
          <Form-item size="large" label="成员身份" prop="identity" required>
            <Radio-group v-model="identity" size="large"  type="button" disabled>
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
            <Input v-model="remark" type="textarea" :rows="4" placeholder="如有必要，请输入备注信息" style="width: 600px" disabled></Input>
          </Form-item>
          <Form-item label="注销原因" required>
            <Input v-model="reason" type="textarea" :rows="4" placeholder="请输入注销的原因" style="width: 600px"></Input>
          </Form-item>
          <Form-item>
            <Button size="large" type="success" @click="goSave">注销</Button>
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

  export default {
    name: 'edit',
    components: {MenuBar},
    data () {
      return {
        userName: window.userName,
        sys: window.sys,
        active: 'family',
        number: '',
        name: '',
        phone: '',
        marriage: '2',
        identity: '1',
        remark: '',
        reason: ''
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
        this.reason = ''
      },
      goSave () {
        this.$Loading.start()
        this.$http.get(
          API.Edit,
          { params: {
            id: this.$route.params.id,
            reason: this.reason
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'OK') {
            this.$Loading.finish()
            this.$Message.success('注销成功!')
            this.$Notice.success({
              title: '操作完成!',
              desc: '家属：' + this.name + '已注销！'
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
            title: '服务器内部错误，无法注销该家属!'
          })
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
