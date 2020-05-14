	require([ "rca/appMain", "dojo/dom" ], 
		function(appMain, dom)
		{
			console.debug("inside index.js");
			
			var myWidgetContainer = dom.byId("myWidgetContainer");
			 
			if(!appMainApp)
				var appMainApp = new appMain().placeAt(myWidgetContainer);
		}
	);