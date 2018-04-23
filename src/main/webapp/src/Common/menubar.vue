<template>
  <Menu mode="horizontal" theme="dark" :active-name="active" @on-select="MenuClick">
    <div class="layout-nav">
      <MenuItem name="main">
        <Icon type="user"></Icon>
        当前用户：{{ userName }}
      </MenuItem>
      <MenuItem name="personList">
        <Icon type="android-person"></Icon>
        困难人员
      </MenuItem>
      <MenuItem name="familyList">
        <Icon type="android-people"></Icon>
        家庭成员
      </MenuItem>
      <Submenu name="change">
        <template slot="title">
          <Icon type="cube"></Icon>
          变更记录
        </template>
        <MenuItem name="personChange">困难人员</MenuItem>
        <MenuItem name="familyChange">家庭成员</MenuItem>
      </Submenu>
      <MenuItem name="user" v-if="sys">
        <Icon type="ios-people-outline"></Icon>
        用户管理
      </MenuItem>
      <MenuItem name="message">
        <Icon type="chatbubbles"></Icon>
        消息管理
      </MenuItem>
      <MenuItem name="pass">
        <Icon type="android-settings"></Icon>
        修改密码
      </MenuItem>
      <MenuItem name="logout">
        <Icon type="power"></Icon>
        退出系统
      </MenuItem>
    </div>
  </Menu>
</template>
<script>
  import * as BASE from './Base.js'
  export default {
    name: 'menubar',
    props: ['sys', 'active', 'userName'],
    methods: {
      MenuClick (name) {
        if (name.toString() === 'personList') {
          window.location.href = BASE.base + 'person'
        } else if (name.toString() === 'personChange') {
          window.location.href = BASE.base + 'personChange'
        } else if (name.toString() === 'familyList') {
          window.location.href = BASE.base + 'family'
        } else if (name.toString() === 'familyChange') {
          window.location.href = BASE.base + 'familyChange'
        } else if (name.toString() === 'pass') {
          window.location.href = BASE.base + 'pass'
        } else if (name.toString() === 'user') {
          window.location.href = BASE.base + 'user'
        } else if (name.toString() === 'message') {
          window.location.href = BASE.base + 'message'
        } else if (name.toString() === 'logout') {
          window.location.href = BASE.base + 'logout'
        } else if (name.toString() === 'main') {

        } else if (name.toString() === 'person') {

        } else if (name.toString() === 'family') {

        } else {
          window.location.href = BASE.base
        }
      }
    }
  }
</script>
<style scoped>
  .layout-nav {
    width: 1200px;
    margin: 0 auto;
    margin-right: 20px;
  }
</style>
