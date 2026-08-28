package com.example.data.local.model

import com.example.R

data class KeyVocabItem(
    val word: String,
    val ipa: String,
    val translation: String,
    val partOfSpeech: String,
    val contextSentence: String
)

data class AudiobookSentence(
    val id: String,
    val textEn: String,
    val textEs: String,
    val startSecond: Int,
    val durationSeconds: Int,
    val keyVocab: List<KeyVocabItem> = emptyList()
)

data class AudiobookChapter(
    val id: String,
    val chapterNumber: Int,
    val titleEn: String,
    val titleEs: String,
    val sentences: List<AudiobookSentence>
)

data class AudiobookItem(
    val id: String,
    val titleEn: String,
    val titleEs: String,
    val author: String,
    val cefrLevel: String, // A1, A2, B1, B2
    val durationMinutes: Int,
    val coverResId: Int,
    val descriptionEn: String,
    val descriptionEs: String,
    val tags: List<String>,
    val chapters: List<AudiobookChapter>
)

object AudiobookDataBank {
    val sampleAudiobooks: List<AudiobookItem> = listOf(
        AudiobookItem(
            id = "secret_garden",
            titleEn = "The Secret Garden",
            titleEs = "El Jardín Secreto",
            author = "Frances Hodgson Burnett",
            cefrLevel = "A2-B1",
            durationMinutes = 18,
            coverResId = R.drawable.img_secret_garden_1787935288072,
            descriptionEn = "A timeless story about Mary Lennox, who discovers a hidden, overgrown garden on the Yorkshire Moors and brings it back to life.",
            descriptionEs = "Una historia clásica sobre Mary Lennox, quien descubre un jardín escondido en los páramos de Yorkshire y le devuelve la vida.",
            tags = listOf("Classic", "Nature", "A2", "B1", "Cambridge Movers/Flyers"),
            chapters = listOf(
                AudiobookChapter(
                    id = "sg_ch1",
                    chapterNumber = 1,
                    titleEn = "The Forgotten Key",
                    titleEs = "La Llave Olvidada",
                    sentences = listOf(
                        AudiobookSentence(
                            id = "sg_1_1",
                            textEn = "Mary walked slowly across the wide lawn toward the old ivy-covered wall.",
                            textEs = "Mary caminó lentamente a través del amplio césped hacia la vieja pared cubierta de hiedra.",
                            startSecond = 0,
                            durationSeconds = 6,
                            keyVocab = listOf(
                                KeyVocabItem("ivy", "/ˈaɪ.vi/", "hiedra", "noun", "The wall was covered in ivy."),
                                KeyVocabItem("lawn", "/lɔːn/", "césped / jardín", "noun", "She walked across the green lawn.")
                            )
                        ),
                        AudiobookSentence(
                            id = "sg_1_2",
                            textEn = "A friendly robin was perched on a branch, singing its cheerful morning song.",
                            textEs = "Un simpático petirrojo estaba posado en una rama, cantando su alegre canción matutina.",
                            startSecond = 6,
                            durationSeconds = 6,
                            keyVocab = listOf(
                                KeyVocabItem("robin", "/ˈrɒb.ɪn/", "petirrojo (pájaro)", "noun", "A robin has a red breast."),
                                KeyVocabItem("perched", "/pɜːtʃt/", "posado / encaramado", "verb/adj", "The bird was perched high up.")
                            )
                        ),
                        AudiobookSentence(
                            id = "sg_1_3",
                            textEn = "Suddenly, a gust of wind moved the thick leaves aside, revealing an ancient brass key in the soil.",
                            textEs = "De repente, una ráfaga de viento apartó las espesas hojas, revelando una antigua llave de latón en la tierra.",
                            startSecond = 12,
                            durationSeconds = 7,
                            keyVocab = listOf(
                                KeyVocabItem("gust", "/ɡʌst/", "ráfaga (de viento)", "noun", "A cold gust blew through the trees."),
                                KeyVocabItem("brass", "/brɑːs/", "latón / bronce dorado", "noun", "The handle was made of brass.")
                            )
                        ),
                        AudiobookSentence(
                            id = "sg_1_4",
                            textEn = "Her heart beat fast as she picked up the key. Could this unlock the mysterious locked garden?",
                            textEs = "Su corazón latió rápido cuando recogió la llave. ¿Podría esto abrir el misterioso jardín cerrado?",
                            startSecond = 19,
                            durationSeconds = 7,
                            keyVocab = listOf(
                                KeyVocabItem("unlock", "/ʌnˈlɒk/", "abrir / desbloquear", "verb", "Can you unlock the door?"),
                                KeyVocabItem("mysterious", "/mɪˈstɪə.ri.əs/", "misterioso", "adj", "There was a mysterious sound.")
                            )
                        ),
                        AudiobookSentence(
                            id = "sg_1_5",
                            textEn = "She found the wooden door hidden beneath the ivy and inserted the key into the lock.",
                            textEs = "Encontró la puerta de madera escondida bajo la hiedra e insertó la llave en la cerradura.",
                            startSecond = 26,
                            durationSeconds = 6,
                            keyVocab = listOf(
                                KeyVocabItem("beneath", "/bɪˈniːθ/", "debajo de", "prep", "The cat slept beneath the table."),
                                KeyVocabItem("lock", "/lɒk/", "cerradura", "noun", "Turn the key in the lock.")
                            )
                        ),
                        AudiobookSentence(
                            id = "sg_1_6",
                            textEn = "With a gentle click, the heavy door swung open, welcoming her into a secret world of wonder.",
                            textEs = "Con un suave clic, la pesada puerta se abrió, dándole la bienvenida a un mundo secreto de asombro.",
                            startSecond = 32,
                            durationSeconds = 7,
                            keyVocab = listOf(
                                KeyVocabItem("swung open", "/swʌŋ ˈəʊ.pən/", "se abrió de par en par", "verb phrase", "The gate swung open."),
                                KeyVocabItem("wonder", "/ˈwʌn.dər/", "maravilla / asombro", "noun", "Her eyes were full of wonder.")
                            )
                        )
                    )
                )
            )
        ),
        AudiobookItem(
            id = "sherlock_red_headed",
            titleEn = "The Red-Headed League",
            titleEs = "La Liga de los Pelirrojos",
            author = "Sir Arthur Conan Doyle",
            cefrLevel = "B1-B2",
            durationMinutes = 24,
            coverResId = R.drawable.img_sherlock_holmes_1787935300349,
            descriptionEn = "Sherlock Holmes and Dr. Watson investigate an extraordinary puzzle involving an unusual pawnshop and a mysterious league.",
            descriptionEs = "Sherlock Holmes y el Dr. Watson investigan un enigma extraordinario que involucra una inusual casa de empeños y una liga misteriosa.",
            tags = listOf("Mystery", "Detective", "B1", "B2", "Cambridge B1/B2"),
            chapters = listOf(
                AudiobookChapter(
                    id = "sh_ch1",
                    chapterNumber = 1,
                    titleEn = "The Strange Advertisement",
                    titleEs = "El Extraño Anuncio",
                    sentences = listOf(
                        AudiobookSentence(
                            id = "sh_1_1",
                            textEn = "I had called upon my friend Sherlock Holmes at Baker Street one autumn afternoon.",
                            textEs = "Había visitado a mi amigo Sherlock Holmes en Baker Street una tarde de otoño.",
                            startSecond = 0,
                            durationSeconds = 6,
                            keyVocab = listOf(
                                KeyVocabItem("call upon", "/kɔːl əˈpɒn/", "visitar a alguien", "phrasal verb", "I will call upon them tomorrow."),
                                KeyVocabItem("autumn", "/ˈɔː.təm/", "otoño", "noun", "Leaves turn orange in autumn.")
                            )
                        ),
                        AudiobookSentence(
                            id = "sh_1_2",
                            textEn = "He was in deep conversation with a stout, florid-faced gentleman with fiery red hair.",
                            textEs = "Estaba en profunda conversación con un caballero corpulento, de rostro sonrosado y cabello rojo fuego.",
                            startSecond = 6,
                            durationSeconds = 7,
                            keyVocab = listOf(
                                KeyVocabItem("stout", "/staʊt/", "corpulento / robusto", "adj", "A stout wooden door."),
                                KeyVocabItem("fiery", "/ˈfaɪə.ri/", "ardiente / de fuego", "adj", "Her fiery red scarf stood out.")
                            )
                        ),
                        AudiobookSentence(
                            id = "sh_1_3",
                            textEn = "Holmes pointed to a peculiar newspaper clipping lying on the mahogany table.",
                            textEs = "Holmes señaló un peculiar recorte de periódico que yacía sobre la mesa de caoba.",
                            startSecond = 13,
                            durationSeconds = 6,
                            keyVocab = listOf(
                                KeyVocabItem("clipping", "/ˈklɪp.ɪŋ/", "recorte de prensa", "noun", "He kept a newspaper clipping."),
                                KeyVocabItem("mahogany", "/məˈhɒɡ.ən.i/", "caoba (madera noble)", "noun/adj", "The table was made of mahogany.")
                            )
                        ),
                        AudiobookSentence(
                            id = "sh_1_4",
                            textEn = "'Observe this notice, Watson,' said Holmes with a keen spark in his grey eyes.",
                            textEs = "'Observa este aviso, Watson', dijo Holmes con un agudo destello en sus ojos grises.",
                            startSecond = 19,
                            durationSeconds = 6,
                            keyVocab = listOf(
                                KeyVocabItem("notice", "/ˈnəʊ.tɪs/", "aviso / anuncio", "noun", "There was a warning notice on the wall."),
                                KeyVocabItem("keen", "/kiːn/", "agudo / perspicaz", "adj", "He has a keen eye for details.")
                            )
                        ),
                        AudiobookSentence(
                            id = "sh_1_5",
                            textEn = "'It offers four pounds a week for purely nominal services to men with bright red hair.'",
                            textEs = "'Ofrece cuatro libras a la semana por servicios puramente nominales a hombres con cabello rojo brillante.'",
                            startSecond = 25,
                            durationSeconds = 7,
                            keyVocab = listOf(
                                KeyVocabItem("nominal", "/ˈnɒm.ɪ.nəl/", "simbólico / nominal", "adj", "They charged a nominal fee."),
                                KeyVocabItem("bright", "/braɪt/", "brillante / encendido", "adj", "The sun is very bright.")
                            )
                        )
                    )
                )
            )
        ),
        AudiobookItem(
            id = "little_prince",
            titleEn = "The Little Prince & The Asteroid",
            titleEs = "El Principito y el Asteroide",
            author = "Antoine de Saint-Exupéry",
            cefrLevel = "A1-A2",
            durationMinutes = 15,
            coverResId = R.drawable.img_little_prince_1787935313387,
            descriptionEn = "A gentle philosophical journey about love, friendship, and seeing what is invisible to the eye.",
            descriptionEs = "Un viaje filosófico y entrañable sobre el amor, la amistad y ver lo que es invisible a los ojos.",
            tags = listOf("Poetic", "Philosophical", "A1", "A2", "Starters/Movers"),
            chapters = listOf(
                AudiobookChapter(
                    id = "lp_ch1",
                    chapterNumber = 1,
                    titleEn = "The Unique Rose",
                    titleEs = "La Rosa Única",
                    sentences = listOf(
                        AudiobookSentence(
                            id = "lp_1_1",
                            textEn = "On Asteroid B-612, the little prince took great care of his tiny planet every morning.",
                            textEs = "En el asteroide B-612, el principito cuidaba de su diminuto planeta cada mañana.",
                            startSecond = 0,
                            durationSeconds = 6,
                            keyVocab = listOf(
                                KeyVocabItem("take care of", "/teɪk keər ɒv/", "cuidar de", "phrasal verb", "I take care of my dog."),
                                KeyVocabItem("tiny", "/ˈtaɪ.ni/", "diminuto / muy pequeño", "adj", "The ant is tiny.")
                            )
                        ),
                        AudiobookSentence(
                            id = "lp_1_2",
                            textEn = "One sunrise, a wonderful flower bloomed with four sharp thorns and crimson petals.",
                            textEs = "Un amanecer, una flor maravillosa floreció con cuatro espinas afiladas y pétalos carmesí.",
                            startSecond = 6,
                            durationSeconds = 7,
                            keyVocab = listOf(
                                KeyVocabItem("bloom", "/bluːm/", "florecer", "verb", "Flowers bloom in springtime."),
                                KeyVocabItem("thorn", "/θɔːn/", "espina", "noun", "Roses have sharp thorns.")
                            )
                        ),
                        AudiobookSentence(
                            id = "lp_1_3",
                            textEn = "'It is only with the heart that one can see rightly,' whispered the wise fox.",
                            textEs = "'Solo con el corazón se puede ver bien', susurró el sabio zorro.",
                            startSecond = 13,
                            durationSeconds = 6,
                            keyVocab = listOf(
                                KeyVocabItem("rightly", "/ˈraɪt.li/", "correctamente / con acierto", "adv", "He judged the situation rightly."),
                                KeyVocabItem("whisper", "/ˈwɪs.pər/", "susurrar", "verb", "She whispered a secret in my ear.")
                            )
                        ),
                        AudiobookSentence(
                            id = "lp_1_4",
                            textEn = "'What is essential is invisible to the eye,' repeated the little prince.",
                            textEs = "'Lo esencial es invisible a los ojos', repitió el principito.",
                            startSecond = 19,
                            durationSeconds = 6,
                            keyVocab = listOf(
                                KeyVocabItem("essential", "/ɪˈsen.ʃəl/", "esencial / fundamental", "adj", "Water is essential for life."),
                                KeyVocabItem("invisible", "/ɪnˈvɪz.ə.bəl/", "invisible", "adj", "Air is invisible.")
                            )
                        )
                    )
                )
            )
        )
    )
}
