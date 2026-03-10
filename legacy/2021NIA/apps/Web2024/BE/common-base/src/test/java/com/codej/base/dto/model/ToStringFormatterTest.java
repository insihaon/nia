package com.codej.base.dto.model;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;    
    
public class ToStringFormatterTest {

    @Before
    public void setup(){
    }
        
    @Test
    public void test() {

        class TestClass extends ToStringFormatter {
            private int id = 1;
            private String name = "Test";
            private boolean isActive = true;
            private ArrayList<String> items = new ArrayList<String>() {{
                add("Item1");
                add("Item2");
                add("Item3");
            }};
            private LinkedHashMap<String, Object> scores = new LinkedHashMap<String, Object>() {{
                put("Math", 90);
                put("Science", 85);
                put("History", 88);
                put("array", items);
            }};
            private Object[] nestedArray = new Object[] { 1, "Two", true };
        }

        TestClass obj = new TestClass();
        System.out.println(obj.toString());
        
    }

    @Test
    public void test2() {

        @Component
        @Setter
        @Getter
        class TestClass  extends ToStringFormatter {
            private int id = 1;
            private String name = "Test";
            private boolean isActive = true;
        }

        TestClass obj = new TestClass();
        System.out.println(obj.toString());
        
    }
}
    