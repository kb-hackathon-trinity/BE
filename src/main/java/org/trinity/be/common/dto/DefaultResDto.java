package org.trinity.be.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class DefaultResDto<T> {

    private String responseCode;
    private String responseMessage;
    private DataDto responseData;

    @Builder(builderMethodName = "noDataBuilder", builderClassName = "noDataBuilder")
    public DefaultResDto(final String responseCode, final String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseData = DataDto.noDataBuilder().build();
    }

    @Builder(builderMethodName = "singleDataBuilder", builderClassName = "singleDataBuilder")
    public DefaultResDto(final String responseCode,
                         final String responseMessage,
                         final T data) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseData = DataDto.singleDataBuilder()
                .data(data)
                .build();
    }

    @Builder(builderMethodName = "multiDataBuilder", builderClassName = "multiDataBuilder")
    public DefaultResDto(final String responseCode,
                         final String responseMessage,
                         final T data,
                         final Integer size) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseData = DataDto.multiDataBuilder()
                .data(data)
                .size(size)
                .build();
    }
}
