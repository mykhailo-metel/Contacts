angular.module('contacts').component('addContact', {
    templateUrl: '/angular/saveNewContact/addContact.html',
    controller: function AddContactController ($scope, $http, DataService) {

        addContact = this;

        addContact.contact = {
            name: "",
            surname: "",
            tel: "",
            address: ""
        };

        addContact.saveContact = function() {
            $http.put('/contacts', this.contact).then(function (response) {
                DataService.getAll($scope);
            });
        }
    }
});