package com.argo.collect.crawler;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Getter;
import org.jsoup.helper.DataUtil;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Builder
public class WebCrawlerSourceTree {
    @Getter
    private String url;

    @Getter
    private String path;

    @Getter
    @Builder.Default
    private long loadSecond = 3;

    @Getter
    private Set<String> extractTags;

    @Getter
    private Map<String /* tag */, Set<String> /* value */> validateMap;

    @Getter
    private boolean findAll;

    @Getter
    private Map<String, String> queryStrings;

    @Getter
    @Builder.Default
    private String charset = "utf-8";

    @Getter
    private Set<WebCrawlerSourceTree> childNodes;

    public synchronized void setParentNode(WebCrawlerSourceTree parent) {
        if (CollectionUtils.isEmpty(parent.childNodes)) {
            parent.childNodes = Sets.newHashSet();
        }
        parent.childNodes.add(this);
    }

    public boolean isLeaf() {
        return CollectionUtils.isEmpty(this.childNodes);
    }

    public String getUrlPath() {
        return this.url + Optional.ofNullable(this.path).orElse("");
    }

    public WebCrawlerSourceTree create(String path, Map<String, String> queryStrings) {
        Map<String, String> mergedQueryString = Optional.ofNullable(queryStrings).orElse(Maps.newHashMap());

        if(Objects.nonNull(this.queryStrings)) {
            this.queryStrings.forEach((key, value) -> mergedQueryString.merge(key, value, (value1, value2) -> value2));
        }

        return WebCrawlerSourceTree.builder()
                .url(this.url)
                .path(path)
                .extractTags(this.getExtractTags())
                .validateMap(this.validateMap)
                .findAll(this.isFindAll())
                .childNodes(this.childNodes)
                .queryStrings(mergedQueryString)
                .build();
    }

    public String getFullUrl() throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        builder.append("http://").append(this.url).append(this.path);

        if(Objects.nonNull(this.queryStrings) && this.queryStrings.size() > 0) {
            boolean first = true;

            for(Map.Entry<String, String> entry : this.queryStrings.entrySet()) {
                builder
                        .append(first ? '?' : '&')
                        .append(URLEncoder.encode(entry.getKey(), this.charset))
                        .append('=')
                        .append(URLEncoder.encode(entry.getValue(), this.charset));

                first = false;
            }
        }

        return builder.toString();
    }
}