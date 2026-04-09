
### RCA 네트워크 토폴로지 (Map2D 라이브러리)

d3.js v4 기반 네트워크 토폴로지 시각화 라이브러리.

---

#### 빌드 구조

이 라이브러리는 **2개의 소스 파일**에서 **2개의 번들**을 생성한다.

| 소스 | 번들 | 사용처 | 빌드 방법 |
|------|------|--------|----------|
| `index_nia.js` (126KB) | `dist/index_nia_bundle.js` (676KB) | 대시보드 토폴로지 (niaTopology) | `node merge.js` |
| `index_dev.js` (232KB) | `build/map2d.min.js` (341KB) | 예지보전, 순단장애 토폴로지 | `build/build_index_dev.bat` |

배포 경로: `apps/Web2024/FE/public/extlib/map2d/lib/`

| 번들 파일 | 배포 파일명 |
|----------|------------|
| `dist/index_nia_bundle.js` | `index_nia_bundle.js` |
| `build/map2d.min.js` | `map2d_untact.min.js` |

#### 두 소스의 차이

| 항목 | `index_nia.js` | `index_dev.js` |
|------|---------------|----------------|
| 크기 | 3,028줄 | 5,450줄 |
| `initialize()` | 인자 없음 (`svg#topology_container` 하드코딩) | 요소 ID를 인자로 받음 |
| `load()` 데이터 포맷 | `{ nodes, links, config }` | `{ config, data: { nodes, links } }` |
| d3-context-menu | 번들에 포함 | 번들 미포함 (별도 로드) |
| global.js | 번들 미포함 | 번들에 포함 |

두 소스는 같은 `window.Map2d`에 등록되므로, 동일 페이지에서 동시 사용 시 충돌 발생.
각 컴포넌트에서 로드 직후 `this._Map2dRef = window.Map2d`로 참조를 보존하여 충돌 회피.

---

#### 특색(feature)
* 데이터에 따른 노드/노드뱃지 가시화
* 데이터에 따른 링크/트래픽 가시화
* 노드/링크 장애
* 노드/링크 선택
* 자동 배치 
* 노드의 드래그 앤 드랍
* 맵 저장 및 로드 기능
* 확대/축소/이동 기능
* 노드/링크 툴팁기능
* 노드/링크 상세정보 및 정보창 드래그 기능
* 노드 추가 삭제 기능 (편집모드 시 추가 : ctrl+바탕클릭, 삭제 : shift+노드클릭 )
* 링크 삭제 기능 (편집모드 시 삭제 : shift+링크클릭 )

#### 추가 예정 기능
* 링크 추가 삭제 기능
* 노드/링크 show/hide 기능   
* 노드 그룹핑 및 접기 펴기 기능
* 애니메에션 줌인 패닝 기능 

#### 개선사항
* 가상링크 제거
    + 노드와 링크의 animated 끌기 기능을 위해 가상 노드링크를 확용한다. 이로인한 데이터 관리 포인트가 많다. 가상데이터 없이 레이아웃을 자연스럽게 이동시킬 수 있는 방법을 찾아보자
    + RCA 토폴로지
+ svg의 defs를 이용하여 에니메이션 기능 정리

---

#### 빌드 방법

##### index_nia_bundle.js (대시보드용)

```bash
cd library/Front/topology-2d
node merge.js
# 결과: dist/index_nia_bundle.js
# 배포: apps/Web2024/FE/public/extlib/map2d/lib/index_nia_bundle.js 에 복사
```

##### map2d.min.js (예지보전/순단장애용)

```bash
cd library/Front/topology-2d/build

# 사전 요구: npm install -g uglify-js (3.14.3)
build_index_dev.bat
# 결과: build/map2d.min.js
# 배포: apps/Web2024/FE/public/extlib/map2d/lib/map2d_untact.min.js 에 복사
```

##### uglify with es6
 + (https://www.npmjs.com/package/uglify-es)
 + install
   npm install uglify-es -g  
   
 1. source-map 사용
    uglifyjs d3.v4.js d3-tip.js dat.gui.min.js index_dev.js -cmo index_dev.min.js --source-map url=index_dev.min.js.map
 2. source-map 미사용
    uglifyjs d3.v4.js d3-tip.js dat.gui.min.js index_dev.js -cmo index_dev.min.js          

---

#### 토폴로지 로드시 config 세팅 가이드 (2020-11-16)
 1. config > options 
    + options 세팅을 위해 값을 머지한다.
    + ex) 
        "options": {
             "node": { "fix_size": true, "r": 20, "width": 15, "height": 20, "badge_size": 25 },
             "link": { "text_size": 6, "badge_size": 15 }
         }
 2. config > map
    + background 맵 관련 세팅 (수정중)
    + ex) 
        "map": {
             "width": "100%",
             "height": "120%",
             "path": "images/background3.png",
             "opacity": "0.5"
         }
 3. config > zoom
    + 로드시 줌 애니메이션 효과 세팅   
    1) reset : 로드시 줌을 초기화 한다 (false : 초기화x / true or undefined : 초기화o)
    2) init : 로드 후 줌 효과 즉시 실행
        + "type": "link"   =>   links 데이터에서 "key"/"value"로 특정 링크로 이동    ({"type": "link", "key": "id", "value": "44"})
        + "type": "node"   =>   nodes 데이터에서 "key"/"value"로 특정 노드로 이동    ({"type": "node", "key": "device_name", "value": "서울NIA"})
        + "type": "pos"    =>   "x"/"y" 좌표값으로 특정 좌표로 이동                  ({"type": "pos", "x": "803.5076296799211", "y": "255.1357123648781"})    
    + ex)
        "zoom" : {
             "reset": false,
             "initElement": {
                 "type": "link",
                 "key": "id",
                 "value": "44",
                 "scale": 5,
                 "duration": 1000
             }
         }
         
         data_nia_1_1.json 참조

#### demo/index_dev.js 적용시 주의사항 (2020-10-06)
 1. window.onload 이벤트로 선언된 function 부분은 즉시 return 처리됨
    (컴파일후 실행하게 되면 파일이 로드되는 시점으로 인해 컨트롤러에 정의한 이벤트와 다중으로 호출되는 오류가 발생)
 2. window.onload 이벤트로 선언된 function 로직이 수정될 경우 컨트롤러에도 똑같이 적용해야 한다.
    (일부 다른 부분이 존재하므로 수정시 주의 필요)
 3. nia/app/websrc/lib/topology2 > 1_update_index_dev.bat 배치파일 생성(2020-10-16) 하여 수정사항 적용 작업 자동화.
     같은 경로 내 replace-str.js 파일 참조     
