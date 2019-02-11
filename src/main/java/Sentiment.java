import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/*import org.json.simple.JSONArray;
import org.json.simple.JSONObject;*/
import java.io.*;
import java.net.URLDecoder;

public class Sentiment {
	static StanfordCoreNLP pipeline;

	public static void init() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit , parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
	}

	public static JsonArray findSentiment(String line1) throws UnsupportedEncodingException {
		String line = URLDecoder.decode(line1, "UTF-8");
		String[] s = line.split(Pattern.quote("but"));
		line = line.replaceAll("but", ".");
		int jsonval = 0;
		int mainSentiment = 0;
		JsonArray finalarr = null;
		if (line != null && line.length() > 0) {
			int longest = 0;
			Annotation annotation = new Annotation(line);
			pipeline.annotate(annotation);
			List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
			final JsonBuilderFactory jsonFactory = Json.createBuilderFactory(Collections.emptyMap());
			JsonArrayBuilder arr = Json.createArrayBuilder(); // Add person to array.
			for (CoreMap sentence : sentences) {
				JsonObjectBuilder jsonobj = Json.createObjectBuilder();
				Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				jsonobj.add("Sentence", sentence.toString());
				jsonobj.add("Sentiment", sentiment);
				arr.add(jsonobj);
				jsonval = jsonval + 1;
				String partText = sentence.toString();
				if (partText.length() > longest) {
					mainSentiment = sentiment;
					longest = partText.length();
				}
			}

			finalarr = arr.build();
		}
		return finalarr;
	}
}
