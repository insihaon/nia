/*
 1.  {{user.name | uppercase:true}}

 {{ expression | filter }}
 {{ expression | filter1 | filter2 | ... }}
 {{ expression | filter:argument1:argument2:... }}

 2.
 app.controller('MyController', function($filter) {
 var text = $filter('uppercase')('hello');   // HELLO
 var text = $filter('uppercase')('hello', true); // Hello
 });

 3.
 app.controller('MyController', function(uppercaseFilter) {
 var text = uppercaseFilter('hello');    // HELLO
 var text = uppercaseFilter('hello', true);  // Hello
 });
 */
export function UpperFilter() {
    return function(string, firstCharOnly) {
        return (!firstCharOnly)
            ? string.toUpperCase()
            : string.charAt(0).toUpperCase() + string.slice(1);
    }
}

export function LowerFilter() {
    return function(input) {
        return input.toLowerCase();
    };
}

/*
 https://toddmotto.com/everything-about-custom-filters-in-angular-js/
 <ul>
     <li ng-repeat="friend in person.friends | startsWithA">
        {{ friend }}
     </li>
 </ul>
 */
export function startsWithA() {
    return function (items) {
        var filtered = [];
        for (var i = 0; i < items.length; i++) {
            var item = items[i];
            if (/a/i.test(item.name.substring(0, 1))) {
                filtered.push(item);
            }
        }
        return filtered;
    };
}






