import 'angular'

let event = function (rx) {

    let subject = window.subject || new rx.Subject();
    window.subject = subject;

    return {
        subscribe: function (obj) {
            return subject.subscribe(obj);
        },
        fireEvent: function (obj) {
            subject.onNext(obj);
        },
    };

    // [rx를 이용한 통신 예제](https://plnkr.co/edit/YVKGhCx0DrzyzckMDjtv?p=preview)
};

export default ['rx', event]


