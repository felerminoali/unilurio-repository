package mz.ac.unilurio.repository.repository;

import mz.ac.unilurio.repository.model.Document;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class MyDocumentRepositoryCustomImpl implements MyDocumentRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Document> findByFilter(String year, String category, String groupBy, String search) {

        String sql = "SELECT d FROM Document d";

        String and = "";
        if(year != null){
            sql += " WHERE d.year ="+year ;
            and = " AND";
        }

        if(category != null) {
            sql += (and.isEmpty() ? " WHERE" : and) + " d.category.id =" + category ;
            and = " AND";
        }

        sql += (search != null) ? (and.isEmpty() ? " WHERE":and) + " d.title LIKE \'%"+search+"%\'" : "";
        sql += (groupBy !=null) ? " "+groupBy : "";

        List<Document> list = em.createQuery(sql).getResultList();
        return list;
    }
}
