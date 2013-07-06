var scaffold = {
	locale : {
		noRecordsSelectedMsg: "No records selected",
		onlyOneRecordSelectedMsg: "Only one record should be selected",
		removeConfirmationMsg : "Are you sure you want to remove the selected(s) record(s)",
		alertTitle: "Alert",
		confirmTitle: "Confirm"
	},
	route : {
		save: 'save',
		remove: 'delete',
		show: 'show',
		list: 'list'
	}
}

function Scaffold(options) {
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
	
	var options = $.extend({}, defaults, options);
	var self = this; 		
		
	this.win = options.window;
	this.route = options.route.replace('/index','');
	this.frm = options.window.find('form:first');
	this.grid = options.grid;
			
	this.noRecordsSelectedMsg = scaffold.locale.noRecordsSelectedMsg;
	this.onlyOneRecordSelectedMsg = scaffold.locale.onlyOneRecordSelectedMsg;
	this.removeConfirmationMsg = scaffold.locale.removeConfirmationMsg;
	this.alertTitle = scaffold.locale.alertTitle;
	this.confirmTitle = scaffold.locale.confirmTitle;
	
	this.formLoad = function(data) {
		if (data.success === false)
			$.messager.alert(self.alertTitle, data.error, 'error');
		else {
			options.onBeforeEdit(false, data);
			self.win.dialog('open');
			options.onAfterEdit(false);
		}
	}			
	
	this.windowOpen = function() {				
		self.frm.find('input').each(function() {																		
	    	if ($(this).attr('type')!= 'hidden') {																				
	        	$(this).focus();	
	        	$(this).select();
	        	return false;
	    	}
		});		
	}
	
	this.windowClose = function(){
		self.clearErrors();
	}	
		
	this.win.dialog({		
		onOpen : self.windowOpen, 															
		onClose: self.windowClose
	});
	
	this.frm.form({
		onLoadSuccess: self.formLoad
	});
			
	this.validate = function(multiple) {							
		var selected = self.grid.datagrid('getSelected');
		var rows = self.grid.datagrid('getSelections');
		var valid = false;

		if (!selected)
			$.messager.alert(self.alertTitle, self.noRecordsSelectedMsg, 'warning');
		else if ((multiple) && (rows.length > 1))
			$.messager.alert(self.alertTitle, self.onlyOneRecordSelectedMsg, 'warning');
		else
			valid = true;
			
		return valid;
	}
		
	this.clearErrors = function() {
		var div = $('.row-errors:first');
		if (div.length) div.empty();
	}
	
	this.showErrors = function(errors) {
		var div = $('.row-errors:first');
		if (div.length) {						
			var li = "";			
			for (i in errors)
				li += '<li>'+errors[i].message+'</li>';							
			div.append('<ul>'+li+'</ul>');
		}		
	}
		
	this.clear = function () {
		self.frm.find('input,select,textarea').each(function(){			
			var t = this.type, tag = this.tagName.toLowerCase();
			if (t == 'text' || t == 'hidden' || t == 'password' || tag == 'textarea'){
				this.value = '';
			} else if (t == 'file'){
				var file = $(this);
				file.after(file.clone().val(''));
				file.remove();
			} else if (t == 'checkbox' || t == 'radio'){
				this.checked = false;
			} else if (tag == 'select'){
				this.selectedIndex = -1;
			}			
		});
						
		if ($.fn.combo) self.frm.find('.combo-f').combo('clear');
		if ($.fn.combobox) self.frm.find('.combobox-f').combobox('clear');
		if ($.fn.combotree) self.frm.find('.combotree-f').combotree('clear');
		if ($.fn.combogrid) self.frm.find('.combogrid-f').combogrid('clear');		
	}
			
	this.add = function() {																	
		self.clear();		
		options.onBeforeEdit(true, {});
    	self.win.dialog('open');    
    	options.onAfterEdit(true);
	}
	
	this.edit = function() {		
		if (self.validate(true)) {				
			var url = self.route+"/"+ scaffold.route.show +"/"+self.grid.datagrid('getSelected').id;			
			self.frm.form('reset');		
			self.frm.form('load', url);			
		}
	} 
	
	this.remove = function () {		
		if (self.validate(false)) {
			$.messager.confirm(self.confirmTitle, self.removeConfirmationMsg, function(r){			
				if (r) {
					var rows = self.grid.datagrid('getSelections');		
					options.onBeforeRemove(rows);
					for(var i in rows) {							
						$.post(self.route +'/'+ scaffold.route.remove+'/'+rows[i].id, function(data) {
							if (!data.success) {
								$.messager.alert(self.alertTitle, data.error, 'error');
							}
							else {
								self.grid.datagrid('reload');
								self.grid.datagrid('clearSelections');
							}
						});
					}
					options.onAfterRemove();
				}							
			}); 				
		}
	}		
	
	this.refresh = function() {		
		options.onBeforeRefresh();
		self.grid.datagrid('reload');
		self.grid.datagrid('clearSelections');
		options.onAfterRefresh();
	} 	
	
	this.save = function() {				
		if (self.frm.form('validate')) {			
			self.clearErrors();					
			$.post(this.route+'/'+scaffold.route.save, self.frm.serialize(), function(data) {																							
				if (data.success) {			
					options.onAfterSave();
					self.win.window('close');
					self.grid.datagrid('reload');
				}
				else {
					if (data.error)
						$.messager.alert(self.alertTitle, data.error, 'error');
					else
						self.showErrors(data.messages.errors);
				}				
			});
		}		
	}
	
	this.search = function(name, value) {				
		self.grid.datagrid('clearSelections');				
		self.grid.datagrid('options').queryParams = {'field': name, 'value': value};		
		self.grid.datagrid('load');		
	}
}