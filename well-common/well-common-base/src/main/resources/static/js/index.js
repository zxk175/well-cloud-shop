new Vue({
    el: "#app",
    data: {
        userName: "",
        mobile: ""
    },
    mounted() {
        this.userName = localStorage.getItem("userName") || "未登录";
        this.mobile = localStorage.getItem("mobile") || "未登录";
    },
    methods: {
        clearAuth: function () {
            let that = this;

            that.$confirm('是否退出登录?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                localStorage.removeItem('token');
                Cookies.remove('token');
                let flag = localStorage.getItem("token");
                if (flag === null) {
                    that.$message.success("清除成功");
                    setTimeout(function () {
                        toLoginPage();
                    }, 1000);
                } else {
                    that.$message.error("清除失败");
                }
            }).catch(() => {
                that.$message.info("取消清除操作");
            });
        }
    }
});