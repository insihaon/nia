<template>
  <div :class="{[name]: true}">
    <el-select
      v-model="treeLabel"
      collapse-tags
      multiple
      filterable
      remote
      reserve-keyword
      :disabled="item.disabled || item.readonly"
      :remote-method="onSearch"
      :popper-class="item.model + '_treeSelector'"
      @visible-change="openDropDown"
    >
      <el-option label="" value="">
        <el-tree
          ref="treeDropDown"
          class="treeDropDown"
          show-checkbox
          :data="treeOptions"
          node-key="id"
          :props="defaultProps"
          :filter-node-method="onFiltering"
          :default-expanded-keys="initExpandList"
          :default-checked-keys="searchModel"
          :is-leaf="true"
          @node-click="handleNodeClick($event, 'node')"
          @check="handleNodeClick($event, 'check')"
        />
      </el-option>
    </el-select>
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'

const routeName = 'CompTreeSelector'

import ComponentTesterMixins from '@/test/ComponentTesterMixins'

export default {
  name: routeName,
  components: { },
  extends: Base,
  mixins: [ComponentTesterMixins],
  props: {
    searchModel: {
      type: Array,
      default: () => { return [] }
    },
    item: {
      type: Object,
      default: () => {
        return {
    'prop': 'group',
    'label': '조직',
    'type': 'tree',
    'size': 12,
    'model': 'ORG_ID_LIST',
    'options': [
        {
            'id': '000001',
            'label': 'KT',
            'children': [
                {
                    'id': '498151',
                    'label': '강북&강원NW운용본부(N)',
                    'children': [
                        {
                            'id': '498152',
                            'label': '강북&강원ICT기술담당',
                            'children': [
                                {
                                    'id': '498154',
                                    'label': '기술지원부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498157',
                                    'label': '엔지니어링기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '498165',
                            'label': '강북&강원코어운용센터',
                            'children': [
                                {
                                    'id': '498166',
                                    'label': '액세스망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498167',
                                    'label': 'IP망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498168',
                                    'label': '전송망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498169',
                                    'label': '전원기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498170',
                                    'label': '교환기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '498171',
                            'label': '서울강북액세스운용센터',
                            'children': [
                                {
                                    'id': '498172',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498178',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498179',
                                    'label': '광진전원팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498174',
                                    'label': '충정엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498176',
                                    'label': '청량엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498177',
                                    'label': '지하철엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498180',
                                    'label': '광화문운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498182',
                                    'label': '서대문운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498181',
                                    'label': '원효운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498183',
                                    'label': '광진운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498184',
                                    'label': '도봉운용팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '498188',
                            'label': '강원액세스운용센터',
                            'children': [
                                {
                                    'id': '498189',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498194',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498191',
                                    'label': '영서엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498193',
                                    'label': '영동엔지니어링팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498195',
                                    'label': '원주운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498197',
                                    'label': '강릉운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498196',
                                    'label': '춘천운용팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '498200',
                            'label': '경기북부액세스운용센터',
                            'children': [
                                {
                                    'id': '498201',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498205',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498202',
                                    'label': '신내엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498204',
                                    'label': '고양엔지니어링팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498206',
                                    'label': '의정부운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498207',
                                    'label': '구리운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498208',
                                    'label': '고양운용부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        }
                    ],
                    'level': 2
                },
                {
                    'id': '498468',
                    'label': '강남NW운용본부(N)',
                    'children': [
                        {
                            'id': '498469',
                            'label': '강남ICT기술담당',
                            'children': [
                                {
                                    'id': '498471',
                                    'label': '기술지원부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498475',
                                    'label': '엔지니어링기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '498480',
                            'label': '강남코어운용센터',
                            'children': [
                                {
                                    'id': '498481',
                                    'label': '액세스망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498486',
                                    'label': '강서운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498482',
                                    'label': 'IP망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498483',
                                    'label': '전송망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498487',
                                    'label': '양재운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498484',
                                    'label': '전원기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498485',
                                    'label': '교환기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '498488',
                            'label': '강남액세스운용센터',
                            'children': [
                                {
                                    'id': '498489',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498495',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498496',
                                    'label': '강서전원팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498491',
                                    'label': '강남엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498493',
                                    'label': '강서엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498494',
                                    'label': '강동엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498498',
                                    'label': '구로운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498497',
                                    'label': '영등포운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498500',
                                    'label': '송파운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498499',
                                    'label': '강남운용팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        }
                    ],
                    'level': 2
                },
                {
                    'id': '498748',
                    'label': '서부NW운용본부(N)',
                    'children': [
                        {
                            'id': '498749',
                            'label': '서부ICT기술담당',
                            'children': [
                                {
                                    'id': '498751',
                                    'label': '기술지원부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498754',
                                    'label': '엔지니어링기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '498759',
                            'label': '서부코어운용센터',
                            'children': [
                                {
                                    'id': '498763',
                                    'label': '전원기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498760',
                                    'label': '액세스망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498761',
                                    'label': 'IP망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498766',
                                    'label': '인천운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498762',
                                    'label': '전송망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '509813',
                                    'label': '평택IP운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498764',
                                    'label': '안양운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498765',
                                    'label': '모란운용팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '498767',
                            'label': '서부액세스운용센터',
                            'children': [
                                {
                                    'id': '498768',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498778',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498779',
                                    'label': '안양전원팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498784',
                                    'label': '모란전원팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498770',
                                    'label': '안양엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498773',
                                    'label': '성남엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498772',
                                    'label': '인천엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498775',
                                    'label': '수원엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498776',
                                    'label': '용인엔지니어링팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498780',
                                    'label': '서인천운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498781',
                                    'label': '부천운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498786',
                                    'label': '남수원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498782',
                                    'label': '안산운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '498787',
                                    'label': '성남운용부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        }
                    ],
                    'level': 2
                },
                {
                    'id': '499935',
                    'label': '충남&충북NW운용본부(N)',
                    'children': [
                        {
                            'id': '499936',
                            'label': '충남&충북ICT기술담당',
                            'children': [
                                {
                                    'id': '499938',
                                    'label': '기술지원부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499941',
                                    'label': '엔지니어링기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499947',
                            'label': '충남&충북코어운용센터',
                            'children': [
                                {
                                    'id': '499948',
                                    'label': '코어망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499950',
                                    'label': '액세스망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499951',
                                    'label': 'IP망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499952',
                                    'label': '전송망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499953',
                                    'label': '전원기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499954',
                                    'label': '교환기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499955',
                            'label': '충남액세스운용센터',
                            'children': [
                                {
                                    'id': '499956',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499959',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499958',
                                    'label': '충남엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499960',
                                    'label': '둔산운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499961',
                                    'label': '대전운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499963',
                                    'label': '천안운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499964',
                                    'label': '홍성운용팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499969',
                            'label': '충북액세스운용센터',
                            'children': [
                                {
                                    'id': '499970',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499972',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499971',
                                    'label': '충북엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499973',
                                    'label': '청주운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499974',
                                    'label': '진천운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499975',
                                    'label': '충주운용팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        }
                    ],
                    'level': 2
                },
                {
                    'id': '499596',
                    'label': '전남&전북NW운용본부(N)',
                    'children': [
                        {
                            'id': '499597',
                            'label': '전남&전북ICT기술담당',
                            'children': [
                                {
                                    'id': '499599',
                                    'label': '기술지원부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499606',
                                    'label': '엔지니어링기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499613',
                            'label': '전남&전북코어운용센터',
                            'children': [
                                {
                                    'id': '499614',
                                    'label': '코어망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499615',
                                    'label': '액세스망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499616',
                                    'label': 'IP망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499617',
                                    'label': '전송망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499618',
                                    'label': '전원기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499619',
                                    'label': '교환기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499620',
                            'label': '전남액세스운용센터',
                            'children': [
                                {
                                    'id': '499621',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499626',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499627',
                                    'label': '여수전원팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499623',
                                    'label': '전남엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499624',
                                    'label': '제주엔지니어링팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499625',
                                    'label': '순천엔지니어링팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499628',
                                    'label': '광주운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499629',
                                    'label': '서광주운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499630',
                                    'label': '순천운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499632',
                                    'label': '목포운용부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499637',
                            'label': '전북액세스운용센터',
                            'children': [
                                {
                                    'id': '499638',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499640',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499639',
                                    'label': '전북엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499641',
                                    'label': '전주운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499642',
                                    'label': '익산운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499644',
                                    'label': '정읍운용팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        }
                    ],
                    'level': 2
                },
                {
                    'id': '499307',
                    'label': '대구&경북NW운용본부(N)',
                    'children': [
                        {
                            'id': '499308',
                            'label': '대구&경북ICT기술담당',
                            'children': [
                                {
                                    'id': '499310',
                                    'label': '기술지원부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499313',
                                    'label': '엔지니어링기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499319',
                            'label': '대구&경북코어운용센터',
                            'children': [
                                {
                                    'id': '499320',
                                    'label': '코어망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499321',
                                    'label': '액세스망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499322',
                                    'label': 'IP망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499323',
                                    'label': '전송망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499324',
                                    'label': '전원기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499325',
                                    'label': '교환기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499326',
                            'label': '대구액세스운용센터',
                            'children': [
                                {
                                    'id': '499327',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499330',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499329',
                                    'label': '대구엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499332',
                                    'label': '서대구운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499331',
                                    'label': '동대구운용부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499336',
                            'label': '경북액세스운용센터',
                            'children': [
                                {
                                    'id': '499337',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499339',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499340',
                                    'label': '포항전원팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499338',
                                    'label': '경북엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499341',
                                    'label': '안동운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499342',
                                    'label': '구미운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499343',
                                    'label': '포항운용부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        }
                    ],
                    'level': 2
                },
                {
                    'id': '499056',
                    'label': '부산&경남NW운용본부(N)',
                    'children': [
                        {
                            'id': '499057',
                            'label': '부산&경남ICT기술담당',
                            'children': [
                                {
                                    'id': '499059',
                                    'label': '기술지원부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499063',
                                    'label': '엔지니어링기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499069',
                            'label': '부산&경남코어운용센터',
                            'children': [
                                {
                                    'id': '499070',
                                    'label': '코어망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499071',
                                    'label': '액세스망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499072',
                                    'label': 'IP망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499073',
                                    'label': '전송망기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499074',
                                    'label': '전원기술부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499075',
                                    'label': '교환기술부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499076',
                            'label': '부산액세스운용센터',
                            'children': [
                                {
                                    'id': '499077',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499081',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499079',
                                    'label': '동부산엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499080',
                                    'label': '서부산엔지니어링부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499085',
                                    'label': '서부산운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499084',
                                    'label': '북부산운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499082',
                                    'label': '동부산운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499083',
                                    'label': '남부산운용팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499088',
                            'label': '경남액세스운용센터',
                            'children': [
                                {
                                    'id': '499089',
                                    'label': '품질계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499091',
                                    'label': '전원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499092',
                                    'label': '울산전원팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499090',
                                    'label': '경남엔지니어링팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499093',
                                    'label': '창원운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499095',
                                    'label': '동진주운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499096',
                                    'label': '진주운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499097',
                                    'label': '울산운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499094',
                                    'label': '김해운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499098',
                                    'label': '경남도서무선통신팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        }
                    ],
                    'level': 2
                },
                {
                    'id': '499647',
                    'label': '제주단(C)',
                    'children': [
                        {
                            'id': '499678',
                            'label': '법인고객영업부',
                            'children': [
                                {
                                    'id': '499685',
                                    'label': '제주법인고객지원팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '499687',
                            'label': '네트워크운용센터',
                            'children': [
                                {
                                    'id': '499689',
                                    'label': '품질계획팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499690',
                                    'label': 'IP-BCN운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499691',
                                    'label': '전송망운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '499692',
                                    'label': '전원운용팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        }
                    ],
                    'level': 2
                },
                {
                    'id': '497480',
                    'label': '네트워크코어망본부(N)',
                    'children': [
                        {
                            'id': '497489',
                            'label': '수도권제어운용센터',
                            'children': [
                                {
                                    'id': '497490',
                                    'label': '강북코어망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497491',
                                    'label': '강남코어망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497492',
                                    'label': '서부코어망제어부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497493',
                                    'label': '(구)강남지능망로밍운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497494',
                                    'label': '(구)강남HLR운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497495',
                                    'label': '서부HLR지능망운용부',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '497481',
                            'label': '코어망운용담당',
                            'children': [],
                            'level': 3
                        },
                        {
                            'id': '497486',
                            'label': '코어망ER-TF',
                            'children': [
                                {
                                    'id': '497487',
                                    'label': '무선코어ER-TF',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497488',
                                    'label': 'IP ER-TF',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '497525',
                            'label': '보안운용센터',
                            'children': [
                                {
                                    'id': '497529',
                                    'label': 'IoT서비스팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '497520',
                            'label': '(구)부산국제센터',
                            'children': [
                                {
                                    'id': '497521',
                                    'label': '국제해저NOC팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497522',
                                    'label': '부산국제운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497523',
                                    'label': '국제해저인프라팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497524',
                                    'label': '거제해저중계소',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '497499',
                            'label': 'IP운용센터',
                            'children': [
                                {
                                    'id': '497500',
                                    'label': '혜화코넷망운용부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497501',
                                    'label': '구로코넷망팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497502',
                                    'label': '프리미엄망팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497503',
                                    'label': '기업통화서비스팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497504',
                                    'label': '통화서비스팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497505',
                                    'label': '지능망서비스팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497506',
                                    'label': '접속제어팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497507',
                                    'label': '서비스제어팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497508',
                                    'label': '융합서비스팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497509',
                                    'label': 'IP운용계획팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '497510',
                            'label': '국제통신운용센터',
                            'children': [
                                {
                                    'id': '497511',
                                    'label': '국제사업계획부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497512',
                                    'label': '글로벌네트워크설계부',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497513',
                                    'label': 'GNOC팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497515',
                                    'label': '국제스위칭허브팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497516',
                                    'label': '국제응용플랫폼팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497517',
                                    'label': '국제IP서비스팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497518',
                                    'label': '국제방송팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497519',
                                    'label': '국제품질관리팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        }
                    ],
                    'level': 2
                },
                {
                    'id': '497426',
                    'label': '네트워크기술본부(N)',
                    'children': [
                        {
                            'id': '497427',
                            'label': '코어망기술담당',
                            'children': [
                                {
                                    'id': '497428',
                                    'label': '코어망기술팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497429',
                                    'label': '제어망기술팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497431',
                                    'label': 'IP망기술팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497432',
                                    'label': 'SoIP기술팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '497434',
                            'label': '액세스망기술담당',
                            'children': [
                                {
                                    'id': '497435',
                                    'label': '액세스망기술1팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497436',
                                    'label': '액세스망기술2팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497437',
                                    'label': '액세스망기술3팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497439',
                                    'label': '인터넷기술팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '497444',
                            'label': '단말기술담당',
                            'children': [
                                {
                                    'id': '497445',
                                    'label': '무선단말기술팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497446',
                                    'label': '융합단말기술팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        }
                    ],
                    'level': 2
                },
                {
                    'id': '497372',
                    'label': '네트워크운용본부(N)',
                    'children': [
                        {
                            'id': '497397',
                            'label': '네트워크관제2센터',
                            'children': [
                                {
                                    'id': '497398',
                                    'label': 'IP망관제팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497399',
                                    'label': '전송망관제팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497400',
                                    'label': '전원관제팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '497415',
                            'label': '중앙통신지원단',
                            'children': [
                                {
                                    'id': '497416',
                                    'label': '사업계획팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497417',
                                    'label': '고도화시설팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497418',
                                    'label': '통신운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497419',
                                    'label': '안보통신팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497420',
                                    'label': '운용혁신팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497422',
                                    'label': '서울통신운용센터',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497423',
                                    'label': '대전통신운용센터',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497424',
                                    'label': '세종통신운용센터',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497425',
                                    'label': '101운용센터',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        },
                        {
                            'id': '497388',
                            'label': '네트워크관제1센터',
                            'children': [
                                {
                                    'id': '497389',
                                    'label': '관제계획팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497390',
                                    'label': '무선망관제팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497391',
                                    'label': 'IP망관제팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497392',
                                    'label': '코어망관제팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497393',
                                    'label': '코어망집중운용팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497394',
                                    'label': '전송망관제팀',
                                    'children': [],
                                    'level': 4
                                },
                                {
                                    'id': '497395',
                                    'label': '전원관제팀',
                                    'children': [],
                                    'level': 4
                                }
                            ],
                            'level': 3
                        }
                    ],
                    'level': 2
                }
            ],
            'level': 1
        }
    ],
    'ruleProp': 'rulesOrg'
}
      }
    }
  },

  data() {
    return {
      name: routeName,
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      treeVisible: false,
      labelArr: [],
      idNodeMap: undefined,
      currentClickNodeId: undefined,
      initExpandList: []
    }
  },

  computed: {
    loadingToggle() {
      return this.searchModel && this.idNodeMap
    },
    treeLabel: {
      get() {
        if (this.searchModel && this.idNodeMap) {
          if (this.item.valueConsistsOf === 'LEAF_PRIORITY') {
            return this.getBranchSelectedValue('label')
          } else {
            return this.searchModel.map((id, i) => this.idNodeMap[id].label)
          }
        } else {
          return []
        }
      },
      set(v) {
        if (v?.length === 0) {
          this.devEmit('update:searchModel', [])
          this.$refs.treeDropDown.setCheckedKeys(this.searchModel)
        }
      }
    },

    treeOptions() {
      let cloneOptions = this._cloneDeep(this.item.options)
      if (this.item.nodeOption?.onlyLeaf) {
        return this.getOnlyLeafOptions(cloneOptions)
      }

      if (this.item.nodeOption && this.item.options.length > 0) {
        if (this.item.nodeOption.limitLevel) {
          this.deleteUnderLevelChildren(cloneOptions, this.item.nodeOption.limitLevel)
        }

        if (this.item.nodeOption.parentLimitType) {
          switch (this.item.nodeOption.parentLimitType) {
            case 'hidden':
              cloneOptions = this.getOnlyLeafOptions(cloneOptions)
              break
            case 'disable':
              this.disabledParentNodes(cloneOptions, this.item.nodeOption.limitLevel)
              break
          }
        }
      }
      return cloneOptions
    }

  },

  watch: {
    'item.options'(n, o) {
      if (n && n.length > 0) {
        // this.setOptionsDisabledLvl()
        this.idNodeMap = this.makeIdNodeMap(this.treeOptions)
        setTimeout(() => {
          this.setInitExpandList()
        }, 500)
      }
    },
    searchModel: {
      deep: true,
      handler(n, o) {
        this.$refs.treeDropDown.setCheckedKeys([])
        n.forEach(id => {
          const toggle = !this.$refs.treeDropDown.getCheckedKeys().includes(id)
          this.$refs.treeDropDown.setChecked(id, toggle, true)
        })
      }
    }
  },
  mounted() {
    this.setInitExpandList()
  },
  methods: {
    getOptionsLvl(options) {
      if (options.length === 0) return -1
       options.reduce((acc, o) => {
          if (acc.length > 0) {
            if (acc[0] === o.level) {
              return acc
            } else {
              throw new Error(`
                같은 수준의 option에서 다른 level이 있는것은 잘못된 데이터입니다 
                item.id: ${this.item.id} item.options: ${options}
              `)
            }
          }

          return acc.concat(o.level)
       }, [])

      return options[0].level
    },

    disabledParentNodes(options, goalLvl) {
      if (this.getOptionsLvl(options) === goalLvl) {
        return
      }

     if (String(this.getOptionsLvl(options)) < goalLvl) {
        options.forEach((o) => { o.disabled = true })
        const childrensOptions = options.reduce((acc, o) => {
          return [...acc, ...o.children]
        }, [])

        this.disabledParentNodes(childrensOptions, goalLvl)
      }
    },

    deleteUnderLevelChildren(options, deleteLvl) {
      if (options.length === 0) return
      if (String(this.getOptionsLvl(options)) === String(deleteLvl)) {
        options.forEach((o) => delete o.children)
      } else {
        const childrensOptions = options.reduce((acc, o) => {
          return [...acc, ...o.children]
        }, [])
        this.deleteUnderLevelChildren(childrensOptions, deleteLvl)
      }
    },
    getOnlyLeafOptions(options) {
      return options.reduce((acc, o) => {
        if (!o.children || o.children.length === 0) {
          acc.push(o)
          return acc
        } else {
          return [...acc, ...this.getOnlyLeafOptions(o.children)]
        }
      }, [])
    },

    setInitExpandList() {
      if (this.loadingToggle && this.treeOptions.length > 0) {
        const halfIds = this.$refs.treeDropDown.getHalfCheckedNodes().map((v) => v.id)
        this.initExpandList = this._cloneDeep(this.returnIfNotEmpty(halfIds) || this.returnIfNotEmpty(this.getBranchSelectedValue('id')) || [this.treeOptions[0].id])
      } else {
        this.initExpandList = []
      }
    },

    returnIfNotEmpty(arr) {
      return arr.length === 0 ? false : arr
    },
    getParent(id) {
      const parentKey = Object.keys(this.idNodeMap).find(k => !!this.idNodeMap[k].children?.find(v => v.id === id))
      return this.idNodeMap[parentKey]
    },
    getBranchSelectedValue(valueType = 'id') {
      const halfNodes = this.$refs.treeDropDown.getHalfCheckedNodes()
      const checkedIds = this.$refs.treeDropDown.getCheckedKeys()
      const topNode = this.treeOptions.filter((v) => { return checkedIds.includes(v.id) })
      halfNodes.push(...topNode)
      const values = halfNodes.reduce((acc, node) => {
        if (node.children) {
          const existChildrenValues = this.getExistChildren(checkedIds, node.children, valueType)
          if (existChildrenValues.length !== node.children.length) {
            acc.push(...existChildrenValues)
          } else {
            acc.push(node[valueType])
          }
        } else {
          acc.push(node[valueType])
        }
        return acc
      }, [])
      return values
    },
    getExistChildren(fullArr, childrenArr, valueType = 'id') {
      if (childrenArr) {
        return childrenArr.reduce((acc, c) => {
          if (fullArr.includes(c.id)) acc.push(c[valueType])
          return acc
        }, [])
      } else {
        return []
      }
    },
    openDropDown(e) {
      this.$refs.treeDropDown.filter(undefined)
    },
    handleNodeClick(event, type) {
      this.currentClickNodeId = event.id
      if (this.item.valueConsistsOf === 'LEAF_PRIORITY') {
        this.devEmit('update:searchModel', this._cloneDeep(this.$refs.treeDropDown.getCheckedKeys()))
      } else {
        this.devEmit('update:searchModel', this.getBranchSelectedValue('id'))
      }

      if (type === 'check') {
        this.devEmit('selectedChange', event)
      }
    },
    onSearch(query) {
      this.$refs.treeDropDown.filter(query)
    },
    onFiltering(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    makeIdNodeMap(options) {
      return options.reduce((acc, v, i) => {
        if (acc[v.id]) throw new Error('id는 고유해야합니다. 확인 필요 ' + v.id)
        acc[v.id] = v

        if (v.children) {
          /* id가 중복되는 경우 체크 불가 */
          const childrenIdLabelMap = this.makeIdNodeMap(v.children)
          Object.assign(acc, childrenIdLabelMap)
        }
        return acc
      }, {})
    }
  }
}

</script>

<style lang="scss" scoped>

.CompTreeSelector::v-deep{
}

.el-select-dropdown__item::v-deep{
  height: auto !important;
  .el-tree-node__label{
    margin-right: 15px;
  }

  .el-tree-node__content{
    padding-left: 0px !important;
  }

  .el-tree.treeDropDown > .el-tree-node > .el-tree-node__content > .el-icon-caret-right{
    margin-top: 0px;
  }

  .el-icon-caret-right{
    margin-top: 8px;
  }
}

.el-scrollbar__view.el-select-dropdown__list > .el-select-dropdown__item{
  padding: 0px;
}

</style>
