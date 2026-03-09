let range = function () {
    return  {
        //restrict   : "E",
        //scope      : {
        //    max       : '=',
        //    min       : '=',
        //    gap       : '=?',
        //    step      : '=?',
        //    lowerValue: "=",
        //    upperValue: "="
        //},
        //template: '<div class=\'range-slider-container\'>' +
        //    '   <div class=\'range-slider-left\' ng-style="{width:lowerWidth+\'%\'}">' +
        //    '      <md-slider ng-model="lowerValue" min=\'{{min}}\' max=\'{{lowerMaxLimit}}\' step="{{step}}"' +
        //    '          aria-label="lowerSlider"></md-slider>' +
        //    '   </div>' +
        //    '   <div class="range-track-line"' +
        //    '      ng-style="{width:tracker.width+\'%\',left:tracker.left+\'%\',right:tracker.right+\'%\'}"></div>' +
        //    '   <div class=\'range-slider-right\' ng-style="{width:upperWidth+\'%\'}">' +
        //    '      <md-slider ng-model="upperValue" min="{{upperMinLimit}}" max="{{max}}" step="{{step}}"' +
        //    '          aria-label="upperSlider"></md-slider>' +
        //    '   </div>' +
        //    '</div>' +
        //'<span> 1:{{min}} 2:{{lowerMaxLimit}} 3:{{upperMinLimit}} 4:{{max}} 5:{{step}} </span>',
        //
        //controller :  ["$scope", function ($scope) {
        //    var COMFORTABLE_STEP = $scope.step, // whether the step is comfortable that depends on u
        //        tracker = $scope.tracker = {    // track style
        //            width: 0,
        //            left : 0,
        //            right: 0
        //        };
        //    function updateSliders() {
        //        if ($scope.upperValue - $scope.lowerValue > $scope.gap) {
        //            $scope.lowerMaxLimit = $scope.lowerValue + COMFORTABLE_STEP;
        //            $scope.upperMinLimit = $scope.upperValue - COMFORTABLE_STEP;
        //        } else {
        //            $scope.lowerMaxLimit = $scope.lowerValue;
        //            $scope.upperMinLimit = $scope.upperValue;
        //        }
        //        updateSlidersStyle();
        //    }
        //    function updateSlidersStyle() {
        //        // update sliders style
        //        $scope.lowerWidth = $scope.lowerMaxLimit / $scope.max * 100;
        //        $scope.upperWidth = ($scope.max - $scope.upperMinLimit) / $scope.max * 100;
        //        // update tracker line style
        //        tracker.width = 100 - $scope.lowerWidth - $scope.upperWidth;
        //        tracker.left = $scope.lowerWidth || 0;
        //        tracker.right = $scope.upperWidth || 0;
        //    }
        //    // watch lowerValue & upperValue to update sliders
        //    $scope.$watchGroup(["lowerValue", "upperValue"], function (newVal) {
        //        // filter the default initialization
        //        if (newVal !== undefined) {
        //            updateSliders();
        //        }
        //    });
        //    // init
        //    $scope.step = $scope.step || 1;
        //    $scope.gap = $scope.gap || 0;
        //    $scope.lowerMaxLimit = $scope.lowerValue + COMFORTABLE_STEP;
        //    $scope.upperMinLimit = $scope.upperValue - COMFORTABLE_STEP;
        //    updateSlidersStyle();
        //}]

   //     restrict   : "E",
   //     scope      : {
   //         max:'=',
   //         min:'=',
   //         minGap: '=',
   //         step:'=',
   //         lowerValue: "=lowerValue",
   //         upperValue: "=upperValue"
   //     },
   //     template: [
   //         '<div class="range-slider-container">',
   //         '<div class="range-slider-left">',
   //         '<md-slider aria-label="upperValue" ng-model="lowerValue" step="{{step}}" min="{{min}}" max="{{lowerMax}}"></md-slider>',
   //         '</div>',
   //         '<div class="range-slider-right" ng-style="{width: upperWidth}">',
   //         '<md-slider aria-label="upperValue" ng-model="upperValue" step="{{step}}" min="{{upperMin}}" max="{{max}}"></md-slider>',
   //         '</div>',
   //         '<p> <br>max : {{max}}'+
   //'         <br>step :  {{step}}'+
   //' <br>lowerMax :  {{lowerMax}}'+
   //' <br>lowerValue :  {{lowerValue}}'+
   //' <br>upperValue :  {{upperValue}}'+
   //' <br>upperMin :  {{upperMin}}'+
   //'<br>upperWidth : {{upperWidth}}</p>'+
   //         '</div>'
   //     ].join(''),
   //
   //     controller :  ["$scope", function ($scope) {
   //         if(!$scope.step){
   //             $scope.step = 1;
   //         }
   //         $scope.lowerMax = $scope.max - $scope.step;
   //         $scope.upperMin = $scope.lowerValue + $scope.step;
   //         if(!$scope.lowerValue || $scope.lowerValue<$scope.min){
   //             $scope.lowerValue = $scope.min;
   //         }else{
   //             $scope.lowerValue*=1;
   //         }
   //         if(!$scope.upperValue || $scope.upperValue>$scope.max){
   //             $scope.upperValue = $scope.min;
   //         }else{
   //             $scope.upperValue*=1;
   //         }
   //
   //         $scope.$watch('lowerValue',function(){
   //             $scope.upperMin = $scope.lowerValue + $scope.step;
   //             $scope.upperWidth = ((($scope.max-($scope.lowerValue + $scope.step))/($scope.max - $scope.min)) * 100) + "%";
   //             if($scope.lowerValue > ($scope.upperValue - $scope.minGap) && $scope.upperValue < $scope.max) {
   //                 $scope.upperValue = $scope.lowerValue + $scope.minGap;
   //             }
   //         });
   //     }]


        restrict: 'E',
        scope: {
            min: '@',
            max: '@'
        },
        require: 'ngModel',
        template: '<div class="range"></div>',
        link: function($scope, $element, $attrs, ngModelController) {
            var $range = $($element).find('.range');
            $scope.min = $scope.min || 0;
            $scope.max = $scope.max || 100;
            function updateCustomProperties(range) {
                var from = Math.min.apply(Math, range);
                var to = Math.max.apply(Math, range);
                $range[0].style.setProperty("--from", (from / $scope.max) * 100);
                $range[0].style.setProperty("--to", (to / $scope.max) * 100);
            }
            var min = +$scope.min;
            var max = +$scope.max;
            $range.slider({
                min: min,
                max: max,
                step: 1,
                values: ngModelController.$modelValue || [min, max],
                slide: function(event, ui) {
                    function updateModel() {
                        ngModelController.$setViewValue(ui.values);
                    }
                    if (!$scope.$$phase) {
                        $scope.$apply(updateModel);
                    } else {
                        updateModel();
                    }
                    updateCustomProperties(ui.values);
                }
            });
            $scope.$watch('min', function(value) {
                $range.slider('option', 'min', JSON.parse(value));
            });
            $scope.$watch('max', function(value, oldValue) {
                $range.slider('option', 'max', JSON.parse(value));
                updateCustomProperties(ngModelController.$modelValue);
            });
            ngModelController.$render = function() {
                if (ngModelController.$modelValue) {
                    console.log('render', ngModelController.$modelValue);
                    $range.slider('option', 'values', ngModelController.$modelValue);
                    updateCustomProperties(ngModelController.$modelValue);
                }
            };
        }
    };
}

export default [range]
