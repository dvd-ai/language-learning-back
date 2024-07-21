CREATE TABLE german_vocabulary_topic_entity (
  id BIGINT NOT NULL,
   german_vocabulary_topic JSONB,
   translation_language VARCHAR(255),
   CONSTRAINT pk_germanvocabularytopicentity PRIMARY KEY (id)
);