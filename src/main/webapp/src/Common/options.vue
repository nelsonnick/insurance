<template>
  <div>
    尺寸：
    <i-switch v-model="tableSize" style="margin-right: 5px">
      <span slot="open">大</span>
      <span slot="close">小</span>
    </i-switch>
    边框：
    <i-switch size="large" v-model="showBorder" style="margin-right: 5px">
      <span slot="open">有</span>
      <span slot="close">无</span>
    </i-switch>
    斑马纹：
    <i-switch v-model="showStripe" style="margin-right: 5px">
      <span slot="open">有</span>
      <span slot="close">无</span>
    </i-switch>
  </div>
</template>
<script>
  export default {
    name: 'options',
    data () {
      return {
        showBorder: false,
        showStripe: false,
        tableSize: false
      }
    },
    watch: {
      showBorder: function (val) {
        this.$emit('showBorder', val)
      },
      showStripe: function (val) {
        this.$emit('showStripe', val)
      },
      tableSize: function (val) {
        this.$emit('tableSize', val)
      }
    }
  }
</script>
