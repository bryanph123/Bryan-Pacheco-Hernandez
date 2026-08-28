package com.example.data.repository

import com.example.data.local.dao.SavedVocabDao
import com.example.data.local.dao.TopicDao
import com.example.data.local.dao.TopicNoteDao
import com.example.data.local.entities.SavedVocabItemEntity
import com.example.data.local.entities.TopicEntity
import com.example.data.local.entities.TopicNoteEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class TopicRepository(
    private val topicDao: TopicDao,
    private val topicNoteDao: TopicNoteDao,
    private val savedVocabDao: SavedVocabDao
) {
    val allTopics: Flow<List<TopicEntity>> = topicDao.getAllTopics()

    suspend fun ensureTopicsSeeded() = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val count = topicDao.getTopicCount()
        val initialList = com.example.data.local.seed.TopicSeedData.getInitialTopics()
        if (count < initialList.size) {
            topicDao.insertTopics(initialList)
        }
    }

    fun getTopic(id: String): Flow<TopicEntity?> = topicDao.getTopicById(id)

    fun getNotes(topicId: String): Flow<List<TopicNoteEntity>> = topicNoteDao.getNotesForTopic(topicId)

    suspend fun addNote(topicId: String, content: String) {
        topicNoteDao.insertNote(
            TopicNoteEntity(
                topicId = topicId,
                noteContent = content,
                timestamp = System.currentTimeMillis()
            )
        )
        topicDao.updateTopicStatus(topicId, "IN_PROGRESS", System.currentTimeMillis())
    }

    suspend fun updateStatus(topicId: String, status: String) {
        topicDao.updateTopicStatus(topicId, status, System.currentTimeMillis())
    }

    suspend fun addCustomTopic(
        title: String,
        titleSpanish: String,
        category: String,
        moduleGroup: String,
        explanation: String,
        examples: List<Pair<String, String>>
    ) {
        val id = "custom_" + UUID.randomUUID().toString().substring(0, 8)
        val examplesJson = examples.joinToString(prefix = "[", postfix = "]") { (en, es) ->
            "{\"en\":\"${en.replace("\"", "\\\"")}\",\"es\":\"${es.replace("\"", "\\\"")}\"}"
        }

        val topic = TopicEntity(
            id = id,
            title = title,
            titleSpanish = titleSpanish,
            category = category,
            moduleGroup = moduleGroup,
            orderIndex = 999,
            explanation = explanation,
            examplesJson = examplesJson,
            commonMistakesJson = "[]",
            miniGlossaryJson = "[]",
            difficulty = "B2",
            estimatedMinutes = 20,
            status = "NOT_STARTED",
            isCustom = true
        )
        topicDao.insertTopic(topic)
    }

    suspend fun deleteCustomTopic(id: String) {
        topicDao.deleteCustomTopic(id)
    }

    suspend fun saveVocabToSrs(
        sourceText: String,
        translation: String,
        phonetic: String,
        sourceModule: String,
        definition: String,
        examplesJson: String = "[]",
        synonymsJson: String = "[]",
        linkedTopicId: String? = null
    ) {
        val id = "vocab_" + UUID.randomUUID().toString()
        val item = SavedVocabItemEntity(
            id = id,
            sourceText = sourceText,
            translation = translation,
            phonetic = phonetic,
            sourceModule = sourceModule,
            definition = definition,
            examplesJson = examplesJson,
            synonymsJson = synonymsJson,
            savedAt = System.currentTimeMillis(),
            linkedTopicId = linkedTopicId,
            repetitionNumber = 0,
            intervalDays = 1,
            easeFactor = 2.5f,
            nextReviewTimestamp = System.currentTimeMillis(),
            masteryLevel = 0
        )
        savedVocabDao.insertVocabItem(item)
    }
}
