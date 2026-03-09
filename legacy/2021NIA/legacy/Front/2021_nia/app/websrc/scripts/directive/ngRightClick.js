let ngRightClick = function ($parse) {
    return function(scope, element, attrs) {
        var fn = $parse(attrs.ngRightClick);
        element.bind('contextmenu', function(event) {
            scope.$apply(function() {
                //var selected_row = scope.$parent.$parent.row;
                //selected_row.setSelected(true);
                event.preventDefault();
                fn(scope, {$event:event});
            });
        });
    };
}

export default ['$parse',ngRightClick]
