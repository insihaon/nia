class base {
    constructor($scope, tools) {
        if ($scope != null) {
            tools.injection($scope);
            $scope.control = this;
        }
    }
}

export default base;