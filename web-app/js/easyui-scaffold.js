var scaffold = {
	locale : {
		noRecordsSelectedMsg: "No records selected",
		onlyOneRecordSelectedMsg: "Only one record should be selected",
		removeConfirmationMsg : "Are you sure you want to remove the selected(s) record(s)",
		alertTitle: "Alert",
		confirmTitle: "Confirm"
	},
	route : {
		create: 'create',
		update: 'update',
		remove: 'delete',
		show: 'show',
		list: 'list'
	}
}

var Scaffold = function (options) {
	this.init(options);
}

Scaffold.prototype = {
		
	constructor: Scaffold,
	
	init : function (options) {				
		var defaults = { 
			window: null,		
			route: "",
			grid: null,
			onBeforeEdit: function(isNewRecord, data){},	    
			onAfterEdit: function(isNewRecord){},
			onBeforeRemove: function(rows){},	    
			onAfterRemove: function(){},
			onBeforeRefresh: function(){},	    
			onAfterRefresh: function(){},
			onBeforeSave: function(param){},	    
			onAfterSave: function(){}
		};
			
		var that = this;
		this.options = $.extend({}, defaults, options);		
		 				
		this.win = options.window;
		this.route = options.route.replace('/index','');
		this.frm = options.window.find('form:first');
		this.grid = options.grid;
		this.isNewRecord = null;
					
		this.noRecordsSelectedMsg = scaffold.locale.noRecordsSelectedMsg;
		this.onlyOneRecordSelectedMsg = scaffold.locale.onlyOneRecordSelectedMsg;
		this.removeConfirmationMsg = scaffold.locale.removeConfirmationMsg;
		this.alertTitle = scaffold.locale.alertTitle;
		this.confirmTitle = scaffold.locale.confirmTitle;
			
		this.formLoad = function(data) {
			if (data.success === false)
				$.messager.alert(this.alertTitle, data.error, 'error');
			else {
				that.options.onBeforeEdit(false, data);
				that.isNewRecord = false;
				that.win.dialog('open');
				that.options.onAfterEdit(false);
			}
		};			
		
		this.windowOpen = function() {				
			that.frm.find('input').each(function() {																		
		    	if ($(this).attr('type')!= 'hidden') {																				
		        	$(this).focus();	
		        	$(this).select();
		        	return false;
			    }
			});		
		};
									
		this.win.dialog({		
			onOpen : that.windowOpen, 															
			onClose: that.windowClose
		});
			
		this.frm.form({
			onLoadSuccess: that.formLoad
		});		
	},
	
	validate: function(multiple) {							
		var selected = this.grid.datagrid('getSelected');
		var rows = this.grid.datagrid('getSelections');
		var valid = false;

		if (!selected)
			$.messager.alert(this.alertTitle, this.noRecordsSelectedMsg, 'warning');
		else if ((multiple) && (rows.length > 1))
			$.messager.alert(this.alertTitle, this.onlyOneRecordSelectedMsg, 'warning');
		else
			valid = true;
			
		return valid;
	},
		
	clearErrors: function() {
		var div = $('.row-errors:first');
		if (div.length) div.empty();
	},
	
	showErrors: function(errors) {
		var div = $('.row-errors:first');
		if (div.length) {						
			var li = "";			
			for (i in errors){				
				console.log(errors[i]);
				li += '<li>'+errors[i].message+'</li>';
			}
			
			div.append('<ul>'+li+'</ul>');
		}		
	},
		
	clear: function () {				
		this.frm.form('clear');												
		if ($.fn.combo) this.frm.find('.combo-f').combo('clear');
		if ($.fn.combobox) this.frm.find('.combobox-f').combobox('clear');
		if ($.fn.combotree) this.frm.find('.combotree-f').combotree('clear');
		if ($.fn.combogrid) this.frm.find('.combogrid-f').combogrid('clear');		
	},
		
	add: function() {											
		this.clear();		
		this.isNewRecord = true;		
		this.options.onBeforeEdit(true, {});		
		this.win.dialog('open');    
		this.options.onAfterEdit(true);
	},
	
	edit: function() {		
		if (this.validate(true)) {						
			var url = this.route+"/"+ scaffold.route.show +"/"+this.grid.datagrid('getSelected').id+'.json';			
			this.frm.form('reset');		
			this.frm.form('load', url);			
		}
	},
	
	remove: function () {		
		if (this.validate(false)) {
			var that = this;
			
			$.messager.confirm(this.confirmTitle, this.removeConfirmationMsg, function(r){			
				if (r) {
					var rows = that.grid.datagrid('getSelections');		
					that.options.onBeforeRemove(rows);
					for(var i in rows) {
						console.log(that.route +'/'+ scaffold.route.remove+'/'+rows[i].id);
						$.post(that.route +'/'+ scaffold.route.remove+'/'+rows[i].id, function(data, status) {							
							if (status != "success") {
								$.messager.alert(this.alertTitle, data.error, 'error');
							}
							else {
								that.grid.datagrid('reload');
								that.grid.datagrid('clearSelections');
							}
						});
					}
					that.options.onAfterRemove();
				}							
			}); 				
		}
	},
	
	save: function() {		
		if (this.frm.form('validate')) {			
			this.clearErrors();
			
			var that = this,
				url = (this.isNewRecord) ? scaffold.route.create : scaffold.route.update,						
				param = {};
						
			this.options.onBeforeSave(param);			
			param = (!$.isEmptyObject(param)) ? '&' + $.param(param, true) : '';		
						
			$.post(this.route+'/'+url+'.json', this.frm.serialize() + param, function(data, status) {				
				if (status == "success") {			
					that.options.onAfterSave();
					that.win.window('close');
					that.grid.datagrid('reload');
				}							
			}).fail(function(xhr, status) {					
				if (xhr.status == 422) {
					that.showErrors($.parseJSON(xhr.responseText).errors);															
				} else {								
					$.messager.alert(that.alertTitle, xhr.responseText, 'error');
				}
					
			});
		}		
	},
	
	refresh: function() {		
		this.options.onBeforeRefresh();
		this.grid.datagrid('reload');
		this.grid.datagrid('clearSelections');
		this.options.onAfterRefresh();
	},
	
	search: function(name, value) {				
		this.grid.datagrid('clearSelections');				
		this.grid.datagrid('options').queryParams = {'field': name, 'value': value};		
		this.grid.datagrid('load');		
	}		
}