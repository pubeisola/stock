package cc.mrbird.febs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import lombok.NoArgsConstructor;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FebsShiroApplication.class)
@NoArgsConstructor
@WebAppConfiguration
public class SpringDemoApplicationTests {

    @Test
    public void contextLoads() {
    }

}