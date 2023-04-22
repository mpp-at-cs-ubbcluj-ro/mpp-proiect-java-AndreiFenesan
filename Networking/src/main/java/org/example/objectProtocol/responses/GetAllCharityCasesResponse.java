package org.example.objectProtocol.responses;

import org.models.dtos.CharityCaseDto;

import java.util.List;

public class GetAllCharityCasesResponse implements Response{
    private Iterable<CharityCaseDto> charityCasesDto;

    public GetAllCharityCasesResponse(Iterable<CharityCaseDto> charityCasesDto) {
        this.charityCasesDto = charityCasesDto;
    }

    public Iterable<CharityCaseDto> getCharityCasesDto() {
        return charityCasesDto;
    }
}
