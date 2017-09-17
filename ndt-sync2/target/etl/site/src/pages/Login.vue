<template>
  <row>
    <column class="col-md-4">

    </column>
    <column class="col-md-4">
      <va-box
        title="农贷通运维子系统"
        theme="box-info"
        :isBorder="true"
        widgetType="">

        <div slot="content">


          <form class="form-horizontal">

            <va-input
              :isHorizontal="true"
              title="姓名"
              placeholder="请输入您的姓名"
              type="text"
              vaId="horizontal-email"
              >

            </va-input>

            <va-input
              :isHorizontal="true"
              title="密码"
              placeholder="请输入您的登陆密码"
              type="password"
              vaId="horizontal-email"
              >

            </va-input>

          </form>

        </div>

        <div slot="footer">

          <button class="btn btn-primary" type="submit" @click="goSample">登   陆</button>
          <va-button :isBlock="false" theme="info" align="right" name="忘记密码"></va-button>

        </div>

      </va-box>

    </column>

    <column class="col-md-4">
    
    </column>
  </row>
</template>

<script>
import VABox from '../widgets/VABox'
import VAInput from '../components/VAInput'
import VAButton from '../components/VAButton'

export default {
  name: 'login',
  data () {
    return {
    }
  },
  created () {
  },
  components: {
    'va-box': VABox,
    'va-input': VAInput,
    'va-button': VAButton
  },
  methods: {
    goSample: function () {
      this.$router.push('/hello')
    }
  }
}

</script>
