package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
@Data
public class TextSimilarityAnalyzerRequest extends Request {
    private String destinationFolder;
    private boolean analyzeAllTags;
    private Set<String> tagsToAnalyze;
    private String processUrl;

    public TextSimilarityAnalyzerRequest(Map<String, String> request) throws InvalidJarRequestException {
        validate(request);
        this.processUrl = request.get(TextSimilarityAnalyzerRequest.REQUEST_PARAMETER.PAGE_DOMAIN_URL.name());
        this.destinationFolder = Paths.get(request.get(TextSimilarityAnalyzerRequest.REQUEST_PARAMETER.ROOT_FOLDER_PATH.name()), FolderName.REPORT.name()).toString();
        String tagsToAnalyzeParameter = request.get(TextSimilarityAnalyzerRequest.REQUEST_PARAMETER.TAGS_TO_ANALYZE.name());
        if ("ALL".equalsIgnoreCase(tagsToAnalyzeParameter)) {
            analyzeAllTags = true;
            this.tagsToAnalyze = new HashSet<>();
        } else {
            analyzeAllTags = false;
            this.tagsToAnalyze = Stream.of(request.get(TextSimilarityAnalyzerRequest.REQUEST_PARAMETER.TAGS_TO_ANALYZE.name()).split(",")).map(String::trim).collect(Collectors.toSet());
        }
    }

    @Override
    public void validate(Map<String, String> request) throws InvalidJarRequestException {
        for (TextSimilarityAnalyzerRequest.REQUEST_PARAMETER parameter : TextSimilarityAnalyzerRequest.REQUEST_PARAMETER.values()) {
            String value = request.get(parameter.name().toUpperCase());
            if (value == null) {
                throw new InvalidJarRequestException("There isn't '" + parameter + "' parameter in execute text analyzing.");
            }

        }
    }

    public enum REQUEST_PARAMETER {
        ROOT_FOLDER_PATH, TAGS_TO_ANALYZE, PAGE_DOMAIN_URL
    }
}
