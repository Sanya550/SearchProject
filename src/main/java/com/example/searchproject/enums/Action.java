package com.example.searchproject.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Action {
    TO_LOWER_CASE,
    TOKENIZATION_BY_WORDS,
    TOKENIZATION_BY_SENTENCES,
    REMOVE_STOP_WORDS,
    STEMING,
    LEMMING,
    INVERT_LIST,
    FIND_DOCUMENT,
    GET_CURRENT_DOCUMENTS
}
