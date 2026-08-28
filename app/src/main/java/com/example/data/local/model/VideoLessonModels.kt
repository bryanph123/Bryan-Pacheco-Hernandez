package com.example.data.local.model

import com.example.R

data class VideoTimelineMarker(
    val timeSeconds: Int,
    val titleEn: String,
    val titleEs: String,
    val transcriptEn: String,
    val transcriptEs: String,
    val grammarRule: String,
    val keyExpression: String
)

data class VideoLessonItem(
    val id: String,
    val titleEn: String,
    val titleEs: String,
    val instructor: String,
    val level: String, // A1, A2, B1, B2
    val durationMinutes: Int,
    val bannerResId: Int,
    val category: String, // Speaking, Grammar, Listening, Pronunciation
    val descriptionEn: String,
    val descriptionEs: String,
    val keyTakeaways: List<String>,
    val timeline: List<VideoTimelineMarker>
)

object VideoLessonDataBank {
    val sampleVideoLessons: List<VideoLessonItem> = listOf(
        VideoLessonItem(
            id = "cambridge_speaking_masterclass",
            titleEn = "Cambridge Speaking Test: Complete Strategy",
            titleEs = "Examen de Speaking Cambridge: Estrategia Completa",
            instructor = "Prof. David Harrison & Cambridge Examiners",
            level = "A2-B2",
            durationMinutes = 12,
            bannerResId = R.drawable.img_speaking_video_1787935329772,
            category = "Speaking",
            descriptionEn = "Learn exactly what Cambridge examiners look for in Starters, Movers, Flyers, B1 Preliminary and B2 First speaking tests.",
            descriptionEs = "Aprende exactamente qué buscan los examinadores de Cambridge en las pruebas orales con ejemplos prácticos y frases clave.",
            keyTakeaways = listOf(
                "Evita respuestas de una sola palabra (utiliza la técnica PREP: Point, Reason, Example, Point).",
                "Conectores esenciales para ganar fluidez: 'In addition to that', 'On the other hand', 'What I mean is...'",
                "Manejo de errores: Si te equivocas, autocorrígete con 'Sorry, I mean...' sin perder puntos de fluidez."
            ),
            timeline = listOf(
                VideoTimelineMarker(
                    timeSeconds = 0,
                    titleEn = "Part 1: Personal Introductions",
                    titleEs = "Parte 1: Presentaciones Personales",
                    transcriptEn = "Hello! Welcome to the Cambridge Speaking exam simulation. In Part 1, the examiner asks personal questions about your hometown, family, and hobbies.",
                    transcriptEs = "¡Hola! Bienvenidos a la simulación del examen de Speaking de Cambridge. En la Parte 1, el examinador formula preguntas personales sobre tu ciudad, familia y pasatiempos.",
                    grammarRule = "Usa el Present Simple para hábitos y el Present Continuous para actividades temporales.",
                    keyExpression = "I've been living in... for three years / I'm currently studying..."
                ),
                VideoTimelineMarker(
                    timeSeconds = 45,
                    titleEn = "Part 2: Photo Description & Comparison",
                    titleEs = "Parte 2: Descripción y Comparación de Fotografías",
                    transcriptEn = "When describing photographs, avoid just listing objects. Start with the overall scene, describe what people are doing, and speculate about their feelings.",
                    transcriptEs = "Al describir fotografías, evita solo enumerar objetos. Comienza con la escena general, describe qué hacen las personas y especula sobre cómo se sienten.",
                    grammarRule = "Usa verbos modales de deducción: 'They might be...', 'It looks as if they are...'",
                    keyExpression = "In the foreground, I can see... / They appear to be enjoying..."
                ),
                VideoTimelineMarker(
                    timeSeconds = 90,
                    titleEn = "Part 3: Collaborative Discussion",
                    titleEs = "Parte 3: Discusión Colaborativa con tu Compañero",
                    transcriptEn = "In Part 3, you interact with your partner. Ask for their opinion, agree or politely disagree, and reach a consensus together.",
                    transcriptEs = "En la Parte 3, interactúas con tu compañero. Pide su opinión, muestra acuerdo o desacuerdo cortés, y lleguen a un consenso juntos.",
                    grammarRule = "Usa preguntas de sondeo: 'What's your take on this?', 'Shall we consider...?'",
                    keyExpression = "I see your point, however... / That's a valid argument, but..."
                ),
                VideoTimelineMarker(
                    timeSeconds = 140,
                    titleEn = "Part 4: Extended Discussion & Conclusion",
                    titleEs = "Parte 4: Preguntas Profundas y Conclusión",
                    transcriptEn = "Finally, in Part 4, provide longer answers with clear justifications and real-life examples from your experience.",
                    transcriptEs = "Finalmente, en la Parte 4, brinda respuestas más elaboradas con justificaciones claras y ejemplos de tu vida diaria.",
                    grammarRule = "Estructura tus ideas con conectores lógicos: 'Firstly', 'Furthermore', 'Consequently'.",
                    keyExpression = "From my personal perspective... / Taking everything into account..."
                )
            )
        ),
        VideoLessonItem(
            id = "connected_speech_pronunciation",
            titleEn = "Connected Speech & Native Pronunciation",
            titleEs = "Habla Conectada y Pronunciación Nativa",
            instructor = "Dr. Emma Watson (Phonetics Specialist)",
            level = "A1-B2",
            durationMinutes = 10,
            bannerResId = R.drawable.img_video_lessons_hero_1787935273584,
            category = "Pronunciación",
            descriptionEn = "Master linking sounds, intrusive /r/ and /w/, weak forms, and the magic Schwa /ə/ sound.",
            descriptionEs = "Domina el enlace de sonidos, sonidos intrusivos, formas débiles y el sonido Schwa /ə/ para sonar natural.",
            keyTakeaways = listOf(
                "El sonido Schwa /ə/ es el más común del inglés; nunca se acentúa (ej. 'about' /əˈbaʊt/, 'banana' /bəˈnɑː.nə/).",
                "Consonante + Vocal se fusionan: 'pick it up' suena como /pɪ.kɪ.tʌp/.",
                "Sonidos intrusivos: 'go on' añade un suave /w/ -> /ɡəʊ wɒn/."
            ),
            timeline = listOf(
                VideoTimelineMarker(
                    timeSeconds = 0,
                    titleEn = "1. The Power of the Schwa Sound /ə/",
                    titleEs = "1. El Poder del Sonido Schwa /ə/",
                    transcriptEn = "In English, unstressed vowels reduce to the relaxed Schwa sound /ə/. Notice how 'photograph' differs from 'photography'.",
                    transcriptEs = "En inglés, las vocales no acentuadas se reducen al sonido Schwa /ə/. Nota cómo 'photograph' cambia de acento en 'photography'.",
                    grammarRule = "El inglés es un idioma 'stress-timed' (ritmo por acentos, no por sílabas).",
                    keyExpression = "doctor /ˈdɒk.tər/, banana /bəˈnɑː.nə/, police /pəˈliːs/"
                ),
                VideoTimelineMarker(
                    timeSeconds = 50,
                    titleEn = "2. Linking Consonant to Vowel",
                    titleEs = "2. Enlace Consonante a Vocal",
                    transcriptEn = "When a word ends in a consonant and the next begins with a vowel, push the consonant to the next syllable: 'an apple' sounds like 'a-napple'.",
                    transcriptEs = "Cuando una palabra termina en consonante y la siguiente empieza en vocal, la consonante salta a la siguiente sílaba: 'an apple' suena como 'a-napple'.",
                    grammarRule = "Linking C+V crea fluidez continua en el discurso oral.",
                    keyExpression = "Hold on -> Hol-don / Turn off -> Tur-noff"
                ),
                VideoTimelineMarker(
                    timeSeconds = 100,
                    titleEn = "3. Weak Forms in Natural Sentences",
                    titleEs = "3. Formas Débiles en Oraciones Cotidianas",
                    transcriptEn = "Grammar words like 'to', 'for', 'at', 'and' are rarely stressed. 'Fish and chips' becomes 'fish-n-chips' /fɪʃ ən tʃɪps/.",
                    transcriptEs = "Palabras gramaticales como 'to', 'for', 'at', 'and' rara vez llevan acento. 'Fish and chips' se pronuncia 'fish-n-chips' /fɪʃ ən tʃɪps/.",
                    grammarRule = "Las palabras de contenido (sustantivos, verbos) llevan el acento; las palabras funcionales van en forma débil.",
                    keyExpression = "I want to go -> /aɪ wɒnt tə ɡəʊ/"
                )
            )
        )
    )
}
