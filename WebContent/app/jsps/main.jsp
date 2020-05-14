<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="stylesheet" href="resources/js/lib/dijit/themes/claro/claro.css">
	<link rel="stylesheet" href="resources/js/lib/dojox/form/resources/UploaderFileList.css">
	<link rel="stylesheet" href="resources/js/lib/dojox/form/resources/FileUploader.css">

	<title>DB2</title>
	
	
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
	<script type="text/javascript" src="resources/js/main.js"></script>
	
</head>

<body class="claro">

    <div data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="gutters:true" id="borderContainerThree">
    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'top'"></div>
    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'left', splitter:false"></div>
    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="region:'center', splitter:false">
        <div id="workareaPane" style="width: 100%; height: 100%"></div>
    </div>
</div>	
	
</body>
</html>
