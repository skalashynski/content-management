package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.Constant;
import com.productiveedge.content_mgmt_automation.entity.request.SaveTxtRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.PageInfoCollectorHelper;
import com.productiveedge.content_mgmt_automation.report.FileWriter;
import com.productiveedge.content_mgmt_automation.repository.container.impl.PageContainer;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Data
public class TxtFileGeneratorFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(TxtFileGeneratorFlow.class);

    private final SaveTxtRequest saveTxtRequest;
    private final PageContainer pageContainer;

    public TxtFileGeneratorFlow(SaveTxtRequest saveTxtRequest) {
        this.pageContainer = PageContainer.getInstance();
        this.saveTxtRequest = saveTxtRequest;
    }

    @Override
    public void run() {
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext pcontext = new ParseContext();
        HtmlParser htmlparser = new HtmlParser();
        pageContainer.getProcessedPageEntries().forEach(e -> {
            String pageUrl = e.getValue().getUrl();
            logger.info("Converting html and saving txt content of url: " + pageUrl);
            String htmlContent = e.getValue().getHtmlContent();
            String destinationTxtFilePath = Paths.get(saveTxtRequest.getDestinationFolder(), Constant.generateDate(), PageInfoCollectorHelper.generateNameByKey(e.getKey())).toString();
            try {
                htmlparser.parse(IOUtils.toInputStream(htmlContent, StandardCharsets.UTF_8), handler, metadata, pcontext);
                saveTxtContent(destinationTxtFilePath + ".txt", handler.toString(), pageUrl);
                saveMetadata(destinationTxtFilePath + "_metadata.txt", metadata, pageUrl);
                logger.info("Html content of " + pageUrl + " successfully converted to txt and saved");
            } catch (TikaException | SAXException | IOException ex) {
                logger.error("Can't parse and covert html to txt. Url: " + pageUrl + ". " + ex.getMessage());
            }
        });
    }

    private void saveTxtContent(String destinationTxtFilePath, String txtContent, String url) {
        try {
            FileWriter.write(destinationTxtFilePath, txtContent);
        } catch (IOException ex) {
            logger.error("Can't save txt file content of page processUrl: " + url + "." + IOUtils.LINE_SEPARATOR + ex.getMessage());
        }
    }

    private void saveMetadata(String destinationTxtFilePath, Metadata metadata, String url) {
        try {
            FileWriter.write(destinationTxtFilePath, String.join(IOUtils.LINE_SEPARATOR, metadata.names()));
        } catch (IOException ex) {
            logger.error("Can't save metadata file content of page processUrl: " + url + "." + IOUtils.LINE_SEPARATOR + ex.getMessage());
        }
    }
}
