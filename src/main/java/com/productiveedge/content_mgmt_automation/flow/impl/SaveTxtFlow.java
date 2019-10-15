package com.productiveedge.content_mgmt_automation.flow.impl;

import com.productiveedge.content_mgmt_automation.entity.request.SaveTxtRequest;
import com.productiveedge.content_mgmt_automation.flow.Flow;
import com.productiveedge.content_mgmt_automation.flow.impl.helper.GrabAllLinksHelper;
import com.productiveedge.content_mgmt_automation.repository.container.impl.PageContainer;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.productiveedge.content_mgmt_automation.flow.impl.helper.FlowHelper.generateDateFolderName;

@Data
public class SaveTxtFlow implements Flow {
    private static final Logger logger = LoggerFactory.getLogger(SaveTxtFlow.class);

    private final SaveTxtRequest saveTxtRequest;
    private final PageContainer pageContainer;

    public SaveTxtFlow(SaveTxtRequest saveTxtRequest) {
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
            String destinationTxtFilePath = Paths.get(saveTxtRequest.getDestinationFolder(), generateDateFolderName(), GrabAllLinksHelper.generateNameByKey(e.getKey())).toString();
            try {
                htmlparser.parse(IOUtils.toInputStream(htmlContent, "UTF-8"), handler, metadata, pcontext);
                saveTxtContent(destinationTxtFilePath + ".txt", handler.toString(), pageUrl);
                saveMetadata(destinationTxtFilePath + "_metadata.txt", metadata, pageUrl);
                logger.info("Html content of " + pageUrl + " successfully converted to txt and saved");
            } catch (TikaException | SAXException | IOException ex) {
                logger.error("Can't parse and covert html to txt. Url: " + pageUrl + ". " + ex.getMessage());
            }
        });
    }

    private void saveTxtContent(String destinationTxtFilePath, String txtContent, String url) {
        File file = new File(destinationTxtFilePath);
        try {
            FileUtils.writeStringToFile(file, txtContent, "UTF8");
        } catch (IOException ex) {
            logger.error("Can't save txt file content of page processUrl: " + url + ".\n" + ex.getMessage());
        }
    }

    private void saveMetadata(String destinationTxtFilePath, Metadata metadata, String url) {
        File file = new File(destinationTxtFilePath);
        try {
            FileUtils.writeLines(file, Arrays.asList(metadata.names()));
        } catch (IOException ex) {
            logger.error("Can't save metadata file content of page processUrl: " + url + ".\n" + ex.getMessage());
        }
    }
}
