package classieTalkie;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
public class Config {
		@XmlAttribute(name = "xmlns:xsi", required = true)
		protected String xmlns;

		@XmlAttribute(name = "xsi:noNamespaceSchemaLocation", required = true)
		protected String noNamespaceSchemaLocation;

		@XmlElement(required = true)
		protected Message mesg;

		public Message getMesg() {
			return mesg;
		}

		public void setMesg(Message mesg) {
			this.mesg = mesg;
		}
	}
