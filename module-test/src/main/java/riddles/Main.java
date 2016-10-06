package riddles;

import java.net.URL;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;


public class Main {

  public static void main(String[] args) throws Exception {
    URL url = Main.class.getResource("/riddles.xml");
    System.out.println("Reading riddles from: " + url);
    try (InputStream is = url.openStream()) {
      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
      NodeList riddleNodes = document.getElementsByTagName("riddle");
      for (int i = 0; i < riddleNodes.getLength(); i++) {
        System.out.println(riddleNodes.item(i).getTextContent());
      }
    }
  }

}
