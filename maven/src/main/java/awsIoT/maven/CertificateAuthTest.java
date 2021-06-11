package awsIoT.maven;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.osfg.utils.SampleUtil;
import com.osfg.utils.SampleUtil.KeyStorePasswordPair;


public class CertificateAuthTest {

	public static void main(String[] args) throws AWSIotException, InterruptedException {
		
		String clientEndpoint = "a331kmkc0h9mef.iot.us-east-2.amazonaws.com";   // use value returned by describe-endpoint --endpoint-type "iot:Data-ATS"
		String clientId = "coisacliente1";                              // replace with your own client ID. Use unique client IDs for concurrent connections.
		String certificateFile = "e2e1d64332-certificate.pem.crt";                       // X.509 based certificate file
		String privateKeyFile = "e2e1d64332-private.pem.key";                        // PKCS#1 or PKCS#8 PEM encoded private key file

		// SampleUtil.java and its dependency PrivateKeyReader.java can be copied from the sample source code.
		// Alternatively, you could load key store directly from a file - see the example included in this README.
		KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile);
		AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);

		// optional parameters can be set before connect()
		client.connect();
		System.out.println("Connected");
		
		String topicName = "topic1";
		AWSIotQos qos = AWSIotQos.QOS0;

		MyTopic topic = new MyTopic(topicName, qos);
		client.subscribe(topic);
		
		Thread.sleep(15000);
		
		String payload = "Hello World!";

		client.publish(topicName, qos, payload);
		

	}

}

class MyTopic extends AWSIotTopic {
    public MyTopic(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    @Override
    public void onMessage(AWSIotMessage message) {
    	System.out.println("Mensagem recebida: " + message.getStringPayload());
        // called when a message is received
    }
}
