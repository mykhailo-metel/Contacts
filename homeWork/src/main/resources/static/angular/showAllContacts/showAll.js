angular.module('contacts').component('showAll', {
    templateUrl: '/angular/showAllContacts/listAll.html',
    controller: function ShowAllController ($scope, $http, DataService) {
        var showAllController = this;

        showAllController.refresh = function(){
            DataService.getAll($scope);
        }

        showAllController.delete = function (id) {
            $http.delete('/contacts/'+ id).then(function (response) {
                showAllController.refresh();
            });
        }

        showAllController.refresh();
    }
});