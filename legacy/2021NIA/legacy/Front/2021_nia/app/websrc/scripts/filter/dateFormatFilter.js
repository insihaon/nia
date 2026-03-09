export function DateFormatFilter() {
    return function (input, format1, format2) {
        if(!input){
            return '';
        }

        format1 = format1 || 'YYYY-MM-DD HH:mm:ss';
        format2 = format2 || 'YYYY-MM-DD HH:mm:ss';
        if (input instanceof Date) // returns true or false
        {
            return moment(input.getTime()).format(format1);
        } else if (!isNaN(input)) {
            return moment(input).format(format1);
        } else {
            return moment(input, format1).format(format2);
        }
    }
}
