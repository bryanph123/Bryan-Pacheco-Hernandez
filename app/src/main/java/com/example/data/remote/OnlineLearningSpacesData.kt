package com.example.data.remote

data class OnlineLearningSpace(
    val id: String,
    val title: String,
    val organization: String,
    val category: String,
    val targetLevels: List<String>,
    val description: String,
    val webUrl: String,
    val badge: String,
    val keyFeatures: List<String>,
    val quickExercisePrompt: String? = null
)

object OnlineLearningSpacesData {
    val spaces = listOf(
        OnlineLearningSpace(
            id = "space_cambridge_official",
            title = "Cambridge English Exam Preparation Portal",
            organization = "Cambridge University Press & Assessment",
            category = "Simulación & Examen Oficial",
            targetLevels = listOf("A2", "B1", "B2"),
            description = "Portal oficial de recursos y pruebas de muestra gratuitas para KET (A2), PET (B1) y B2 First (FCE) con claves de respuesta y grabaciones de audio de examen.",
            webUrl = "https://www.cambridgeenglish.org/learning-english/exam-preparation/",
            badge = "OFICIAL CAMBRIDGE",
            keyFeatures = listOf(
                "Muestras descargables de exámenes en papel y computadora",
                "Guías oficiales para candidatos y listas de vocabulario",
                "Criterios de evaluación de Speaking y Writing"
            ),
            quickExercisePrompt = "¿Sabías que el examen B2 First evalúa Reading & Use of English en 1h 15m con 7 partes? Practica las partes 1 a 4 con las oraciones de la guía en esta app."
        ),
        OnlineLearningSpace(
            id = "space_cambridge_write_improve",
            title = "Cambridge Write & Improve",
            organization = "Cambridge English & University of Cambridge",
            category = "Writing & Corrección por IA",
            targetLevels = listOf("A1", "A2", "B1", "B2"),
            description = "Herramienta interactiva gratuita de Cambridge que evalúa tus redacciones en segundos, asigna un nivel CEFR y te ofrece sugerencias de mejora inmediata.",
            webUrl = "https://writeandimprove.com/",
            badge = "WRITING EN TIEMPO REAL",
            keyFeatures = listOf(
                "Corrección automática en menos de 10 segundos",
                "Temas clasificados desde principiante hasta B2/C1",
                "Seguimiento de progreso y versiones sucesivas"
            ),
            quickExercisePrompt = "Redacta un párrafo de 50 palabras respondiendo a: 'How technology has changed daily communication' y pon a prueba tu puntuación B2."
        ),
        OnlineLearningSpace(
            id = "space_bbc_6minute",
            title = "BBC Learning English: 6 Minute English",
            organization = "BBC World Service",
            category = "Listening & Vocabulario",
            targetLevels = listOf("B1", "B2"),
            description = "Episodios semanales de audio de 6 minutos sobre temas actuales y curiosidades del mundo con vocabulario intermedio y avanzado explicado de forma amena.",
            webUrl = "https://www.bbc.co.uk/learningenglish/english/features/6-minute-english",
            badge = "AUDIO & PODCAST",
            keyFeatures = listOf(
                "Transcripción completa en texto de cada programa",
                "Lista de 6 palabras clave contextualizadas con definiciones",
                "Pregunta de trivia interactiva al inicio y final"
            ),
            quickExercisePrompt = "Escucha atentamente conectores de contraste como 'on the flip side', 'whereas' y 'nonetheless'."
        ),
        OnlineLearningSpace(
            id = "space_british_council",
            title = "British Council: Skills Practice Hub",
            organization = "British Council",
            category = "Comprensión Integral",
            targetLevels = listOf("A1", "A2", "B1", "B2"),
            description = "Espacio interactivo con textos de lectura, audios con acentos internacionales y ejercicios interactivos auto-corregibles divididos por niveles del marco europeo.",
            webUrl = "https://learnenglish.britishcouncil.org/skills",
            badge = "INTERACTIVO",
            keyFeatures = listOf(
                "Lecturas con actividades de opción múltiple y verdadero/falso",
                "Audios con ejercicios de ordenación de oraciones",
                "Secciones específicas para corrección gramatical"
            ),
            quickExercisePrompt = "Practica la identificación del propósito del autor (informar, persuadir o entretener)."
        ),
        OnlineLearningSpace(
            id = "space_cambridge_dict",
            title = "Cambridge Dictionary & Essential Grammar Lab",
            organization = "Cambridge University Press",
            category = "Diccionario & Colocaciones",
            targetLevels = listOf("A1", "A2", "B1", "B2"),
            description = "El estándar de referencia para definiciones claras en inglés británico y americano, etiquetas de nivel CEFR por acepción y pronunciación nativa.",
            webUrl = "https://dictionary.cambridge.org/",
            badge = "DICCIONARIO NATIVO",
            keyFeatures = listOf(
                "Etiqueta CEFR exacta (A1, A2, B1, B2) para cada significado",
                "Pronunciación en audio UK y US con transcripción fonética IPA",
                "Colocaciones y preposiciones habituales de cada verbo"
            ),
            quickExercisePrompt = "Busca colocaciones con los verbos 'make' vs 'do' (ej. make an effort, do homework)."
        ),
        OnlineLearningSpace(
            id = "space_oxford_learners",
            title = "Oxford Learner's 3000 & 5000 Word Hub",
            organization = "Oxford University Press",
            category = "Vocabulario de Frecuencia",
            targetLevels = listOf("A1", "A2", "B1", "B2"),
            description = "Las listas de palabras de mayor frecuencia de uso comprobado en exámenes internacionales y comunicación profesional en lengua inglesa.",
            webUrl = "https://www.oxfordlearnersdictionaries.com/wordlists/oxford3000-5000",
            badge = "VOCABULARIO FRECUENCIA",
            keyFeatures = listOf(
                "Palabras clasificadas según el marco común europeo",
                "Filtro por nivel CEFR desde A1 hasta B2",
                "Ejemplos reales de uso de corpus lingüístico"
            ),
            quickExercisePrompt = "Asegúrate de dominar los 3,000 términos núcleo antes de pasar a expresiones idiomáticas complejas."
        )
    )
}
