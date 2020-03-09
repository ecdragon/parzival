
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// START - GLOBAL FUNCTIONS

window.voScui = window.voScui || {};

voScui.fGetOneStringFromDataInResponseRawData = function(rawData) {
	try {
		vaData = voScui.fDataAsJsonArrFromResponseRawData(rawData);
		vsOneString = vaData[0];
		return vsOneString;
	}
	catch(error) {
		alert(JSON.stringify(error));
	}
}

voScui.fDataAsJsonArrFromResponseRawData = function(rawData) {
	try {
		vaData = JSON.parse(rawData).response.data;
		return vaData;
	}
	catch(error) {
		alert(JSON.stringify(error));
	}
}

voScui.fClearAndFetchScFormField = function(vsScFormFieldId) {
	try {
		window[vsScFormFieldId].clearValue();
		window[vsScFormFieldId].fetchData();
	}
	catch(error) {
		alert(JSON.stringify(error));
	}
}

voScui.fNavigateToDirectory = function(vsDirectoryPath) {
	try {
		IDDynamicFormSelectFile_FieldDirectoryPath.setValue(vsDirectoryPath);
		voScui.fClearAndFetchScFormField("IDDynamicFormSelectFile_FieldDirectoryEntry");
		voScui.fClearAndFetchScFormField("IDDynamicFormSelectFile_FieldFileDirectoryEntry");
	}
	catch(error) {
		alert(JSON.stringify(error));
	}
}

// END - GLOBAL FUNCTIONS
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// START - PROJECT RESTDATASOURCE STUFF - DIRECTORY ENTRY

voScui.vaScFieldsDirectoryEntry = [ 
	{name: "name", primaryKey: true}
	]; 

voScui.fTransformResponseDirectoryEntry = function (dsResponse, dsRequest, data) {        
	var dsResponse = this.Super("transformResponse", arguments);
	// alert("data: " + JSON.stringify(data));
	// ... do something to dsResponse ...
	return dsResponse;
}

isc.RestDataSource.create({ 
	ID: "IDRestDataSourceDirectoryEntry", 
	dataFormat: "json", 
	fetchDataURL: "/api/directory_entry/sc/fetch-directories", 
	fields: voScui.vaScFieldsDirectoryEntry,
	jsonPrefix: "",
	jsonSuffix: ""
	,
	transformResponse : voScui.fTransformResponseDirectoryEntry,
    transformRequest : function (dsRequest) {
        // modify dsRequest.data here, for example, add fixed criteria
        // dsRequest.data.xxx = someWidget.getXxxx();
    	dsRequest.data.directoryPath = IDDynamicFormSelectFile_FieldDirectoryPath.getValue();
    	// alert("doing requesting of app");
    	// IDDynamicFormXxxx.clearValue();
        return this.Super("transformRequest", arguments);
    }
});

// END - PROJECT RESTDATASOURCE STUFF - DIRECTORY ENTRY
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// START - PROJECT RESTDATASOURCE STUFF - FILE DIRECTORY ENTRY

voScui.fTransformResponseFileDirectoryEntry = function (dsResponse, dsRequest, data) {        
	var dsResponse = this.Super("transformResponse", arguments);
	// alert("data: " + JSON.stringify(data));
	// ... do something to dsResponse ...
	return dsResponse;
}

isc.RestDataSource.create({ 
	ID: "IDRestDataSourceFileDirectoryEntry", 
	dataFormat: "json", 
	fetchDataURL: "/api/directory_entry/sc/fetch-files", 
	fields: voScui.vaScFieldsDirectoryEntry,
	jsonPrefix: "",
	jsonSuffix: ""
		,
		transformResponse : voScui.fTransformResponseFileDirectoryEntry,
		transformRequest : function (dsRequest) {
			// modify dsRequest.data here, for example, add fixed criteria
			// dsRequest.data.xxx = someWidget.getXxxx();
			dsRequest.data.directoryPath = IDDynamicFormSelectFile_FieldDirectoryPath.getValue();
			// alert("doing requesting of app");
			// IDDynamicFormXxxx.clearValue();
			return this.Super("transformRequest", arguments);
		}
});

// END - PROJECT RESTDATASOURCE STUFF - FILE DIRECTORY ENTRY
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// START - PROJECT RESTDATASOURCE STUFF - ENVIRONMENT CONTEXT

voScui.vaScFieldsEnvironmentContext = [ 
	{name: "name", primaryKey: true}
	]; 

voScui.fTransformResponseEnvironmentContext = function (dsResponse, dsRequest, data) {        
	var dsResponse = this.Super("transformResponse", arguments);
	// alert("data: " + JSON.stringify(data));
	// ... do something to dsResponse ...
	return dsResponse;
}

isc.RestDataSource.create({ 
	ID: "IDRestDataSourceEnvironmentContext", 
	dataFormat: "json", 
	fetchDataURL: "/api/environment_context/sc/fetch", 
	fields: voScui.vaScFieldsDirectoryEntry,
	jsonPrefix: "",
	jsonSuffix: ""
		,
		transformResponse : voScui.fTransformResponseDirectoryEntry,
		transformRequest : function (dsRequest) {
			// modify dsRequest.data here, for example, add fixed criteria
			// dsRequest.data.xxx = someWidget.getXxxx();
			// alert("doing requesting of app");
			// IDDynamicFormXxxx.clearValue();
			return this.Super("transformRequest", arguments);
		}
});

// END - PROJECT RESTDATASOURCE STUFF - ENVIRONMENT CONTEXT
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// START - MAIN VLAYOUT

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// START - TITLE LABEL
	
	isc.Label.create({
		ID: "IDLabelHeader",
		contents: "PARZIVAL ADMIN",
		width: "100%",
		height: 40,
		styleName: "windowHeaderText hc_isc_label_header"
	});

	// END - TITLE LABEL
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// START - MAIN TABSET
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// START - SETUP DEVELOPMENT ENVIRONMENT VLAYOUT
			
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// START - SETUP DEVELOPMENT ENVIRONMENT DYNAMIC FORM
			
			voScui.fClickGenerateApplicationSource = function (form, item) {
				var viApplicationId = IDDynamicFormGenerateSource_FieldApplication.getValue();
				isc.RPCManager.sendRequest({ 
					params : { 
						"applicationId" : viApplicationId 
					}, 
					callback: voScui.fCallbackResult, 
					actionURL: "/api/application/generate"
				});
			}
			
			voScui.fCallbackResult = function (response, rawData, request) {
				IDDynamicFormGenerateSource_FieldResult.setValue(rawData);
			}
			
			isc.DynamicForm.create({
				ID: "IDDynamicForm_SetupDevelopmentEnvironment",
				width: "100%",
				height: "100%",
				numCols: 2,
				colWidths: [350, "*"],
				fields: [
					
					////////////////////////////////////////////////////
					// This section is for selecting a file
					
					{name: "spacer",
						type: "spacer",
						endRow: false
					},
					{name: "btnConfigureParzival",
						startRow: false,
						title: "Parzival",
						type: "button",
						width: 300,
						click: function() {
							IDDynamicForm_SetupDevelopmentEnvironment_Field_EnvironmentContext.setValue("GenericGithubHttps");
							IDDynamicForm_SetupDevelopmentEnvironment_Field_ProjectName.setValue("parzival");
							IDDynamicForm_SetupDevelopmentEnvironment_Field_Intent.setValue("Parzival - Buckaroo Banzai? Yeah! Really? You're gonna wear the outfit from your favourite movie. Don't be that guy. I am that guy.");
							IDDynamicForm_SetupDevelopmentEnvironment_Field_BranchName.clearValue();
							IDDynamicForm_SetupDevelopmentEnvironment_Field_GitAccountName.setValue("ecdragon");
						}
					},
					
					{name: "spacer",
						type: "spacer",
						endRow: false
					},
					{name: "btnConfigureSpringCloudGateway",
						startRow: false,
						title: "Spring Cloud Gateway",
						type: "button",
						width: 300,
						click: function() {
							IDDynamicForm_SetupDevelopmentEnvironment_Field_EnvironmentContext.setValue("GenericGithubHttps");
							IDDynamicForm_SetupDevelopmentEnvironment_Field_ProjectName.setValue("spring-cloud-gateway");
							IDDynamicForm_SetupDevelopmentEnvironment_Field_Intent.setValue("Spring Cloud Gateway - Investigating source");
							IDDynamicForm_SetupDevelopmentEnvironment_Field_BranchName.clearValue();
							IDDynamicForm_SetupDevelopmentEnvironment_Field_GitAccountName.setValue("spring-cloud");
						}
					},
					
					{ID: "IDDynamicForm_SetupDevelopmentEnvironment_Field_EnvironmentContext",
						name:"directoryEntry", 
						title:"Environment Context", 
						type: "select",
						width: 300,
						optionDataSource:IDRestDataSourceEnvironmentContext,
						valueField: "name",
						displayField: "name",
						changed: function(form, item, value) {
							// Do nothing
						}
					},
					
					{ID: "IDDynamicForm_SetupDevelopmentEnvironment_Field_ProjectName",
						name: "projectName",
						title: "Project Name",
						type: "text",
						width: 300
					},
					
					{ID: "IDDynamicForm_SetupDevelopmentEnvironment_Field_Intent",
						name: "intent",
						title: "Intent (what and why)",
						type: "text",
						width: 800
					},
					
					{ID: "IDDynamicForm_SetupDevelopmentEnvironment_Field_BranchName",
						name: "branchName",
						title: "Branch Name (can include \"/\", be null, or \"master\"(default))",
						type: "text",
						width: 400
					},
					
					{ID: "IDDynamicForm_SetupDevelopmentEnvironment_Field_GitAccountName",
						name: "gitAccountName",
						title: "Git Account Name (For getting random source like Spring...)",
						type: "text",
						width: 400
					},
					
					{name: "spacer",
						type: "spacer",
						endRow: false
					},
					{name: "btnSetupDevelopmentEnvironment",
						startRow: false,
						title: "Setup Development Environment",
						type: "button",
						width: 200,
						click: function() {
							try {
								isc.RPCManager.sendRequest({ 
									params : { 
										"environmentContextName" : IDDynamicForm_SetupDevelopmentEnvironment_Field_EnvironmentContext.getValue(),
										"projectName" : IDDynamicForm_SetupDevelopmentEnvironment_Field_ProjectName.getValue(),
										"intent" : IDDynamicForm_SetupDevelopmentEnvironment_Field_Intent.getValue(),
										"branchName" : IDDynamicForm_SetupDevelopmentEnvironment_Field_BranchName.getValue(),
										"gitAccountName" : IDDynamicForm_SetupDevelopmentEnvironment_Field_GitAccountName.getValue(),
									}, 
									callback: function(response, rawData, request) {
										vsSetupResults = voScui.fGetOneStringFromDataInResponseRawData(rawData);
										IDDynamicFormSetupDevelopmentEnvironment_Field_Results.setValue(vsSetupResults);
									}, 
									actionURL: "/api/development_environment/setup-an-environment"
								});
							}
							catch(error) {
								alert(JSON.stringify(error));
							}
						}
					},
					
					// Display results
					{ID: "IDDynamicFormSetupDevelopmentEnvironment_Field_Results",
						name: "result",
						title: "Results",
						// https://www.smartclient.com/smartclient-release/isomorphic/system/reference/?id=class..StaticTextItem
						type: "staticText",
						defaultValue: "",
						width: 500
					},
					]
			});
			
			// END - SETUP DEVELOPMENT ENVIRONMENT DYNAMIC FORM
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		isc.VLayout.create({
			ID : "IDVLayout_SetupDevelopmentEnvironment",
			width : "100%",
			height : "100%",
			overflow: "hidden",
			members: [
				IDDynamicForm_SetupDevelopmentEnvironment
				]
		});
		
		// END - SETUP DEVELOPMENT ENVIRONMENT VLAYOUT
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// START - POM FILE BROWSER VLAYOUT
	
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// START - POM FILE BROWSER DYNAMIC FORM
			
			voScui.fClickGenerateApplicationSource = function (form, item) {
				var viApplicationId = IDDynamicFormGenerateSource_FieldApplication.getValue();
				isc.RPCManager.sendRequest({ 
					params : { 
						"applicationId" : viApplicationId 
						}, 
					callback: voScui.fCallbackResult, 
					actionURL: "/api/application/generate"
				});
			}
			
			voScui.fCallbackResult = function (response, rawData, request) {
				IDDynamicFormGenerateSource_FieldResult.setValue(rawData);
			}
			
			isc.DynamicForm.create({
				ID: "IDDynamicFormSelectFile",
				width: "100%",
				height: "100%",
				numCols: 2,
				colWidths: [350, "*"],
				fields: [
					
					////////////////////////////////////////////////////
					// This section is for selecting a file
					
					// Display current path
					{ID: "IDDynamicFormSelectFile_FieldDirectoryPath",
						name: "directoryPath",
						title: "Directory Path",
						// https://www.smartclient.com/smartclient-release/isomorphic/system/reference/?id=class..StaticTextItem
						type: "staticText",
						defaultValue: "/",
						width: 500
					},
					
					{ID: "IDDynamicFormSelectFile_FieldDirectoryEntry",
						name:"directoryEntry", 
						title:"Navigate to Subdirectory (select '..' to navigate up one dir)", 
						type: "select",
						width: 500,
						// editorType:"SelectItem",
						optionDataSource:IDRestDataSourceDirectoryEntry,
						valueField: "name",
						displayField: "name",
						changed: function(form, item, value) {
							vsCurrentDirPath = IDDynamicFormSelectFile_FieldDirectoryPath.getValue();
							vsLastDirPathChar = vsCurrentDirPath[vsCurrentDirPath.length - 1];
							vsDirSeparator = "/";
							vsDirToNavigateTo = "/";
							if (vsLastDirPathChar == '/') {
								vsDirSeparator = "";
							}
							if (value == "..") {
								if (vsCurrentDirPath != "/") {
									viLastIndexOfSlash = vsCurrentDirPath.lastIndexOf("/");
									if (viLastIndexOfSlash > 0) {
										vsDirToNavigateTo = vsCurrentDirPath.slice(0, viLastIndexOfSlash);
									}
								}
							}
							else {
								vsDirToNavigateTo = vsCurrentDirPath + vsDirSeparator + value;
							}
							voScui.fNavigateToDirectory(vsDirToNavigateTo);
						}
					},
					
					{name: "spacer",
						type: "spacer",
						endRow: false
					},
					{name: "btnGoToRoot",
						startRow: false,
						title: "Navigate To Root Directory",
						type: "button",
						width: 300,
						click: function() {
							voScui.fNavigateToDirectory("/");
						}
					},
					
					{name: "spacer",
						type: "spacer",
						endRow: false
					},
					{name: "btnGoToUserHome",
						startRow: false,
						title: "Navigate To User Home Directory",
						type: "button",
						width: 300,
						click: function() {
							isc.RPCManager.sendRequest({ 
//								params : { 
//									"applicationId" : viApplicationId 
//									}, 
								callback: function(response, rawData, request) {
									vsUserHomeDirPath = voScui.fGetOneStringFromDataInResponseRawData(rawData);
									voScui.fNavigateToDirectory(vsUserHomeDirPath);
								}, 
								actionURL: "/api/directory_entry/sc/fetch-user-home"
							});
						}
					},
					
					{name: "spacer",
						type: "spacer",
						endRow: false
					},
					{name: "btnGoToCustom",
						startRow: false,
						title: "Navigate To Custom - projects",
						type: "button",
						width: 300,
						click: function() {
							isc.RPCManager.sendRequest({ 
//								params : { 
//									"applicationId" : viApplicationId 
//									}, 
								callback: function(response, rawData, request) {
									vsUserHomeDirPath = voScui.fGetOneStringFromDataInResponseRawData(rawData);
									voScui.fNavigateToDirectory(vsUserHomeDirPath + "/Data/projects/generic");
								}, 
								actionURL: "/api/directory_entry/sc/fetch-user-home"
							});
						}
					},
					
					{ID: "IDDynamicFormSelectFile_FieldFileDirectoryEntry",
						name:"directoryEntry", 
						title:"Select a File in Directory", 
						type: "select",
						width: 500,
						// editorType:"SelectItem",
						optionDataSource:IDRestDataSourceFileDirectoryEntry,
						valueField: "name",
						displayField: "name"
					},
					
					{name: "spacer",
						type: "spacer",
						endRow: false
					},
					{name: "btnCleanPom",
						startRow: false,
						title: "Alphabetize dependencies in selected Pom file",
						type: "button",
						width: 300,
						click: function() {
							isc.RPCManager.sendRequest({ 
								params : { 
									"fullPathToLocalFileDirectory": IDDynamicFormSelectFile_FieldDirectoryPath.getValue(),
									"localFileName": IDDynamicFormSelectFile_FieldFileDirectoryEntry.getValue()
									}, 
//								callback: voScui.fCallbackResult, 
								callback: function(response, rawData, request) {
									vsResults = voScui.fGetOneStringFromDataInResponseRawData(rawData);
									IDDynamicFormSelectFile_FieldResults.setValue(vsResults);
								}, 
								actionURL: "/api/maven/cleanDependenciesInPomXmlLocalFileToLocalFile"
							});
						}
					},
					
					// Display results
					{ID: "IDDynamicFormSelectFile_FieldResults",
						name: "result",
						title: "Results",
						// https://www.smartclient.com/smartclient-release/isomorphic/system/reference/?id=class..StaticTextItem
						type: "staticText",
						defaultValue: "",
						width: 500
					},
					]
			});
			
			// END - POM FILE BROWSER DYNAMIC FORM
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		isc.VLayout.create({
			ID : "IDVLayoutSelectFile",
			width : "100%",
			height : "100%",
			overflow: "hidden",
			members: [
				IDDynamicFormSelectFile
				]
		});
		
		// END - POM FILE BROWSER VLAYOUT
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	isc.TabSet.create({
	    ID: "IDTabSetMain",
	    tabBarPosition: "top",
	    width: "100%",
	    // http://localhost:8117/isomorphic/system/reference/?id=attr..TabSet.selectedTab
	    selectedTab: 1,
	    height: "100%",
	    tabs: [
	    	{title: "Select File", pane: IDVLayoutSelectFile},
	    	{title: "Setup Development Environment", pane: IDVLayout_SetupDevelopmentEnvironment}
	    ]
	});
	
	// END - MAIN TABSET
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

isc.VLayout.create({
	ID : "IDVLayoutMain",
	width : "100%",
	height : "100%",
	overflow: "hidden",
	members: [
		IDLabelHeader,
		IDTabSetMain
	]
});

// END - MAIN VLAYOUT
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

IDVLayoutMain.draw();
