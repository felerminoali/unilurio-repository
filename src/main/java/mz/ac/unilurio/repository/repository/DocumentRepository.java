package mz.ac.unilurio.repository.repository;

import mz.ac.unilurio.repository.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentRepository extends JpaRepository<Document, Integer>, MyDocumentRepositoryCustom{

    @Query("SELECT d FROM Document d WHERE d.googleId =:googleId")
    public Document findByGoogleId(@Param("googleId") String googleId);

}
