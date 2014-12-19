import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

// AAAAEBWB

public class BruteForce {

	public static final String ALIAS = "p79p70";
	public static final String URL = "http://quest.skule.ca/1T5/php_source/puzzles/answer_check.php";
	public static final String[] Q4_HINTS = { "HERM", "HOOT ART", "HROW",
			"IDES", "RECOUPS", "SUBADAR", "THETA", "THOG" };
//	public static final String[] ALPHANUM = { " ", "A", "B", "C", "D", "E",
//			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
//			"S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4",
//			"5", "6", "7", "8", "9" };
//	public static final String CORRECT_MSG = "Correct!";

	public static void main(String args[]) throws ClientProtocolException,
			URISyntaxException, IOException {

		ArrayList<String> q4CharList = new ArrayList<String>();
		for (String hint : Q4_HINTS) {
			for (char c : hint.toCharArray()) {
				if (!q4CharList.contains(Character.toString(c))) {
					q4CharList.add(Character.toString(c));

				}
			}
		}
		Collections.sort(q4CharList);
		String[] q4Chars = new String[1];
		q4Chars = q4CharList.toArray(q4Chars);
		for (String c : q4Chars)
			System.out.print(c + ", ");
		System.out.println();

		CloseableHttpClient client = getHttpClient();
		// String answer = "herm";

		for (int c1 = 0; c1 < q4Chars.length; c1++)
			for (int c2 = 1; c2 < q4Chars.length; c2++)
				for (int c3 = 1; c3 < q4Chars.length; c3++)
					for (int c4 = 1; c4 < q4Chars.length; c4++)
						for (int c5 = 1; c5 < q4Chars.length; c5++)
							for (int c6 = 1; c6 < q4Chars.length; c6++)
								for (int c7 = 1; c7 < q4Chars.length; c7++)
									for (int c8 = 1; c8 < q4Chars.length; c8++)
										for (int c9 = 1; c9 < q4Chars.length; c9++) {
											String answer = (q4Chars[c1]
													+ q4Chars[c2] + q4Chars[c3]
													+ q4Chars[c4] + q4Chars[c5]
													+ q4Chars[c6] + q4Chars[c7]
													+ q4Chars[c8] + q4Chars[c9]);
											String response = sendAnswer(
													client, answer).replaceAll(
													"\\?message=", "");

											System.out.println(answer + ": "
													+ response);
											if (!response.contains("Incorrect")) {
												BufferedReader reader = new BufferedReader(
														new InputStreamReader(
																System.in));
												reader.readLine();
												reader.close();
											}
										}

	}

	private static String sendAnswer(CloseableHttpClient client, String answer)
			throws URISyntaxException, ClientProtocolException, IOException {
		HttpUriRequest post = RequestBuilder
				.post()
				.setUri(new URI(URL))
				.addParameter("alias", ALIAS)
				.addParameter("answer", answer)
				.addParameter("question", "4")
				.addParameter("questionURL",
						"/1T5/phase1/bottoms_up/index.php/../").build();
		CloseableHttpResponse response = client.execute(post);

		String locationHeader = response.getHeaders("Location")[0].getValue();

		EntityUtils.consume(response.getEntity());
		response.close();
		return locationHeader;

	}

	public static CloseableHttpClient getHttpClient() {
		// set up cookies
		RequestConfig globalConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.BEST_MATCH).build();
		CookieStore cookieStore = (CookieStore) new BasicCookieStore();
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore((org.apache.http.client.CookieStore) cookieStore);

		// set up timeout
		RequestConfig config = RequestConfig.custom().setSocketTimeout(5000)
				.setConnectTimeout(5000).setConnectionRequestTimeout(5000)
				.build();

		// create HttpClient
		return HttpClients
				.custom()
				.setDefaultRequestConfig(globalConfig)
				.setDefaultCookieStore(
						(org.apache.http.client.CookieStore) cookieStore)
				.setDefaultRequestConfig(config).disableAutomaticRetries()
				.build();
	}
}
