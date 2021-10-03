package io.github.patrykblajer.todo;

enum Category {
    HOME("dom"),
    WORK("praca"),
    ENTERTAINMENT("rozrywka"),
    OTHER("pozostałe");

    private final String plTranslation;

    Category(String plTranslation) {
        this.plTranslation = plTranslation;
    }

    public String getCategoryInPlTranslation() {
        return plTranslation;
    }
}