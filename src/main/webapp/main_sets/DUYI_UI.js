var DUYI_UI = {};
DUYI_UI.init_checkbox = function () {
    var node = document.getElementsByClassName("DUYI_checkbox");
    for (var i = 0; i < node.length; i++) {
        (function () {
            var _i = i;
            (node[_i].getAttribute("value")) || (node[_i].setAttribute("value", "选择框"));
            (node[_i].getAttribute("active")) || (node[_i].setAttribute("active", "false"));
            node[_i].innerHTML = node[_i].getAttribute("value");
            node[_i].addEventListener("click", function () {
                if (node[_i].getAttribute("active") == "false") {
                    node[_i].setAttribute("active", "true")
                } else {
                    node[_i].setAttribute("active", "false")
                }
            })
        })();
    }
}

//===============================================================================================================
//渡一功能函数集合
DUYI_UI.tool = {};
DUYI_UI.tool.delCookie = function(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    document.cookie = name + "=" + "" + ";expires=" + exp.toGMTString();
}
DUYI_UI.tool.getCookie = function (name) {
    var text = document.cookie.split('; ');
    var b = "";
    for (var i in text) {
        var b = text[i].split(name + '=');
        if (b.length == 2) {
            return b[1];
        }
    }
    return "";
}
DUYI_UI.tool.setCookie = function (name, value, days) {
    if (days) {
        var exp = new Date();
        exp.setTime(exp.getTime() + days * 24 * 60 * 60 * 1000);
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    } else {
        document.cookie = name + "=" + escape(value);
    }

}













//===============================================================================================================
//渡一组件化开发，下面是组建工厂集合
DUYI_UI.awt = {};



//进度条

// 提示框
DUYI_UI.awt.tipBox = function () {
    var obj = document.createElement("div");
    obj.style.opacity = 0;
    var timer = 0;
    var timer2 = 0;
    var i = 0;
    var _i = 0;
    var fade = function (lifetime, _i, speed, speed2) {
        timer = setInterval(function () {
            _i = _i + 0.01;
            obj.style.opacity = _i;
            if (_i >= 1) {
                clearInterval(timer);
                if (lifetime > 0) {
                    timer2 = setTimeout(function () {
                        i = 1;
                        timer = setInterval(function () {
                            i = i - 0.01;
                            obj.style.opacity = i;
                            (i <= 0) && (obj.remove(), clearInterval(timer));
                        }, speed2);
                    }, lifetime * 1000);
                }
            }
        }, speed);
    }



    obj.show = function (odiv, msg, Oskin) {

    }
}

//信息框
// 使用方法：    var msg = DUYI_UI.awt.msgBox()
//              msg.show(x, y, title, msg, lifetime, addition, Oskin{title: , msg: , closeButton})
DUYI_UI.awt.msgBox = function () {
    var obj = document.createElement("div");
    obj.style.opacity = 0;
    var timer = 0;
    var timer2 = 0;
    var i = 0;
    var _i = 0;
    var fade = function (lifetime, _i, speed, speed2) {
        timer = setInterval(function () {
            _i = _i + 0.05;
            obj.style.opacity = _i;
            if (_i >= 1) {
                clearInterval(timer);
                if (lifetime > 0) {
                    timer2 = setTimeout(function () {
                        i = 1;
                        timer = setInterval(function () {
                            i = i - 0.05;
                            obj.style.opacity = i;
                            (i <= 0) && (obj.remove(), clearInterval(timer));
                        }, speed2);
                    }, lifetime * 1000);
                }
            }
        }, speed);

    }
    obj.show = function (x, y, title, msg, lifetime, addition, Oskin, speed, speed2) {
        // 初始化参数
        (speed) || (speed = 10);
        (speed2) || (speed2 = 10);
        (lifetime) || (lifetime = 5);
        (x) || (x = 10);
        (y) || (y = 10);
        (msg) || (msg = "此人过于慵懒，没有写信息");
        (title) || (title = "来自网站的信息");
        (Oskin) || (Oskin = {
            title: 'color: #fff;padding: 5px 24px;background-color: #222;font-family: monospace;',
            closeButton: 'height: 0px;float: right;background: #f00;cursor: pointer;overflow: hidden;border: 8px #f00 solid;width: 0px;border-radius: 8px;',
            msg: 'padding: 5px 14px;word-wrap: break-word;',
            wrapper: 'background-color:#fff;',
            addition: "",
            drag: true
        });
        (addition) || (addition = "");
        clearInterval(timer);
        clearTimeout(timer2);
        obj.setAttribute('style', "opacity:0;" + Oskin.wrapper);
        obj.className = "DUYI_AWT_msgBox";
        obj.style.left = x + "px";
        obj.style.top = y + "px";

        // title
        obj.innerHTML = ""
        obj.innerHTML += '<div class="DUYI_AWT_TEMP_ID_title" style="' + Oskin.title + '"><span>' + title + '</span><span class="DUYI_AWT_TEMP_ID_close" style="float:right;cursor:pointer;' + Oskin.closeButton + '">关闭</span></div>';
        obj.innerHTML += '<div class="DUYI_AWT_TEMP_ID_msg" style="' + Oskin.msg + '"><span>' + msg + '</span></div>';
        obj.innerHTML += addition;
        obj.innerHTML += Oskin.addition;
        document.body.appendChild(obj);
        // 生命周期
        fade(lifetime, 0, speed, speed2);
        // 事件绑定区域
        obj.getElementsByClassName("DUYI_AWT_TEMP_ID_close")[0].addEventListener('click', function () {
            clearInterval(timer);
            clearTimeout(timer2);
            fade(0.01, 10);
        });
        // 拖动事件
        (function () {
            var press = false;
            var x = 0;
            var y = 0;
            obj.getElementsByClassName("DUYI_AWT_TEMP_ID_title")[0].addEventListener('mousedown', function (e) {
                press = true;
                x = e.pageX - obj.offsetLeft;
                y = e.pageY - obj.offsetTop;
                var move_f = function (e) {
                    if (press && Oskin.drag) {
                        obj.style.left = (e.pageX - x) + 'px';
                        obj.style.top = (e.pageY - y) + 'px';
                    }
                }
                var up_f = function (e) {
                    document.removeEventListener('mousemove', move_f);
                    document.removeEventListener('mouseup', up_f);
                    press = false;
                };
                document.addEventListener('mousemove', move_f);
                document.addEventListener('mouseup', up_f);
            });
        })();
        // 手机端
        (function () {
            var press = false;
            var x = 0;
            var y = 0;
            obj.getElementsByClassName("DUYI_AWT_TEMP_ID_title")[0].addEventListener('touchstart', function (e) {
                press = true;
                x = e.changedTouches[0].pageX - obj.offsetLeft;
                y = e.changedTouches[0].pageY - obj.offsetTop;
                var move_f = function (e) {
                    if (press && Oskin.drag) {
                        obj.style.left = (e.changedTouches[0].pageX - x) + 'px';
                        obj.style.top = (e.changedTouches[0].pageY - y) + 'px';
                    }
                }
                var up_f = function (e) {
                    document.removeEventListener('touchmove', move_f);
                    document.removeEventListener('touchend', up_f);
                    press = false;
                };
                document.addEventListener('touchmove', move_f);
                document.addEventListener('touchend', up_f);
            });
        })();
    }
    // 皮肤常量
    obj.skin = {
        duyiBlack: {
            title: 'color: #fff;padding: 5px 24px;background-color: #222;font-family: monospace;',
            closeButton: 'height: 0px;float: right;background: #f00;cursor: pointer;overflow: hidden;border: 8px #f00 solid;width: 0px;border-radius: 8px;',
            msg: 'padding: 5px 14px;word-wrap: break-word;',
            wrapper: 'background-color:#fff',
            addition: "",
            drag: true
        },
        errorTip: {
            title: 'color: #000;padding: 5px 24px;background-color: #f00;font-family: monospace;font-weight:6;',
            closeButton: 'height: 0px;float: right;background: #000;cursor: pointer;overflow: hidden;border: 8px #000 solid;width: 0px;border-radius: 8px;',
            msg: 'padding: 5px 14px;word-wrap: break-word;',
            wrapper: 'background-color:#fff',
            addition: "",
            drag: true
        }
    }
    return obj;
}
