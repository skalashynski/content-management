package com.productiveedge.content_mgmt_automation.entity.request;

import com.productiveedge.content_mgmt_automation.entity.FolderName;
import com.productiveedge.content_mgmt_automation.flow.exception.InvalidJarRequestException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class TextSimilarityAnalyzerRequest extends Request {
    private String destinationFolder;
    private boolean analyzeAllTags;
    private Set<String> tagsToAnalyze;

    public TextSimilarityAnalyzerRequest(Map<String, String> request) throws InvalidJarRequestException {
        validate(request);
        this.destinationFolder = Paths.get(request.get(TextSimilarityAnalyzerRequest.REQUEST_PARAMETER.ROOT_FOLDER_PATH.name()), FolderName.REPORT.name()).toString();
        String tagsToAnalyzeParameter = request.get(TextSimilarityAnalyzerRequest.REQUEST_PARAMETER.TAGS_TO_ANALYZE.name());
        if ("ALL".equalsIgnoreCase(tagsToAnalyzeParameter)) {
            analyzeAllTags = true;
        } else {
            Collection<String> tags = Arrays.asList(request.get(TextSimilarityAnalyzerRequest.REQUEST_PARAMETER.TAGS_TO_ANALYZE.name()).split(","));
            this.tagsToAnalyze = tags.stream().map(String::trim).collect(Collectors.toSet());
        }
    }

    @Override
    public void validate(Map<String, String> request) throws InvalidJarRequestException {
        for (SaveHtmlRequest.REQUEST_PARAMETER parameter : SaveHtmlRequest.REQUEST_PARAMETER.values()) {
            String value = request.get(parameter.name().toUpperCase());
            if (value == null) {
                throw new InvalidJarRequestException("There isn't '" + parameter + "' parameter in execute text analyzing.");
            }

        }
    }

    public enum REQUEST_PARAMETER {
        ROOT_FOLDER_PATH, TAGS_TO_ANALYZE,
    }
}
