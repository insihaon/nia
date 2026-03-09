(function() {

    Date.prototype.yyyymmdd = function() {
        return this.getFullYear() + "년 " +  (this.getMonth() + 1) + "월 " + this.getDate() + "일";
    };

    Date.prototype.yyyymm = function() {
        return this.getFullYear() + "년 " +  (this.getMonth() + 1) + "월";
    };

    Date.prototype.yyyymmweek = function() {
        return this.getFullYear() + "년 " +  (this.getMonth() + 1) + "월 " + this.getMonthWeek() + "주" ;//+ "(" + this.getWeek() + ")";
    };

    Date.prototype.yyyy_mm_dd = function() {
        var year = this.getFullYear().toString();
        var month = (this.getMonth()+1).toString();
        var day  = this.getDate().toString();

        return year + '-' + (month[1]?month:"0"+month[0]) + '-' + (day[1]?day:"0"+day[0]);
    };

    Date.prototype.getWeek = function() {
        // Copy date so don't modify original
        var d = new Date(this.getFullYear(), this.getMonth(), this.getDate());
        // Set to nearest Thursday: current date + 4 - current day number
        // Make Sunday's day number 7
        //d.setDate(d.getDate() + 4 - (d.getDay()||7));
        d.setDate(d.getDate() + 4 - d.getDay());
        // Get first day of year
        var yearStart = new Date(d.getFullYear(),0,1);
        // Calculate full weeks to nearest Thursday
        var weekNo = Math.ceil(( ( (d - yearStart) / 86400000) + 1)/7);
        // Return array of year and week number
        return weekNo;
    };

    Date.prototype.rangeWeek = function(add) {
        var dt = new Date(this.getTime());
        if(add != null && add !== 0) {
            dt = dt.addDays(7 * add);
        }
        dt = new Date(dt.getFullYear(), dt.getMonth(), dt.getDate());
        dt = new Date(dt.getTime() - dt.getDay() * 1000 * 60 * 60 * 24);
        return { start: dt, end: new Date(dt.getTime() + 1000 * 60 * 60 * 24 * 7 - 1) };
    };

    Date.prototype.rangeMonth = function(add) {
        var month = add || 0;
        var start = new Date(this.getFullYear(), this.getMonth() + month, 1);
        return { start: start, end: new Date(this.getFullYear(), this.getMonth() + month, start.getDaysInMonth()) };
    };

    Date.prototype.daysInMonth = function()
    {
        return new Date(this.getFullYear(), this.getMonth() + 1, 0).getDate();
    };

    Date.prototype.getMonthWeek = function(){
        var firstDay = new Date(this.getFullYear(), this.getMonth(), 1).getDay();
        return Math.ceil((this.getDate() + firstDay)/7);
    };

    Date.prototype.countWeeksOfMonth2 = function() {
        var firstDay = new Date(this.getFullYear(), this.getMonth(), 1).getDay();
        return Math.ceil((this.daysInMonth() + firstDay)/7);
    };

    //new Date(2012,12-1,1).countWeeksOfMonth(); // 6
    Date.prototype.countWeeksOfMonth = function() {
        var year         = this.getFullYear();
        var month_number = this.getMonth();
        var firstOfMonth = new Date(year, month_number, 1);
        var lastOfMonth  = new Date(year, month_number+1, 0);
        var used         = firstOfMonth.getDay() + lastOfMonth.getDate();
        return Math.ceil( used / 7);
    };

    Date.prototype.addDays = function (num) {
        var value = this.valueOf();
        value += 86400000 * num;
        return new Date(value);
    };

    Date.prototype.addSeconds = function (num) {
        var value = this.valueOf();
        value += 1000 * num;
        return new Date(value);
    };

    Date.prototype.addMinutes = function (num) {
        var value = this.valueOf();
        value += 60000 * num;
        return new Date(value);
    };

    Date.prototype.addHours = function (num) {
        var value = this.valueOf();
        value += 3600000 * num;
        return new Date(value);
    };

    Date.isLeapYear = function (year) {
        return (((year % 4 === 0) && (year % 100 !== 0)) || (year % 400 === 0));
    };

    Date.getDaysInMonth = function (year, month) {
        return [31, (Date.isLeapYear(year) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month];
    };

    Date.prototype.isLeapYear = function () {
        return Date.isLeapYear(this.getFullYear());
    };

    Date.prototype.getDaysInMonth = function () {
        return Date.getDaysInMonth(this.getFullYear(), this.getMonth());
    };

    Date.prototype.addMonths = function (value) {
        var n = this.getDate();
        this.setDate(1);
        this.setMonth(this.getMonth() + value);
        this.setDate(Math.min(n, this.getDaysInMonth()));
        return this;
    };

    this.daysInMonth = function(iMonth, iYear)
    {
        return new Date(iYear, iMonth, 0).getDate();
    };

    this.getWeekNumber = function(d) {
        // Copy date so don't modify original
        d = new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()));
        // Set to nearest Thursday: current date + 4 - current day number
        // Make Sunday's day number 7
        //d.setDate(d.getDate() + 4 - (d.getDay()||7));
        d.setDate(d.getDate() + 4 - d.getDay());
        // Get first day of year
        var yearStart = new Date(d.getFullYear(),0,1);
        // Calculate full weeks to nearest Thursday
        var weekNo = Math.ceil(( ( (d - yearStart) / 86400000) + 1)/7);
        // Return array of year and week number
        return [d.getFullYear(), weekNo];
    };

    this.stopActions = function ($event) {
        if ($event.stopPropagation) {
            $event.stopPropagation();
        }
        if ($event.preventDefault) {
            $event.preventDefault();
        }
        $event.cancelBubble = true;
        $event.returnValue = false;
    };
})();