let ctx = window.location.origin;

axios.defaults.timeout = 60 * 1000;
axios.defaults.withCredentials = true;

axios.interceptors.request.use(function (config) {
    // 在发送请求之前
    config.headers = {};
    config.headers = {
        'Content-Type': 'application/json; charset=utf-8',
        'X-Requested-With': 'XMLHttpRequest'
    };

    if (config.auth) {
        config.headers.token = getToken();
    }

    return config
}, function (err) {
    alert('请求超时');
    return Promise.resolve(err)
});

axios.interceptors.response.use(function (data) {
    if (data.data.code === 401) {
        data.data.msg = "权限不足";
    }

    // 数据统一校验处理
    return data
}, function (err) {
    // 数据异常统一处理
    if (err.response.status === 504 || err.response.status === 404) {
        alert('服务器被吃了');
    } else if (err.response.status === 403) {
        alert('权限不足,请联系管理员');
    } else {
        alert('未知错误');
    }

    return Promise.resolve(err);
});

function get(url, success, error) {
    return axios({
        url: url,
        method: 'get'
    }).then(success).catch(error)
}

function getAuth(url, params, success) {
    return axios({
        url: url,
        auth: true,
        method: 'get',
        data: params
    }).then(success)
}

function post(url, params, success) {
    return axios({
        url: url,
        method: 'post',
        data: params
    }).then(success)
}

function postAuth(url, params, success) {
    return axios({
        url: url,
        auth: true,
        method: 'post',
        data: params
    }).then(success)
}

function del(url, success) {
    return axios({
        url: url,
        method: 'delete'
    }).then(success)
}

function put(url, params, success) {
    return axios({
        url: url,
        method: 'put',
        data: params
    }).then(success)
}

function putAuth(url, params, success) {
    return axios({
        url: url,
        auth: true,
        method: 'put',
        data: params
    }).then(success)
}

function toLoginPage() {
    location.href = ctx + "/well/api/sys/login";
}

function getToken() {
    let token = localStorage.getItem("token");
    if (token) {
        return token;
    } else {
        toLoginPage();
        // 推出调用函数 即父函数
        throw new Error("token is null");
    }
}

function getQueryString(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    // search,查询？后面的参数，并匹配正则
    let regMatch = window.location.search.substr(1).match(reg);

    if (regMatch == null) {
        return null;
    }

    return unescape(regMatch[2]);
}

function getReferer() {
    if (document.referrer) {
        return document.referrer;
    } else {
        return false;
    }
}