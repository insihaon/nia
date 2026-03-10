package com.kt.ipms.legacy.cmn.util;

import java.util.ArrayList;
import java.util.List;

class PrintLogUtilTest {
    
    /**
     * 테스트를 위한 VO 클래스 정의
     */
    static class SampleVO {
        private String name;
        private int age;
        private List<SampleVO> friends;
        
        public SampleVO(String name, int age) {
            this.name = name;
            this.age = age;
            this.friends = new ArrayList<>();
        }
        
        public String getName() {
            return name;
        }
        
        public int getAge() {
            return age;
        }
        
        public List<SampleVO> getFriends() {
            return friends;
        }
        
        public void addFriend(SampleVO friend) {
            this.friends.add(friend);
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== 단일 VO 객체 테스트 ===");
        SampleVO vo = new SampleVO("Alice", 25);
        PrintLogUtil.printLogInfoValueObject(vo);
        
        System.out.println("=== List<VO> 객체 테스트 ===");
        List<SampleVO> voList = new ArrayList<>();
        voList.add(new SampleVO("Bob", 30));
        voList.add(new SampleVO("Charlie", 28));
        PrintLogUtil.printLogInfoValueObject(voList);
        
        System.out.println("=== VO 내부에 List<VO> 포함 테스트 ===");
        SampleVO parentVO = new SampleVO("David", 40);
        parentVO.addFriend(new SampleVO("Eve", 22));
        parentVO.addFriend(new SampleVO("Frank", 27));
        PrintLogUtil.printLogInfoValueObject(parentVO);
        PrintLogUtil.printLogInfoValueObject(parentVO.getFriends());
    }
}
