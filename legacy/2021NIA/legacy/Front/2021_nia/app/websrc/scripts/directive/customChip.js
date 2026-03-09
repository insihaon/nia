let customChip = function () {
    return {
        restrict: 'A',
        link: function(scope, elem, attrs) {
            let chipClass = scope.$chip.style;
            let mdChip = elem.parent().parent();
            // mdChip.addClass(chipClass);
            // console.info(mdChip);
            mdChip.css('background', scope.$chip.C_COLOR);
            mdChip.css('color', scope.$chip.C_COLOR_FG);
            mdChip.css('border', 'rgba(0,0,0,0.1) 1px solid');
        }
    }
};

export default [customChip]