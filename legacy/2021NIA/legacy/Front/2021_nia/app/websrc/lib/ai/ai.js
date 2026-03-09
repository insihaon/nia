/*!
 *
 * WebRTC Lab 
 * @author dodortus (codejs.co.kr / dodortus@gmail.com)
 *
 */

$(function () {

    let AI = (function () {

        let instance;
        let $scope, tools, events;

        if (typeof webkitSpeechRecognition !== 'function') {
            alert('크롬에서만 동작 합니다.');
            return false;
        }

        String.prototype.trim = function () {
            return this.replace(/^\s+|\s+$/g, "");
        }
        String.prototype.ltrim = function () {
            return this.replace(/^\s+/, "");
        }
        String.prototype.rtrim = function () {
            return this.replace(/\s+$/, "");
        }

        let simpleTts = false;
        let isRecognizing = false;
        let recordOff = true;
        let ignoreEndProcess = false;
        let finalTranscript = '';
        let intervalTimer = null;
        let lastCommand = null;

        const audio = document.querySelector('#audio');
        const recognition = new webkitSpeechRecognition();
        const language = 'ko-KR';
        const two_line = /\n\n/g;
        const one_line = /\n/g;
        const first_char = /\S/;

        let $btnMic;
        let $recognition;
        let $iconMusic;

        let talkIndex = 0;
        let speed = 1.0;
        /*@formatter:off*/
        const talk = [
            {index: 0, talker: 'USER', message: '무슨 일이야?', keywords: ['무슨', '일']},
            {index: 1, talker: 'SYSTEM', message: '장애가 발생했습니다.', end_action: {page: '2_1', run: '#차트가 빤짝거리면서 실시간 증가. 장애데이터 출력. '}, delay: (2.5 + 0.5) * speed },
            {index: 2, talker: 'SYSTEM', message: '동부산 지사 부근 모든 도메인에서 임계치를 넘어서는 장애 경보가 발생되고 있습니다.', end_action: {page: '2_2', run: '#무선, IP, 전송 토폴로지 순차적 보임'}, delay: 7 * speed },
            {index: 3, talker: 'SYSTEM', message: '무선, IP, 전송 모든 도메인의 토폴로지 상관관계를 1차 분석한 결과 다량의 광케이블 장애로 판단됩니다.', delay: (9 + 3) * speed },
            {index: 4, talker: 'SYSTEM', message: '동시간 대 다량의 광케이블 장애가 발생되었으므로 All-in-OSP를 연결하여 2차 정밀 진단을 시작합니다.', action: {run: '#01_3D 토폴로지가 사라지면서 5장 광케이블 그림이 겹쳐 출력 '}, delay: 8 * speed },
            {index: 5, talker: 'SYSTEM', message: '장애 광케이블 정보 전달을 통한 All-in-OSP 의 GIS 분석을 진행중입니다.', delay: 7 * speed },
            {index: 6, talker: 'SYSTEM', message: '분석결과 동부산지사 통신구 장애로 확인 되었습니다.', action: {run: '#02_통신구 위치가 줌인 되면서 출력, 동부산 지사 통신구 장애 깜빡거림'}, delay: 6 * speed },
            {index: 7, talker: 'USER', message: '피해 현황은 어때?', keywords: ['피해', '현황', '어때']},
            {index: 8, talker: 'SYSTEM', message: '전송 분야 17건, IP/무선 분야 3건, 총 20건의 서비스 장애가 확인됩니다.', action: {page: '3_1', run: '#서비스 상태에 ‘서비스 중단＇ 이라고 나오고 복구가능한 회선은 깜빡깜빡하게 표시하기' }, delay: (7.5 + 0.5) * speed },
            {index: 9, talker: 'USER', message: '조치방안을 알려 줘', keywords: ['조치', '방안', '알려']},
            {index: 10, talker: 'SYSTEM', message: '전송 분야 17건은 우회절체가 가능하여 T-SDN 우회절체를 수행하겠습니다.', action: {page: '3_2', run: '#T-SDN 루트가 변경되기 전 화면만 출력'}, delay: (6.7 + 0.5) * speed },
            {index: 11, talker: 'SYSTEM', message: '대량회선 절체 기능을 사용하여 지금 즉시 동부산지사 MSPP 장비를 북부산지사 MSPP 장비로 우회하여 17회선의 우회 절체를 완료하였습니다.', action: {run: '#03_T-SDN 루트가 변경된 후 화면 띄움'}, delay: (11 + 3) * speed },
            {index: 12, talker: 'SYSTEM', message: '전송 분야 17건의 회선이 복구 되었습니다. IP/무선 분야 3건의 회선은 자동 복구가 불가능합니다. ', action: {page: '3_3', run: '#서비스 상태에 ‘서비스 복구 이라고 나오고 복구완료한 17개 회선 깜빡깜빡하게 표시하기'}, delay: (7.5 + 0.5) * speed },
            {index: 13, talker: 'USER', message: '자동 복구가 안되는 회선은 어떻게 하지?', keywords: ['자동', '복구', '회선', '어떻게', '어떡해']},
            {index: 14, talker: 'SYSTEM', message: '장애 국사의 인접 국사로 우회 연결이 가능합니다. 현재 3개의 인접 국사가 조회되며 북부산 국사를 추천드립니다. \n북부산 국사에 절체 자원이 충분한 것으로 확인되어 광케이블만 연결하시면 됩니다.', action: {page: '3_4', run: '#3초간 동부산 빨간색 깜빡 -> 동부산국사의 인접국사(후보국사)는 초록색으로 깜빡거리며 테이블 내용 출력'},  end_action: {run: '#04_북부산국사가 파란색으로 색이 바뀌면서 깜빡거림'}, delay: 15 * speed },
            {index: 15, talker: 'USER', message: '북부산 국사로 우회 광케이블 루트를 찾아 줘', keywords: ['북부산', '부산', '국사', '우회', '위에', '광케이블', '루트', '찾아']},
            {index: 16, talker: 'SYSTEM', message: 'OSP-SDN 으로 연결하여 최적 루트를 검색한 결과 2번 루트로 신규 광케이블 연결이 가능합니다.', action: {page: '3_5', run: '#OSP-SDN 루트 변경전/변경후 모두 표시, 위치테이블 출력' }, delay: 8.5 * speed },
            {index: 17, talker: 'SYSTEM', message: '해당 루트로 신규 광케이블 포설용 작업지시서를 생성할까요?', action: {run: '#05_최적 루트로 변경 후 광케이블 루트가 빨간색 깜빡깜빡'}, delay: 5.5 * speed },
            {index: 18, talker: 'USER', message: '생성해 줘', keywords: ['처리', '생성', '실행', '성']},
            {index: 19, talker: 'SYSTEM', message: '작업지시서를 생성 하였습니다.', action: {page: '3_6', run: '#작업지시서가 나옴'}, delay: (2.5 + 1) * speed },
            {index: 20, talker: 'SYSTEM', message: '관할 국사 직원에게 지시서를 할당합니다.', action: {run: '#06_작업사항 테이블 출력'}, delay: (3.5 + 2) * speed },
            {index: 21, talker: 'USER', message: '덕분에 한시름 놓았네, 고마워 닥터로렌', keywords: ['덕분', '한시름', '고마워', '닥터', '로렌']},
            {index: 22, talker: 'SYSTEM', message: '관제모드로 돌아갑니다.', delay: (2 + 2) * speed },
            {index: 23, talker: 'SYSTEM', message: '', action: {page: '1_1'}},
        ];
        /*@formatter:on*/

        recognition.continuous = true;
        recognition.interimResults = true;

        /**
         * 음성 인식 시작 처리
         */
        recognition.onstart = function () {
        // console.log('onstart', arguments);
            isRecognizing = true;
            $btnMic.attr('class', 'on');
        };

        /**
         * 음성 인식 종료 처리
         * @returns {boolean}
         */
        recognition.onend = function (event) {

        // console.log('onend', arguments, ', ignoreEndProcess=' + ignoreEndProcess, ', finalTranscript=' + finalTranscript);

            stop();

            if (!recordOff || ignoreEndProcess) {
                start();
                return false;
            }

            // DO end process
            if (!finalTranscript) {
            // console.log('empty finalTranscript');
                return false;
            }
        };

        /**
         * 음성 인식 결과 처리
         * @param event
         */
        recognition.onresult = function (event) {

        // console.log('onresult', event);

            if (recordOff == true) {
                return;
            }

            let interimTranscript = '';
            if (typeof (event.results) === 'undefined') {
                recognition.onend = null;
                recognition.stop();
                return;
            }

            for (let i = event.resultIndex; i < event.results.length; ++i) {
                if (event.results[i].isFinal) {
                    finalTranscript = event.results[i][0].transcript;
                    showDemoText(finalTranscript);
                } else {
                    interimTranscript = event.results[i][0].transcript;
                }
            }

            finalTranscript = capitalize(finalTranscript);

            if (finalTranscript.length > 0) {
                final_span.innerHTML = '';
                final_span.innerHTML = linebreak(finalTranscript);
                console.log('finalTranscript', finalTranscript);
                fireCommand(finalTranscript);
            }

            if (interimTranscript.length > 0) {
                interim_span.innerHTML = linebreak(interimTranscript);
                // console.log('interimTranscript', interimTranscript);
                // fireCommand(interimTranscript);
            }

        };

        function showDemoText(message, color) {
            message = message || '';

            if (message.length < 1) return;

            let container = document.getElementById('chat')
            let messageElement = document.createElement('div')
            messageElement.innerText = message;
            messageElement.style.color = color || '#000';
            container.appendChild(messageElement);

            $(container).scrollTop(container.scrollHeight);
        }

        /**
         * 음성 인식 에러 처리
         * @param event
         */
        recognition.onerror = function (event) {
            // console.log('onerror', event);

            if (event.error.match(/no-speech|audio-capture|not-allowed/)) {
                ignoreEndProcess = true;
            } else if (event.error == 'network') {
                recordOff = true;
            } else {
                console.error(event.error)
            }
        };

        /**
         * 명령어 처리
         * @param string
         */
        function fireCommand(string) {

            string = string || '';
            if (string.length < 1)
                return;

            lastCommand = string;

            function run(keyword) {

                setTimeout(function () {
                    executeScript(1);

                    // console.log('Index=' + talkIndex + ' keyword=' + keyword);
                    document.getElementById("keyword").innerHTML = keyword;
                }, 1500);
            }

            if (intervalTimer) {
                return;
            }

            function clearTimer() {
                if (intervalTimer) {
                    clearTimeout(intervalTimer);
                    intervalTimer = null;
                }
            }

            try {

                string = string.trim();
                let keywords = talk[talkIndex].keywords || [];
                let found = keywords.find(function (k) {
                    return string.includes(k);
                });

                if (found) {
                    setTimeout(run.bind(null, found), 200);
                } else if (string.endsWith('줘') || string.endsWith('야') || string.endsWith('해') || string.endsWith('때')) {
                    run(string.substr(string.length - 1, 1));
                } else if (string == '이전' || string == '다시' || string.endsWith('음')) {
                    if (events && events.onProcess) {
                        events.onProcess.call($scope, null, {action: string});
                    }
                } else if (string.endsWith('닥터로렌') || string.endsWith('닥터 로렌') || string.endsWith('로렌')) {
                    textToSpeech('네 ');
                } else if (string == '하이' || string == '아이') {
                    textToSpeech('안녕하세요.');
                }

                intervalTimer = setTimeout(function () {
                    clearTimer();
                }, 2000);

            } catch (e) {
            }
        }

        /**
         * 개행 처리
         * @param s
         * @returns {string}
         */
        function linebreak(s) {
            return s.replace(two_line, '<p></p>').replace(one_line, '<br>');
        }

        /**
         * 첫문자를 대문자로 변환
         * @param s
         * @returns {string | void | *}
         */
        function capitalize(s) {
            return s.replace(first_char, function (m) {
                return m.toUpperCase();
            });
        }

        /**
         * 음성 인식 트리거
         * @param event
         */
        function start(event) {

            if (isRecognizing) {
                stop();

                if (event) {
                    recordOff = true;
                    return;
                }
            }

            try {

                if (event) {
                    recordOff = false;
                }

                if (recordOff == false) {
                    ignoreEndProcess = false;

                    finalTranscript = '';
                    final_span.innerHTML = '';
                    interim_span.innerHTML = '';

                    // console.log('============ Start');

                    recognition.lang = language;
                    recognition.start();
                }

            } catch (e) {
            }
        }

        function stop() {
            isRecognizing = false;
            $btnMic.attr('class', 'off');
            recognition.stop();
            // console.log('============ Stop');
        }

        /**
         * 문자를 음성으로 읽어 줍니다.
         * 지원: 크롬, 사파리, 오페라, 엣지
         */
        function textToSpeech(text, onend) {

            function speech(text, first, last) {

                // if (recordOff == true) {
                //     return;
                // }

                // <editor-fold desc="[speechSynthesis option]">
                const u = new SpeechSynthesisUtterance();
                u.text = text;
                u.lang = 'ko-KR';
                u.rate = 1.0;
                u.pitch = -1.3;
                u.onstart = function (event) {
                    if (first) {
                        stop();
                    }
                };
                u.onend = function (event) {
                    console.log('Finished in ' + event.elapsedTime / 1000 + ' seconds.', event.currentTarget.text);

                    // let {pending, speaking} = speechSynthesis;
                    // pending == false && speaking == false 이면 더 이상 플레이 할 보이스가 없다는 것임
                    if (last) {
                        start();
                        if (onend) onend.call(null, event);
                    }
                };
                // u.voice = speechSynthesis.getVoices()[11];

                setTimeout( function () {
                    speechSynthesis.speak(u);
                })

                // <!--</editor-fold desc="[speechSynthesis option]">

                // <editor-fold desc="[Simple Version]">
                // speechSynthesis.speak(new SpeechSynthesisUtterance(arr[i]));
                // <!--</editor-fold desc="[Simple Version]">

            }

            // try {
            //     let p = new SpeechSynthesisUtterance('테스트입니다. ');
            //     speechSynthesis.speak(p);
            // } catch (e) {
            //
            // }

            // console.log('textToSpeech', arguments);
            showDemoText(text, '#F00');

            let arr = text.split('\n');
            for (let i in arr) {

                let t = simpleTts ? arr[i].substr(0, arr[i].indexOf(' ')) : arr[i]

                // 1 문장만 TTS
                // speech(t, true);
                // break;

                // 모든 문장 TTS
                speech(t, i == 0, arr.length - 1 == i);

            }
        }

        /**
         * 미사용
         * requestServer
         * key - AIzaSyDiMqfg8frtoZflA_2LPqfGdpjmgTMgWhg
         */
        function requestServer() {
            $.ajax({
                method: 'post',
                url: 'https://www.google.com/speech-api/v2/recognize?output=json&lang=en-us&key=AIzaSyDiMqfg8frtoZflA_2LPqfGdpjmgTMgWhg',
                data: '/examples/speech-recognition/hello.wav',
                contentType: 'audio/l16; rate=16000;', // 'audio/x-flac; rate=44100;',
                success: function (data) {

                },
                error: function (xhr) {

                }
            });
        }

        /**
         * 초기 바인딩
         */
        function initialize(s, t, e) {

            $scope = s;
            tools = t;
            events = e;

            simpleTts = false;
            isRecognizing = false;
            recordOff = true;
            ignoreEndProcess = false;
            finalTranscript = '';
            intervalTimer = null;
            lastCommand = null;

            talkIndex = 0;

            $btnMic = $('#btn-mic');
            $recognition = $('#recognition');
            $iconMusic = $('#icon-music');

            $btnMic.click(start);
            toggleMic(true);

            $('#btn-tts').click(function () {
                const text = $('#final_span').text() || '전 음성 인식된 글자를 읽습니다.';
                textToSpeech(text);
            });

            $('#retry').click(executeScript.bind(this, 0, true));
        }

        function toggleMic(turnOn) {

            if (turnOn == undefined) {
                turnOn = recordOff;
            }

            if (turnOn && recordOff) {
                $btnMic.trigger('click');
            } else if (turnOn == false && recordOff == false) {
                $btnMic.trigger('click');
            }
        }

        function getCurrentTalk() {
            return talk[talkIndex];
        }

        function goIndex(index) {
            if (talkIndex != index) {
                talkIndex = index;
                executeScript(0, true);
            }
        }

        function goNext() {
            executeScript(1, true);
        }

        function goPrev(talker) {
        if (talker) {
            for (let i = talkIndex - 1; i >= 0; i--) {
                if (talk[i].talker == talker) {
                    talkIndex = i;
                    break;
                }
            }
        } else {
            talkIndex -= 1;
        }
            executeScript(0, true);
        }

        window.goIndex = function (index) {
            // 엔터키의 코드는 13입니다.
            if (event.keyCode == 13) {
                // talkIndex = parseInt(document.getElementById("go-index").value);
                // executeScript(0);
                goIndex(parseInt(document.getElementById("go-index").value));
            }
        };

        function makeHtmlAiScript(ai) {
            let system = ai.talker == 'SYSTEM';
            let talker = (system ? ' [닥터로렌, ' : '<div style="color:blue">[운용자, ') + ai.index + '] ';
            let message = talker + ai.message.replace('\n', '');
            if (ai.keywords) {
                ai.keywords.forEach(function (keyword) {
                    message = message.replace(keyword, '<strong>' + keyword + '</strong>');
                });
            }
            message += system ? '' : '</div>';
            message = '<div>' + message + '</div>';
            return message;
        }

        function showAllScript() {
            let $allScenario = $('#all-scenario');
            $allScenario.empty();

            let html = '';
            talk.forEach(function (i) {
                html += makeHtmlAiScript(i);
            });

            $allScenario.append(html)
        }

        function executeScript(offset, cancel) {

            if (cancel) {
                speechSynthesis.cancel();
            }

            function step(offset) {

                try {

                    let $scenario = $('#scenario');
                    $scenario.empty();

                    let html = '';
                    for (let i = 0; i < 4; i++) {
                        let ai = talk[talkIndex + i];
                        html += makeHtmlAiScript(ai);
                    }

                    $scenario.append(html)


                } catch (e) {
                }

                talkIndex += offset;
                if (talkIndex >= talk.length) {
                    talkIndex = 0;
                    executeScript(0);
                }
            }

            step(offset);

            try {

                if (talk[talkIndex].talker == 'SYSTEM') {
                    // console.log('Index=' + talkIndex);
                    textToSpeech(talk[talkIndex].message, function (event) {
                    });

                    if (events && events.onProcess) {
                        events.onProcess.call($scope, talkIndex, talk[talkIndex]);
                    }
                }

            } catch (e) {
            }
        }

        function createInstance() {
            return {
                initialize,
                showAllScript,
                showDemoText,
                goPrev,
                goNext,
                goIndex,
                getCurrentTalk,
                executeScript,
                textToSpeech,
                toggleMic
            }
        }

        return {
            getInstance: function ($scope, tools, events) {
                if (!instance) {
                    instance = createInstance();
                    instance.initialize($scope, tools, events);
                }
                return instance;
            }
        }
    })();


    setTimeout(() => {

        let aiTimer = null;
        let $btnPrev = $('#btn-prev');
        let $btnNext = $('#btn-next');

        $btnPrev.click(function () {
            let {ai} = this;
            killAiTimer();
            ai.goPrev.call(ai);
        }.bind(this));

        $btnNext.click(function () {
            let {ai} = this;
            killAiTimer();
            ai.goNext.call(ai);
        }.bind(this));

        function killAiTimer() {
            if (aiTimer) {
                clearTimeout(aiTimer);
                aiTimer = null;    
            }
        }

        let onProcess = (index, talk) => {

            let {ai} = this;

            if (!talk)
                return;

            try {

                if (talk.action) {

                    let {page, video, run} = talk.action || {};

                    if (page) {
                        ai.showDemoText('#페이지전환:' + page, '#4547BA');
                    }

                    if (video) {
                    }

                    if (run) {
                        ai.showDemoText(run, '#40903D');
                    }

                    switch (talk.action) {
                        case '이전':
                            ai.goPrev.call(ai, 'USER');
                            return;
                        case '다시':
                            ai.goPrev.call(ai, 'SYSTEM');
                            return;
                        case '다음':
                        case '마음':
                            ai.goNext.call(ai);
                            return;
                    }
                }

                let delay = talk.delay || 0;
                aiTimer = setTimeout(() => {

                    killAiTimer();

                    let {run, page} = talk.end_action || {};

                    if (page) {
                        ai.showDemoText('#페이지전환:' + page, '#4547BA');
                    }

                    if (run) {
                        ai.showDemoText(run, '#40903D');
                    }
                    ai.goNext.call(ai);
                }, delay * 1000);

            } catch (e) {
            }
        };

        let onMicChange = () => {
        };

        var events = {
            onProcess: onProcess.bind(this),
            onMicChange: onMicChange.bind(this),
        };

        this.ai = AI.getInstance(null, null, events);  //(typeof exports !== 'undefined' ? exports : this);
        this.ai.showAllScript();

        speechSynthesis.cancel();

        this.ai.textToSpeech('환영합니다.');

    }, 500);

})
