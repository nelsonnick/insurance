<template>
  <Form :model="user" inline>
    <FormItem prop="user">
      <Input type="text" v-model="user.login" placeholder="请输入用户名">
      <Icon type="ios-person-outline" slot="prepend"></Icon>
      </Input>
    </FormItem>
    <FormItem prop="password">
      <Input type="password" v-model="user.pass" placeholder="请输入密码">
      <Icon type="ios-locked-outline" slot="prepend"></Icon>
      </Input>
    </FormItem>
    <FormItem>
      <Button type="primary" @click="goLogin">登录</Button>
    </FormItem>
  </Form>
</template>
<script>
  import * as API from './API.js'
  export default {
    name: 'login',
    data () {
      return {
        user: {
          login: '',
          pass: ''
        }
      }
    },
    methods: {
      goLogin () {
        this.$http.get(
          API.login,
          { params: {
            login: this.user.login,
            pass: this.user.pass
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'OK') {
            this.getLocation
            window.location.href = '/main'
          } else {
            this.$Notice.error({
              title: response.body
            })
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误-无法验证登录信息!'
          })
        })
      },
      getLocation () {
        this.$http.get(
          API.location
        ).then((response) => {
          window.CurrentLocation = response.body.toString().trim()
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误-无法获取当前用户部门!'
          })
        })
      }
    }
  }
</script>
