<template>
  <Menu mode="horizontal" theme="dark" :active-name="active" @on-select="MenuClick">
    <div class="layout-nav">
      <MenuItem name="main">
        <Icon type="user"></Icon>
        当前用户：{{ name }}
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
      <MenuItem name="user" v-if="sys">
        <Icon type="ios-people-outline"></Icon>
        用户管理
      </MenuItem>
      <MenuItem name="logout">
        <Icon type="power"></Icon>
        退出系统
      </MenuItem>
    </div>
  </Menu>
</template>
<script>
  export default {
    name: 'menubar',
    props: ['sys', 'active', 'name'],
    methods: {
      MenuClick (name) {
        if (name.toString() === 'person') {
          window.location.href = '/person'
        } else if (name.toString() === 'family') {
          window.location.href = '/family'
        } else if (name.toString() === 'pass') {
          window.location.href = '/user/pass'
        } else if (name.toString() === 'user') {
          window.location.href = '/user'
        } else if (name.toString() === 'logout') {
          window.location.href = '/logout'
        } else if (name.toString() === 'main') {

        } else {
          window.location.href = '/'
        }
      }
    }
  }
</script>
<style scoped>
  .layout-nav {
    width: 1000px;
    margin: 0 auto;
    margin-right: 20px;
  }
</style>
