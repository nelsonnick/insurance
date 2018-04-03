<template>
  <div class="query">
    <div class="left">
      <Input type="text" v-model="keyword" placeholder="请输入关键词" style="width:270px;">
      <span slot="prepend">关键词</span>
      </Input>
    </div>
    <div class="right">
      <Button-group>
        <Button type="ghost" @click="goQuery">
          <Icon type="search"></Icon>
          搜索
        </Button>
        <Button type="ghost" @click="goRefresh">
          <Icon type="refresh"></Icon>
          重置
        </Button>
      </Button-group>
    </div>
  </div>
</template>
<script>
  export default {
    data () {
      return {
        keyword: ''
      }
    },
    methods: {
      goQuery () {
        this.$emit('goQuery', this.keyword)
      },
      goRefresh () {
        this.keyword = ''
        this.$emit('goQuery', this.keyword)
      }
    }
  }
</script>
<style>
  .query{
    float:right ;
  }
  .left{
    float:left ;
  }
  .right{
    float:right ;
  }
</style>
