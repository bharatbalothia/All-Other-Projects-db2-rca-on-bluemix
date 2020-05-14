	require([ "rca/dataFileUploader/DataFileUploader", "dojo/dom" ], 
		function(DataFileUplaoder, dom)
		{
			console.debug("inside main.js");
			
			var workareaPane = dom.byId("workareaPane");
			
			widget = dijit.byId("dataFileUploader");
			
			if(!widget)
				dataFileUplaoder = new DataFileUploader({id: "dataFileUploader"}).placeAt(workareaPane);
			
				dataFileUplaoder.startup();
		}
	);