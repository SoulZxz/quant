package com.ricequant.strategy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ricequant.strategy.support.FileUtils;

public class DataExtractor {

	public static void main(String[] args) {
		FileUtils utils = new FileUtils();
		Document doc = Jsoup.parseBodyFragment(utils.readFileAsString("data/rawDomNodes.txt"));
		Elements newsHeadlines = doc.select("ul > p");

		StringBuilder result = new StringBuilder();

		for (Element ele : newsHeadlines) {
			result.append(ele.ownText().trim()).append(",").append(ele.child(0).text())
					.append("\n");
		}

		System.out.println(result.toString());
	}

}
