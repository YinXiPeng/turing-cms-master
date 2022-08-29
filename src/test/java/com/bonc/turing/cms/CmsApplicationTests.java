package com.bonc.turing.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmsApplicationTests {

	@Test
	public void contextLoads() {
		String path = "test.ipynb";
		path = path.replace(path.substring(path.lastIndexOf(".")+1),"py");
		System.out.println(path);

	}

}
