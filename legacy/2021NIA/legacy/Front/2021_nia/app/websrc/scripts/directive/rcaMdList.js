export const rcaMdList = function () {
    return {
        restrict: 'C',
        compile: function(element, attrs) {
            element.on('scroll', function () {
                element.find('div.rml-header div.rml-row').css({'width': 'calc(100% + ' + element.scrollLeft() + 'px)'});
                element.find('div.rml-body').css({'width': 'calc(100% + ' + element.scrollLeft() + 'px)'});

            });
        }
    }
}

export const rmlColumn = function () {
    return {
        restrict: 'C',
        link: function ($scope, element, attrs) {
            if(!attrs['columnSize']){ return; }

            let size = attrs['columnSize'];
            size = isNaN(size) ? size : size.toString() + 'px';

            element.css({
                'width': size,
                'min-width': size,
                'max-width': size
            });

            element.wrapInner(`<span class="wrapper-span"></span>`);

        }
    }
}

