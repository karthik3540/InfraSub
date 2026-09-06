package com.infrasub.backend.repository;

import com.infrasub.backend.entity.KnowledgeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocument, Long> {

    List<KnowledgeDocument> findByCategory(String category);

    @Query(value = """
            SELECT * FROM knowledge_documents
            WHERE LOWER(title) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(content) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(category) LIKE LOWER(CONCAT('%', :query, '%'))
            LIMIT :limit
            """, nativeQuery = true)
    List<KnowledgeDocument> searchByText(@Param("query") String query, @Param("limit") int limit);

    @Query(value = """
            SELECT * FROM knowledge_documents
            WHERE embedding IS NOT NULL
            ORDER BY embedding <-> CAST(:vectorStr AS vector)
            LIMIT :limit
            """, nativeQuery = true)
    List<KnowledgeDocument> findSimilarByVector(@Param("vectorStr") String vectorStr, @Param("limit") int limit);
}
