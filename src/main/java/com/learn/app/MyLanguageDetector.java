package com.learn.app;

import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class MyLanguageDetector extends LanguageDetector {

    LanguageDetector languageDetector = new LanguageDetector() {
        @Override
        public LanguageDetector loadModels() throws IOException {
            return null;
        }

        @Override
        public LanguageDetector loadModels(Set<String> set) throws IOException {
            return null;
        }

        @Override
        public boolean hasModel(String s) {
            return false;
        }

        @Override
        public LanguageDetector setPriors(Map<String, Float> map) throws IOException {
            return null;
        }

        @Override
        public void reset() {

        }

        @Override
        public void addText(char[] chars, int i, int i1) {

        }

        @Override
        public List<LanguageResult> detectAll() {
            return null;
        }
    };

}
