package org.hobbit.qaldbuilder;

import org.apache.jena.atlas.json.JSON;

public class TestClass {
	public static void main(String[] args) {
		QaldBuilder qa = new QaldBuilder(getQaldFormat());
		qa.setQuestionAsJson(getQuestion());
		//System.out.println(qa.getQaldQuestion());
		qa.setDatasetID("test");
		qa.setAnswers("http://dbpedia.org/sparql");
		if(qa.getAnswers()!=null)
			for(String ans:qa.getAnswers()) {
				System.out.println(ans);
			}
		else
			System.out.println(qa.getQaldQuestion());
		System.out.println(qa.getQaldQuestion());
		//JsonObject json = JSON.parse(qa.getQaldQuestion());
		//System.out.println(json.get("questions").getAsArray().get(0).getAsObject().get("answers").getAsArray().get(0).getAsObject().hasKey("boolean"));
		//qa.removeAnswers();
		//qa.removeQuery();
		//System.out.println("\n\n"+qa.getQaldQuestion());
	}
	public static String getQaldFormat() {
		return JSON.read("data/qald_format.json").toString();
	}
	public static String getQuestion() {
		return JSON.read("data/question.json").toString();
	}
}
