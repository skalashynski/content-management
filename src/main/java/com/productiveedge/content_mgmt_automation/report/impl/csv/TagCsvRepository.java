package com.productiveedge.content_mgmt_automation.report.impl.csv;

import com.productiveedge.content_mgmt_automation.entity.tag.Tag;
import com.productiveedge.content_mgmt_automation.report.Report;
import com.productiveedge.content_mgmt_automation.report.exception.ReportException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Deprecated
public class TagCsvRepository implements Report<List<Tag>> {
    private final String fileName;

    public TagCsvRepository(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void saveAll(List<Tag> elements) throws ReportException {
        int numFiles = elements.size() / 10 + 1;
        for (int i = 0; i < numFiles; i++) {
            int startItr = i * 10;
            //int lastIrt = (i + 1) * 10;
            int lastIrt = (i + 1) * 10 > elements.size() ? elements.size() - 1 : (i + 1) * 10;
            String testFileName = "testData/result1/" + startItr + "-" + (startItr + 10) + " test.csv";
            try (FileWriter csvWriter = new FileWriter(testFileName)) {
                for (int j = startItr; j < lastIrt; j++) {
                    csvWriter.append(String.join(",", quote(elements.get(j).getPageUrl()), quote(elements.get(j).getShortXPath()), quote(elements.get(j).getFullXPath()), quote(elements.get(j).getFullTagXPath()), quote(elements.get(j).getName()), quote(elements.get(j).getTextContent())));
                    csvWriter.append("\n");
                }
                csvWriter.flush();
            } catch (IOException e) {
                throw new ReportException(e);
            }
        }

    }

    private String quote(String s) {
        return "\"" + s + "\"";
    }
}
