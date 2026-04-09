package kr.go.ap.linkage.mba.service;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class EmsMmcHdlServiceTest {

    @Autowired
    public EmsMmcHdlService emsMmcHdlService;

    @Test
    public void pmCollectHdl() {
        emsMmcHdlService.pmCollectHdl();
    }
}