package com.edu.silva.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class DefaultResponse {

    private String message;
    private Object data;
    private Object links;

    public DefaultResponse(String message, Object data, String baseUrl) {
        this.message = message;

        if (data instanceof Page<?> page) {
            this.data = new PageResponse(page.getTotalElements(), page.getNumber(), page.getSize(), page.getContent());
            this.links = buildPaginationLinks(baseUrl, page);
        }
    }

    public DefaultResponse(String message) {
        this.message = message;
    }

    public DefaultResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public DefaultResponse(String message, Object data, String baseUrl, UUID id) {
        this.message = message;
        this.data = data;
        this.links = buildEntityLinks(baseUrl, id);
    }

    private Map<String, String> buildEntityLinks(String baseUrl, UUID id) {
        Map<String, String> links = new HashMap<>();

        String url = String.format("%s/%s", baseUrl, id);

        links.put("self", url);
        links.put("edit", url);
        links.put("delete", url);

        return links;
    }

    private Map<String, String> buildPaginationLinks(String baseUrl, Page<?> page) {
        Map<String, String> links = new HashMap<>();

        int pageNumber = page.getNumber();
        int size = page.getSize();

        links.put("self", String.format("%s?page=%d&size=%d", baseUrl, pageNumber, size));

        if (page.hasNext()) {
            links.put("next", String.format("%s?page=%d&size=%d", baseUrl, pageNumber + 1, size));
        }

        if (page.hasPrevious()) {
            links.put("prev", String.format("%s?page=%d&size=%d", baseUrl, pageNumber - 1, size));
        }

        return links;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private static class PageResponse {
        private long total;
        private Integer page;
        private Integer size;
        private Object items;
    }
}
