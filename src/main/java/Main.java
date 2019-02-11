import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.json.JsonSupport;

public class Main {

	public static void main(String[] args) throws UnknownHostException {
		Sentiment.init();
		ServerConfiguration configuration = ServerConfiguration.builder()
				.port(8080).build();
		WebServer.create(configuration,
				Routing.builder().register(JsonSupport.create()).post("/sentiment", (req, res) -> {
					req.content().as(String.class).thenAccept(entity -> {
						try {
							res.send(Sentiment.findSentiment(entity));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					});
				}).build()).start();

	}
}