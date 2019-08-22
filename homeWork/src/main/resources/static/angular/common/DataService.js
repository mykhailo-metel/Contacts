angular.module('contacts').service('DataService', function ($http) {
    dataService = this;

    dataService.save = function(contact){

    }

    dataService.getAll = function(scope){
        $http.get('/contacts').then(function (response) {
            tempContacts = response.data;
            if(tempContacts && tempContacts.length > 0){
                scope.contacts = tempContacts;
            }
        });
    }

    dataService.delete = function(id){

    }
})