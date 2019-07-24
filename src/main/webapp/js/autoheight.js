// 增加 textarea 自动高度实现
define(['jquery'],function ($) {
     $.fn.autoHeight = function(){
        function autoHeight(elem){
            elem.style.height = 'auto';
            elem.scrollTop = 0; //防抖动
            elem.style.height = elem.scrollHeight + 'px';
        }
        this.each(function(){
            autoHeight(this);
            $(this).on('keyup', function(){
                autoHeight(this);
            });
            $(this).on('change',function () {
                autoHeight(this);
            })
        });
    }
})