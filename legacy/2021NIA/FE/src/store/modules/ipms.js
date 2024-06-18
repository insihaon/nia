const state = {
    EventType: {
        changeLvl: 'changeLvl',
    },
    tempAuthCenterList: [
        {
            'label': '-------',
            'value': '000000'
        },
        {
            'label': '네트워크관제센터',
            'value': '369794'
        },
        {
            'label': 'IP운용센터',
            'value': '369807'
        },
        {
            'label': '미디어운용센터',
            'value': '369819'
        },
        {
            'label': '제주고객본부',
            'value': '382932'
        },
        {
            'label': '강북/강원NW운용본부',
            'value': '382973'
        },
        {
            'label': '강남/서부NW운용본부',
            'value': '383040'
        },
        {
            'label': '충남/충북NW운용본부',
            'value': '383126'
        },
        {
            'label': '전남/전북NW운용본부',
            'value': '383167'
        },
        {
            'label': '대구/경북NW운용본부',
            'value': '383212'
        },
        {
            'label': '부산/경남NW운용본부',
            'value': '383251'
        },
        {
            'label': '국제통신운용센터',
            'value': '423440'
        },
        {
            'label': 'IDC운용센터',
            'value': 'VV0053'
        }
    ],
    tempOfficeList: [
        {
            'label': '-------',
            'value': '000000'
        },
        {
            'label': '가경국사',
            'value': 'R02471'
        },
        {
            'label': '가락국사',
            'value': 'R00444'
        },
        {
            'label': '가야국사',
            'value': 'R00949'
        },
        {
            'label': '가양국사',
            'value': 'R00458'
        },
        {
            'label': '가평국사',
            'value': 'R00780'
        },
        {
            'label': '강경국사',
            'value': 'R02386'
        },
        {
            'label': '강동국사',
            'value': 'R00445'
        },
        {
            'label': '강릉국사',
            'value': 'R03485'
        },
        {
            'label': '강북국사',
            'value': 'R00430'
        },
        {
            'label': '강서국사',
            'value': 'R00435'
        },
        {
            'label': '강원고성국사',
            'value': 'R03762'
        },
        {
            'label': '강진국사',
            'value': 'R03137'
        },
        {
            'label': '강화국사',
            'value': 'R00843'
        },
        {
            'label': '개봉국사',
            'value': 'R00442'
        },
        {
            'label': '거제국사',
            'value': 'R01225'
        },
        {
            'label': '거창국사',
            'value': 'R01202'
        },
        {
            'label': '건천국사',
            'value': 'R01570'
        },
        {
            'label': '검단분기국사(서인천)',
            'value': 'R00512'
        },
        {
            'label': '경기광주국사',
            'value': 'R00880'
        },
        {
            'label': '경기중앙국사',
            'value': 'R00536'
        },
        {
            'label': '경산국사',
            'value': 'R01740'
        },
        {
            'label': '경주국사',
            'value': 'R01534'
        },
        {
            'label': '계룡국사',
            'value': 'R02392'
        },
        {
            'label': '계양국사',
            'value': 'R00499'
        },
        {
            'label': '고덕국사',
            'value': 'R00446'
        },
        {
            'label': '고령국사',
            'value': 'R01827'
        },
        {
            'label': '고산국사',
            'value': 'R01461'
        },
        {
            'label': '고성국사',
            'value': 'R01131'
        },
        {
            'label': '고양국사',
            'value': 'R00590'
        },
        {
            'label': '고창국사',
            'value': 'R03295'
        },
        {
            'label': '고흥국사',
            'value': 'R02751'
        },
        {
            'label': '곡성국사',
            'value': 'R02792'
        },
        {
            'label': '공릉국사',
            'value': 'R00450'
        },
        {
            'label': '공주국사',
            'value': 'R02153'
        },
        {
            'label': '공항국사',
            'value': 'R00456'
        },
        {
            'label': '과천국사',
            'value': 'R00457'
        },
        {
            'label': '관악국사',
            'value': 'R00433'
        },
        {
            'label': '광산국사',
            'value': 'R02886'
        },
        {
            'label': '광양국사',
            'value': 'R02842'
        },
        {
            'label': '광주국사',
            'value': 'R02590'
        },
        {
            'label': '광주하남국사',
            'value': 'R02893'
        },
        {
            'label': '광진국사',
            'value': 'R00428'
        },
        {
            'label': '광천국사',
            'value': 'R02393'
        },
        {
            'label': '광화문국사',
            'value': 'R00412'
        },
        {
            'label': '괴산국사',
            'value': 'R02564'
        },
        {
            'label': '구례국사',
            'value': 'R02810'
        },
        {
            'label': '구로국사',
            'value': 'R00437'
        },
        {
            'label': '구리국사',
            'value': 'R00567'
        },
        {
            'label': '구미공단국사',
            'value': 'R01636'
        },
        {
            'label': '구미국사',
            'value': 'R01628'
        },
        {
            'label': '구봉국사',
            'value': 'R02122'
        },
        {
            'label': '구포국사',
            'value': 'R01124'
        },
        {
            'label': '군산국사',
            'value': 'R03203'
        },
        {
            'label': '군위국사',
            'value': 'R02086'
        },
        {
            'label': '군포국사',
            'value': 'R00599'
        },
        {
            'label': '금곡국사',
            'value': 'R00166'
        },
        {
            'label': '금사국사',
            'value': 'R01119'
        },
        {
            'label': '금산국사',
            'value': 'R02333'
        },
        {
            'label': '금왕국사',
            'value': 'R02498'
        },
        {
            'label': '금정국사',
            'value': 'R00951'
        },
        {
            'label': '금천국사',
            'value': 'R00455'
        },
        {
            'label': '기장국사',
            'value': 'R01450'
        },
        {
            'label': '김제국사',
            'value': 'R03251'
        },
        {
            'label': '김천국사',
            'value': 'R01641'
        },
        {
            'label': '김포국사',
            'value': 'R00897'
        },
        {
            'label': '김해국사',
            'value': 'R01044'
        },
        {
            'label': '나주국사',
            'value': 'R02730'
        },
        {
            'label': '남광주국사',
            'value': 'R02598'
        },
        {
            'label': '남대구국사',
            'value': 'R01462'
        },
        {
            'label': '남대전국사',
            'value': 'R02123'
        },
        {
            'label': '남부산국사',
            'value': 'R00954'
        },
        {
            'label': '남수원국사',
            'value': 'R00476'
        },
        {
            'label': '남양국사',
            'value': 'R00809'
        },
        {
            'label': '남양주국사',
            'value': 'R00640'
        },
        {
            'label': '남울산국사',
            'value': 'R01112'
        },
        {
            'label': '남원국사',
            'value': 'R03271'
        },
        {
            'label': '남원주국사',
            'value': 'R03446'
        },
        {
            'label': '남전주국사',
            'value': 'R03154'
        },
        {
            'label': '남천안국사',
            'value': 'R02151'
        },
        {
            'label': '남청주국사',
            'value': 'R04000'
        },
        {
            'label': '남춘천국사',
            'value': 'R03475'
        },
        {
            'label': '남해국사',
            'value': 'R01153'
        },
        {
            'label': '내서국사',
            'value': 'R00990'
        },
        {
            'label': '노원국사',
            'value': 'R00449'
        },
        {
            'label': '녹동국사',
            'value': 'R02782'
        },
        {
            'label': '논산국사',
            'value': 'R02290'
        },
        {
            'label': '능곡국사',
            'value': 'R00594'
        },
        {
            'label': '단양국사',
            'value': 'R02549'
        },
        {
            'label': '달서국사',
            'value': 'R03973'
        },
        {
            'label': '달성국사',
            'value': 'R01980'
        },
        {
            'label': '담양국사',
            'value': 'R02824'
        },
        {
            'label': '당진국사',
            'value': 'R02348'
        },
        {
            'label': '대구국사',
            'value': 'R01458'
        },
        {
            'label': '대방국사',
            'value': 'R00425'
        },
        {
            'label': '대부국사',
            'value': 'R08495'
        },
        {
            'label': '대불국사',
            'value': 'R02942'
        },
        {
            'label': '대연국사',
            'value': 'R00955'
        },
        {
            'label': '대전국사',
            'value': 'R02112'
        },
        {
            'label': '덕소국사',
            'value': 'R00577'
        },
        {
            'label': '덕양국사',
            'value': 'R00593'
        },
        {
            'label': '덕포국사',
            'value': 'R00960'
        },
        {
            'label': '도계국사',
            'value': 'R03686'
        },
        {
            'label': '도봉국사',
            'value': 'R00451'
        },
        {
            'label': '동광주국사',
            'value': 'R02592'
        },
        {
            'label': '동구미국사',
            'value': 'R01639'
        },
        {
            'label': '동군산국사',
            'value': 'R03218'
        },
        {
            'label': '동대구국사',
            'value': 'R01465'
        },
        {
            'label': '동대문국사',
            'value': 'R00415'
        },
        {
            'label': '동대전국사',
            'value': 'R02114'
        },
        {
            'label': '동두천국사',
            'value': 'R00617'
        },
        {
            'label': '동래국사',
            'value': 'R00950'
        },
        {
            'label': '동마산국사',
            'value': 'R00981'
        },
        {
            'label': '동목포국사',
            'value': 'R03826'
        },
        {
            'label': '동부산국사',
            'value': 'R00961'
        },
        {
            'label': '동부천국사',
            'value': 'R00520'
        },
        {
            'label': '동삼국사',
            'value': 'R00957'
        },
        {
            'label': '동송국사',
            'value': 'R03752'
        },
        {
            'label': '동수원국사',
            'value': 'R00466'
        },
        {
            'label': '동순천국사',
            'value': 'R03829'
        },
        {
            'label': '동안산국사',
            'value': 'R00588'
        },
        {
            'label': '동안양국사',
            'value': 'R00523'
        },
        {
            'label': '동여수국사',
            'value': 'R02675'
        },
        {
            'label': '동울산국사',
            'value': 'R01129'
        },
        {
            'label': '동의정부국사',
            'value': 'R03856'
        },
        {
            'label': '동작국사',
            'value': 'R00424'
        },
        {
            'label': '동전주국사',
            'value': 'R03162'
        },
        {
            'label': '동진주국사',
            'value': 'R01003'
        },
        {
            'label': '동촌국사',
            'value': 'R01468'
        },
        {
            'label': '동탄국사',
            'value': 'R08432'
        },
        {
            'label': '동해국사',
            'value': 'R03507'
        },
        {
            'label': '둔산국사',
            'value': 'R02127'
        },
        {
            'label': '마산국사',
            'value': 'R00964'
        },
        {
            'label': '만수국사',
            'value': 'R00517'
        },
        {
            'label': '모란국사',
            'value': 'R00527'
        },
        {
            'label': '모슬포국사',
            'value': 'R03791'
        },
        {
            'label': '목동국사',
            'value': 'R00436'
        },
        {
            'label': '목포국사',
            'value': 'R02608'
        },
        {
            'label': '무안국사',
            'value': 'R02856'
        },
        {
            'label': '무주국사',
            'value': 'R03394'
        },
        {
            'label': '문경국사',
            'value': 'R01782'
        },
        {
            'label': '문산국사',
            'value': 'R00868'
        },
        {
            'label': '미남국사',
            'value': 'R00963'
        },
        {
            'label': '밀양국사',
            'value': 'R01178'
        },
        {
            'label': '반송국사',
            'value': 'R01120'
        },
        {
            'label': '반포국사',
            'value': 'R00440'
        },
        {
            'label': '발안국사',
            'value': 'R00803'
        },
        {
            'label': '방학국사',
            'value': 'R00452'
        },
        {
            'label': '백령국사',
            'value': 'R00932'
        },
        {
            'label': '벌교국사',
            'value': 'R03130'
        },
        {
            'label': '범물국사',
            'value': 'R01487'
        },
        {
            'label': '법성포국사',
            'value': 'R02931'
        },
        {
            'label': '법원리국사',
            'value': 'R00874'
        },
        {
            'label': '보령국사',
            'value': 'R02314'
        },
        {
            'label': '보성국사',
            'value': 'R02872'
        },
        {
            'label': '보은국사',
            'value': 'R02509'
        },
        {
            'label': '본촌국사',
            'value': 'R02606'
        },
        {
            'label': '봉덕국사',
            'value': 'R01480'
        },
        {
            'label': '봉동국사',
            'value': 'R03183'
        },
        {
            'label': '봉화국사',
            'value': 'R01905'
        },
        {
            'label': '부산강서국사',
            'value': 'R01424'
        },
        {
            'label': '부송국사',
            'value': 'R03200'
        },
        {
            'label': '부안국사',
            'value': 'R03317'
        },
        {
            'label': '부여국사',
            'value': 'R02217'
        },
        {
            'label': '부천국사',
            'value': 'R00519'
        },
        {
            'label': '부평국사',
            'value': 'R00495'
        },
        {
            'label': '북광주국사',
            'value': 'R02604'
        },
        {
            'label': '북대구국사',
            'value': 'R01475'
        },
        {
            'label': '북대전국사',
            'value': 'R02362'
        },
        {
            'label': '북부산국사',
            'value': 'R00958'
        },
        {
            'label': '북부천국사',
            'value': 'R00602'
        },
        {
            'label': '북수원국사',
            'value': 'R00462'
        },
        {
            'label': '북순천국사',
            'value': 'R03830'
        },
        {
            'label': '북여수국사',
            'value': 'R02698'
        },
        {
            'label': '북일산국사',
            'value': 'R00595'
        },
        {
            'label': '북전주국사',
            'value': 'R03169'
        },
        {
            'label': '북평국사',
            'value': 'R03514'
        },
        {
            'label': '북포항국사',
            'value': 'R03989'
        },
        {
            'label': '분당국사',
            'value': 'R00603'
        },
        {
            'label': '사음국사',
            'value': 'R03584'
        },
        {
            'label': '사천국사',
            'value': 'R01260'
        },
        {
            'label': '사하국사',
            'value': 'R01121'
        },
        {
            'label': '산격국사',
            'value': 'R03962'
        },
        {
            'label': '산청국사',
            'value': 'R01247'
        },
        {
            'label': '삼례국사',
            'value': 'R03831'
        },
        {
            'label': '삼송국사',
            'value': 'R03824'
        },
        {
            'label': '삼척국사',
            'value': 'R03524'
        },
        {
            'label': '삼천포국사',
            'value': 'R01173'
        },
        {
            'label': '삽교국사',
            'value': 'R02262'
        },
        {
            'label': '상당국사',
            'value': 'R02586'
        },
        {
            'label': '상동국사',
            'value': 'R01481'
        },
        {
            'label': '상무국사',
            'value': 'R02600'
        },
        {
            'label': '상주국사',
            'value': 'R01691'
        },
        {
            'label': '서강릉국사',
            'value': 'R03501'
        },
        {
            'label': '서광양국사',
            'value': 'R02804'
        },
        {
            'label': '서광주국사',
            'value': 'R02597'
        },
        {
            'label': '서구미국사',
            'value': 'R03964'
        },
        {
            'label': '서군산국사',
            'value': 'R03204'
        },
        {
            'label': '서귀포국사',
            'value': 'R03781'
        },
        {
            'label': '서대구국사',
            'value': 'R01488'
        },
        {
            'label': '서대문국사',
            'value': 'R00423'
        },
        {
            'label': '서대전국사',
            'value': 'R02120'
        },
        {
            'label': '서면국사',
            'value': 'R00948'
        },
        {
            'label': '서문경국사',
            'value': 'R01796'
        },
        {
            'label': '서부산국사',
            'value': 'R00945'
        },
        {
            'label': '서산국사',
            'value': 'R02202'
        },
        {
            'label': '서수원국사',
            'value': 'R00461'
        },
        {
            'label': '서안동국사',
            'value': 'R01583'
        },
        {
            'label': '서안산국사',
            'value': 'R00585'
        },
        {
            'label': '서안양국사',
            'value': 'R00521'
        },
        {
            'label': '서울산국사',
            'value': 'R01118'
        },
        {
            'label': '서인천국사',
            'value': 'R00510'
        },
        {
            'label': '서창원국사',
            'value': 'R01070'
        },
        {
            'label': '서천국사',
            'value': 'R02267'
        },
        {
            'label': '서청주국사',
            'value': 'R02466'
        },
        {
            'label': '서초국사',
            'value': 'R00439'
        },
        {
            'label': '서포항국사',
            'value': 'R01507'
        },
        {
            'label': '석남국사',
            'value': 'R00513'
        },
        {
            'label': '석수국사',
            'value': 'R00522'
        },
        {
            'label': '선산국사',
            'value': 'R02104'
        },
        {
            'label': '성남국사',
            'value': 'R00525'
        },
        {
            'label': '성북국사',
            'value': 'R00418'
        },
        {
            'label': '성산포국사',
            'value': 'R03808'
        },
        {
            'label': '성서국사',
            'value': 'R01489'
        },
        {
            'label': '성수국사',
            'value': 'R00429'
        },
        {
            'label': '성주국사',
            'value': 'R02038'
        },
        {
            'label': '성환국사',
            'value': 'R02384'
        },
        {
            'label': '속초국사',
            'value': 'R03515'
        },
        {
            'label': '송도국사',
            'value': 'R00501'
        },
        {
            'label': '송우국사',
            'value': 'R00732'
        },
        {
            'label': '송정국사',
            'value': 'R02894'
        },
        {
            'label': '송탄국사',
            'value': 'R00608'
        },
        {
            'label': '송파국사',
            'value': 'R00443'
        },
        {
            'label': '수내국사',
            'value': 'R00605'
        },
        {
            'label': '수산국사',
            'value': 'R01198'
        },
        {
            'label': '수서국사',
            'value': 'R00453'
        },
        {
            'label': '수성국사',
            'value': 'R01460'
        },
        {
            'label': '수영국사',
            'value': 'R00962'
        },
        {
            'label': '수원국사',
            'value': 'R00459'
        },
        {
            'label': '수지국사',
            'value': 'R00474'
        },
        {
            'label': '순창국사',
            'value': 'R03356'
        },
        {
            'label': '순천국사',
            'value': 'R02700'
        },
        {
            'label': '숭의국사',
            'value': 'R00480'
        },
        {
            'label': '시화국사',
            'value': 'R00923'
        },
        {
            'label': '시흥국사',
            'value': 'R00635'
        },
        {
            'label': '신갈국사',
            'value': 'R00468'
        },
        {
            'label': '신금천국사',
            'value': 'R03958'
        },
        {
            'label': '신내국사',
            'value': 'R03828'
        },
        {
            'label': '신동국사',
            'value': 'R03560'
        },
        {
            'label': '신사국사',
            'value': 'R00448'
        },
        {
            'label': '신서귀포국사',
            'value': 'R03784'
        },
        {
            'label': '신제주국사',
            'value': 'R03775'
        },
        {
            'label': '신촌국사',
            'value': 'R00420'
        },
        {
            'label': '신탄진국사',
            'value': 'R02363'
        },
        {
            'label': '신태인국사',
            'value': 'R03247'
        },
        {
            'label': '아산국사',
            'value': 'R02192'
        },
        {
            'label': '아현국사',
            'value': 'R00421'
        },
        {
            'label': '안강국사',
            'value': 'R01561'
        },
        {
            'label': '안계국사',
            'value': 'R01951'
        },
        {
            'label': '안동국사',
            'value': 'R01580'
        },
        {
            'label': '안산국사',
            'value': 'R00580'
        },
        {
            'label': '안성국사',
            'value': 'R00645'
        },
        {
            'label': '안심국사',
            'value': 'R01467'
        },
        {
            'label': '안양국사',
            'value': 'R00919'
        },
        {
            'label': '안중국사',
            'value': 'R00553'
        },
        {
            'label': '애월국사',
            'value': 'R03795'
        },
        {
            'label': '양구국사',
            'value': 'R03691'
        },
        {
            'label': '양산국사',
            'value': 'R01369'
        },
        {
            'label': '양양국사',
            'value': 'R03702'
        },
        {
            'label': '양을산국사',
            'value': 'R02609'
        },
        {
            'label': '양재국사',
            'value': 'R00454'
        },
        {
            'label': '양정국사',
            'value': 'R01032'
        },
        {
            'label': '양주국사',
            'value': 'R01381'
        },
        {
            'label': '양평국사',
            'value': 'R00755'
        },
        {
            'label': '언양국사',
            'value': 'R01432'
        },
        {
            'label': '여수국사',
            'value': 'R02667'
        },
        {
            'label': '여의도국사',
            'value': 'R00447'
        },
        {
            'label': '여주국사',
            'value': 'R00682'
        },
        {
            'label': '여천국사',
            'value': 'R02897'
        },
        {
            'label': '연무국사',
            'value': 'R02387'
        },
        {
            'label': '연수국사',
            'value': 'R00922'
        },
        {
            'label': '연천국사',
            'value': 'R00926'
        },
        {
            'label': '영광국사',
            'value': 'R02914'
        },
        {
            'label': '영덕국사',
            'value': 'R01880'
        },
        {
            'label': '영도국사',
            'value': 'R00956'
        },
        {
            'label': '영동국사',
            'value': 'R00432'
        },
        {
            'label': '영등포국사',
            'value': 'R00431'
        },
        {
            'label': '영산국사',
            'value': 'R01283'
        },
        {
            'label': '영암국사',
            'value': 'R02933'
        },
        {
            'label': '영양국사',
            'value': 'R02018'
        },
        {
            'label': '영월국사',
            'value': 'R03538'
        },
        {
            'label': '영주국사',
            'value': 'R01669'
        },
        {
            'label': '영천국사',
            'value': 'R01745'
        },
        {
            'label': '영통국사',
            'value': 'R03850'
        },
        {
            'label': '예산국사',
            'value': 'R02251'
        },
        {
            'label': '예천국사',
            'value': 'R01991'
        },
        {
            'label': '오산국사',
            'value': 'R00626'
        },
        {
            'label': '오천국사',
            'value': 'R01522'
        },
        {
            'label': '옥곡국사',
            'value': 'R02846'
        },
        {
            'label': '옥천국사',
            'value': 'R02535'
        },
        {
            'label': '옥포국사',
            'value': 'R01423'
        },
        {
            'label': '온산국사',
            'value': 'R01445'
        },
        {
            'label': '온수국사',
            'value': 'R00860'
        },
        {
            'label': '와수분기국사(동송)',
            'value': 'R03756'
        },
        {
            'label': '완도국사',
            'value': 'R02956'
        },
        {
            'label': '왜관국사',
            'value': 'R01843'
        },
        {
            'label': '용산국사',
            'value': 'R00414'
        },
        {
            'label': '용인국사',
            'value': 'R00734'
        },
        {
            'label': '용전국사',
            'value': 'R02125'
        },
        {
            'label': '울릉국사',
            'value': 'R01735'
        },
        {
            'label': '울산국사',
            'value': 'R01026'
        },
        {
            'label': '울산성남국사',
            'value': 'R01023'
        },
        {
            'label': '울진국사',
            'value': 'R02061'
        },
        {
            'label': '원주국사',
            'value': 'R03424'
        },
        {
            'label': '원효국사',
            'value': 'R00434'
        },
        {
            'label': '월곡국사',
            'value': 'R00419'
        },
        {
            'label': '월배국사',
            'value': 'R01463'
        },
        {
            'label': '유구국사',
            'value': 'R02178'
        },
        {
            'label': '유성국사',
            'value': 'R02128'
        },
        {
            'label': '은평국사',
            'value': 'R00422'
        },
        {
            'label': '을지국사',
            'value': 'R00417'
        },
        {
            'label': '음성국사',
            'value': 'R02494'
        },
        {
            'label': '의령국사',
            'value': 'R01382'
        },
        {
            'label': '의성국사',
            'value': 'R01929'
        },
        {
            'label': '의왕국사',
            'value': 'R00601'
        },
        {
            'label': '의정부국사',
            'value': 'R00530'
        },
        {
            'label': '이천국사',
            'value': 'R00665'
        },
        {
            'label': '익산국사',
            'value': 'R03186'
        },
        {
            'label': '인제국사',
            'value': 'R03608'
        },
        {
            'label': '인천공항국사',
            'value': 'R04003'
        },
        {
            'label': '인천국사',
            'value': 'R00515'
        },
        {
            'label': '일광국사',
            'value': 'R01455'
        },
        {
            'label': '일산국사',
            'value': 'R00598'
        },
        {
            'label': '임실국사',
            'value': 'R03336'
        },
        {
            'label': '장림국사',
            'value': 'R01123'
        },
        {
            'label': '장성국사',
            'value': 'R02986'
        },
        {
            'label': '장수국사',
            'value': 'R03410'
        },
        {
            'label': '장승포국사',
            'value': 'R01418'
        },
        {
            'label': '장항국사',
            'value': 'R02391'
        },
        {
            'label': '장호원국사',
            'value': 'R00936'
        },
        {
            'label': '장흥국사',
            'value': 'R03006'
        },
        {
            'label': '재송국사',
            'value': 'R01128'
        },
        {
            'label': '전곡국사',
            'value': 'R00829'
        },
        {
            'label': '전농국사',
            'value': 'R00427'
        },
        {
            'label': '전주국사',
            'value': 'R03151'
        },
        {
            'label': '정선국사',
            'value': 'R03569'
        },
        {
            'label': '정읍국사',
            'value': 'R03223'
        },
        {
            'label': '제주고산국사',
            'value': 'R03792'
        },
        {
            'label': '제주국사',
            'value': 'R03771'
        },
        {
            'label': '제주남원국사',
            'value': 'R03788'
        },
        {
            'label': '제천국사',
            'value': 'R02442'
        },
        {
            'label': '조암국사',
            'value': 'R00821'
        },
        {
            'label': '조치원국사',
            'value': 'R02238'
        },
        {
            'label': '좌동국사',
            'value': 'R03957'
        },
        {
            'label': '주문진국사',
            'value': 'R03503'
        },
        {
            'label': '주안국사',
            'value': 'R00509'
        },
        {
            'label': '죽교국사',
            'value': 'R03019'
        },
        {
            'label': '중동국사',
            'value': 'R00921'
        },
        {
            'label': '중랑국사',
            'value': 'R00438'
        },
        {
            'label': '중부국사',
            'value': 'R00947'
        },
        {
            'label': '중앙국사',
            'value': 'R00411'
        },
        {
            'label': '증평국사',
            'value': 'R02580'
        },
        {
            'label': '지족국사',
            'value': 'R01163'
        },
        {
            'label': '진도국사',
            'value': 'R03043'
        },
        {
            'label': '진북국사',
            'value': 'R00970'
        },
        {
            'label': '진안국사',
            'value': 'R03386'
        },
        {
            'label': '진영국사',
            'value': 'R01060'
        },
        {
            'label': '진주국사',
            'value': 'R00991'
        },
        {
            'label': '진천국사',
            'value': 'R02524'
        },
        {
            'label': '진해국사',
            'value': 'R01033'
        },
        {
            'label': '창녕국사',
            'value': 'R01272'
        },
        {
            'label': '창원국사',
            'value': 'R01066'
        },
        {
            'label': '천안국사',
            'value': 'R02131'
        },
        {
            'label': '철원국사',
            'value': 'R03748'
        },
        {
            'label': '청도국사',
            'value': 'R01862'
        },
        {
            'label': '청량국사',
            'value': 'R00426'
        },
        {
            'label': '청룡국사',
            'value': 'R00953'
        },
        {
            'label': '청송국사',
            'value': 'R01804'
        },
        {
            'label': '청양국사',
            'value': 'R02304'
        },
        {
            'label': '청주국사',
            'value': 'R02402'
        },
        {
            'label': '청평국사',
            'value': 'R00789'
        },
        {
            'label': '춘천국사',
            'value': 'R03457'
        },
        {
            'label': '충북영동국사',
            'value': 'R02475'
        },
        {
            'label': '충주국사',
            'value': 'R02414'
        },
        {
            'label': '칠곡국사',
            'value': 'R01476'
        },
        {
            'label': '칠원국사',
            'value': 'R01413'
        },
        {
            'label': '태백국사',
            'value': 'R03672'
        },
        {
            'label': '태안국사',
            'value': 'R02366'
        },
        {
            'label': '통영국사',
            'value': 'R01071'
        },
        {
            'label': '통진분기국사(김포)',
            'value': 'R00900'
        },
        {
            'label': '퇴계원국사',
            'value': 'R00571'
        },
        {
            'label': '파주국사',
            'value': 'R00908'
        },
        {
            'label': '팔복국사',
            'value': 'R03180'
        },
        {
            'label': '평창국사',
            'value': 'R03590'
        },
        {
            'label': '평택국사',
            'value': 'R00547'
        },
        {
            'label': '포천국사',
            'value': 'R00708'
        },
        {
            'label': '포항국사',
            'value': 'R01494'
        },
        {
            'label': '표선국사',
            'value': 'R03817'
        },
        {
            'label': '하남국사',
            'value': 'R00630'
        },
        {
            'label': '하당국사',
            'value': 'R03825'
        },
        {
            'label': '하동국사',
            'value': 'R01295'
        },
        {
            'label': '하양국사',
            'value': 'R01965'
        },
        {
            'label': '한림국사',
            'value': 'R03799'
        },
        {
            'label': '함안국사',
            'value': 'R01403'
        },
        {
            'label': '함양국사',
            'value': 'R01353'
        },
        {
            'label': '함열국사',
            'value': 'R03388'
        },
        {
            'label': '함창국사',
            'value': 'R01726'
        },
        {
            'label': '함평국사',
            'value': 'R03050'
        },
        {
            'label': '합덕국사',
            'value': 'R02399'
        },
        {
            'label': '합천국사',
            'value': 'R01316'
        },
        {
            'label': '항동국사',
            'value': 'R00494'
        },
        {
            'label': '해남국사',
            'value': 'R03065'
        },
        {
            'label': '해미국사',
            'value': 'R02216'
        },
        {
            'label': '해운대국사',
            'value': 'R01126'
        },
        {
            'label': '행당국사',
            'value': 'R00416'
        },
        {
            'label': '혜화국사',
            'value': 'R00441'
        },
        {
            'label': '호계국사',
            'value': 'R00600'
        },
        {
            'label': '홍성국사',
            'value': 'R02280'
        },
        {
            'label': '홍제국사',
            'value': 'R00413'
        },
        {
            'label': '홍천국사',
            'value': 'R03716'
        },
        {
            'label': '화광국사',
            'value': 'R03683'
        },
        {
            'label': '화순국사',
            'value': 'R03107'
        },
        {
            'label': '화전국사',
            'value': 'R03827'
        },
        {
            'label': '화천국사',
            'value': 'R03628'
        },
        {
            'label': '횡성국사',
            'value': 'R03645'
        },
        {
            'label': '후포국사',
            'value': 'R02077'
        },
        {
            'label': '흥해국사',
            'value': 'R01527'
        }
    ],
    tempNodeList: [
        {
            'label': '전체',
            'value': ''
        },
        {
            'label': '-------',
            'value': '000000'
        },
        {
            'label': '목동IDC',
            'value': '000901'
        },
        {
            'label': '강남IDC',
            'value': '000904'
        },
        {
            'label': '대구IDC',
            'value': '000905'
        },
        {
            'label': '부산IDC',
            'value': '000906'
        },
        {
            'label': '청주IDC',
            'value': '000907'
        },
        {
            'label': '춘천IDC',
            'value': '000908'
        },
        {
            'label': '광주IDC',
            'value': '000909'
        },
        {
            'label': '분당IDC',
            'value': '000913'
        },
        {
            'label': '김해IDC',
            'value': '000916'
        },
        {
            'label': 'ICSIDC',
            'value': '000951'
        },
        {
            'label': '남수원IDC',
            'value': '000999'
        },
        {
            'label': '광화문국사',
            'value': 'R00412'
        },
        {
            'label': '행당국사',
            'value': 'R00416'
        },
        {
            'label': '신촌국사',
            'value': 'R00420'
        },
        {
            'label': '강북국사',
            'value': 'R00430'
        },
        {
            'label': '강서국사',
            'value': 'R00435'
        },
        {
            'label': '목동국사',
            'value': 'R00436'
        },
        {
            'label': '구로국사',
            'value': 'R00437'
        },
        {
            'label': '혜화국사',
            'value': 'R00441'
        },
        {
            'label': '송파국사',
            'value': 'R00443'
        },
        {
            'label': '양재국사',
            'value': 'R00454'
        },
        {
            'label': '수원국사',
            'value': 'R00459'
        },
        {
            'label': '남수원국사',
            'value': 'R00476'
        },
        {
            'label': '인천국사',
            'value': 'R00515'
        },
        {
            'label': '부천국사',
            'value': 'R00519'
        },
        {
            'label': '모란국사',
            'value': 'R00527'
        },
        {
            'label': '평택국사',
            'value': 'R00547'
        },
        {
            'label': '구리국사',
            'value': 'R00567'
        },
        {
            'label': '안산국사',
            'value': 'R00580'
        },
        {
            'label': '일산국사',
            'value': 'R00598'
        },
        {
            'label': '분당국사',
            'value': 'R00603'
        },
        {
            'label': '안양국사',
            'value': 'R00919'
        },
        {
            'label': '서부산국사',
            'value': 'R00945'
        },
        {
            'label': '동래국사',
            'value': 'R00950'
        },
        {
            'label': '북부산국사',
            'value': 'R00958'
        },
        {
            'label': '동부산국사',
            'value': 'R00961'
        },
        {
            'label': '동마산국사',
            'value': 'R00981'
        },
        {
            'label': '동진주국사',
            'value': 'R01003'
        },
        {
            'label': '울산국사',
            'value': 'R01026'
        },
        {
            'label': '김해국사',
            'value': 'R01044'
        },
        {
            'label': '거제국사',
            'value': 'R01225'
        },
        {
            'label': '동대구국사',
            'value': 'R01465'
        },
        {
            'label': '북대구국사',
            'value': 'R01475'
        },
        {
            'label': '포항국사',
            'value': 'R01494'
        },
        {
            'label': '서안동국사',
            'value': 'R01583'
        },
        {
            'label': '구미국사',
            'value': 'R01628'
        },
        {
            'label': '대전국사',
            'value': 'R02112'
        },
        {
            'label': '둔산국사',
            'value': 'R02127'
        },
        {
            'label': '남천안국사',
            'value': 'R02151'
        },
        {
            'label': '홍성국사',
            'value': 'R02280'
        },
        {
            'label': '충주국사',
            'value': 'R02414'
        },
        {
            'label': '서광주국사',
            'value': 'R02597'
        },
        {
            'label': '북광주국사',
            'value': 'R02604'
        },
        {
            'label': '북전주국사',
            'value': 'R03169'
        },
        {
            'label': '익산국사',
            'value': 'R03186'
        },
        {
            'label': '군산국사',
            'value': 'R03203'
        },
        {
            'label': '원주국사',
            'value': 'R03424'
        },
        {
            'label': '춘천국사',
            'value': 'R03457'
        },
        {
            'label': '강릉국사',
            'value': 'R03485'
        },
        {
            'label': '동해국사',
            'value': 'R03507'
        },
        {
            'label': '속초국사',
            'value': 'R03515'
        },
        {
            'label': '제주국사',
            'value': 'R03771'
        },
        {
            'label': '신제주국사',
            'value': 'R03775'
        },
        {
            'label': '서귀포국사',
            'value': 'R03781'
        },
        {
            'label': '하당국사',
            'value': 'R03825'
        },
        {
            'label': '북순천국사',
            'value': 'R03830'
        },
        {
            'label': '동의정부국사',
            'value': 'R03856'
        },
        {
            'label': '남청주국사',
            'value': 'R04000'
        },
        {
            'label': '둔산센터',
            'value': 'R08457'
        },
        {
            'label': 'DATA망(광주)',
            'value': 'VV0014'
        },
        {
            'label': 'DATA망(구로)',
            'value': 'VV0015'
        },
        {
            'label': 'DATA망(대구)',
            'value': 'VV0016'
        },
        {
            'label': 'DATA망(대전)',
            'value': 'VV0017'
        },
        {
            'label': 'DATA망(부산)',
            'value': 'VV0019'
        },
        {
            'label': 'DATA망(용인)',
            'value': 'VV0020'
        },
        {
            'label': 'DATA망(혜화)',
            'value': 'VV0022'
        },
        {
            'label': 'SG망(광주)',
            'value': 'VV0023'
        },
        {
            'label': 'SG망(구로)',
            'value': 'VV0024'
        },
        {
            'label': 'SG망(대구)',
            'value': 'VV0025'
        },
        {
            'label': 'SG망(대전)',
            'value': 'VV0026'
        },
        {
            'label': 'SG망(부산)',
            'value': 'VV0028'
        },
        {
            'label': 'SG망(용인)',
            'value': 'VV0029'
        },
        {
            'label': 'SG망(혜화)',
            'value': 'VV0031'
        },
        {
            'label': 'ACCESS(강남)',
            'value': 'VV0043'
        },
        {
            'label': 'ACCESS(강북)',
            'value': 'VV0044'
        },
        {
            'label': 'ACCESS(충청)',
            'value': 'VV0045'
        },
        {
            'label': 'ACCESS(호남)',
            'value': 'VV0046'
        },
        {
            'label': 'ACCESS(대구)',
            'value': 'VV0047'
        },
        {
            'label': 'ACCESS(부산)',
            'value': 'VV0048'
        },
        {
            'label': '테스트베드(우면동)',
            'value': 'VV0049'
        },
        {
            'label': '테스트베드(일산)',
            'value': 'VV0050'
        },
        {
            'label': 'ACCESS(강서)',
            'value': 'VV0057'
        },
        {
            'label': 'KT사내망',
            'value': 'VV0059'
        },
        {
            'label': '타사이관 가상국사',
            'value': 'VV0060'
        },
        {
            'label': '국제통신운용센터(노드)',
            'value': 'VV0061'
        },
        {
            'label': 'IP정보조회(멀티)권한용 가상국사',
            'value': 'VV0062'
        },
        {
            'label': '가상국사(공통)',
            'value': 'VV0063'
        },
        {
            'label': 'ACCESS(강원)',
            'value': 'VV0064'
        },
        {
            'label': '5G DATA망(구로)',
            'value': 'VV0067'
        },
        {
            'label': '5G DATA망(혜화)',
            'value': 'VV0068'
        },
        {
            'label': '5G DATA망(대전)',
            'value': 'VV0069'
        },
        {
            'label': '5G DATA망(광주)',
            'value': 'VV0070'
        },
        {
            'label': '5G DATA망(대구)',
            'value': 'VV0071'
        },
        {
            'label': '5G DATA망(부산)',
            'value': 'VV0072'
        },
        {
            'label': '5G ACCESS망(구로)',
            'value': 'VV0073'
        },
        {
            'label': '5G ACCESS망(혜화)',
            'value': 'VV0074'
        },
        {
            'label': '5G ACCESS망(대전)',
            'value': 'VV0075'
        },
        {
            'label': '5G ACCESS망(광주)',
            'value': 'VV0076'
        },
        {
            'label': '5G ACCESS망(대구)',
            'value': 'VV0077'
        },
        {
            'label': '5G ACCESS망(부산)',
            'value': 'VV0078'
        },
        {
            'label': '5G ACCESS망(강원)',
            'value': 'VV0079'
        },
        {
            'label': '5G ACCESS망(강서)',
            'value': 'VV0080'
        },
        {
            'label': '5G DATA망(용인)',
            'value': 'VV0081'
        },
        {
            'label': 'IPC',
            'value': 'VV0083'
        },
        {
            'label': 'EPC',
            'value': 'VV0084'
        },
        {
            'label': 'Legacy',
            'value': 'VV0085'
        },
        {
            'label': '미디어계획팀',
            'value': 'VV0091'
        },
        {
            'label': '품질개선팀',
            'value': 'VV0092'
        },
        {
            'label': '미디어인프라팀',
            'value': 'VV0093'
        },
        {
            'label': 'VOD플랫폼팀',
            'value': 'VV0094'
        },
        {
            'label': '채널제어플랫폼팀',
            'value': 'VV0095'
        },
        {
            'label': '모바일TV플랫폼팀',
            'value': 'VV0096'
        },
        {
            'label': '기가지니플랫폼팀',
            'value': 'VV0097'
        },
        {
            'label': '신사업운용팀',
            'value': 'VV0098'
        },
        {
            'label': '방송송출팀',
            'value': 'VV0099'
        },
        {
            'label': '방송중계팀',
            'value': 'VV0100'
        },
        {
            'label': '목동2IDC Legacy',
            'value': 'VV0101'
        },
        {
            'label': '목동2IDC OneIDC',
            'value': 'VV0102'
        },
        {
            'label': '목동1IDC Legacy',
            'value': 'VV0103'
        },
        {
            'label': '목동1IDC OneIDC',
            'value': 'VV0104'
        },
        {
            'label': '분당IDC Legacy',
            'value': 'VV0105'
        },
        {
            'label': '분당IDC OneIDC',
            'value': 'VV0106'
        },
        {
            'label': '용산IDC OneIDC',
            'value': 'VV0107'
        },
        {
            'label': '강남IDC Legacy',
            'value': 'VV0108'
        },
        {
            'label': '강남IDC OneIDC',
            'value': 'VV0109'
        },
        {
            'label': '강남IDC CDN',
            'value': 'VV0110'
        },
        {
            'label': '천안CDC',
            'value': 'VV0111'
        },
        {
            'label': 'GiGAeyes Cloud',
            'value': 'VV0112'
        },
        {
            'label': '5G DATA망(원주)',
            'value': 'VV0113'
        },
        {
            'label': '서울국제센터',
            'value': 'VV0114'
        },
        {
            'label': '부산국제센터',
            'value': 'VV0115'
        },
        {
            'label': '가평교육지원청(구리)',
            'value': 'VV0118'
        },
        {
            'label': '고양교육지원청(일산)',
            'value': 'VV0119'
        },
        {
            'label': '광명교육지원청(강서)',
            'value': 'VV0120'
        },
        {
            'label': '광주하남교육지원청(모란)',
            'value': 'VV0121'
        },
        {
            'label': '구리남양주교육지원청(구리)',
            'value': 'VV0122'
        },
        {
            'label': '김포교육지원청(인천)',
            'value': 'VV0123'
        },
        {
            'label': '동두천양주교육지원청(의정부)',
            'value': 'VV0124'
        },
        {
            'label': '성남교육지원청(모란)',
            'value': 'VV0125'
        },
        {
            'label': '수원교육지원청(수원)',
            'value': 'VV0126'
        },
        {
            'label': '시흥교육지원청(서안양)',
            'value': 'VV0127'
        },
        {
            'label': '안산교육지원청(서안양)',
            'value': 'VV0128'
        },
        {
            'label': '안성교육지원청(평택)',
            'value': 'VV0129'
        },
        {
            'label': '안양과천교육지원청(서안양)',
            'value': 'VV0130'
        },
        {
            'label': '양평교육지원청(구리)',
            'value': 'VV0131'
        },
        {
            'label': '여주교육지원청(모란)',
            'value': 'VV0132'
        },
        {
            'label': '연천교육지원청(의정부)',
            'value': 'VV0133'
        },
        {
            'label': '용인교육지원청(수원)',
            'value': 'VV0134'
        },
        {
            'label': '의정부교육지원청(의정부)',
            'value': 'VV0135'
        },
        {
            'label': '이천교육지원청(모란)',
            'value': 'VV0136'
        },
        {
            'label': '파주교육지원청(일산)',
            'value': 'VV0137'
        },
        {
            'label': '평택교육지원청(평택)',
            'value': 'VV0138'
        },
        {
            'label': '포천교육지원청(의정부)',
            'value': 'VV0139'
        },
        {
            'label': '화성오산교육지원청(수원)',
            'value': 'VV0140'
        },
        {
            'label': '무선망전체_예비',
            'value': 'VV0141'
        },
        {
            'label': 'SG망_예비',
            'value': 'VV0142'
        },
        {
            'label': 'DATA망_예비',
            'value': 'VV0143'
        },
        {
            'label': 'ACCESS_예비',
            'value': 'VV0144'
        },
        {
            'label': '5G_DATA망_예비',
            'value': 'VV0145'
        },
        {
            'label': '5G_ACCESS망_예비',
            'value': 'VV0146'
        },
        {
            'label': '프리미엄망팀',
            'value': 'VV0147'
        },
        {
            'label': '백석GMC운용팀',
            'value': 'VV0148'
        },
        {
            'label': '구로센터',
            'value': 'Z00001'
        },
        {
            'label': '혜화센터',
            'value': 'Z00002'
        },
        {
            'label': '여의도센타',
            'value': 'Z00003'
        }
    ],
    tempSassignTypeCdList: [
        {
            'label': '기업고객(고정)',
            'value': 'SA0001'
        },
        {
            'label': '홈고객(유동)',
            'value': 'SA0002'
        },
        {
            'label': '홈고객(고정)',
            'value': 'SA0003'
        },
        {
            'label': '홈고객(시설)',
            'value': 'SA0004'
        },
        {
            'label': '미분류서비스',
            'value': 'SA1001'
        },
        {
            'label': '홈고객(Secured IP)',
            'value': 'SA1008'
        },
        {
            'label': '타사이관',
            'value': 'SA1009'
        },
        {
            'label': '홍콩DC구축용',
            'value': 'SA1010'
        },
        {
            'label': 'Cloud',
            'value': 'SA1011'
        },
        {
            'label': '대군화 시설용',
            'value': 'SA1014'
        },
        {
            'label': '스쿨넷',
            'value': 'SA1032'
        },
        {
            'label': '홈고객(10G고정IP)',
            'value': 'SA1034'
        },
        {
            'label': 'IPTV',
            'value': 'SA1035'
        },
        {
            'label': '대군화-loopback0',
            'value': 'SA1036'
        },
        {
            'label': '대군화-loopback10',
            'value': 'SA1037'
        },
        {
            'label': '대군화-Serial IP',
            'value': 'SA1038'
        },
        {
            'label': '대군화-서버/플랫폼',
            'value': 'SA1039'
        },
        {
            'label': '대군화-Serial IP(DCS)',
            'value': 'SA1040'
        },
        {
            'label': '대군화-Serial IP(OLT업링크)',
            'value': 'SA1041'
        },
        {
            'label': '일반-Serial IP',
            'value': 'SA1042'
        },
        {
            'label': '일반-Serial IP(기업고객)',
            'value': 'SA1043'
        },
        {
            'label': '일반-Serial IP(홈고객)',
            'value': 'SA1044'
        },
        {
            'label': 'IDC',
            'value': 'SA9008'
        },
        {
            'label': '코넷센터 고객(국내/국제)',
            'value': 'SA9011'
        },
        {
            'label': 'N/W INFRA - 장비/IF',
            'value': 'SA9035'
        },
        {
            'label': '시스템 - 운용실',
            'value': 'SA9039'
        },
        {
            'label': '서버/플랫폼 - 서버',
            'value': 'SA9041'
        }
    ]
}

const mutations = {}

const actions = {}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
