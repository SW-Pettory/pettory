package com.pettory.pettory.jointshopping.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@ToString
@Schema(description = "공동구매모임 DTO")
public class JointShoppingGroupDTO {
    private Long jointShoppingGroupNum;
    private String jointShoppingGroupName;
    private String jointShoppingProducts;
    private String jointShoppingProductsState;
    private String jointShoppingProductsFileDirectory;
    private String jointShoppingInfo;
    private Integer jointShoppingCost;
    private Integer jointShoppingGroupMaximumCount;
    private Integer jointShoppingParticipationMaximumCount;
    private String hostCourierCode;
    private String hostInvoiceNum;
    private LocalDate jointShoppingGroupInsertDatetime;
    private String jointShoppingGroupState;
    private JointShoppingCategoryDTO category;
    private Long userId;
}
