<template>
  <div>
    <Layout>
      <Row>
        <Col>
          <div>
            <Breadcrumb :style="{margin: '20px 15px 0px 15px'}">
              <BreadcrumbItem>槐荫区就业困难人员管理</BreadcrumbItem>
              <BreadcrumbItem>享受人员</BreadcrumbItem>
              <BreadcrumbItem>新增</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="6">&nbsp;</Col>
        <Col span="12">
        <Form :label-width="100" :model="person"  ref="addForm">
          <Form-item size="large" label="申请类别" required>
            <Cascader size="large" :data="type" v-model="person.tid" style="width: 600px"></Cascader>
          </Form-item>
          <Form-item label="证件号码" prop="number" required>
            <Input size="large" v-model="person.number" placeholder="请输入身份证号码" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="人员姓名" prop="name" required>
            <Input size="large" v-model="person.name" placeholder="请输入姓名" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="联系电话" prop="phone" required>
            <Input size="large" v-model="person.phone" placeholder="请输入联系电话" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="联系地址" prop="address" required>
            <Input size="large" v-model="person.address" placeholder="请输入联系地址" style="width: 600px"></Input>
          </Form-item>
          <Form-item size="large" label="婚姻状况" required>
            <Radio-group v-model="person.marriage" type="button">
              <Radio label="2">已婚</Radio>
              <Radio label="3">离异</Radio>
              <Radio label="1">未婚</Radio>
              <Radio label="4">丧偶</Radio>
            </Radio-group>
          </Form-item>
          <Form-item size="large" label="延期政策" required>
            <Radio-group v-model="person.delay" type="button">
              <Radio label="0">不符合</Radio>
              <Radio label="1">符合</Radio>
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
    </Layout>
  </div>
</template>
<script>
  import * as API from './API.js'
  export default {
    name: 'personList',
    data () {
      return {
        person: [{
          number: '',
          name: '',
          phone: '',
          address: '',
          tid: ['1', '1'],
          marriage: '2',
          delay: '0'
        }],
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
        ]
      }
    },
    methods: {
      goReset () {
        this.$refs.addForm.resetFields()
        this.person.name = ''
        this.person.number = ''
        this.person.phone = ''
        this.person.address = ''
        this.person.tid = ['1', '1']
      },
      goSave () {
        this.$Loading.start()
        this.$Message.info('正在进行保存操作，请稍后...')
        this.$http.get(
          API.personSave,
          { params: {
            name: this.person.name,
            number: this.person.number,
            phone: this.person.phone,
            address: this.person.address,
            tid: this.person.tid[1],
            delay: this.person.delay
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'OK') {
            this.$Loading.finish()
            this.$Notice.success({
              title: '操作完成!',
              desc: '人员：' + this.person.name + '已保存！'
            })
            setTimeout(() => { this.$router.push({ path: '/personList' }) }, 1000)
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
        this.$router.push({ path: '/personList' })
      }
    }
  }
</script>
