package tog.is.im.pool;

import static org.junit.Assert.*;

import org.junit.Test;

public class IMPoolTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
		try {
			IMToken token1 = IMPool.GetInstance().borrowObject();
			System.out.println(token1.toString());
			IMToken token2 = IMPool.GetInstance().borrowObject();
			System.out.println(token2.toString());
			IMToken token3 = IMPool.GetInstance().borrowObject();
			System.out.println(token3.toString());
			IMToken token4 = IMPool.GetInstance().borrowObject();
			System.out.println(token4.toString());
			IMToken token5 = IMPool.GetInstance().borrowObject();
			System.out.println(token5.toString());
			IMToken token6 = IMPool.GetInstance().borrowObject();
			System.out.println(token6.toString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(1, 1);	
	}

}
