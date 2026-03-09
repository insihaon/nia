let showOverText = function () {
    return {
        restrict: 'A',
        link: function ($scope, element, attrs) {
            if (element.prop('tagName') == 'DIV') {
                element.on('mouseenter', function (e) {
                    if (e.currentTarget.offsetWidth < e.currentTarget.scrollWidth ||
                        ($(event.currentTarget).css('max-width').replace(/[^0-9]/g, "") * 1) == e.currentTarget.offsetWidth) {
                        e.currentTarget.classList.add('overflow-action');
                    }
                });
                element.on('mouseleave', function (e) {
                    e.currentTarget.classList.remove('overflow-action');
                });
                return;
            } else {
                element.wrapInner('<div class="over-cell"></div>');
                return;
            }

            element.wrapInner('<div class="over-cell"></div>');
            let elDiv = element.find('div.over-cell');

            let padding = getNumSize(element.css('padding-left')) + getNumSize(element.css('padding-right'));
            let size = attrs['mdColumnSize'] || 0;

            elDiv.css({ 'max-width': (size - padding) + 'px' });
            elDiv.hover().before().css({
                'width': (size - padding) + 'px'
            });

            elDiv.on('mouseenter', function (e) {
                if (e.currentTarget.offsetWidth < e.currentTarget.scrollWidth ||
                    (getNumSize($(event.currentTarget).css('max-width'))) == e.currentTarget.offsetWidth) {
                    e.currentTarget.classList.add('overflow-action');
                }
            });
            elDiv.on('mouseleave', function (e) {
                e.currentTarget.classList.remove('overflow-action');
            });

            function getNumSize(val) {
                return (val || '').replace(/[^0-9]/g, "") * 1;
            }
        }
    }
}

export default [showOverText]

