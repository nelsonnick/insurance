<template>
  <div>
    <div class="Noprint" style="position:absolute; top: 0cm; left:0cm;">
      <p>
        <object id="WebBrowser" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" width="0"></object>
        <button type="button"  class="btn btn-success"  onclick="window.print()">直接打印</button>
        <button type="button"  class="btn btn-default"  onclick="history.go(-1)">返回首页</button>
        <br />
      </p>
    </div>
    <p class="title" align="center">就业困难人员（灵活就业）认定和社会保险补贴申请表</p>
    <div align="center">
      <table border="1" cellspacing="0" cellpadding="0" width="0">
        <tbody>
        <tr>
          <td width="63" height="50px" colspan="2">
            <p align="center">姓名</p>
          </td>
          <td width="94" colspan="2">
            <p align="center">{{name}}</p>
          </td>
          <td width="95" colspan="2">
            <p align="center">性别</p>
          </td>
          <td width="88" colspan="3">
            <p align="center">{{sex}}</p>
          </td>
          <td width="82"><p align="center">身份证号码</p>
          </td>
          <td width="166" colspan="2">
            <p align="center">{{number}}</p>
          </td>
        </tr>
        <tr>
          <td width="63" height="50px" colspan="2">
            <p align="center">失业时间</p>
          </td>
          <td width="189" colspan="4">
            <p align="center">{{timeOut}}</p>
          </td>
          <td width="88" colspan="3">
            <p align="center">城镇登记失业时间</p>
          </td>
          <td width="249" colspan="3">
            <p align="center">{{timeRegist}}</p>
          </td>
        </tr>
        <tr >
          <td width="63" height="50px" colspan="2">
            <p align="center">户籍地址</p>
          </td>
          <td width="189" colspan="4">
            <p align="center">{{address}}</p>
          </td>
          <td width="88" colspan="3">
            <p align="center">失业前所在单位</p>
          </td>
          <td width="249" colspan="3">
            <p align="center">{{company}}</p>
          </td>
        </tr>
        <tr>
          <td width="63" height="100px" colspan="2">
            <p align="center">从事灵活就业的岗位（□划“√”）</p>
          </td>
          <td width="526" colspan="10">
            <p>□ 钟点工&nbsp;□ 早市、夜市摆摊经营&nbsp;□ 街道、社区组织的帮扶&nbsp;</p>
            <p>□ 家庭服务&nbsp;□ 家庭手工业&nbsp;□ 社区公益性劳动&nbsp;□其他就业帮扶</p>
          </td>
        </tr>
        <tr>
          <td width="589" height="50px" colspan="12">
            <p align="center">本人家庭主要成员情况</p>
          </td>
        </tr>
        <tr>
          <td width="57" height="50px">
            <p align="center">姓名</p>
          </td>
          <td width="58" colspan="2">
            <p align="center">与本人关系</p>
          </td>
          <td width="121" colspan="2">
            <p align="center">身份证号码</p>
          </td>
          <td width="31" colspan="2">
            <p align="center">性别</p>
          </td>
          <td width="155" colspan="3">
            <p align="center">原（现）工作（学习）单位</p>
          </td>
          <td width="83">
            <p align="center">城镇登记失业时间</p>
          </td>
          <td width="84">
            <p align="center">备注</p>
          </td>
        </tr>
        <tr>
          <td width="57" height="50px">
            <p align="center">{{name1}}</p>
          </td>
          <td width="58" colspan="2">
            <p align="center">{{identity1}}</p>
          </td>
          <td width="121" colspan="2">
            <p align="center">{{number1}}</p>
          </td>
          <td width="31" colspan="2">
            <p align="center">{{sex1}}</p>
          </td>
          <td width="155" colspan="3">
            <p align="center">{{company1}}</p>
          </td>
          <td width="83">
            <p align="center">{{timeRegist1}}</p>
          </td>
          <td width="84">
            <p align="center"></p>
          </td>
        </tr>
        <tr>
          <td width="57" height="50px">
            <p align="center">{{name2}}</p>
          </td>
          <td width="58" colspan="2">
            <p align="center">{{identity2}}</p>
          </td>
          <td width="121" colspan="2">
            <p align="center">{{number2}}</p>
          </td>
          <td width="31" colspan="2">
            <p align="center">{{sex2}}</p>
          </td>
          <td width="155" colspan="3">
            <p align="center">{{company2}}</p>
          </td>
          <td width="83">
            <p align="center">{{timeRegist2}}</p>
          </td>
          <td width="84">
            <p align="center"></p>
          </td>
        </tr>
        <tr>
          <td width="57" height="50px">
            <p align="center">{{name3}}</p>
          </td>
          <td width="58" colspan="2">
            <p align="center">{{identity3}}</p>
          </td>
          <td width="121" colspan="2">
            <p align="center">{{number3}}</p>
          </td>
          <td width="31" colspan="2">
            <p align="center">{{sex3}}</p>
          </td>
          <td width="155" colspan="3">
            <p align="center">{{company3}}</p>
          </td>
          <td width="83">
            <p align="center">{{timeRegist3}}</p>
          </td>
          <td width="84">
            <p align="center"></p>
          </td>
        </tr>
        <tr>
          <td width="57" height="250px" rowspan="7">
            <p align="center">申请就业困难人员类别（□划“√”）</p>
          </td>
          <td width="283" colspan="8">
            <p align="center">就业困难人员类别</p>
          </td>
          <td width="249" colspan="3">
            <p align="center">证件号码或相关证明</p>
          </td>
        </tr>
        <tr>
          <td width="283" colspan="8">
            <p>□①城镇零就业家庭成员的“4050”失业人员</p>
          </td>
          <td width="249" colspan="3">
            <p></p>
          </td>
        </tr>
        <tr>
          <td width="283" colspan="8">
            <p>□②抚养未成年子女单亲家庭成员的“4050”失业人员</p>
          </td>
          <td width="249" colspan="3">
            <p></p>
          </td>
        </tr>
        <tr>
          <td width="283" colspan="8">
            <p>□③享受城市居民最低生活保障的“4050”失业人员</p>
          </td>
          <td width="249" colspan="3">
            <p></p>
          </td>
        </tr>
        <tr>
          <td width="283" colspan="8">
            <p>□④持有《中华人民共和国残疾人证》的“4050”失业人员</p>
          </td>
          <td width="249" colspan="3">
            <p></p>
          </td>
        </tr>
        <tr>
          <td width="283" colspan="8">
            <p>□⑤特困家庭未就业的高校毕业生</p>
          </td>
          <td width="249" colspan="3">
            <p></p>
          </td>
        </tr>
        <tr>
          <td width="283" colspan="8">
            <p>□⑥城镇登记失业的成年后孤儿</p>
          </td>
          <td width="249" colspan="3">
            <p></p>
          </td>
        </tr>
        <tr>
          <td width="57" height="50px">
            <p align="center">申请登记人签名</p>
          </td>
          <td width="206" colspan="7">
            <p></p>
            <p></p>
          </td>
          <td width="64">
            <p>联系电话</p>
          </td>
          <td width="205" colspan="3">
            <p></p>
          </td>
        </tr>
        <tr>
          <td width="57" height="150px">
            <p align="center">镇（街道）人力资源社会保障服务中心意见</p>
          </td>
          <td width="206" colspan="7">
            <p align="center"></p>
            <p align="center"></p>
            <p align="center"></p>
            <p align="right">（ 盖&nbsp;&nbsp;&nbsp;章 ）</p>
            <p align="center"></p>
            <p align="left">负责人签名：&nbsp;&nbsp;</p>
            <p align="left">经办人签名：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp; 月&nbsp;&nbsp; 日</p>
          </td>
          <td width="64">
            <p align="center">县区劳动就业办公室意见</p>
          </td>
          <td width="205" colspan="3">
            <p align="center"></p>
            <p align="center"></p>
            <p align="center"></p>
            <p align="right">（ 盖&nbsp;&nbsp;&nbsp; 章 ）</p>
            <p align="center"></p>
            <p align="left">负责人签名：</p>
            <p align="left">经办人签名：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp; 月&nbsp;&nbsp;&nbsp;日
            </p>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <p align="center">备注：此表1式2份。镇（街道）人力资源社会保障服务中心留存1份，县区劳动就业办公室留存1份。</p>
  </div>
</template>
<script>
  export default {
    name: 'print',
    data () {
      return {
        userName: ''
      }
    }
  }
</script>
<style scoped>
  p {
    text-align: center;
    font-family: "FangSong_GB2312",serif;
    font-size: 9pt;
  }
  .title {
    font-family: "STZhongsong",serif;
    font-size: 18pt;
  }
  @media print {
    .Noprint {
      display: none;
    }
  }
</style>
