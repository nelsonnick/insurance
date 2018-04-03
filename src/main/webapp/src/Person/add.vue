<template>
  <div>
    <Layout class="layout">
      <Header>
        <Menu mode="horizontal" theme="dark" active-name="person" @on-select="MenuClick">
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
              <BreadcrumbItem>困难人员</BreadcrumbItem>
              <BreadcrumbItem>新增</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="6">&nbsp;</Col>
        <Col span="12">
        <Form :label-width="100" :model="person"  ref="Form">
          <Form-item size="large" label="申请类别" required>
            <Cascader size="large" :data="type" v-model="tid" style="width: 600px" clearable="false"></Cascader>
          </Form-item>
          <Form-item label="证件号码"  prop="numberValidate" required>
            <Input size="large" v-model="number" placeholder="请输入身份证号码" style="width: 600px" maxlength="18"></Input>
          </Form-item>
          <Form-item label="人员姓名" prop="nameValidate" required>
            <Input size="large" v-model="name" placeholder="请输入姓名" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="联系电话" prop="phoneValidate" required>
            <Input size="large" v-model="phone" placeholder="请输入联系电话" style="width: 600px" maxlength="11"></Input>
          </Form-item>
          <Form-item label="联系地址" prop="addressValidate" required>
            <Input size="large" v-model="address" placeholder="请输入联系地址" style="width: 600px"></Input>
          </Form-item>
          <Form-item size="large" label="婚姻状况" prop="marriage" required>
            <Radio-group v-model="marriage" size="large"  type="button">
              <Radio label="2">已婚</Radio>
              <Radio label="3">离异</Radio>
              <Radio label="1">未婚</Radio>
              <Radio label="4">丧偶</Radio>
            </Radio-group>
          </Form-item>
          <Form-item size="large" label="延期政策" prop="delay" required>
            <Radio-group v-model="delay" size="large"  type="button">
              <Radio label="0">不符合</Radio>
              <Radio label="1">符合</Radio>
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
    name: 'add',
    data () {
      return {
        userName: window.userName,
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
        remark: ''
      }
    },
    methods: {
      goReset () {
        this.tid = ['1', '1']
        this.number = ''
        this.name = ''
        this.phone = ''
        this.address = ''
        this.marriage = '2'
        this.delay = '0'
        this.remark = ''
      },
      goSave () {
        this.$Loading.start()
        this.$http.get(
          API.Add,
          { params: {
            name: this.name,
            number: this.number,
            phone: this.phone,
            address: this.address,
            tid: this.tid[1],
            delay: this.delay,
            marriage: this.marriage,
            remark: this.remark
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'OK') {
            this.$Loading.finish()
            this.$Message.success('新增成功!')
            this.$Notice.success({
              title: '操作完成!',
              desc: '人员：' + this.name + '已保存！'
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
            title: '服务器内部错误，无法保存人员信息!'
          })
        })
      },
      goBack () {
        this.$router.push({ path: '/list' })
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
