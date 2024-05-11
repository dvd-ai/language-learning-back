CREATE TABLE english_vocabulary_topic_entity (
  id BIGINT NOT NULL,
   english_vocabulary_topic JSONB,
   translation_language VARCHAR(255) NOT NULL,
   CONSTRAINT pk_englishvocabularytopicentity PRIMARY KEY (id)
);