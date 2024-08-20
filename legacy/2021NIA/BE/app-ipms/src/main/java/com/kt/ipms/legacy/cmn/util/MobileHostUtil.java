package com.kt.ipms.legacy.cmn.util;

/**
 * 무선 시설 정보 유틸
 *
 */
public class MobileHostUtil {
	
	/**
	 * 수용국 맵핑 위한 장비보유네트워크운용본부, 장비구분, 수용국 정보
	 */
	public enum LVLMapping {
		
		// 개발 테스트 
		/*LVL_NM_01("강북/강원NW운용본부", "5G DU", "ACCESS(강북)")
		, LVL_NM_02("강북/강원NW운용본부", "주제어 및 IP장비", "DATA망(혜화)")
		, LVL_NM_03("강남/서부NW운용본부", "5G DU", "ACCESS(강남,강서)")
		, LVL_NM_04("강남/서부NW운용본부", "주제어 및 IP장비", "DATA망(구로)")
		, LVL_NM_05("부산/경남NW운용본부", "5G DU", "ACCESS(부산)")
		, LVL_NM_06("부산/경남NW운용본부", "주제어 및 IP장비", "DATA망 (부산)")
		, LVL_NM_07("대구/경북NW운용본부", "5G DU", "ACCESS(대구)")
		, LVL_NM_08("대구/경북NW운용본부", "주제어 및 IP장비", "DATA망 (대구)")
		, LVL_NM_09("전남/전북NW운용본부", "5G DU", "ACCESS(호남)")
		, LVL_NM_10("전남/전북NW운용본부", "주제어 및 IP장비", "DATA망 (광주)")
		, LVL_NM_11("충남/충북NW운용본부", "5G DU", "ACCESS(충청)")
		, LVL_NM_12("충남/충북NW운용본부", "주제어 및 IP장비", "DATA망 (대전)")
		, LVL_ERROR("", "", "매핑되는 수용국이 존재하지 않습니다.")
		;*/
		
		LVL_NM_01("강북/강원NW운용본부", "5G DU", "5G ACCESS망 (혜화)")
		, LVL_NM_02("강북/강원NW운용본부", "주제어 및 IP장비", "5G DATA망 (혜화)")
		, LVL_NM_03("강남/서부NW운용본부", "5G DU", "5G ACCESS망 (구로)")
		, LVL_NM_04("강남/서부NW운용본부", "주제어 및 IP장비", "5G DATA망 (구로)")
		, LVL_NM_05("부산/경남NW운용본부", "5G DU", "5G ACCESS망 (부산)")
		, LVL_NM_06("부산/경남NW운용본부", "주제어 및 IP장비", "5G DATA망 (부산)")
		, LVL_NM_07("대구/경북NW운용본부", "5G DU", "5G ACCESS망 (대구)")
		, LVL_NM_08("대구/경북NW운용본부", "주제어 및 IP장비", "5G DATA망 (대구)")
		, LVL_NM_09("전남/전북NW운용본부", "5G DU", "5G ACCESS망 (광주)")
		, LVL_NM_10("전남/전북NW운용본부", "주제어 및 IP장비", "5G DATA망 (광주)")
		, LVL_NM_11("충남/충북NW운용본부", "5G DU", "5G ACCESS망 (대전)")
		, LVL_NM_12("충남/충북NW운용본부", "주제어 및 IP장비", "5G DATA망 (대전)")
		, LVL_ERROR("", "", "매핑되는 수용국이 존재하지 않습니다.")
		;
		
		private String NetWorkGroup;		//  NW 운용본부
		private String FcltType;				// 장비 구분
		private String LvlNm;					// 수용국 명

		private LVLMapping(String netWorkGroup, String fcltType, String lvlNm) {
			this.NetWorkGroup = netWorkGroup;
			this.FcltType = fcltType;
			this.LvlNm = lvlNm;
		}
		
		public String getNetWorkGroup() {
			return NetWorkGroup;
		}

		public String getFcltType() {
			return FcltType;
		}
		
		public String getLvlNm() {
			return LvlNm;
		}
		
	} 
	
	public static String findLvlNm(String nwGroup, String fcltType) {
		
		String result = "";
		LVLMapping[] mapping = LVLMapping.values();

		try {
			for(LVLMapping m : mapping) {
				
				// 장비보유네트워크운용본부, 장비구분 비교하여 수용국 데이터 맵핑
				String nw = m.getNetWorkGroup().replaceAll(" ", "");
				String ft = m.getFcltType().replaceAll(" ", "");

				if(nw.equals(nwGroup.replaceAll(" ", "")) && ft.equals(fcltType.replaceAll(" ", ""))) {
					result = m.getLvlNm().toString();
					return result;
				}
				
			}
		} catch (Exception e) {
			result = LVLMapping.LVL_ERROR.getLvlNm();
		}
		return result;
	}
}
