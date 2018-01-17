// rest request handling services
app.factory('restResource', ['$http', '$q', function($http,  $q){
	return {
		entity : null,
		url : null,
		get : function(ref){
            var deferred = $q.defer();
            var _this = this;
            
            if (typeof ref == 'string' || ref instanceof String)
            	this.url = ref;
            else
            	this.url = ref.link.href;
            
			$http.get(this.url )
			.success(function(data){
				console.log("restResource:GET URL : " + this.url);
				console.log(data);
				_this.entity = data;
				deferred.resolve(data);
			})
			.error(function(data){
				console.log('ERROR'); 
				deferred.reject();
			});	
			return deferred.promise;
		},
		put : function(data){
			console.log("restProjectResource:PUTING ");
			var deferred = $q.defer();
			
			if (data == null)
				putData = this.entity;
			else
				putData = data;
			console.log(this.url);
			console.log(putData);
			
			$http.put(this.url,putData)
	    	.success(function(data){
	    		console.log("restStudentResource:PUTTED");
	    		console.log(data);
	    		deferred.resolve(data);
	    	})
	    	.error(function(data){
	    		console.log('ERROR');
	    		deferred.reject();
	    	});  	
			return deferred.promise; 
		},		
		post : function(data){
			console.log("restStudentResource:POSTING ");
			var deferred = $q.defer();
			
			if (data == null)
				postData = this.entity;
			else
				postData = data;
			console.log(this.url);
			console.log(postData);
			
			$http.post(this.url,postData)
	    	.success(function(data){
	    		console.log("restStudentResource:POSTED");
	    		console.log(data);
	    		deferred.resolve(data);
	    	})
	    	.error(function(data){
	    		console.log('ERROR');
	    		deferred.reject();
	    	});  	
			return deferred.promise; 			
		},
		remove : function(data){
			console.log("restStudentResource:DELETING ");
			var deferred = $q.defer();
			
			if (data == null)
				deleteData = this.entity;
			else
				deleteData = data;
			console.log(this.url);
			console.log(deleteData);
			
			var config = {
					method : 'DELETE',
					url : this.url,
					data : deleteData,
					headers: {'Content-Type':'application/json;charset=UTF-8'}
			};			
			
//			$http.delete(this.url, deleteData, config)
			$http(config)
	    	.success(function(data){
	    		console.log("restStudentResource:DELETED");
	    		console.log(data);
	    		deferred.resolve(data);
	    	})
	    	.error(function(data){
	    		console.log('ERROR');
	    		deferred.reject();
	    	});  	
			return deferred.promise; 				
		}
	}
}]);