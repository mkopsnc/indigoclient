package pl.psnc.indigo.fg.api.restful.jaxb;

import java.beans.*;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 *
 * @author michalo
 *
 * POJO class for storing InputFile description
 *
 */
@JsonDeserialize(using = InputFileDeserializer.class)
public class InputFile implements Serializable {

  String name;
  String status;

  public InputFile() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
