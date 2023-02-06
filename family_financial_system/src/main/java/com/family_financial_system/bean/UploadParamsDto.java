package com.family_financial_system.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadParamsDto {
    @NotBlank(message = "id:不能为空")
    private String id;
    @NotBlank(message = "name:不能为空")
    private String name;
    @NotBlank(message = "type:不能为空")
    private String type;
    @NotBlank(message = "lastModifiedDate:不能为空")
    private String lastModifiedDate;
    @NotBlank(message = "size:不能为空")
    private String size;
    @NotNull(message = "file:不能为空")
    private MultipartFile file;
}
