(function(){
    //add cdn link in there please.
    // 此处添加cdn连接 
    var cdn = {
        jquery:"http://libs.baidu.com/jquery/2.0.0/jquery.min.js",
        san:"https://unpkg.com/san@latest/dist/san.dev.js"
    }
    var cdn_ok = {};
    var cdn_length=0;
    var oknum = 0;
    //===========================
    var all = function (){
        for(var name_ in cdn){
            var sp = document.createElement('script');
            sp.src = cdn[name_];
            document.head.appendChild(sp);
            (function () {
                var names = name_;
                cdn_ok[names] = "on";
                sp.onload = function(){
                    cdn_ok[names] = "ok";
                    oknum++;
                }
            })();
            cdn_length++;
        }
        all = function(){};
    }
    var others = function (x) {
            var sp = document.createElement('script');
            sp.src = cdn[x];
            document.head.appendChild(sp);
            cdn_ok[x] = "no";
            sp.onload = function(){
                cdn_ok[x] = "ok";
            }
    }
    //==================================
    // 此处开放了scope域，在这个域里面，你可以
    window.CDNscope = function(fun,type){
        (type) || (type = 'all');
        if(type == "all"){
            all();
            var timer = setInterval(function () {
                if(cdn_length == oknum){
                    clearInterval(timer);
                    fun();
                }
            },100);
        } else {
            var types = type.split(" ");
            for(var i in types){
                (!cdn_ok[types[i]]) && (others(types[i]));
            }
            var timer = setInterval(function () {
                var m = true;
                for(var i in types){
                    if(cdn_ok[types[i]] != "ok"){m = false;break;}
                }
                (m) && (clearInterval(timer),fun());
            },100);
        }
    }
})()

// 使用方法：  CDNscope( function (){
//                  这里写你的代码
//             }, "这里写你要加载的库，不写或者写all那就默认全部加载" )