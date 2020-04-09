package mz.ac.unilurio.repository.repository;

import mz.ac.unilurio.repository.model.Document;

import java.util.List;

public interface MyDocumentRepositoryCustom {
    List<Document> findByFilter(String year, String category, String groupby, String search);
}