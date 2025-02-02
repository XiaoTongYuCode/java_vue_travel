<script>
export default {
  name: "PayStatus",
  data() {
    return {
      success: false,
    };
  },
  created() {
    // 解析URL中的参数
    const query = new URLSearchParams(window.location.search);
    const tradeStatus = query.get('trade_status');

    // 判断支付结果
    if (tradeStatus === 'TRADE_SUCCESS') {
      this.success = true;
    } else {
      this.success = false;
    }
  },
}
</script>
<template>
  <div>
    <h1>支付结果</h1>
    <p v-if="success">支付成功！</p>
    <p v-else>支付失败，请重试。</p>
  </div>
</template>

<style scoped>

</style>