package com.cs.entities;

import java.util.List;

public class WordCloudResponse {

	public WordCloudObject getWordCloudObject() {
		return wordCloudObject;
	}

	public void setWordCloudObject(WordCloudObject wordCloudObject) {
		this.wordCloudObject = wordCloudObject;
	}

	private WordCloudObject wordCloudObject;
	private WordCloudObject wordCloudObjectWeek;
	private WordCloudObject wordCloudObjectMonth;
	public WordCloudObject getWordCloudObjectWeek() {
		return wordCloudObjectWeek;
	}

	public void setWordCloudObjectWeek(WordCloudObject wordCloudObjectWeek) {
		this.wordCloudObjectWeek = wordCloudObjectWeek;
	}

	public WordCloudObject getWordCloudObjectMonth() {
		return wordCloudObjectMonth;
	}

	public void setWordCloudObjectMonth(WordCloudObject wordCloudObjectMonth) {
		this.wordCloudObjectMonth = wordCloudObjectMonth;
	}
}
