function Scaffold(options) {
	var defaults = { 
		window: null,		
		route: "",
		grid: null
	};
	
	var options = $.extend({}, defaults, options);
	var self = this; 		
	
	this.win = options.window;
	this.route = options.route.replace('/index','');
	this.frm = options.window.find('form');
	this.grid = options.grid;	
	
	this.win.dialog({		
		onOpen : function() {									
			self.frm.find('input').each(function() {																		
		    	if ($(this).attr('type')!= 'hidden') {																				
		        	$(this).focus();					
		        	return false;
		    	}
			});															
		}
	});
			
	this.validate = function(multiple) {							
		var selected = self.grid.datagrid('getSelected');
		var rows = self.grid.datagrid('getSelections');
		var valid = false;

		if (!selected)
			$.messager.alert('Aviso','Nenhum registro foi selecionado', 'warning');
		else if ((multiple) && (rows.length > 1))
			$.messager.alert('Aviso', 'Apenas um registro deve ser selecionado', 'warning');
		else
			valid = true;
			
		return valid;
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
    	self.win.dialog('open');        	
	}
	
	this.edit = function() {		
		if (self.validate(true)) {			
			var url = self.route+"/show/"+self.grid.datagrid('getSelected').id;			
			self.frm.form('reset');
			self.frm.form('load', url);																															 	
			self.win.dialog('open');
		}
	} 
	
	this.remove = function () {		
		$.messager.confirm('Confirmação', 'Confirma exclusão do(s) regsitro(s) selecionado(s)?', function(r){			
			if (r) {
				var rows = self.grid.datagrid('getSelections');				
				for(var i in rows) {							
					$.post(self.route +'/delete/'+rows[i].id, function(data) {
						self.grid.datagrid('reload');
						self.grid.datagrid('clearSelections');			
					});
				}				
			}							
		}); 				
	}		
	
	this.refresh = function() {					
		self.grid.datagrid('reload');
		self.grid.datagrid('clearSelections');
	} 	
	
	this.save = function() {				
		if (self.frm.form('validate')) {			
			var selec = self.grid.datagrid('getSelected');				
			self.frm.form('submit',{
				url: this.route+'/save',
				success:function(data){							
					self.win.window('close');
					self.grid.datagrid('reload');							
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
