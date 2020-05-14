<!DOCTYPE html>
<html >
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" href="resources/js/lib/dijit/themes/claro/claro.css">
	<title>DB2</title>	
	<style type="text/css">
html, body {
    width: 100%;
    height: 100%;
    margin: 0;
}

#borderContainerThree {
    width: 100%;
    height: 100%;
    overflow:hidden;
    border: none;
}
	</style>
	<script type="text/javascript">
		var dojoConfig = {
			
			parseOnLoad:false,		
			// Enable the AMD loader
			async: true,
			// Define the base URL for all of our modules and packages
			baseUrl: 'resources/js/lib',
			// Enable debugging
			isDebug: true,
			// Register the packages we are going to be using. These same packages should be defined in the
			// build profile in `app.profile.js`.
			packages: [
				// Using a string as a package is shorthand for `{ name: 'app', location: 'app' }`		
				{ name: 'rca', location: '../rca' },
				{ name: 'dijit', location: 'dijit' },
				{ name: 'dojo', location: 'dojo' },
				{ name: 'dojox', location: 'dojox' }		
			],

			selectorEngine: 'lite',
			tlmSiblingOfDojo: false
		};
	</script>
	<script type="text/javascript" src="resources/js/lib/dojo/dojo.js"></script>	
	
	<script>
		require([
		    "dojo/parser",
		    "dijit/form/Button",
		    "dijit/layout/ContentPane",
		    "dijit/layout/BorderContainer",
		
		], function(parser){
		    parser.parse();
		});
	</script>
</head>
<body class="claro">
    <div data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="gutters:true" id="borderContainerThree">
    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'top'">
    </div>
    <!-- div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'left', splitter:false" style="width: 20%; height: 100%">

    </div-->
    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'center', splitter:false">
        <div data-dojo-id="workspacePane" id="workspacePane" style="width: 100%; height: 100%"></div>
    </div>
    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'bottom', splitter:false" style="width: 100%;height: 5%">
        <div data-dojo-type="dijit/form/Button">Upload Files
             <script type="dojo/on" data-dojo-event="click">
				require([ "rca/dataFileUploader/DataFileUploader", "dojo/dom" ], 
					function(DataFileUploader, dom)
					{			
						var widgetParent = dom.byId("workspacePane");

						dojo.empty(widgetParent); 	
			
						widget = dijit.byId("dataFileUploader");
			
						if(!widget)
						{
							dataFileUplaoder = new DataFileUploader({id: "dataFileUploader"}).placeAt(widgetParent);
							
							dataFileUplaoder.startup();
						}
						else
						{
							widget.placeAt(widgetParent);
						}						
					}
				);
            </script>
        </div>
        <div data-dojo-type="dijit/form/Button">Reset Session        
			<script type="dojo/on" data-dojo-event="click">
				require(["dojo/dom"], 
					function(dom)
					{
						var widgetParent = dom.byId("workspacePane");
						dojo.empty(widgetParent);

			var requestBodyToServer = {logout: true};

			var data = dojo.toJson(requestBodyToServer);
			
			var logoutService = {			
					postData : data,
					url: "/logout",					
					method: "post",
					preventCache: true
			};
					
			dojo.xhrPost(logoutService); 	
				
					}
				);
            </script>
        </div>
        <div data-dojo-type="dijit/form/Button">Performance Degradation Root Cause Analysis
        			<script type="dojo/on" data-dojo-event="click">
						require([ "dojo/dom", "rca/appMain"], 
							function(dom, appMain)
							{
								var widgetParent = dom.byId("workspacePane");
								
								dojo.empty(widgetParent); 					

								widget = dijit.byId("performanceDegradationRootCauseAnalyzer");
								
								if(!widget)
								{
									console.debug("Widget Not present");
									widget = new appMain({id: "performanceDegradationRootCauseAnalyzer"}).placeAt(widgetParent);
									
									widget.startup();
								}						
								else
								{
									console.debug("Widget present");
									widget.placeAt(widgetParent);
								}
							}
						);
					</script>
        </div>
        <div data-dojo-type="dijit/form/Button">Download Scripts</div>
    </div>
</div>
</body>
</html>