let draggable = function () {
    return {
        restrict: 'A',
        link: function (scope, elem, attrs) {
            angular.element(document).ready(() => {
                elem.draggable({
                    containment:".md-dialog-container",
                    cancel: ".noDrag",
                    start: function(){
                        this.parentElement.parentElement.children[0].classList.add('move-dialog');
                    },
                });
            });
        }
    };
}

export default [draggable]

