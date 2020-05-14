define([ "dojo/_base/declare",
         "dojox/form/Uploader",
         "dojo/dom",
         "dojox/form/uploader/FileList",
         "dijit/form/Button",
         "dijit/_Widget",
         "dijit/_TemplatedMixin", 
         "dijit/_WidgetsInTemplateMixin", 
 		 "dojo/text!rca/dataFileUploader/resources/templates/DataFileUploader.html",
 		 "dojo/text!rca/dataFileUploader/resources/templates/UplaodedFileResultTableHead.html",
 		 "dojo/text!rca/dataFileUploader/resources/templates/UplaodedFileResultTableRows.html",
 		 "dojo/_base/lang",
 		 "dojo/dom-construct",
 		 "dojo/dom-style",
 		 "dijit/registry",
 		 "dojo/_base/array",
 		 "dojo/store/Memory", 
 		 "dijit/form/ComboBox",
         "dojo/domReady!",
 		 
 		"dijit/layout/BorderContainer", 
 		"dijit/layout/ContentPane", 
		"dijit/form/TextBox" 
		 			
		], 
		function(declare,Uploader,dom,FileList,Button,_Widget,_TemplatedMixin, _WidgetsInTemplateMixin, 
				template, uplaodedFileResultTableHeadTemplate, uplaodedFileResultTableRowsTemplate, lang, domConstruct, domStyle, registry, array, Memory, ComboBox) {
	
		return declare('rca.dataFileUploader.DataFileUploader',[ _Widget, _TemplatedMixin, _WidgetsInTemplateMixin ], {
			
			baseClass: "dataFileUploader",
			
			widgetId: "dataFileUploader",
			
			templateString : template,
			
			// Override this method to perform custom behavior during dijit construction.
			// Common operations for constructor:
			// 1) Initialize non-primitive types (i.e. objects and arrays)
			// 2) Add additional properties needed by succeeding lifecycle methods
			constructor : function() {
				console.debug("I am inside rca.dataFileUploader.DataFileUploader constructor.");		
			},
			
			
			// When this method is called, all variables inherited from superclasses are 'mixed in'.
			// Common operations for postMixInProperties
			// 1) Modify or assign values for widget property variables defined in the template HTML file
			postMixInProperties : function() {
			},

			startup : function(){
				
				console.debug("Startup Function called.");
				
				var up = dijit.byId(this.selectFilesButton.id);
				
				if(!up)
					{
						up = new Uploader({
				            label: 'Select Files',
				            multiple: true,
				            class:"browseButton",
				            url: "uploadMultipleFile.json",
				            name: "uploadedfile"
			        	}, this.selectFilesButton);
						
				        var successCallback = lang.hitch(this, "processSuccessUpload");
				        
				        dojo.connect(up, "onComplete", successCallback);
				        
				        var onChangeCallback = lang.hitch(this, "onChangeOfDataFileUploader");
				        
				        dojo.connect(up, "onChange", onChangeCallback);
					}

/*		        var list = new FileList({
		            uploader: up,
		        }, this.fileList);*/
		        
				var uploadBtn = dijit.byId(this.submitButton.id);
				
				if(!uploadBtn)
					{
						uploadBtn = new Button({
				            label: 'Upload Selected Files',
				            onClick: function() {		            	
				                up.submit(dom.byId("dataFileUploaderForm"));
				            }
				        }, this.submitButton);
					}
		        
				var resetBtn = dijit.byId(this.clearFileSelection.id);
		        
		        if(!resetBtn)
		        	{
				        resetBtn = new Button({
				            label: 'Reset Selected Files'
				        }, this.clearFileSelection);
				        
				        var resetButtonOnClickCallback = lang.hitch(this, "onResetOfDataFileUploader");
				        
				        dojo.connect(resetBtn, "onClick", resetButtonOnClickCallback);
		        	}
		        
		        /*var dbNameComboBox = dijit.byId(this.dbNameComboBox.id);
		        

		        
		        if(!dbNameComboBox)
		        {
		        	
					var requestBodyToServer = 
					{
						methodName: "getDatabaseListForTheLoggedInCustomer", 
						customerId : ""
					};

					var data = dojo.toJson(requestBodyToServer);
					
					var dbNameComboBoxStoreDataService = {
							postData: data,					
							url: "service/methodCall.do",
							handleAs: "json",
							method: "post",
							preventCache: true,
							headers: { "Content-Type": "application/json"},
							load: successCallback,
							error: errorCallback
					};
							
					dojo.xhrPost(dbNameComboBoxStoreDataService);
					
		        	dbNameComboBox = new ComboBox({
			            name: "dbName",
			            value: "--Select or Enter DB Name--",
			            store: dbNameStore,
			            searchAttr: "name"
			        	}, this.dbNameComboBox);
		        }
		        
		        dbNameComboBox.startup();
		        
		        */
		        
		        uploadBtn.startup();
		        
		        resetBtn.startup();
		        
		       
		        
		        up.startup();
		        
		        //list.startup();
		        
		        this.onResetOfDataFileUploader();
		    	   
			},
			
			onResetOfDataFileUploader: function()
			{
				this.uploadStatus.innerHTML = "";
				
				dijit.byId(this.selectFilesButton.id).reset();

				domStyle.set(dijit.byId(this.clearFileSelection.id).domNode, 'display', 'none');
				
				domStyle.set(dijit.byId(this.submitButton.id).domNode, 'display', 'none');
			},
			
			onChangeOfDataFileUploader: function()
			{
				this.uploadStatus.innerHTML = "";
	        	
				domStyle.set(dijit.byId(this.clearFileSelection.id).domNode, 'display', 'block');
				
				domStyle.set(dijit.byId(this.submitButton.id).domNode, 'display', 'block');				
				
				var fileArray = dijit.byId(this.selectFilesButton.id).getFileList();
				
            	var tableHtml = '<table>' + 
				'<thead>' +
					'<tr>' +
						'<th>#</th>' +
						'<th>Filename</th>' +
						'<th>Type</th>' +
						'<th>Size</th>' +
						'<th>Status</th>' +
					'</tr>'+
				'</thead>'+
				'<tbody id="fileUploadResultTableBodyDiv">';
            	
				array.forEach(fileArray, function(file, i){

			    	tableHtml = tableHtml + 
			    	'<tr class="fileRow" id="fileRow">' +
			        	'<td id="fileNumberFileUpload">' + (i + 1)+'</td>' +
			        	'<td id="filenameFileUpload">' + file.name  +'</td>' +
			        	'<td id="typeFileUpload">' + file.type + '</td>' +
			        	'<td id="sizeFileUpload">' + file.size +'</td>' +
			        	'<td id="sizeFileUpload">Selected</td>'+
			        '</tr>';					                	

				}, this);
				
	            tableHtml = tableHtml +
	            
	            '</tbody>'+
			'</table>';	
	            
	            this.uploadStatus.innerHTML = tableHtml;
			},
			
			processSuccessUpload : function(dataArray)
			{
	            var i = 0;	
	            
	            this.uploadStatus.innerHTML = "";
	            
	            var tableHtml = "";
					
					            if (!dataArray.error)
					            {	
					            	tableHtml = '<table>' + 
													'<thead>' +
														'<tr>' +
															'<th>#</th>' +
															'<th>Filename</th>' +
															'<th>Type</th>' +
															'<th>Size</th>' +
															'<th>Status</th>' +
														'</tr>'+
													'</thead>'+
													'<tbody id="fileUploadResultTableBodyDiv">';
					            
					                for (i = 0; i < dataArray.length; ++i) 
					                {
					                	tableHtml = tableHtml + 
					                	'<tr class="fileRow" id="fileRow">' +
						                	'<td id="fileNumberFileUpload">' + (i + 1)+'</td>' +
						                	'<td id="filenameFileUpload">' + dataArray[i].name  +'</td>' +
						                	'<td id="typeFileUpload">' + dataArray[i].type + '</td>' +
						                	'<td id="sizeFileUpload">' + dataArray[i].size +'</td>' +
						                	'<td id="sizeFileUpload">Uploaded Successfully</td>'+
						                '</tr>';					                	
					                }
										            tableHtml = tableHtml + 
										            '</tbody>'+
												'</table>';	
									
									this.uploadStatus.innerHTML = tableHtml;
					            } 
					            else 
					            {
					            	this.uploadStatus.innerHTML = "Unable to upload the file(s)";
					            }

				
								domStyle.set(dijit.byId(this.clearFileSelection.id).domNode, 'display', 'none');
								
								domStyle.set(dijit.byId(this.submitButton.id).domNode, 'display', 'none');

			},
			
			// postCreate() is called after buildRendering().  This is useful to override when 
			// you need to access and/or manipulate DOM nodes included with your widget.
			// DOM nodes and widgets with the dojoAttachPoint attribute specified can now be directly
			// accessed as fields on "this". 
			// Common operations for postCreate
			// 1) Access and manipulate DOM nodes created in buildRendering()
			// 2) Add new DOM nodes or widgets 
			postCreate : function() {
				
				this.inherited(arguments);
				
			},
	 	
		});
});

