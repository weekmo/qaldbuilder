package org.hobbit.qaldbuilder;

import org.apache.jena.atlas.json.JSON;

public class TestClass {
	public static void main(String[] args) {
		QaldBuilder qa = new QaldBuilder(getQaldFormat());
		//qa.setQuestionAsJson(getQuestion());
		//System.out.println(qa.getQaldQuestion());
		qa.setDatasetID("test");
		//qa=new QaldBuilder(getQaldFormat());
		System.out.println(qa.getQaldQuestion());
		qa.removeAnswers();
		qa.removeQuery();
		System.out.println("\n\n"+qa.getQaldQuestion());
	}
	public static String getQaldFormat() {
		return JSON.read("data/qald_format.json").toString();
	}
	public static String getQuestion() {
		return JSON.read("data/question.json").toString();
	}
}
