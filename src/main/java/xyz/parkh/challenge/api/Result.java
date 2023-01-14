package xyz.parkh.challenge.api;


import lombok.Builder;
import lombok.Data;

@Data
public class Result<T> {
    private T data;
    private T error;

    @Builder
    public Result(T data, T error) {
        this.data = data;
        this.error = error;
    }
}

