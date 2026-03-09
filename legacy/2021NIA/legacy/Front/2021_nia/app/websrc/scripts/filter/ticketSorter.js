export function TicketSorter() {
    function StatusOrder(item)
    {
        switch(item) {
            case 'INIT':
                return 1;
            case 'ACK':
                return 2;
            case 'FIX':
                return 3;
            case 'AUTO_FIN':
                return 4;
            case 'FIN':
                return 5;
            default:
                return 6;
        }
    }
    return function(items) {
        var filtered = [];
        angular.forEach(items, function(item) {
            filtered.push(item);
        });
        filtered.sort(function (a, b) {

            if(a.status != b.status){
                return (StatusOrder(a.status) > StatusOrder(b.status) ? 1 : -1);
            } else {
                if(a.status == b.status) {
                    return (a.ticket_id > b.ticket_id ? -1 : 1);
                }
                return (a.ticket_generation_time > b.ticket_generation_time ? -1 : 1);
            }
        });
        return filtered;
    };
}
