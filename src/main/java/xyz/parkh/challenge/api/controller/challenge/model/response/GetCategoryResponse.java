package xyz.parkh.challenge.api.controller.challenge.model.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetCategoryResponse {
    List<String> categories = new ArrayList<>();

    public GetCategoryResponse(List<String> categories) {
        this.categories = categories;
    }
}

