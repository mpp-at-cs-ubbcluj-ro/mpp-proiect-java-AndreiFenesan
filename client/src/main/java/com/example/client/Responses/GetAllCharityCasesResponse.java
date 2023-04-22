package com.example.client.Responses;

import com.example.client.model.dtos.CharityCaseDto;

public class GetAllCharityCasesResponse implements Response{
    private Iterable<CharityCaseDto> charityCasesDto;

    public GetAllCharityCasesResponse(Iterable<CharityCaseDto> charityCasesDto) {
        this.charityCasesDto = charityCasesDto;
    }

    public Iterable<CharityCaseDto> getCharityCasesDto() {
        return charityCasesDto;
    }
}
