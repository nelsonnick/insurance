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
              <BreadcrumbItem>新增</BreadcrumbItem>
            </Breadcrumb>
          </div>
          </Col>
        </Row>
      <Form :label-width="120" :model="person" ref="Form">
        <Row>
          <Col span="12">
            <Form-item size="large" label="申请类别" required>
              <Cascader size="large" :data="type" v-model="tid" style="width: 400px" clearable="false"></Cascader>
            </Form-item>
            <Form-item label="证件号码" required>
              <Input size="large" v-model="number" placeholder="请输入身份证号码" style="width: 400px" maxlength="18"></Input>
            </Form-item>
            <Form-item label="人员姓名" required>
              <Input size="large" v-model="name" placeholder="请输入姓名" style="width: 400px"></Input>
            </Form-item>
            <Form-item label="联系电话" required>
              <Input size="large" v-model="phone" placeholder="请输入联系电话" style="width: 400px" maxlength="11"></Input>
            </Form-item>
            <Form-item label="联系地址" required>
              <Input size="large" v-model="address" placeholder="请输入联系地址" style="width: 400px"></Input>
            </Form-item>
            <Form-item size="large" label="婚姻状况" prop="marriage" required>
              <Radio-group v-model="marriage" size="large" type="button">
                <Radio label="2">已婚</Radio>
                <Radio label="3">离异</Radio>
                <Radio label="1">未婚</Radio>
                <Radio label="4">丧偶</Radio>
              </Radio-group>
            </Form-item>
            <Form-item size="large" label="银行卡号">
              <Input size="large" v-model="bank" placeholder="请输入银行卡号" style="width: 400px"></Input>
            </Form-item>
          </Col>
          <Col span="12">
            <Form-item size="large" label="从事灵活岗位" required>
              <Cascader size="large" :data="job" v-model="jid" style="width: 400px" clearable="false"></Cascader>
            </Form-item>
            <Form-item label="失业时间">
              <DatePicker size="large" v-model="timeOut" type="date" placeholder="请选择失业时间" format="yyyy年MM月dd日"
                          style="width: 400px"></DatePicker>
            </Form-item>
            <Form-item label="城镇登记失业时间" required>
              <DatePicker size="large" v-model="timeRegist" type="date" placeholder="请选择城镇登记失业时间" format="yyyy年MM月dd日"
                          style="width: 400px"></DatePicker>
            </Form-item>
            <Form-item label="失业前所在单位">
              <Input size="large" v-model="company" placeholder="请输入失业前所在单位" style="width: 400px"></Input>
            </Form-item>
            <Form-item size="large" label="所属社区">
              <Cascader size="large" :data="committees" v-model="cid" style="width: 400px" clearable="false"></Cascader>
            </Form-item>
            <Form-item size="large" label="延期政策" prop="delay" required>
              <Radio-group v-model="delay" size="large" type="button">
                <Radio label="0">不符合</Radio>
                <Radio label="1">符合</Radio>
              </Radio-group>
            </Form-item>
            <Form-item label="备注信息">
              <Input v-model="remark" type="textarea" :rows="4" placeholder="如有必要，请输入备注信息" style="width: 400px"></Input>
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="8">&nbsp;</Col>
          <Col span="8">
          <Form-item>
            <Button size="large" type="success" @click="goSave">保存</Button>
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

  export default {
    name: 'add',
    components: {MenuBar},
    data () {
      return {
        userName: window.userName,
        sys: window.sys,
        active: 'person',
        type: [],
        job: [],
        committees: [],
        number: '',
        name: '',
        phone: '',
        address: '',
        timeOut: new Date(),
        timeRegist: new Date(),
        company: '',
        jid: '1',
        tid: ['1', '1'],
        cid: '1',
        marriage: '2',
        delay: '0',
        bank: '',
        remark: ''
      }
    },
    created: function () {
      this.getType()
      this.getJob()
      this.getCommittees()
    },
    methods: {
      goReset () {
        this.tid = ['1', '1']
        this.jid = '1'
        this.cid = ''
        this.number = ''
        this.name = ''
        this.phone = ''
        this.address = ''
        this.marriage = '2'
        this.delay = '0'
        this.bank = ''
        this.timeOut = new Date()
        this.timeRegist = new Date()
        this.remark = ''
      },
      goSave () {
        this.$Loading.start()
        this.$http.get(
          API.Add,
          {
            params: {
              name: this.name,
              number: this.number,
              phone: this.phone,
              address: this.address,
              tid: this.tid[1],
              jid: this.jid,
              cid: this.cid,
              timeOut: this.timeOut.getTime(),
              timeRegist: this.timeRegist.getTime(),
              delay: this.delay,
              marriage: this.marriage,
              bank: this.bank,
              remark: this.remark
            }
          },
          {headers: {'X-Requested-With': 'XMLHttpRequest'}}
        ).then((response) => {
          if (response.body === 'OK') {
            this.$Loading.finish()
            this.$Message.success('新增成功!')
            this.$Notice.success({
              title: '操作完成!',
              desc: '人员：' + this.name + '已保存！'
            })
            setTimeout(() => {
              this.$router.push({path: '/list'})
            }, 1000)
          } else {
            this.$Loading.error()
            this.$Notice.error({
              title: response.body
            })
          }
        }, (response) => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误，无法保存人员信息!'
          })
        })
      },
      goBack () {
        this.$router.push({path: '/list'})
      },
      getType () {
        this.$http.get(
          API.GetType,
          {headers: {'X-Requested-With': 'XMLHttpRequest'}}
        ).then((response) => {
          this.type = eval('(' + response.bodyText + ')')
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      getJob () {
        this.$http.get(
          API.GetJob,
          {headers: {'X-Requested-With': 'XMLHttpRequest'}}
        ).then((response) => {
          this.job = eval('(' + response.bodyText + ')')
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      getCommittees () {
        this.$http.get(
          API.GetCommittees,
          {headers: {'X-Requested-With': 'XMLHttpRequest'}}
        ).then((response) => {
          this.committees = eval('(' + response.bodyText + ')')
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
