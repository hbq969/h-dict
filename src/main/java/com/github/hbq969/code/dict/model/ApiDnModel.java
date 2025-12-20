package com.github.hbq969.code.dict.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ApiDnModel {
    @NotNull(message = "fs is null")
    private List<String> fs;
}
