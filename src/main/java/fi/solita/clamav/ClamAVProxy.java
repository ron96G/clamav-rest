package fi.solita.clamav;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ClamAVProxy {

  public static Logger logger = LoggerFactory.getLogger(ClamAVProxy.class);

  @Value("${clamd.host}")
  private String hostname;

  @Value("${clamd.port}")
  private int port;

  @Value("${clamd.timeout}")
  private int timeout;

  public ClamAVProxy() {
  }

  public ClamAVProxy(String hostname, int port, int timeout) {
    this.hostname = hostname;
    this.port = port;
    this.timeout = timeout;
  }

  /**
   * @return Clamd status.
   */
  @RequestMapping("/")
  public String ping() throws IOException {
    ClamAVClient a = new ClamAVClient(hostname, port, timeout);
    return "Clamd responding: " + a.ping() + "\n";
  }

  /**
   * @return Clamd scan result
   */
  @RequestMapping(value = "/scan", method = RequestMethod.POST)
  public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
      @RequestParam("file") MultipartFile file) throws IOException {
    if (!file.isEmpty()) {
      ClamAVClient a = new ClamAVClient(hostname, port, timeout);

      // time the request for debug purposes
      long startTime = System.nanoTime();

      byte[] r = a.scan(file.getInputStream());

      double elapsedTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
      logger.info("Time in seconds: " + elapsedTime + " (" + file.getSize() + " bytes)");

      return "Everything ok : " + ClamAVClient.isCleanReply(r) + "\n";
    } else
      throw new IllegalArgumentException("empty file");
  }

  /**
   * @return Clamd scan reply
   */
  @RequestMapping(value = "/scanReply", method = RequestMethod.POST)
  public @ResponseBody String handleFileUploadReply(@RequestParam("name") String name,
      @RequestParam("file") MultipartFile file) throws IOException {
    if (!file.isEmpty()) {
      ClamAVClient a = new ClamAVClient(hostname, port, timeout);
      return new String(a.scan(file.getInputStream()));
    } else
      throw new IllegalArgumentException("empty file");
  }
}
