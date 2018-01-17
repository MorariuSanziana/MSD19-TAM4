// main controller
app.controller('mainController', 
  ['$scope', '$http', '$timeout', 'restResource',	// dependencies
  function($scope, $http, $timeout, restResource) {	// implementation
	console.log("mainController");
	
	$scope.studentsSelected = [];
	
	$http.get(studentsRestURL)
	.success(function(data){
		console.log("mainController:GET URL : " + studentsRestURL);
		$scope.studentsList = data;
	});
}]);

// view1 controller
app.controller('view1Controller', 
  ['$scope', '$http', '$timeout', 'restResource',	// dependencies
  function($scope, $http, $timeout, restResource) {	// implementation
	console.log("view1Controller");
	
	$scope.filterOptions = {
		    filterText: ''
	};
	
	$scope.gridOptions = { 
		      data: 'studentsList',
		      selectedItems: $scope.studentsSelected,
		      enablePaging: true,
		      multiSelect: false,
		      resizable: true,
		      showFilter: true, showColumnMenu:true,
		      sortInfo: { fields: ['idStudent'], directions: ['asc'] },
		      filterOptions: $scope.filterOptions,
		      width: 420,
		      heigh: 100,
		      columnDefs: [{ field: "idStudent", width: 120, displayName: 'ID'},
		                   { field: "numeStudent", width: 300 , displayName: 'Name' }]   
		    };		
	
	$http.get(studentsRestURL)
	.success(function(data){
		console.log("view1Controller:GET URL : " + studentsRestURL);
		$scope.studentsList = data;
	})
	.error(function(data){
		console.log('ERROR');
		console.log(data);
	});		
	
	$scope.view1_name = "Student list";
}]);


//view2 controller
app.controller('view2Controller', 
  ['$scope', '$http', '$timeout', '$location', 'restResource',	// dependencies
  function($scope, $http, $timeout, $location, restResource) {	// implementation
	console.log("view2Controller");
	
    $scope.gridOptions = { 
      data: 'studentsList',
      selectedItems: $scope.studentsSelected,
      enablePaging: true,
      multiSelect: false,
      resizable: true,
      sortInfo: { fields: ['idStudent'], directions: ['asc'] },
      width: 420,
      heigh: 100,
      columnDefs: [{ field: "idStudent", width: 120, displayName: 'ID'},
                   { field: "numeStudent", width: 300 , displayName: 'Name' }]
    };

	restResource.get(studentsRestURL).then(function (data) {
		console.log(data);
		$scope.studentsList = data;
		$timeout(function() {
			idx = 0;
			console.log("student to select [back]: ");
			console.log($scope.studentsSelected[0]);			
			if ($scope.studentsSelected[0] != null){
				for(i in $scope.studentsList){
					if ($scope.studentsList[i].idStudent == $scope.studentsSelected[0].idStudent)
						idx = i;
				}
			}
			try{ $scope.gridOptions.selectRow(idx, true); } catch(e){}
		});
	});	    
    
    $scope.add = function(){
    	console.log("view2Controller: add action");
    	student = $scope.studentsSelected[0];
    	newStudent = JSON.parse(JSON.stringify(student));
    	//
    	newStudent.idStudent = 9999;
    	newStudent.numeStudent = "New Student 9999";
    	
    	
    	today = new Date();
    	dd = today.getDate();
    	mm = today.getMonth()+1; //January is 0!
    	yyyy = today.getFullYear();
    	if (mm < 10)
    		mm = '0' + mm;
    	newProject.startDate = yyyy + "-" + mm + "-" + dd;
    	console.log(newProject.startDate);
    	newProject.link.href = newProject.link.href.replace(project.projectNo, newProject.projectNo);
    	console.log(newProject.link.href);
    	
    	//
    	$scope.studentsList.push(newStudent);
    	idx = $scope.studentsList.indexOf(newStudent);
    	
    	$timeout(function() {
    		console.log(idx);
    		$timeout(function() { $scope.gridOptions.selectRow(idx, true); });
    	});    	
    	
    };
    
    $scope.save = function(){
    	console.log("view2Controller: save action");
    	if($scope.studentsSelected[0] == null)
    		return;
    	student = $scope.studentsSelected[0];
    	restResource.post(student);	
    };
    
    $scope.cancel = function(){
    	console.log("view2Controller: cancel action");
    };    
    
    $scope.remove = function(){
    	console.log("view2Controller: remove action");
    	student = $scope.studentsSelected[0];
    	link = student.link.href;
    	
    	// remove local model
    	var idx = $scope.studentsList.indexOf(student);      	
    	
    	restResource.remove(student).then(function(data){
    		$scope.studentsList.splice(idx, 1);
    		$timeout(function() { $scope.gridOptions.selectRow(0, true); });    		
    	});
    	
    };    
    
    $scope.go = function ( path ) {
    	  $location.path( path );
    };
}]);

//view3 controller
app.controller('view3Controller', 
  ['$scope', '$http', '$timeout', '$location', 'restResource',	// dependencies
  function($scope, $http, $timeout, $location, restResource) {	// implementation
	console.log("view3Controller");
	
    $scope.detailGrid = { 
      data: 'releasesList',
      selectedItems: $scope.releaseSelected,
      enablePaging: true,
      multiSelect: false,
      resizable: true,
      enableCellSelection: true,
      enableRowSelection: false,
      enableCellEdit: true,
      width: 600,
      columnDefs: [{ field: "releaseId", width: 120, displayName: 'ID', enableCellEdit: true},
                   { field: "indicative", width: 180 , displayName: 'Indicative', enableCellEdit: true },
                   { field: "publishDate", width: 300 , displayName: 'PublishDate', enableCellEdit: true }
                   ]
    };		
	
	// projects data model
    studentRestURL = $scope.studentsSelected[0].link.href;
	console.log("studentsRestURL:::: " + studentRestURL);
	//
	restResource.get(studentRestURL).then(function (data) {
		console.log(data);
		$scope.student = data;
		
		console.log("check resource: ");
		console.log(restResource.entity);
	});
	//
	$scope.go = function ( path ) {
  	  $location.path( path );
	};
	//
	$scope.save = function(){
    	console.log("view3Controller: save action");
    	console.log($scope.student);
    	if($scope.student == null)
    		return;  	
    	restResource.put();
    };
	
}]);