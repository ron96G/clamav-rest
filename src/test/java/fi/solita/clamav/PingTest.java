package fi.solita.clamav;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

/**
 * These tests assume clamav-rest Docker container is running and responding locally. 
 */
public class PingTest {

  @Test
  public void testPing() throws IOException {
    ClamAVProxy clamav = new ClamAVProxy("localhost", 3310, 20);
    String s = clamav.ping();
    assertEquals(s, "Clamd responding: true\n");
  }
}
