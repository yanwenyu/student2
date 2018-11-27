CDNscope(function () {
    //全局接口,可配置
    var dark = {
        login: 'http://192.168.124.36:8080/adminLogin',
        findAll: 'http://192.168.124.36:8080/adm/findAll',
        register: 'http://192.168.124.36:8080/register',
        you: false,
        login_: {
            ajaxSuccess_findAll: function (e) { console.log(e) },
            ajaxSuccess_login: function (e) { console.log(e) },
            validatorSuccess: function (e) { console.log(e) }
        },
        register_: {
            ajaxSuccess: function (e) { console.log(e) },
            validatorSuccess: function (e) { console.log(e) }
        }
    }

    // postData是提交时的数据，通过type可以判断是注册还是登陆
    var postData = { type: "" };
    var msgBox = DUYI_UI.awt.msgBox();
    // 全局开关
    var validatorOk = false;
    var i;
    // ==========================================
    // 临时函数：增加Input框用
    // ==========================================
    function Addinput(id, value) {
        if (id && value) {
            var code = '<input type="text" name="ant" id="' + id + '" class="DUYI_input textinput" value="' + value + '"><div></div>';
            $("#inputarea").html($("#inputarea").html() + code);
        } else {
            $("#inputarea").html("");
        }
    }
    function insert(value, invalue, insection){
        var arr = ['',''];
        arr[0] = value.slice(0,insection);
        arr[1] = value.slice(insection,value.length);
        
        return arr[0]+invalue+arr[1];
    }
    function indel(value, start, end){
        var arr = ['',''];
        arr[0] = value.slice(0,start);
        arr[1] = value.slice(end,value.length);
        return arr[0]+arr[1];
    }
    // ===========================================
    // 临时函数：验证判定的临时函数集合
    // ===========================================
    function validators(type, value, callback) {
        (value) || (value = "");
        if (type == 'account') {
            var i = value.match(/[^0-9a-zA-Z]+/g);
            if (i && i.length > 0) {
                validatorOk = false;
                callback("用户名只能是英文和数字。");
            }
            if (value.length < 4 || value.length > 12) {
                validatorOk = false;
                callback("用户名必须大于3个字符，小于13个字符");
            }
        }
        if (type == 'password') {
            if (value.length < 6 || value.length > 12) {
                validatorOk = false;
                callback("密码必须大于5个字符，小于13个字符");
            }
        }
        if (type == 'email') {
            var i = value.match(/[0-9a-zA-Z]+@[0-9a-zA-Z]+\.[0-9a-zA-Z]+/g);
            (i) || (callback("请正确输入您的邮箱。"), validatorOk = false);
        }
    }
    //判断当前是什么方式（注册或者登陆）
    if (location.hash == "#login") {
        (function () {
            //设置初始值
            postData = {
                type: 'login',
                account: '',
                password: '',
                remeber: false
            }
            // 渲染页面
            function render() {
                Addinput();
                Addinput("username", "用户名");
                Addinput("password", "密  码");
                $(".layer.l1").show();
                $(".layer.l2").show();
                $(".layer.l3").show();
                $(".layer.l1").html('<div class="DUYI_DIV DUYI_checkbox" style="float:left;" value="记住密码">记住密码</div><div class="DUYI_DIV" style="float:right; cursor: pointer;">忘记密码</div>');
                $(".layer.l2").html('<div class="DUYI_ICON qq" style="float: left; cursor: pointer; background-image: url(../icon/qq.png)"></div><div class="DUYI_ICON wx" style="float:left; cursor: pointer; background-image: url(../icon/微信.png);margin-left: 16px;"></div><div class="DUYI_button" style="float:right;">登陆</div>');
                $(".layer.l3").html(' <div class="DUYI_DIV" style="float:left;">可以使用以上方式登陆|注册</div><div class="DUYI_DIV zhuche" style="float: right; cursor: pointer;">账号注册</div>');

            }
            render();
            // 绑定用户名框事件=============================================
            function blind_username() {
                var temp = document.querySelector("#username");
                temp.onfocus = function (e) {
                    this.select();
                }
                temp.onchange = function (e) {
                    postData.account = this.value;
                }
                if (DUYI_UI.tool.getCookie("username") != "") {
                    postData.account = DUYI_UI.tool.getCookie("username");
                    temp.value = DUYI_UI.tool.getCookie("username");
                }
            }
            blind_username();
            // 绑定密码框事件===========================================================
            function blind_password() {
                var temp = document.querySelector("#password");
                if(DUYI_UI.tool.getCookie("password") != ""){
                    postData.password = DUYI_UI.tool.getCookie("password");
                    temp.value = "";
                    for (var i = 0; i < postData.password.length; i++) {
                        temp.value += "*";
                    }
                }
                temp.onfocus = function (e) {
                    this.select();
                }
                temp.onkeyup = function () {
                    this.value = "";
                    for (var i = 0; i < postData.password.length; i++) {
                        this.value += "*";
                    }
                }
                temp.onkeypress = function (e) {
                    (e.key) || (e.key = String.fromCharCode(e.charCode));
                    postData.password = indel(postData.password,this.selectionStart,this.selectionEnd);
                    postData.password = insert(postData.password,e.key,this.selectionStart);
                }
                temp.onkeydown = function (e) {
                    if (e.key == "Backspace" || e.keyIdentifier == "U+0008") {
                        if(this.selectionStart == this.selectionEnd){
                            postData.password = indel(postData.password,this.selectionStart-1,this.selectionEnd);
                        }else{
                            postData.password = indel(postData.password,this.selectionStart,this.selectionEnd);
                        }
                    }
                }
                temp.onpaste = function(){
                    return false;
                }
            }
            blind_password();


            // 绑定记住密码复选框事件========================================
            function blind_remember() {
                var temp = document.querySelector(".DUYI_DIV.DUYI_checkbox");
                temp.onclick = function () {
                    if (this.getAttribute("active") == "false") {
                        postData.remeber = "true";
                    } else {
                        postData.remeber = "false";
                    }
                }
                if (DUYI_UI.tool.getCookie("remeber") != "") {
                    postData.remeber = 'true';
                    temp.setAttribute("active", "true");
                }
            }
            blind_remember();

            // 绑定注册事件=============================================
            function blind_regButton() {
                var temp = document.querySelector(".DUYI_DIV.zhuche");
                temp.onclick = function () {
                    location.href = './login.html';
                }
            }
            blind_regButton();
            // 绑定登陆事件==============================================
            function blind_login() {
                var temp = document.querySelector(".DUYI_button");
                temp.onclick = function () {
                    // 检查
                    validatorOk = true;
                    validators('account', postData.account, function (msg) {
                        msgBox.show(document.body.offsetWidth / 2 - 200, document.body.offsetHeight / 2 - 100, "一个和蔼的警告：", msg, 2, "", msgBox.skin.errorTip);
                    });
                    validators('password', postData.password, function (msg) {
                        msgBox.show(document.body.offsetWidth / 2 - 200, document.body.offsetHeight / 2 - 100, "一个和蔼的警告：", msg, 2, "", msgBox.skin.errorTip);
                    });
                    //当检查通过的时候：
                    if (validatorOk) {
                        dark.login_.validatorSuccess(postData);
                        if (postData.remeber == "true") {
                            DUYI_UI.tool.setCookie('username', postData.account);
                            DUYI_UI.tool.setCookie('password', postData.password);
                            DUYI_UI.tool.setCookie("remeber", 'true');
                        } else {
                            DUYI_UI.tool.delCookie('username');
                            DUYI_UI.tool.delCookie('password');
                            DUYI_UI.tool.delCookie("remeber");
                        }
                        if (dark.you == false) {
                            // jsonp种cookie
                            $.ajax({
                                type: 'get',
                                url: dark.login,
                                dataType: 'jsonp',
                                data: postData,
                                success: function (json) {
                                    dark.login_.ajaxSuccess_login(json);
                                    $.ajax({
                                        type: 'get',
                                        url: dark.findAll,
                                        dataType: 'html',
                                        data: postData,
                                        success: function (e) {
                                            dark.login_.ajaxSuccess_findAll(e);
                                        }
                                    });
                                }
                            });
                        } else {
                            $.post(dark.login, postData, function (a, b, c) {
                                dark.login_.ajaxSuccess_login(a);
                                $.post(dark.findAll, postData, function (a, b, c) {
                                    dark.login_.ajaxSuccess_findAll(a);
                                });
                            });
                        }
                    }

                }
            }
            blind_login();
            //=================================================
            //所有组建初始化
            DUYI_UI.init_checkbox();
            // 另一种情况，即注册页面显示
        })();
    } else {
        (function () {
            postData = {
                type: 'reg',
                account: '',
                password: '',
                rePassword: '',
                email: ''
            };
            function render() {
                Addinput();
                Addinput("username", " 用户名");
                Addinput("email", "邮    箱");
                Addinput("password", "密    码");
                Addinput("sure_password", "确认密码");
                $(".layer.l1").hide();
                $(".layer.l2").show();
                $(".layer.l2").html('<div class="DUYI_button" style="float:right; width:100%">注册</div>');
                $(".layer.l3").hide();
            }
            render();
            //用户名输入框事件======================================
            function blind_username() {
                var temp = document.querySelector("#username");
                temp.onfocus = function (e) {
                    this.select();
                }
                temp.onchange = function (e) {
                    postData.account = this.value;
                }
            }
            blind_username();

            //email输入框事件===============================
            function blind_email() {
                var temp = document.querySelector("#email");
                temp.onfocus = function (e) {
                    this.select();
                }
                temp.onchange = function (e) {
                    postData.email = this.value;
                }
            }
            blind_email();


            // 密码框事件===================================================================
            function blind_password() {
                var temp = document.querySelector("#password");
                temp.onfocus = function (e) {
                    this.select();
                }
                temp.onkeyup = function () {
                    this.value = "";
                    for (var i = 0; i < postData.password.length; i++) {
                        this.value += "*";
                    }
                }
                temp.onkeypress = function (e) {
                    postData.password = indel(postData.password,this.selectionStart,this.selectionEnd);
                    (e.key) || (e.key = String.fromCharCode(e.charCode));
                    postData.password = insert(postData.password,e.key,this.selectionStart);
                    
                }
                temp.onkeydown = function (e) {
                    if (e.key == "Backspace" || e.keyIdentifier == "U+0008") {
                        if(this.selectionStart == this.selectionEnd){
                            postData.password = indel(postData.password,this.selectionStart-1,this.selectionEnd);
                        }else{
                            postData.password = indel(postData.password,this.selectionStart,this.selectionEnd);
                        }
                    }
                }
                temp.onpaste = function(){
                    return false;
                }
            }
            blind_password();


            //确认密码框事件================================================================
            function blind_rePassword() {
                var temp = document.querySelector("#sure_password");
                temp.onfocus = function (e) {
                    this.select();
                }
                temp.onkeyup = function () {
                    this.value = "";
                    for (var i = 0; i < postData.rePassword.length; i++) {
                        this.value += "*";
                    }
                }
                temp.onkeypress = function (e) {
                    (e.key) || (e.key = String.fromCharCode(e.charCode));
                    postData.rePassword = indel(postData.rePassword,this.selectionStart,this.selectionEnd);
                    postData.rePassword = insert(postData.rePassword,e.key,this.selectionStart);
                }
                temp.onkeydown = function (e) {
                    if (e.key == "Backspace" || e.keyIdentifier == "U+0008") {
                        if(this.selectionStart == this.selectionEnd){
                            postData.rePassword = indel(postData.rePassword,this.selectionStart-1,this.selectionEnd);
                        }else{
                            postData.rePassword = indel(postData.rePassword,this.selectionStart,this.selectionEnd);
                        }
                    }
                }
                temp.onpaste = function(){
                    return false;
                }
            }
            blind_rePassword();

            // 绑定注册事件=====================================
            function blind_register() {
                temp = document.querySelector(".DUYI_button");
                temp.onclick = function () {
                    validatorOk = true;
                    validators('account', postData.account, function (msg) {
                        msgBox.show(document.body.offsetWidth / 2 - 200, document.body.offsetHeight / 2 - 100, "一个和蔼的警告：", msg, 2, "", msgBox.skin.errorTip);
                    });
                    validators('password', postData.password, function (msg) {
                        msgBox.show(document.body.offsetWidth / 2 - 200, document.body.offsetHeight / 2 - 100, "一个和蔼的警告：", msg, 2, "", msgBox.skin.errorTip);
                    });
                    validators('email', postData.email, function (msg) {
                        msgBox.show(document.body.offsetWidth / 2 - 200, document.body.offsetHeight / 2 - 100, "一个和蔼的警告：", msg, 2, "", msgBox.skin.errorTip);
                    });
                    if (postData.password != postData.rePassword) {
                        validatorOk = false;
                        msgBox.show(document.body.offsetWidth / 2 - 200, document.body.offsetHeight / 2 - 100, "一个和蔼的警告：", "两次密码不一致，请重新输入。", 2, "", msgBox.skin.errorTip);
                    }
                    if (validatorOk) {
                        dark.register_.validatorSuccess(postData);
                        if (dark.you == false) {
                            // jsonp种cookie
                            $.ajax({
                                type: 'get',
                                url: dark.register,
                                dataType: 'jsonp',
                                data: postData,
                                success: function (json) {
                                    dark.register_.ajaxSuccess(json);
                                }
                            });
                        } else {
                            $.post(dark.register, postData, function (a, b, c) {
                                dark.register_.ajaxSuccess(a);
                            });
                        }
                    }
                }
            }
            blind_register();

        })();
    }
}, "jquery");