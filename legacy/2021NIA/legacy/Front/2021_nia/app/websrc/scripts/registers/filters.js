import {CommaFilter} from '../filter/numberFilter';
import {UpperFilter, LowerFilter, startsWithA} from '../filter/textFilter';
import {DateFormatFilter} from '../filter/dateFormatFilter';
import {groupBy} from '../filter/groupBy';
import {RcaAlarmFilter, RcaAlarmTimeFilter, NiaAlarmFilter, NiaPfFilter} from '../filter/rcaTicketFilter';


function filters() {
    this.filter('commaFilter', CommaFilter);
    this.filter('toUpper', UpperFilter);
    this.filter('toLower', LowerFilter);
    this.filter('startsWithA', startsWithA);
    this.filter('dateFormat', DateFormatFilter);
    this.filter('groupBy', groupBy);

    this.filter('rcaAlarmFilter', RcaAlarmFilter);
    this.filter('rcaAlarmTimeFilter', RcaAlarmTimeFilter);
    this.filter('niaAlarmFilter', NiaAlarmFilter);
    this.filter('niaPfFilter', NiaPfFilter);

}

export {filters}
