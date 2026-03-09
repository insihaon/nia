var config = function($mdDateLocaleProvider) {
    $mdDateLocaleProvider.formatDate = function(date) {
        if(date == null) {
            return null;
        }
        return date.yyyy_mm_dd();
    };
};

export default ['$mdDateLocaleProvider', config]