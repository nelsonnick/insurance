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
              <BreadcrumbItem>停止核查</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Form :label-width="120" :model="person" ref="Form">
        <Row>
          <Col span="12">
          <Form-item label="困难人员证件号码" required>
            <Input size="large" v-model="currentNumber" style="width: 400px" disabled></Input>
          </Form-item>
          <Form-item label="困难人员姓名" >
            <Input size="large" v-model="currentName" style="width: 400px" disabled></Input>
          </Form-item>
          <Form-item label="家属证件号码" required>
            <Input size="large" v-model="number" placeholder="请输入证件号码" style="width: 400px" disabled></Input>
          </Form-item>
          <Form-item label="家属姓名" required>
            <Input size="large" v-model="name" placeholder="请输入姓名" style="width: 400px" disabled></Input>
          </Form-item>
          <Form-item label="联系电话" required>
            <Input size="large" v-model="phone" placeholder="请输入联系电话" style="width: 400px" maxlength="11" disabled></Input>
          </Form-item>
          <Form-item label="停止原因" required>
            <Input v-model="reason" type="textarea" :rows="2" placeholder="请输入停止核查的原因" style="width: 600px"></Input>
          </Form-item>
          </Col>
          <Col span="12">
          <Form-item size="large" label="婚姻状况" prop="marriage" required>
            <Radio-group v-model="marriage" size="large"  type="button">
              <Radio label="2" disabled>已婚</Radio>
              <Radio label="3" disabled>离异</Radio>
              <Radio label="1" disabled>未婚</Radio>
              <Radio label="4" disabled>丧偶</Radio>
            </Radio-group>
          </Form-item>
          <Form-item size="large" label="成员身份" prop="identity" required>
            <Radio-group v-model="identity" size="large"  type="button">
              <Radio label="1" disabled>夫</Radio>
              <Radio label="2" disabled>妻</Radio>
              <Radio label="3" disabled>子</Radio>
              <Radio label="4" disabled>女</Radio>
              <Radio label="5" disabled>父</Radio>
              <Radio label="6" disabled>母</Radio>
              <Radio label="7" disabled>兄弟</Radio>
              <Radio label="8" disabled>姐妹</Radio>
            </Radio-group>
          </Form-item>
          <Form-item label="城镇登记失业时间" required>
            <DatePicker size="large" v-model="timeRegist" type="date" placeholder="包括但不限于失业时间、退休时间、入学时间、入伍时间、判刑时间等" format="yyyy年MM月dd日" style="width: 400px" disabled></DatePicker>
          </Form-item>
          <Form-item label="原(现)工作(学习)单位" required>
            <Input size="large" v-model="company" placeholder="包括但不限于退休、学校、部队、监狱等" style="width: 400px" disabled></Input>
          </Form-item>
          <Form-item label="备注信息">
            <Input v-model="remark" type="textarea" :rows="4" placeholder="请务必录入家属的情况，例如在校学生的毕业时间、入伍时间、法院判决相关信息" style="width: 400px" disabled></Input>
          </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="8">&nbsp;</Col>
          <Col span="8">
          <Form-item>
            <Button size="large" type="success" @click="goSave" :disabled="dis">保存</Button>
            <Button size="large" type="warning" style="margin-left: 8px" @click="goReset" :disabled="dis">重置</Button>
            <Button size="large" type="ghost" style="margin-left: 8px" @click="goBack" :disabled="dis">返回</Button>
          </Form-item>
          </Col>
          <Col span="8">&nbsp;</Col>
        </Row>
      </Form>
    </Layout>
  </div>
</template>
<script>
  import * as API from './API.js'
  import MenuBar from '../Common/menubar.vue'
  import axios from 'axios'

  export default {
    name: 'del',
    components: {MenuBar},
    data () {
      return {
        userName: window.userName,
        sys: window.sys,
        active: 'family',
        dis: false,
        number: '',
        name: '',
        phone: '',
        marriage: '2',
        identity: '1',
        company: '',
        timeRegist: '',
        remark: '',
        reason: ''
      }
    },
    created: function () {
      this.fetchData(this.$route.params.id)
      this.fetchDatas(this.$route.params.id)
    },
    watch: {
      // 如果路由有变化，会再次执行该方法
      '$route': 'fetchData'
    },
    methods: {
      goReset () {
        this.fetchData(this.$route.params.id)
        this.fetchDatas(this.$route.params.id)
        this.reason = ''
      },
      goSave () {
        this.dis = true
        this.$Loading.start()
        axios.get(API.Del, {
          params: {
            id: this.$route.params.id,
            reason: this.reason
          }
        }).then(res => {
          if (res.data === 'OK') {
            this.$Loading.finish()
            this.$Message.success('停止核查成功!')
            this.$Notice.success({
              title: '操作完成!',
              desc: '家属：' + this.name + '已停止核查！'
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
            title: '服务器内部错误，该家属无法停止核查!'
          })
        })
      },
      goBack () {
        this.$router.push({ path: '/list' })
      },
      fetchData (id) {
        axios.get(API.Get, {
          params: {
            id: id
          }
        }).then(res => {
          this.number = res.data.number
          this.name = res.data.name
          this.phone = res.data.phone
          this.identity = res.data.identity + ''
          this.marriage = res.data.marriage + ''
          this.timeRegist = res.data.timeRegist
          this.company = res.data.company + ''
          this.remark = res.data.remark
        }).catch(res => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      fetchDatas (id) {
        axios.get(API.GetPerson, {
          params: {
            id: id
          }
        }).then(res => {
          this.currentNumber = res.data.number
          this.currentName = res.data.name
        }).catch(res => {
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
