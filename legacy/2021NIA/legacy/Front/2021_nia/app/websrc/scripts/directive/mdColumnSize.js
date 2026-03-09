let mdColumnSize = function () {
    return {
        restrict: 'A',
        link: function ($scope, element, attrs) {
            if(!attrs['mdColumnSize']){ return; }

            let size = attrs['mdColumnSize'];
            size = isNaN(size) ? size : size.toString() + 'px';

            let wrapperClass = attrs['overCell'] != undefined ? ' class="wrapper-span"' : '';

            element.css({
                'width': size,
                'min-width': size,
                'max-width': size
            });

            element.wrapInner(`<span${wrapperClass}></span>`);

           /* element.on('mouseenter', function (e) {
                if (e.currentTarget.offsetWidth < e.currentTarget.scrollWidth ||
                    ($(event.currentTarget).css('max-width').replace(/[^0-9]/g, "") * 1) == e.currentTarget.offsetWidth) {
                    e.currentTarget.classList.add('overflow-action');
                }
            });
            element.on('mouseleave', function (e) {
                e.currentTarget.classList.remove('overflow-action');
            });*/
        }
    }
}

export default [mdColumnSize]

