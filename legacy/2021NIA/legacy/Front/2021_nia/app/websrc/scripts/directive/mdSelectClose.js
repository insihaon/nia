// <md-select> item 선택후 md-select 창이 닫히지 않을때 "md-option 안에서 사용"
// 보통 <md-select>를 md-dialog 안에서 사용할 경우 발생한다..
let mdSelectClose = function ($mdSelect) {
    return {
        restrict: 'A',
        link: function(scope, elem, attrs) {
            elem.bind('click', function() {
                $mdSelect.hide();
            });
        }
    }
}

export default ['$mdSelect',mdSelectClose]

