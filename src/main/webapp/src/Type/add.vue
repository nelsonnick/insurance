<template>
  <div class="layout">
    <Row>
      <Col>
      <Menu mode="horizontal" theme="dark">
        <div class="layout-nav"></div>
      </Menu>
      </Col>
    </Row>
    <Row>
      <Breadcrumb>
        <Breadcrumb-item>&nbsp;&nbsp;&nbsp;人员</Breadcrumb-item>
        <Breadcrumb-item>新增</Breadcrumb-item>
      </Breadcrumb>
    </Row>
    <Row>
      <Col span="6">&nbsp;</Col>
      <Col span="12">
        <Form :label-width="100" :rules="validate" ref="addForm" :model="object">
          <Form-item size="large" label="申请类别" required>
            <Cascader size="large" :data="type" v-model="object.tid"></Cascader>
          </Form-item>
          <Form-item label="身份证号码" prop="number" required>
            <Input size="large" v-model="object.number" placeholder="请输入身份证号码" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="姓名" prop="name" required>
            <Input size="large" v-model="object.name" placeholder="请输入姓名" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="联系电话" prop="phone" required>
            <Input size="large" v-model="object.phone" placeholder="请输入联系电话" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="联系地址" prop="address" required>
            <Input size="large" v-model="object.address" placeholder="请输入联系地址" style="width: 600px"></Input>
          </Form-item>
          <Form-item size="large" label="婚姻状况" required>
            <Radio-group v-model="object.marriage" type="button">
              <Radio label="已婚">已婚</Radio>
              <Radio label="离异">离异</Radio>
              <Radio label="未婚">未婚</Radio>
              <Radio label="丧偶">丧偶</Radio>
            </Radio-group>
          </Form-item>
          <Form-item size="large" label="延期政策" required>
            <Radio-group v-model="object.delay" type="button">
              <Radio label="不符合">不符合</Radio>
              <Radio label="符合">符合</Radio>
            </Radio-group>
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
  </div>
</template>
<script>
  import * as API from './API.js'
  export default {
    name: 'add',
    data () {
      return {
        number: '',
        name: '',
        phone: '',
        address: '',
        type: [{
          value: ''
        }]
      }
    },
    methods: {
      goReset () {
        this.$refs.addForm.resetFields()
        this.object.name = ''
        this.object.url = '###'
        this.object.type = '素菜'
      },
      goSave () {
        if (this.time1) {
          this.word1 = '早'
        } else {
          this.word1 = ''
        }
        if (this.time2) {
          this.word2 = '午'
        } else {
          this.word2 = ''
        }
        if (this.time3) {
          this.word3 = '晚'
        } else {
          this.word3 = ''
        }
        if (this.time4) {
          this.word4 = '加'
        } else {
          this.word4 = ''
        }
        this.time = this.word1 + this.word2 + this.word3 + this.word4
        if (this.season1) {
          this.words1 = '春'
        } else {
          this.words1 = ''
        }
        if (this.season2) {
          this.words2 = '夏'
        } else {
          this.words2 = ''
        }
        if (this.season3) {
          this.words3 = '秋'
        } else {
          this.words3 = ''
        }
        if (this.season4) {
          this.words4 = '冬'
        } else {
          this.words4 = ''
        }
        this.season = this.words1 + this.words2 + this.words3 + this.words4
        this.$Loading.start()
        this.$Message.info('正在进行保存操作，请稍后...')
        this.$http.get(
          API.save,
          { params: {
            name: this.object.name,
            url: this.object.url,
            type: this.object.type,
            time: this.time,
            season: this.season
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'OK') {
            this.$Loading.finish()
            this.$Notice.success({
              title: '操作完成!',
              desc: '菜谱：' + this.object.name + '已保存！'
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
      },
      goBack () {
        this.$router.push({ path: '/list' })
      }
    }
  }
</script>
<style scoped>
  .layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
  }
  .layout-nav{
    width: 420px;
    margin: 0 auto;
  }
</style>
