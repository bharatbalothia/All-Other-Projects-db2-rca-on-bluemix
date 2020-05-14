define([ "dojo/on", 
         "dojo/dom-style",
         "dojo/_base/array",
         "dojo/_base/declare", 
         "dojo/cache", 
         "dijit/_Widget",
         "dijit/_TemplatedMixin", 
         "dijit/_WidgetsInTemplateMixin", 
         "dojo/text!rca/resources/templates/PerformanceDegradationRootCauseAnalyzer.html", 
         "dojo/_base/lang", 
         "dojo/dom-form" , 
         "dojo/json", 
         "dijit/registry", 
         "dojo/date",
         "dojo/number",
         "dojo/dom-construct",
         "dojo/dom",


		//DoJo Charting Classes
		
		"dojox/charting/Chart",
		"dojox/charting/themes/MiamiNice",
		"dojox/charting/plot2d/Bars",
		"dojox/charting/plot2d/Markers",
		"dojox/charting/axis2d/Default",
		"dojox/charting/plot2d/Lines",
		"dojox/charting/StoreSeries",
		"dojox/charting/widget/SelectableLegend",
		"dojox/charting/action2d/Tooltip",
		"dojo/json",
		"dojo/i18n!nls/UiText",
		"dojo/fx/easing",
		"rca/ioLevelAnalyzer/IoAnalyzer",
		

		
		
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
		function(on, domStyle, array, declare, cache, _Widget, _TemplatedMixin, _WidgetsInTemplateMixin, 
				template, lang, domForm, json, registry, date, number, domConstruct, dom,
				Chart, Theme, Bars, Markers, Default,Lines,StoreSeries,SelectableLegend,Tooltip,JSON,uiText) {
	
		return declare('rca.PerformanceDegradationRootCauseAnalyzer',[ _Widget, _TemplatedMixin,
			_WidgetsInTemplateMixin ], {
		
		templateString : template,
		
		baseClass: "PerformanceDegradationRootCauseAnalyzer",
		
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
			console.debug("Startup Function called.");
	    	   
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
		
		
		fromTimeValueChanged : function(/* event */ event)
		{
//			require(['dojo/dom'], 
					//function(dom){dom.byId('val').value=dom.byId('toDateId').value.toString().replace(/.*1970\s(\S+).*/,'T$1')
				//});
			console.debug("fromDateChanged");	
			
			console.debug(event);
			
				
		},
		
		toTimeValueChanged : function(/* event */ event)
		{
			console.debug("toDateChanged");
			
			console.debug(event);
		},
		
		processServerSuccessResponse : function(jsonData) {
			
			console.debug("Successfully submitted form.");
			
			if(jsonData.error)
			{
				this.showErrorDialog(jsonData.errorMessage);
			}
			else
			{
				
				//*RCA Result DIV*//
				
				var domNode = dojo.byId("rcaDiv");
				
				if(domNode)
				{				
					dojo.empty(domNode);
					
					dojo.destroy(domNode);
				}
				
				
				var rcaDiv = domConstruct.create("div", {id : "rcaDiv", width: "500px" }, this.rcaWidgetWorkspace.domNode, "first");
				
				rcaDiv.innerHTML = jsonData.rcaResultMessage;
				
				//domStyle.set(rcaDiv, {"border": "1px solid black", "font-size": "20px"});
				
				//**//
				
				/// Initial Metrics Rendering Chart starts from here
				
				
				
				this.renderChart(jsonData.dbLevelRcaFinalMetricCollection, "rcaWidgetWorkspaceChildrenLeft");
				
				this.renderChart(jsonData.dbLevelRcaFinalBucketCollection, "rcaWidgetWorkspaceChildrenRight");
				
				this.renderChart(jsonData.dbLevelRcaInitialMetricCollection, "rcaWidgetWorkspaceChildrenLeft");
				
				this.renderChart(jsonData.dbLevelRcaIntialBucketCollection, "rcaWidgetWorkspaceChildrenRight");
				
				
				/*finalMetricCollectionLineChart.connectToPlot("default", function(evt) {
		             console.warn(evt.type, " on element ", evt.element,  " with shape ", evt.shape);
		             console.info("Chart event on default plot!", evt);
		             console.info("Event type is: ", evt.type);
		             console.info("The element clicked was: ", evt.element);
		                        var shape = evt.shape, type = evt.type;
		                          // React to click event
		                        if (type == "onclick") {
		                           alert("onClick event...........");
		                            new dojox.gfx.fx.animateTransform({
		                                duration : 1200,
		                                shape : shape,
		                                transform : [ {
		                                    name : "rotategAt",
		                                    start : [ 0, 240, 240 ],
		                                    end : [ 360, 240, 240 ]
		                                } ]
		                            }).play();
		                            console.log("after rotateFx............");
		                        }
		                        // If it's a mouseover event
		                        else if (type == "onmouseover") {
		                            // Store the original color
		                            if (!shape.originalFill) {
		                                shape.originalFill = shape.fillStyle;
		                            }
		                            // Set the fill color to pink
		                            //`enter code     here`
		                            shape.setFill("pink`enter code here`");
		                        }
		                        // If it's a mouseout event
		                        else if (type == "onmouseout") {
		                            // Set the fill the original fill
		                            shape.setFill(shape.originalFill);
		                        }

		                    });*/
		        
				
				var nextLevelActionCallback = lang.hitch(this, "nextLevelAction");
				
				var nextActionSubmitButton =  new dijit.form.Button({
					label: "Analyze Next.. !",
					onClick: nextLevelActionCallback
				});
				
				domConstruct.place(nextActionSubmitButton.domNode, "rcaWidgetWorkspaceChildrenRight", "last");
				
				
			}
		
		},
		
		destroyDomNode:function(/*String*/ nodeId)
		{
			var domNode = dojo.byId(nodeId);
			
			if(domNode)
			{				
				dojo.empty(domNode);
				
				dojo.destroy(domNode);
			}
				
		},
		
		renderChart : function(/* Metric Collection Object */ metricCollection, /* Container Div */ containerId)
		{
			
			var mcId = metricCollection.id;
			
			// Data Store Preparation
			
			var dataSet = metricCollection.dataSet;
			
			dataSet = JSON.parse(JSON.stringify(dataSet));
			
			/*memoryStore = new dojo.store.Memory({ data: dataStore });*/
			
			// Chart Message Rendering
			
			msgDivId =  mcId + "MsgDiv";
			
			this.destroyDomNode(msgDivId);
			
			var msgDiv = domConstruct.create("div", {id : msgDivId, width: "500px"}, containerId, "last");
			
			msgDiv.innerHTML = metricCollection.message;
						
			// Chart Plotting
			
			chartDivId = mcId + "ChartDiv";
			
			this.destroyDomNode(chartDivId);
			
			var chartDiv = domConstruct.create("div", {id : chartDivId, width: "500px"}, containerId, "last");	
			
			var chart = new dojox.charting.Chart(chartDivId , {
			      title: eval("uiText." + mcId + "ChartTitle"),
			      titlePos: "bottom",
			      titleGap: 10,
			      titleFont: "normal normal normal 15pt 'Segoe UI'",
			      titleFontColor: "black"
			    });
			
			chart.setTheme(Theme);
			
			chart.addPlot("default", { type: "Lines"});		
			
			chart.addAxis("x",{
/*	           labelFunc: function(n) {              
	                if(isNaN(dojo.number.parse(n)) || dojo.number.parse(n) % 1 != 0){
	                    return " ";
	                }
	                else {	                    
	                    var date = new Date(dojo.number.parse(n) * 1000);
	                    return dojo.date.locale.format(date,  {
	                        selector: "time",
	                        datePattern: "HH:mm:ss",
	                        locale: "en"
	                    });
	                }
	            },*/
				
				labels: this.generateChartTimeSeriesLabels(dataSet),
	            rotation: -30,
	            minorTicks : false,	            
			    fixUpper: "major",
			    fixLower:"minor",			    
			    majorTickStep:1,
			    majorTicks:true			    

	        });
						
			chart.addAxis("y", {
				vertical: true,
				title: eval("uiText." + mcId + "ChartYAxisTitle"),
			    min : 0,
			    fixUpper: "major",
			    fixLower:"minor"});
			

			

		    if(dataSet)
		    {
		    	 if (this.getMetricCount(dataSet[0]) > 0)
		    	 {			    	 	
		    		 var metricNameArr = this.getMetricNameArray(dataSet[0]);
		    		 
		    		 for(var i=0; i<metricNameArr.length; i++)
		    		 {
		    			 metricName = metricNameArr[i];		    			 		    			 
		    			 this.addSequenceDataSeries(chart, dataSet, metricName);
		    		 }
		    		 
		    		 this.addSequenceDataSeries(chart, dataSet, "TOTAL_RQST_TIME");

		    	 }
		    }
		    
			
		    
			
		    chart.render();
						
		    // Code for Chart Legend
		    
		    
		    chartLegendDivId = mcId + "ChartLegendDiv";
		    
			domNode = dojo.byId(chartLegendDivId);
			
			if(!domNode)
			{				
				var chartLegendDiv = domConstruct.create("div", {id : chartLegendDivId}, containerId, "last");
				
				new SelectableLegend({
		            chart: chart,
		            horizontal: false
		        }, chartLegendDivId);
				
			}
			else
			{
				dijit.byNode(domNode).chart = chart;
				
				dijit.byNode(domNode).refresh();
				
				domConstruct.place(domNode, chart.node, "after");
			}
		},
		
		generateChartTimeSeriesLabels : function(/* Array of Objects*/ array)
		{
			var n = 0;
			
			var labels = [];
			
	   		for(var i=0; i < array.length; i++)
			{
	   			n = array[i].TS;
	   			
	   			if(isNaN(dojo.number.parse(n)) || dojo.number.parse(n) % 1 != 0)
	   			{
	   				;
	            }
	            else 
	            {	                    
	                var date = new Date(dojo.number.parse(n) * 1000);
	                
	                var formattedDate = dojo.date.locale.format(date,  {
	                    selector: "time",
	                    datePattern: "HH:mm:ss",
	                    locale: "en"
	                });
	                
	                var obj = {value : i+1, text: formattedDate};
	                
	                labels.push(obj);
	            }
			 }
	   		
	   		return labels;

		},
		
		nextLevelAction : function()
		{
			
			var widgetParent = dom.byId("workspacePane");
			
			dojo.empty(widgetParent); 					

			widget = dijit.byId("ioAnalyzer");
			
			if(!widget)
			{
				console.debug("Widget Not present");
				widget = new rca.ioLevelAnalyzer.IoAnalyzer({id: "ioAnalyzer"}).placeAt(widgetParent);				
				widget.startup();
			}						
			else
			{
				console.debug("Widget present");
				widget.placeAt(widgetParent);
			}
			
			/*dijit.byId(this.rcaWidgetWorkspace.id).destroyRecursive(false);
			
			  var cp = new dojox.layout.ContentPane({
			        ioMethod: dojo.xhrPost,
			        ioArgs: {
			            form: formDomNode
			        }
			    }, this.rcaWidgetWorkspace.id);
			    //cp.placeAt("dialogContent");
			    cp.set("href", formDomNode.action);*/ 
		},
		
		
		getMetricCount : function(obj)
		{
			var count = 0;
			
			for (var k in obj) 
			{
				if(k == 'TOTAL_RQST_TIME' || k == 'TS')
					continue;
				
				if (obj.hasOwnProperty(k)) 
				{
				   ++count;
				}
			}

			return count;
		},
		
		getMetricNameArray : function(obj)
		{
			var metricArray = new Array();
			
			for (var k in obj) 
			{
				if(k == 'TOTAL_RQST_TIME' || k == 'TS')
					continue;

				if (obj.hasOwnProperty(k)) 
				{
				   metricArray.push(k);
				}
			}
			
			return metricArray;

		},

		
		addSequenceDataSeries : function(chart, dataSet, sColumnName) {
		    
			var memoryStore = new dojo.store.Memory({ data: dataSet });
			
			var stroeSeries = new dojox.charting.StoreSeries(memoryStore, { query: {} }, sColumnName);
			
			/*chart.addSeries(sColumnName, new dojox.charting.StoreSeries(sequenceDataStore, { query: {} },
		    		{ x: "TS", y: sColumnName }), {stroke: {color: "blue", width: 1, smooth:true}});*/
			
			//chart.addSeries(sColumnName, [{x: 1, y: 5}, {x: 1.5, y: 1.7}, {x: 2, y: 9}, {x: 5, y: 3}]);
			
			chart.addSeries(sColumnName, stroeSeries.data);
		},
			
		processServerErrorResponse : function(){
			console.debug("Failed in form submission.");
		},
		
		
		showErrorDialog : function(/*String*/ errorMessage)
		{
			
			var widget = dijit.byId("errorDialog");
	           
	           if(widget)	
	           {
	        	   widget.destroy();	           
	           }
	           
			//this.closeErrorDialog();
		    var errorDialog = new dijit.Dialog({
		        title: "Sorry!!! There is an error.",
		        style: "width: 300px",
		        id: "errorDialog",
		        hide: function()
		        {
		        	errorDialog.destroy();
		        }
		    });
		    
		    var errorMessageContainer = domConstruct.create("p", {id : "errorMessageContainer"});
		    
		    errorMessageContainer.innerHTML = errorMessage;
		    
		    dojo.place(errorMessageContainer,errorDialog.containerNode,'first');
		    
		    var closeDialogButton = new dijit.form.Button ({label: "Close"});
		    
		    dojo.place(closeDialogButton.domNode,errorDialog.containerNode,'last');
		    
		    on(closeDialogButton,"click", function(){ var widget = dijit.byId("errorDialog"); if(widget) { widget.hide(); }});
		    
		    errorDialog.show();
		},
		
		
		closeErrorDialog : function()
		{
	           var widget = dijit.byId("errorDialog");
	           
	           if(widget)	
	           {
	           		//widget.hide();
	           		
	           		widget.destroy(true);
	           
	           		//dojo.destroy(widget.domNode);
	           }
		},
		
		getDateTime : function(/*Date Widget*/ dateWidget,  /*Time Widget*/ timeWidget)
		{
			var date = dateWidget.displayedValue;

			var time = timeWidget.displayedValue;

			var dateArray = date.split('/');
			
			var timeArray = time.split(':');

			var hours = timeArray[0];

			var minutes = timeArray[1].split(" ")[0];
			
			var ampm = timeArray[1].split(" ")[1];

			if(ampm == "PM")
			{
				hours = parseInt(hours)+12;
			}

			if(parseInt(hours) < 10)
			{
				hours = "0"+hours;
			}

			

			return dateArray[2] + (dateArray[0] < 10 ? '0'+dateArray[0]:dateArray[0]) + (dateArray[1] < 10 ? '0'+dateArray[1]:dateArray[1]) + hours + minutes;

		},
		
/*		resetSession : function() {
		
			var requestBodyToServer = {logout: true};

			var data = dojo.toJson(requestBodyToServer);
			
			var logoutService = {			
					postData : data,
					url: "service/logout.do",					
					method: "post",
					preventCache: true
			};
					
			dojo.xhrPost(logoutService);
		},*/
		
		submitFormAsJson : function () {
			
			var toDateTime = this.getDateTime( dijit.byId("toDateId"), dijit.byId("toTimeId"));
			
			var fromDateTime = this.getDateTime( dijit.byId("fromDateId"), dijit.byId("fromTimeId"));
			
			//var dbNameParam = dijit.byId("dbNameId").value;
			
			var successCallback = lang.hitch(this, "processServerSuccessResponse");
			
			var errorCallback = lang.hitch(this, "processServerErrorResponse");
		
			//var requestBodyToServer = {startDateTime : fromDateTime, endDateTime : toDateTime, dbName : dbNameParam};
			var requestBodyToServer = {startDateTime : fromDateTime, endDateTime : toDateTime};

			var data = dojo.toJson(requestBodyToServer);
			
			var firstAnalysisService = {
					postData: data,					
					url: "service/dbLevelAnalysis.do",
					handleAs: "json",
					method: "post",
					preventCache: true,
					headers: { "Content-Type": "application/json"},
					load: successCallback,
					error: errorCallback
			};
					
			dojo.xhrPost(firstAnalysisService);
							
		}
		
	});
	

    
});