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
              <BreadcrumbItem>困难人员</BreadcrumbItem>
              <BreadcrumbItem>注销</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Form :label-width="120" :model="person" ref="Form">
        <Row>
          <Col span="12">
            <Form-item size="large" label="申请类别" required>
              <Cascader size="large" :data="type" v-model="tid" style="width: 400px" clearable="false" disabled></Cascader>
            </Form-item>
            <Form-item label="证件号码" required>
              <Input size="large" v-model="number" placeholder="请输入身份证号码" style="width: 400px" maxlength="18" disabled></Input>
            </Form-item>
            <Form-item label="人员姓名" required>
              <Input size="large" v-model="name" placeholder="请输入姓名" style="width: 400px" disabled></Input>
            </Form-item>
            <Form-item label="联系电话" required>
              <Input size="large" v-model="phone" placeholder="请输入联系电话" style="width: 400px" maxlength="11" disabled></Input>
            </Form-item>
            <Form-item label="联系地址" required>
              <Input size="large" v-model="address" placeholder="请输入联系地址" style="width: 400px" disabled></Input>
            </Form-item>
            <Form-item size="large" label="婚姻状况" prop="marriage" required>
              <Radio-group v-model="marriage" size="large" type="button">
                <Radio label="2" disabled>已婚</Radio>
                <Radio label="3" disabled>离异</Radio>
                <Radio label="1" disabled>未婚</Radio>
                <Radio label="4" disabled>丧偶</Radio>
              </Radio-group>
            </Form-item>
            <Form-item size="large" label="银行卡号">
              <Input size="large" v-model="bank" placeholder="请输入银行卡号" style="width: 400px" disabled></Input>
            </Form-item>
            <Form-item label="注销原因" required>
              <Input v-model="reason" type="textarea" :rows="2" placeholder="请输入注销的原因" style="width: 600px"></Input>
            </Form-item>
          </Col>
          <Col span="12">
            <Form-item size="large" label="工作岗位" required>
              <Select  size="large" v-model="jid" style="width: 400px" disabled>
                <Option v-for="item in job" :value="item.value" :key="item.value">{{ item.label }}</Option>
              </Select >
            </Form-item>
            <Form-item label="失业时间">
              <DatePicker size="large" v-model="timeOut" type="date" placeholder="请选择失业时间" format="yyyy年MM月dd日" style="width: 400px" disabled></DatePicker>
            </Form-item>
            <Form-item label="城镇登记失业时间" required>
              <DatePicker size="large" v-model="timeRegist" type="date" placeholder="请选择城镇登记失业时间" format="yyyy年MM月dd日" style="width: 400px" disabled></DatePicker>
            </Form-item>
            <Form-item label="失业前所在单位">
              <Input size="large" v-model="company" placeholder="请输入失业前所在单位" style="width: 400px" disabled></Input>
            </Form-item>
            <Form-item size="large" label="所属村居">
              <Select  size="large" v-model="cid" style="width: 400px" disabled>
                <Option v-for="item in community" :value="item.value" :key="item.value">{{ item.label }}</Option>
              </Select >
            </Form-item>
            <Form-item size="large" label="延期政策" prop="delay" required>
              <Radio-group v-model="delay" size="large" type="button">
                <Radio label="0" disabled>不符合</Radio>
                <Radio label="1" disabled>符合</Radio>
              </Radio-group>
            </Form-item>
            <Form-item label="备注信息">
              <Input v-model="remark" type="textarea" :rows="4" placeholder="如有必要，请输入备注信息" style="width: 400px" disabled></Input>
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="8">&nbsp;</Col>
          <Col span="8">
            <Form-item>
              <Button size="large" type="success" @click="goSave">注销</Button>
              <Button size="large" type="warning" style="margin-left: 8px" @click="goReset">重置</Button>
              <Button size="large" type="ghost" style="margin-left: 8px" @click="goBack">返回</Button>
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
        active: 'person',
        type: [],
        job: [],
        community: [],
        number: '',
        name: '',
        phone: '',
        address: '',
        timeOut: '',
        timeRegist: '',
        company: '',
        jid: '1',
        tid: ['1', '1'],
        cid: '1',
        marriage: '2',
        delay: '0',
        bank: '',
        remark: '',
        reason: ''
      }
    },
    created: function () {
      this.getType()
      this.getJob()
      this.getCommunity()
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
      goSave() {
        this.$Loading.start()
        axios.get(API.Del, {
          params: {
            id: this.$route.params.id,
            reason: this.reason
          }
        }).then(res => {
          if (res.data === 'OK') {
            this.$Loading.finish()
            this.$Message.success('注销成功!')
            this.$Notice.success({
              title: '操作完成!',
              desc: '人员：' + this.name + '已注销！其家庭成员同时被全部注销！'
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
            title: '服务器内部错误，无法注销人员!'
          })
        })
      },
      goBack () {
        this.$router.push({ path: '/list' })
      },
      getTypeId (number) {
        if (number.toString() === '1') {
          return ['1', '1']
        } else if (number.toString() === '2') {
          return ['1', '2']
        } else if (number.toString() === '3') {
          return ['1', '3']
        } else if (number.toString() === '4') {
          return ['1', '4']
        } else if (number.toString() === '5') {
          return ['1', '5']
        } else if (number.toString() === '6') {
          return ['1', '6']
        } else if (number.toString() === '7') {
          return ['2', '7']
        } else if (number.toString() === '8') {
          return ['2', '8']
        } else if (number.toString() === '9') {
          return ['2', '9']
        } else if (number.toString() === '10') {
          return ['2', '10']
        } else if (number.toString() === '11') {
          return ['2', '11']
        } else if (number.toString() === '12') {
          return ['2', '12']
        } else if (number.toString() === '13') {
          return ['3', '13']
        } else if (number.toString() === '14') {
          return ['3', '14']
        } else if (number.toString() === '15') {
          return ['3', '15']
        } else if (number.toString() === '16') {
          return ['3', '16']
        } else if (number.toString() === '17') {
          return ['3', '17']
        } else if (number.toString() === '18') {
          return ['3', '18']
        } else if (number.toString() === '19') {
          return ['3', '19']
        }
      },
      getType() {
        axios.get(API.GetType).then(res => {
          this.type = eval('(' + res.data + ')')
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      getJob() {
        axios.get(API.GetJob).then(res => {
          this.job = eval('(' + res.data + ')')
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      getCommunity() {
        axios.get(API.GetCommunity).then(res => {
          this.community = eval('(' + res.data + ')')
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      fetchData (id) {
        axios.get(API.Get,
          { params: { id: id } }
        ).then(res => {
          this.number = res.data.number
          this.name = res.data.name
          this.phone = res.data.phone
          this.address = res.data.address
          this.tid = this.getTypeId(res.data.tid)
          this.jid = new Array(res.data.jid)
          this.cid = new Array(res.data.cid)
          this.timeOut = res.data.timeOut
          this.timeRegist = res.data.timeRegist
          this.delay = res.data.delay + ''
          this.marriage = res.data.marriage + ''
          this.bank = res.data.bank + ''
          this.company = res.data.company + ''
          this.remark = res.data.remark + ''
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
