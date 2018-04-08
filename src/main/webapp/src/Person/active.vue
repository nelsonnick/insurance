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
              <BreadcrumbItem>激活</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="6">&nbsp;</Col>
        <Col span="12">
        <Form :label-width="100" :model="person"  ref="Form">
          <Form-item size="large" label="申请类别" required>
            <Cascader size="large" :data="type" v-model="tid" style="width: 600px" clearable="false" disabled></Cascader>
          </Form-item>
          <Form-item label="证件号码"  prop="numberValidate" required>
            <Input size="large" v-model="number" placeholder="请输入身份证号码" style="width: 600px" maxlength="18" disabled></Input>
          </Form-item>
          <Form-item label="人员姓名" prop="nameValidate" required>
            <Input size="large" v-model="name" placeholder="请输入姓名" style="width: 600px" disabled></Input>
          </Form-item>
          <Form-item label="联系电话" prop="phoneValidate" required>
            <Input size="large" v-model="phone" placeholder="请输入联系电话" style="width: 600px" maxlength="11" disabled></Input>
          </Form-item>
          <Form-item label="联系地址" prop="addressValidate" required>
            <Input size="large" v-model="address" placeholder="请输入联系地址" style="width: 600px" disabled></Input>
          </Form-item>
          <Form-item size="large" label="婚姻状况" prop="marriage" required>
            <Radio-group v-model="marriage" size="large"  type="button" disabled>
              <Radio label="2">已婚</Radio>
              <Radio label="3">离异</Radio>
              <Radio label="1">未婚</Radio>
              <Radio label="4">丧偶</Radio>
            </Radio-group>
          </Form-item>
          <Form-item size="large" label="延期政策" prop="delay" required>
            <Radio-group v-model="delay" size="large"  type="button" disabled>
              <Radio label="0">不符合</Radio>
              <Radio label="1">符合</Radio>
            </Radio-group>
          </Form-item>
          <Form-item label="备注信息" >
            <Input v-model="remark" type="textarea" :rows="4" placeholder="如有必要，请输入备注信息" style="width: 600px" disabled></Input>
          </Form-item>
          <Form-item label="激活原因" required>
            <Input v-model="reason" type="textarea" :rows="4" placeholder="请输入激活的原因" style="width: 600px"></Input>
          </Form-item>
          <Form-item>
            <Button size="large" type="success" @click="goSave">激活</Button>
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
    name: 'active',
    components: {MenuBar},
    data () {
      return {
        userName: window.userName,
        sys: window.sys,
        active: 'person',
        type: [
          {
            value: '1',
            label: '灵活就业',
            children: [{
              value: '1',
              label: '城镇零就业家庭成员的“4050”失业人员'
            }, {
              value: '2',
              label: '抚养未成年子女单亲家庭成员的 “4050” 失业人员'
            }, {
              value: '3',
              label: '享受城市居民最低生活保障的“4050”失业人员'
            }, {
              value: '4',
              label: '持有《中华人民共和国残疾人证》的 “4050” 失业人员'
            }, {
              value: '5',
              label: '特困家庭未就业的高校毕业生'
            }, {
              value: '6',
              label: '城镇登记失业的成年后孤儿'
            }]
          },
          {
            value: '2',
            label: '公益岗位',
            children: [{
              value: '7',
              label: '城镇零就业家庭成员的“4050”失业人员'
            }, {
              value: '8',
              label: '抚养未成年子女单亲家庭成员的 “4050” 失业人员'
            }, {
              value: '9',
              label: '享受城市居民最低生活保障的“4050”失业人员'
            }, {
              value: '10',
              label: '持有《中华人民共和国残疾人证》的 “4050” 失业人员'
            }, {
              value: '11',
              label: '特困家庭未就业的高校毕业生'
            }, {
              value: '12',
              label: '城镇登记失业的成年后孤儿'
            }]
          },
          {
            value: '3',
            label: '企业吸纳',
            children: [{
              value: '13',
              label: '女性年满40周岁、男性年满50周岁以上且连续失业半年以上的失业人员'
            }, {
              value: '14',
              label: '城镇零就业家庭成员的“4050”失业人员'
            }, {
              value: '15',
              label: '抚养未成年子女单亲家庭成员的失业人员'
            }, {
              value: '16',
              label: '享受城市居民最低生活保障的失业人员'
            }, {
              value: '17',
              label: '特困家庭未就业的高校毕业生'
            }, {
              value: '18',
              label: '持有《中华人民共和国残疾人证》的失业人员'
            }, {
              value: '19',
              label: '城镇登记失业的成年后孤儿'
            }]
          }
        ],
        number: '',
        name: '',
        phone: '',
        address: '',
        tid: ['1', '1'],
        marriage: '2',
        delay: '0',
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
          API.Active,
          { params: {
            id: this.$route.params.id,
            reason: this.reason
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'OK') {
            this.$Loading.finish()
            this.$Message.success('激活成功!')
            this.$Notice.success({
              title: '操作完成!',
              desc: '人员：' + this.name + '已激活！其家庭成员同时被全部激活！'
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
            title: '服务器内部错误，无法激活该人员!'
          })
        })
      },
      goBack () {
        this.$router.push({ path: '/list' })
      },
      getType (number) {
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
      fetchData (id) {
        this.$http.get(
          API.Get,
          { params: { id: id } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.number = response.body.number
          this.name = response.body.name
          this.phone = response.body.phone
          this.address = response.body.address
          this.tid = this.getType(response.body.tid)
          this.delay = response.body.delay + ''
          this.marriage = response.body.marriage + ''
          this.remark = response.body.remark + ''
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
