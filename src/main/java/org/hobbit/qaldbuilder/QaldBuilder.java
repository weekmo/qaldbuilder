package org.hobbit.qaldbuilder;

import java.io.ByteArrayOutputStream;

import org.apache.jena.atlas.json.JSON;
import org.apache.jena.atlas.json.JsonBuilder;
import org.apache.jena.atlas.json.JsonObject;
import org.apache.jena.atlas.json.JsonValue;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSetFormatter;
/**
 * 
 * @author Mohammed Abdelgadir, github.com/weekmo
 *
 */
public class QaldBuilder {
	JsonObject qaldFormat,questionObject;
	JsonBuilder jsonBuilder;
	String query;
	/**
	 * Constructor adds empty dataset object and questions array.
	 */
	public QaldBuilder() {
		jsonBuilder = new JsonBuilder();
		jsonBuilder.startObject()
			.key("dataset").startObject().finishObject()
			.key("questions").startArray()
				.startObject().finishObject().finishArray()
		.finishObject();
		qaldFormat = jsonBuilder.build().getAsObject();
		this.questionObject = this.qaldFormat.get("questions").getAsArray().get(0).getAsObject();
	}
	
	/**
	 * Set current qald question as a start
	 * @param question: in json format as a String
	 */
	public void setQuestionAsJson(String question) {
		this.questionObject=JSON.parse(question);
		this.remove("_id");
		
		this.setQuery(this.questionObject.get("query").getAsObject().get("sparql").toString());
	}
	
	public void setQuery(String query) {
		query = query.trim().replace("\"", "");
		this.removeQuery();
		this.questionObject.put("query", query);
		this.query=query;
	}
	
	/**
	 * Set answer as json object
	 * @param answers
	 */
	public void setAnswers(JsonObject answers) {
		this.removeAnswers();
		jsonBuilder.startArray().value(answers).finishArray();
		this.questionObject.put("answers", jsonBuilder.build());
	}
	
	/**
	 * Set answer(s) by retrieving info from sparql service
	 * @param sparqlService
	 */
	public void setAnswers(String sparqlService) {
		if(this.questionObject.hasKey("answers"))
			this.questionObject.remove("answers");
		//"http://dbpedia.org/sparql"
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlService, query);
		//qexec.setTimeout(2000);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		if(query.startsWith("ASK WHERE")) {
			ResultSetFormatter.outputAsJSON(outputStream, qexec.execAsk());
		}else {
			ResultSetFormatter.outputAsJSON(outputStream,qexec.execSelect());
		}
		this.questionObject.put("answers", JSON.parse(outputStream.toString()));
	}
	
	/**
	 * Set question string and language
	 * @param question
	 * @param language
	 */
	public void setQuestionString(String question,String language) {
		language=language.trim().replace("\"", "");
		if(this.questionObject.hasKey("question"))
			this.questionObject.remove("question");
		jsonBuilder.startArray().startObject()
			.key("string").value(question)
			.key("language").value(language)
		.finishObject().finishArray();
		this.questionObject.put("question", jsonBuilder.build());
	}
	
	public void setHybrid(String value) {
		if(this.questionObject.hasKey("hybrid"))
			this.questionObject.remove("hybrid");
		this.questionObject.put("hybrid", value);
	}
	
	public void setOnlydbo(boolean value) {
		if(this.questionObject.hasKey("onlydbo"))
			this.questionObject.remove("onlydbo");
		this.questionObject.put("onlydbo", value);
	}
	
	public void setAggregation(boolean value) {
		if(this.questionObject.hasKey("aggregation"))
			this.questionObject.remove("aggregation");
		this.questionObject.put("aggregation", value);
	}
	
	/**
	 * Set question ID
	 * @param value
	 */
	public void setID(int value) {
		if(this.questionObject.hasKey("id"))
			this.questionObject.remove("id");
		this.questionObject.put("id", value);
	}
	
	/**
	 * Set Dataset ID
	 * @param id
	 */
	public void setDatasetID(String id) {
		if(this.qaldFormat.get("dataset").getAsObject().hasKey("id"))
			this.qaldFormat.get("dataset").getAsObject().remove("id");
		this.qaldFormat.get("dataset").getAsObject().put("id", id);
	}
	
	public void removeQuery() {
		if(this.questionObject.hasKey("query"))
			this.questionObject.remove("query");
	}
	
	public void removeAnswers() {
		if(this.questionObject.hasKey("answers"))
			this.questionObject.remove("answers");
	}
	
	public void remove(String key) {
		if(this.questionObject.hasKey(key))
			this.questionObject.remove(key);
	}
	
	public void add(String key,JsonValue value) {
		this.questionObject.put(key, value);
	}
	
	public void add(String key,String value) {
		this.questionObject.put(key, value);
	}
	
	public void add(String key,long value) {
		this.questionObject.put(key, value);
	}
	
	public void add(String key,boolean value) {
		this.questionObject.put(key, value);
	}
	
	
	/**
	 * Get all question info as Qald format
	 * @return
	 */
	public String getQaldQuestion() {
		this.qaldFormat.get("questions").getAsArray().clear();
		this.qaldFormat.get("questions").getAsArray().add(this.questionObject);
		return this.qaldFormat.toString();
	}
}
