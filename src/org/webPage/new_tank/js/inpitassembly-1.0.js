;(function($, w, document,undefined) {
	var check_ 		  = $("[type='inpit/assembly']"),   // 
		check_class   = check_.attr("name");			// 你必须注意你的选择样式是否与check_class设置正确
		OBJ = {
			check_index   : 0,
			ischeck_	  : true, 							// 是否在设置最少值初始最少选择数量
			ischeck_class : false, 							// 如果已设置，不设置初始值	
		};

	//初始
	+(check_.find("div[radio],div[checkbox]").each(function(){
		var this_ = $(this);
			this_.attr("index",OBJ.check_index);			
				
		// * ===============
		// * radio - 复选
		// * 初始值
		// * ================			
		
		if(this_.attr("radio") != undefined){
			if(OBJ.ischeck_ && this_.attr("min") != undefined){
				//存在min
				var obj = check_.find(" > div").eq(OBJ.check_index);
					if(OBJ.ischeck_class && $(this).find("div[name]").eq(i).hasClass('ack')){
						for(var i = 0 ; i < $(this).find("div[name]").length ; i++){	
							for(var i = 0 ; i < this_.attr("min") ; i++){
							// * ===============
							// * ischeck_class
							// * ischeck_class的作用是在标签中已经加入二个calss="ack"默认值与min="2"情况下，关闭初始值，这防止出现默认选项应该是D/F，而不是变成A/B/D/F
							// * 因此如果ischeck_和ischeck_class同时true将出现多个初始值
							// * 1.若已有默认选项，在inpit/assembly渲染之前，请设置ischeck_= true，ischeck_class = false
							// * 2.若已没有默认选项，设置ischeck_= false，ischeck_class = true
							// * ================
								console.log(obj.eq(i))
								obj.find("> div").eq(i).addClass(check_class)
							}
						}
					}else if(!OBJ.ischeck_class){
						for(var i = 0 ; i < this_.attr("min") ; i++){
							obj.find("> div").eq(i).addClass(check_class)
						}
					}
			}
		}

		// * ===============
		// * checkbox - 单选
		// * 初始值
		// * ================		
		
		if(this_.attr("checkbox") != undefined){
			obj = check_.find(" > div").eq(OBJ.check_index);
			obj.find("> div").eq(0).addClass(check_class)
		}
		++OBJ.check_index;
	}))
	
	//单选
	+(check_.on("click"," [checkbox] div[name]",function() {
		var this_ = $(this),
		// * ===============
		// * 我忘记这是干嘛的
		// * this_siblings = this_.siblings(),  
		// * ================
		this_obj = this_.attr("name"),
		this_ack = check_.find(" *[name=" + this_obj + "]." + check_class);
				
		if( this_ack.length > 0 )
			{ this_ack.removeClass(check_class) }
		
		this_.addClass(check_class);
	}))
	
	//多选
	+(check_.on("click","[radio] div[name]",w.ischeck_ = function(t,max,min) {
		var this_ = $(this),
			this_siblings = this_.siblings(),						
			this_obj = this_siblings.attr("name"),
			this_ack = check_.find(" *[name=" + this_obj + "]." + check_class);
			this_max = (function(){
				var max = this_.parent().attr("max");
				if(max == undefined)
					max = this_.parents().find("div[name]").length + 1;
				return max;
			})();
			this_min = this_.parent().attr("min");
			
		if( this_.hasClass(check_class) ) {
			//至少
			removeClass(this_,check_class);
			if( this_ack.length == this_min ){
				this_.addClass(check_class);
				alert("最小" + this_min + "个");
			}
		}else{
			//最大
			if( this_max != undefined ){
				if(this_ack.length <= this_max){
					this_.addClass(check_class);
					if(this_ack.length == this_max){
						removeClass(this_,check_class);
						alert("最大" + this_max + "个");
					}
				}	
			}
		}
		
		return [this_,this_max,this_min];
	}));
	
	function removeClass(e,selected){
		if( e.attr("class").length <= selected.length )
			e.removeAttr("class")
		else 
			e.removeClass(selected)
	}
	
	// * ===============
	// * 取type='inpit/assembly'内选择值
	// * val则最后结果
	// * ================			
	
	+(w.check_result =  function (obj){
		var val	= [];
		if(obj != undefined){
			$("[type='inpit/assembly'][name=" + obj.parents("[type='inpit/assembly']").attr("name") + "]").find("[index]").each(function(){
				if( $(this).is("[checkbox]") ){
					val.push($(this).find(" *." + check_class).attr("value"));
				}
				if( $(this).is("[radio]") ){
					$(this).find("div." + check_class).each(function(){
						val.push(
							$(this).context.attributes.value.value
						);
					})
				}	
			});			
		}else{
			console.log("向check_result传入$(this)")
		}
		return val
	});
	
    //插件
    $.fn.myDrag = function(options) {
        OBJ = {options}
		return this;
    }	
})(jQuery,window,document)
