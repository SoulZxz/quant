package com.ricequant.strategy.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.ricequant.strategy.support.mock.ReportBuffer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class ReportGenerator {

	private static Configuration cfg;

	static {
		cfg = new Configuration(Configuration.VERSION_2_3_22);
		try {
			cfg.setDirectoryForTemplateLoading(new File("templates"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		cfg.setBooleanFormat("yes,no");
	}

	public static void generateReport(ReportBuffer reportBuffer) {
		String fileName = reportBuffer.getStrategyName() + "-" + reportBuffer.getStartDay() + "-"
				+ reportBuffer.getEndDay();
		String varName = "_" + DigestUtil.sign(reportBuffer.getStrategyParams().toString(), "md5");

		Map<String, Object> root = new HashMap<String, Object>();
		root.put("title", fileName);
		root.put("report", reportBuffer);

		try {
			File outputFile = new File("reports/" + fileName + varName + ".html");
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}

			Template reportTemp = cfg.getTemplate("report-template.html");
			Writer out = new OutputStreamWriter(new FileOutputStream(outputFile));
			reportTemp.process(root, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}

}
