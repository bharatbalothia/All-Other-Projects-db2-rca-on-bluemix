define([ "dojo/on", 
         "dojo/_base/array",
         "dojo/_base/declare", 
         "dojo/cache", 
         "dijit/_Widget",
         "dijit/_TemplatedMixin", 
         "dijit/_WidgetsInTemplateMixin", 
         "dojo/text!rca/ioLevelAnalyzer/resources/templates/IoAnalyzer.html", 
         "dojo/_base/lang", 
         "dojo/dom-form" , 
         "dojo/json", 
         "dijit/registry", 
         "dojo/date",
         "dojo/number",
         "dojo/dom-construct",


		//DoJo Charting Classes
		
		"dojox/charting/Chart",
		"dojox/charting/themes/Claro",
		"dojox/charting/plot2d/Bars",
		"dojox/charting/plot2d/Markers",
		"dojox/charting/axis2d/Default",
		"dojox/charting/plot2d/Lines",
		"dojox/charting/StoreSeries",
		"dojox/charting/widget/SelectableLegend",
		"dojox/charting/action2d/Tooltip",
		"dojo/json",
		"dojo/fx/easing",
		

		
		
		// Other Classes
		
		"dojo/domReady!",
		"dijit/layout/BorderContainer", 
		"dijit/layout/ContentPane", 
		"dijit/form/DateTextBox", 
		"dijit/form/TimeTextBox", 
		"dijit/form/Form", 
		"dijit/form/Button" ,
		"dijit/form/ComboBox",
		"dojo/data/ItemFileWriteStore",		
		"dojo/date/locale",
		"dojo/store/Memory",
		"dijit/Dialog"
		
		
		], 
		function(on, array, declare, cache, _Widget, _TemplatedMixin, _WidgetsInTemplateMixin, 
				template, lang, domForm, json, registry, date, number, domConstruct,
				Chart, Theme, Bars, Markers, Default,Lines,StoreSeries,SelectableLegend,Tooltip,JSON) {
	
		return declare('rca.ioLevelAnalyzer.IoAnalyzer',[ _Widget, _TemplatedMixin,
			_WidgetsInTemplateMixin ], {
		
		templateString : template,
		
		baseClass: "IoAnalyzer",
		
		// Path to the template
		

		// Override this method to perform custom behavior during dijit construction.
		// Common operations for constructor:
		// 1) Initialize non-primitive types (i.e. objects and arrays)
		// 2) Add additional properties needed by succeeding lifecycle methods
		constructor : function() {
			console.debug("I am inside constructor.");		
		},

		// When this method is called, all variables inherited from superclasses are 'mixed in'.
		// Common operations for postMixInProperties
		// 1) Modify or assign values for widget property variables defined in the template HTML file
		postMixInProperties : function() {
		},

		startup : function(){
			console.debug("IoAnalyzer Startup Function called.");
			
			this.submitFormAsJson();

		},
		
		processServerErrorResponse : function(){
			console.debug("Failed in form submission.");
		},
		
		processServerSuccessResponse : function(jsonData) {			
			console.debug("Successfully submitted form.");
		},
		
		submitFormAsJson : function () {

			var successCallback = lang.hitch(this, "processServerSuccessResponse");
			
			var errorCallback = lang.hitch(this, "processServerErrorResponse");
		
			var requestBodyToServer = {analyze : "io"};

			var data = dojo.toJson(requestBodyToServer);
			
			var firstAnalysisService = {
					postData: data,					
					url: "service/ioLevelAnalysis.do",
					handleAs: "json",
					method: "post",
					preventCache: true,
					headers: { "Content-Type": "application/json"},
					load: successCallback,
					error: errorCallback
			};
					
			dojo.xhrPost(firstAnalysisService);
		},
		
		//alert("IoAnalyzer Startup Function called.");
		// postCreate() is called after buildRendering().  This is useful to override when 
		// you need to access and/or manipulate DOM nodes included with your widget.
		// DOM nodes and widgets with the dojoAttachPoint attribute specified can now be directly
		// accessed as fields on "this". 
		// Common operations for postCreate
		// 1) Access and manipulate DOM nodes created in buildRendering()
		// 2) Add new DOM nodes or widgets 
		postCreate : function() {
			
			this.inherited(arguments);
			
		}

	});
	

    
});