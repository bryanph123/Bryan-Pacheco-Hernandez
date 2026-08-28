package com.example.data.local

import com.example.data.local.entities.SavedVocabItemEntity
import com.example.data.remote.CambridgeWordlistsData
import com.example.data.remote.CambridgeYleLevel
import org.json.JSONArray

data class VocabWordItem(
    val id: String,
    val word: String,
    val translation: String,
    val phonetic: String,
    val level: String, // Starters, Movers, Flyers, A1, A2, B1, B2, Mi Vocabulario
    val topic: String, // Categoría temática
    val definition: String = "",
    val exampleEn: String = "",
    val exampleEs: String = "",
    val isFromUserSrs: Boolean = false,
    val srsMastery: Int = 0, // 0 to 5
    val srsIntervalDays: Int = 1,
    val isSrsDue: Boolean = false,
    val originalSavedItem: SavedVocabItemEntity? = null
)

object VocabularyBankData {

    val topicsList = listOf(
        "Todos los Temas",
        "Animals (Animales)",
        "The body and the face (Cuerpo y Cara)",
        "Clothes (Ropa y Accesorios)",
        "Colours (Colores)",
        "Family & friends (Familia y Amigos)",
        "Food & drink (Comida y Bebida)",
        "Health (Salud)",
        "The home (El Hogar)",
        "Materials (Materiales)",
        "Places & directions (Lugares y Direcciones)",
        "School (Escuela y Educación)",
        "Sports & leisure (Deportes y Ocio)",
        "Time (Tiempo y Fechas)",
        "Toys (Juguetes)",
        "Transport (Transporte)",
        "Weather (Clima)",
        "Work (Trabajo y Profesiones)",
        "The world around us (El Mundo que nos Rodea)",
        "Trabajo y Carrera",
        "Viajes y Transporte",
        "Ciencia y Medio Ambiente",
        "Salud y Bienestar",
        "Economía y Finanzas",
        "Expresiones y Modismos",
        "Vida Cotidiana y Rutinas",
        "Comida y Restaurantes",
        "Tecnología y Medios",
        "Educación y Estudio",
        "Relaciones y Sociedad",
        "Hogar y Ciudad"
    )

    val levelsList = listOf(
        "TODOS" to "Todos los Niveles",
        "Starters" to "Pre A1 Starters",
        "Movers" to "A1 Movers",
        "Flyers" to "A2 Flyers",
        "A1" to "A1 Principiante",
        "A2" to "A2 Básico",
        "B1" to "B1 Intermedio",
        "B2" to "B2 Avanzado",
        "SAVED" to "⭐ Mis Guardados SRS"
    )

    private val staticVocabularyList: List<VocabWordItem> = listOf(
        // ================= NIVEL A1 =================
        VocabWordItem(
            id = "v_a1_01",
            word = "greet",
            translation = "saludar",
            phonetic = "/ɡriːt/",
            level = "A1",
            topic = "Vida Cotidiana y Rutinas",
            definition = "To say hello or welcome someone when you meet them.",
            exampleEn = "She greeted her teacher with a warm smile.",
            exampleEs = "Saludó a su maestra con una cálida sonrisa."
        ),
        VocabWordItem(
            id = "v_a1_02",
            word = "schedule",
            translation = "horario / programar",
            phonetic = "/ˈskedʒ.uːl/",
            level = "A1",
            topic = "Vida Cotidiana y Rutinas",
            definition = "A plan that lists the times when events or activities will happen.",
            exampleEn = "What is your class schedule for tomorrow?",
            exampleEs = "¿Cuál es tu horario de clases para mañana?"
        ),
        VocabWordItem(
            id = "v_a1_03",
            word = "breakfast",
            translation = "desayuno",
            phonetic = "/ˈbrek.fəst/",
            level = "A1",
            topic = "Comida y Restaurantes",
            definition = "The first meal of the day, usually eaten in the morning.",
            exampleEn = "I always eat eggs and toast for breakfast.",
            exampleEs = "Siempre como huevos y pan tostado para el desayuno."
        ),
        VocabWordItem(
            id = "v_a1_04",
            word = "classroom",
            translation = "salón de clases / aula",
            phonetic = "/ˈklɑːs.ruːm/",
            level = "A1",
            topic = "Educación y Estudio",
            definition = "A room in a school or college where students are taught.",
            exampleEn = "The students are sitting quietly in the classroom.",
            exampleEs = "Los alumnos están sentados en silencio en el salón de clases."
        ),
        VocabWordItem(
            id = "v_a1_05",
            word = "sibling",
            translation = "hermano / hermana",
            phonetic = "/ˈsɪb.lɪŋ/",
            level = "A1",
            topic = "Relaciones y Sociedad",
            definition = "A brother or sister.",
            exampleEn = "Do you have any siblings?",
            exampleEs = "¿Tienes algún hermano o hermana?"
        ),
        VocabWordItem(
            id = "v_a1_06",
            word = "kitchen",
            translation = "cocina",
            phonetic = "/ˈkɪtʃ.ən/",
            level = "A1",
            topic = "Hogar y Ciudad",
            definition = "A room where food is kept, prepared, and cooked.",
            exampleEn = "My mother is preparing dinner in the kitchen.",
            exampleEs = "Mi madre está preparando la cena en la cocina."
        ),
        VocabWordItem(
            id = "v_a1_07",
            word = "weather",
            translation = "clima / tiempo atmosférico",
            phonetic = "/ˈweð.ər/",
            level = "A1",
            topic = "Ciencia y Medio Ambiente",
            definition = "The conditions in the air above the earth such as wind, rain, or temperature.",
            exampleEn = "The weather is very sunny and warm today.",
            exampleEs = "El clima está muy soleado y cálido hoy."
        ),
        VocabWordItem(
            id = "v_a1_08",
            word = "wallet",
            translation = "billetera / cartera",
            phonetic = "/ˈwɒl.ɪt/",
            level = "A1",
            topic = "Economía y Finanzas",
            definition = "A small, flat folding case made of leather or plastic used for keeping paper money and cards.",
            exampleEn = "I left my wallet on the dining table.",
            exampleEs = "Dejé mi billetera en la mesa del comedor."
        ),

        // ================= NIVEL A2 =================
        VocabWordItem(
            id = "v_a2_01",
            word = "luggage",
            translation = "equipaje",
            phonetic = "/ˈlʌɡ.ɪdʒ/",
            level = "A2",
            topic = "Viajes y Transporte",
            definition = "Bags, cases, and suitcases containing personal belongings used when traveling.",
            exampleEn = "You can leave your heavy luggage at the hotel reception.",
            exampleEs = "Puedes dejar tu equipaje pesado en la recepción del hotel."
        ),
        VocabWordItem(
            id = "v_a2_02",
            word = "flight",
            translation = "vuelo",
            phonetic = "/flaɪt/",
            level = "A2",
            topic = "Viajes y Transporte",
            definition = "A journey on an aircraft or plane.",
            exampleEn = "Our flight to London was delayed by two hours.",
            exampleEs = "Nuestro vuelo a Londres se retrasó por dos horas."
        ),
        VocabWordItem(
            id = "v_a2_03",
            word = "colleague",
            translation = "colega / compañero de trabajo",
            phonetic = "/ˈkɒl.iːɡ/",
            level = "A2",
            topic = "Trabajo y Carrera",
            definition = "A person that you work with in a profession or business.",
            exampleEn = "I shared the project documents with my colleague.",
            exampleEs = "Compartí los documentos del proyecto con mi colega."
        ),
        VocabWordItem(
            id = "v_a2_04",
            word = "receipt",
            translation = "recibo / comprobante de compra",
            phonetic = "/rɪˈsiːt/",
            level = "A2",
            topic = "Economía y Finanzas",
            definition = "A piece of paper showing that you have paid for goods or services.",
            exampleEn = "Keep the receipt if you want to exchange the shirt.",
            exampleEs = "Guarda el recibo si quieres cambiar la camisa."
        ),
        VocabWordItem(
            id = "v_a2_05",
            word = "delicious",
            translation = "delicioso / sabroso",
            phonetic = "/dɪˈlɪʃ.əs/",
            level = "A2",
            topic = "Comida y Restaurantes",
            definition = "Having a very pleasant taste or smell.",
            exampleEn = "This homemade apple pie is absolutely delicious.",
            exampleEs = "Este pay de manzana casero está absolutamente delicioso."
        ),
        VocabWordItem(
            id = "v_a2_06",
            word = "headache",
            translation = "dolor de cabeza",
            phonetic = "/ˈhed.eɪk/",
            level = "A2",
            topic = "Salud y Bienestar",
            definition = "A continuous pain in the head.",
            exampleEn = "I have a terrible headache from looking at the screen all day.",
            exampleEs = "Tengo un terrible dolor de cabeza de mirar la pantalla todo el día."
        ),
        VocabWordItem(
            id = "v_a2_07",
            word = "forecast",
            translation = "pronóstico del tiempo",
            phonetic = "/ˈfɔː.kɑːst/",
            level = "A2",
            topic = "Ciencia y Medio Ambiente",
            definition = "A statement of what is likely to happen in the future, especially regarding weather.",
            exampleEn = "The weather forecast predicts heavy rain this afternoon.",
            exampleEs = "El pronóstico del clima predice lluvia intensa esta tarde."
        ),
        VocabWordItem(
            id = "v_a2_08",
            word = "borrow",
            translation = "pedir prestado",
            phonetic = "/ˈbɒr.əʊ/",
            level = "A2",
            topic = "Educación y Estudio",
            definition = "To get or receive something from someone with the intention of giving it back.",
            exampleEn = "Can I borrow your grammar book until Monday?",
            exampleEs = "¿Puedo pedir prestado tu libro de gramática hasta el lunes?"
        ),

        // ================= NIVEL B1 =================
        VocabWordItem(
            id = "v_b1_01",
            word = "afford",
            translation = "permitirse pagar / costear",
            phonetic = "/əˈfɔːd/",
            level = "B1",
            topic = "Economía y Finanzas",
            definition = "To have enough money or time to buy or do something.",
            exampleEn = "We cannot afford to buy a new car this year.",
            exampleEs = "No podemos permitirnos comprar un auto nuevo este año."
        ),
        VocabWordItem(
            id = "v_b1_02",
            word = "deadline",
            translation = "fecha límite / plazo de entrega",
            phonetic = "/ˈded.laɪn/",
            level = "B1",
            topic = "Trabajo y Carrera",
            definition = "A time or day by which something must be done.",
            exampleEn = "We worked overtime to meet the project deadline.",
            exampleEs = "Trabajamos horas extras para cumplir la fecha límite del proyecto."
        ),
        VocabWordItem(
            id = "v_b1_03",
            word = "commute",
            translation = "trasladarse al trabajo / trayecto diario",
            phonetic = "/kəˈmjuːt/",
            level = "B1",
            topic = "Viajes y Transporte",
            definition = "To make the same journey regularly between work and home.",
            exampleEn = "He commutes forty minutes by train every morning.",
            exampleEs = "Él viaja cuarenta minutos en tren todas las mañanas."
        ),
        VocabWordItem(
            id = "v_b1_04",
            word = "appraisal",
            translation = "evaluación de desempeño",
            phonetic = "/əˈpreɪ.zəl/",
            level = "B1",
            topic = "Trabajo y Carrera",
            definition = "An examination of the value, condition, or quality of work done by an employee.",
            exampleEn = "My manager gave me very positive feedback during my annual appraisal.",
            exampleEs = "Mi gerente me dio comentarios muy positivos durante mi evaluación anual."
        ),
        VocabWordItem(
            id = "v_b1_05",
            word = "treatment",
            translation = "tratamiento médico",
            phonetic = "/ˈtriːt.mənt/",
            level = "B1",
            topic = "Salud y Bienestar",
            definition = "The use of drugs, exercises, or therapies to cure a person of an illness or injury.",
            exampleEn = "The doctor prescribed a new treatment for her back pain.",
            exampleEs = "El médico le recetó un nuevo tratamiento para su dolor de espalda."
        ),
        VocabWordItem(
            id = "v_b1_06",
            word = "renewable",
            translation = "renovable (energía)",
            phonetic = "/rɪˈnjuː.ə.bəl/",
            level = "B1",
            topic = "Ciencia y Medio Ambiente",
            definition = "Energy forms that can be produced as quickly as they are used, like solar or wind power.",
            exampleEn = "Investing in renewable energy helps combat climate change.",
            exampleEs = "Invertir en energía renovable ayuda a combatir el cambio climático."
        ),
        VocabWordItem(
            id = "v_b1_07",
            word = "break the ice",
            translation = "romper el hielo / distender el ambiente",
            phonetic = "/breɪk ðə aɪs/",
            level = "B1",
            topic = "Expresiones y Modismos",
            definition = "To say or do something that makes people feel more relaxed at a meeting or party.",
            exampleEn = "He told a funny joke to break the ice with the new clients.",
            exampleEs = "Contó un chiste gracioso para romper el hielo con los nuevos clientes."
        ),
        VocabWordItem(
            id = "v_b1_08",
            word = "bandwidth",
            translation = "ancho de banda / capacidad de atención",
            phonetic = "/ˈbænd.wɪtθ/",
            level = "B1",
            topic = "Tecnología y Medios",
            definition = "The amount of information or time/energy available to handle tasks.",
            exampleEn = "I don't have the mental bandwidth to take on extra assignments right now.",
            exampleEs = "No tengo la capacidad mental para asumir asignaciones extras ahora mismo."
        ),

        // ================= NIVEL B2 =================
        VocabWordItem(
            id = "v_b2_01",
            word = "troubleshoot",
            translation = "diagnosticar y resolver fallas",
            phonetic = "/ˈtrʌb.əl.ʃuːt/",
            level = "B2",
            topic = "Tecnología y Medios",
            definition = "To discover why a system or machine does not work effectively and fix the problem.",
            exampleEn = "Our engineers spent hours troubleshooting the server outage.",
            exampleEs = "Nuestros ingenieros pasaron horas diagnosticando y resolviendo la caída del servidor."
        ),
        VocabWordItem(
            id = "v_b2_02",
            word = "biodiversity",
            translation = "biodiversidad",
            phonetic = "/ˌbaɪ.əʊ.daɪˈvɜː.sə.ti/",
            level = "B2",
            topic = "Ciencia y Medio Ambiente",
            definition = "The number and variety of plants and animals that exist in a particular area.",
            exampleEn = "Deforestation poses a catastrophic threat to rainforest biodiversity.",
            exampleEs = "La deforestación representa una amenaza catastrófica para la biodiversidad de la selva."
        ),
        VocabWordItem(
            id = "v_b2_03",
            word = "carbon footprint",
            translation = "huella de carbono",
            phonetic = "/ˌkɑː.bən ˈfʊt.prɪnt/",
            level = "B2",
            topic = "Ciencia y Medio Ambiente",
            definition = "A measure of the amount of carbon dioxide produced by the activities of a person or company.",
            exampleEn = "Using public transport significantly reduces your individual carbon footprint.",
            exampleEs = "Usar transporte público reduce significativamente tu huella de carbono individual."
        ),
        VocabWordItem(
            id = "v_b2_04",
            word = "breakthrough",
            translation = "avance decisivo / gran hallazgo",
            phonetic = "/ˈbreɪk.θruː/",
            level = "B2",
            topic = "Ciencia y Medio Ambiente",
            definition = "An important discovery or event that helps to improve a situation or provide an answer.",
            exampleEn = "Scientists announced a major breakthrough in quantum computing.",
            exampleEs = "Los científicos anunciaron un avance decisivo en computación cuántica."
        ),
        VocabWordItem(
            id = "v_b2_05",
            word = "sustainable",
            translation = "sostenible / sustentable",
            phonetic = "/səˈsteɪ.nə.bəl/",
            level = "B2",
            topic = "Ciencia y Medio Ambiente",
            definition = "Able to continue over a period of time without causing damage to the environment.",
            exampleEn = "The city introduced sustainable architecture and solar energy policies.",
            exampleEs = "La ciudad introdujo políticas de arquitectura sostenible y energía solar."
        ),
        VocabWordItem(
            id = "v_b2_06",
            word = "redundancy",
            translation = "despido por reestructuración / recorte",
            phonetic = "/rɪˈdʌn.dən.si/",
            level = "B2",
            topic = "Trabajo y Carrera",
            definition = "A situation in which someone loses their job because their employer no longer needs them.",
            exampleEn = "The tech merger resulted in over two hundred redundancies.",
            exampleEs = "La fusión tecnológica resultó en más de doscientos despidos por reestructuración."
        ),
        VocabWordItem(
            id = "v_b2_07",
            word = "hit the ground running",
            translation = "empezar con gran ritmo y éxito inmediato",
            phonetic = "/hɪt ðə ɡraʊnd ˈrʌn.ɪŋ/",
            level = "B2",
            topic = "Expresiones y Modismos",
            definition = "To immediately work hard and successfully at a new activity.",
            exampleEn = "The new marketing director hit the ground running with three campaigns.",
            exampleEs = "La nueva directora de marketing empezó con todo el ritmo con tres campañas."
        ),
        VocabWordItem(
            id = "v_b2_08",
            word = "scaffolding",
            translation = "andamiaje pedagógico / estructura de apoyo",
            phonetic = "/ˈskæf.əl.dɪŋ/",
            level = "B2",
            topic = "Educación y Estudio",
            definition = "Temporary support given to students during learning until they can do it independently.",
            exampleEn = "The professor provided structured scaffolding before the final dissertation.",
            exampleEs = "El profesor proporcionó un andamiaje estructurado antes de la disertación final."
        ),
        VocabWordItem(
            id = "v_b2_09",
            word = "inflation",
            translation = "inflación económica",
            phonetic = "/ɪnˈfleɪ.ʃən/",
            level = "B2",
            topic = "Economía y Finanzas",
            definition = "A general, continuous increase in prices and a fall in the purchasing power of money.",
            exampleEn = "Central banks raised interest rates to curtail soaring inflation.",
            exampleEs = "Los bancos centrales subieron las tasas de interés para frenar la creciente inflación."
        ),
        VocabWordItem(
            id = "v_b2_10",
            word = "wellbeing",
            translation = "bienestar integral",
            phonetic = "/ˌwelˈbiː.ɪŋ/",
            level = "B2",
            topic = "Salud y Bienestar",
            definition = "The state of feeling healthy, happy, and comfortable in body and mind.",
            exampleEn = "Regular exercise and restful sleep are vital for psychological wellbeing.",
            exampleEs = "El ejercicio regular y el sueño reparador son vitales para el bienestar psicológico."
        ),
        VocabWordItem(
            id = "v_b2_11",
            word = "cost an arm and a leg",
            translation = "costar un ojo de la cara / ser carísimo",
            phonetic = "/kɒst ən ɑːm ænd ə leɡ/",
            level = "B2",
            topic = "Expresiones y Modismos",
            definition = "To be extremely expensive.",
            exampleEn = "Fixing the damaged transmission cost an arm and a leg.",
            exampleEs = "Reparar la transmisión dañada costó un ojo de la cara."
        ),
        VocabWordItem(
            id = "v_b2_12",
            word = "outweigh",
            translation = "pesar más que / superar en importancia",
            phonetic = "/ˌaʊtˈweɪ/",
            level = "B2",
            topic = "Relaciones y Sociedad",
            definition = "To be greater or more important than something else.",
            exampleEn = "The environmental benefits of renewable energy far outweigh the initial costs.",
            exampleEs = "Los beneficios ambientales de la energía renovable superan por mucho los costos iniciales."
        ),
        VocabWordItem(
            id = "v_b2_13",
            word = "reluctant",
            translation = "reacio / renuente",
            phonetic = "/rɪˈlʌk.tənt/",
            level = "B2",
            topic = "Relaciones y Sociedad",
            definition = "Not wanting to do something and therefore slow to do it.",
            exampleEn = "Many employees were initially reluctant to adopt the new software.",
            exampleEs = "Muchos empleados estaban renuentes al principio a adoptar el nuevo software."
        ),
        VocabWordItem(
            id = "v_b2_14",
            word = "cope with",
            translation = "afrontar / lidiar eficazmente con",
            phonetic = "/kəʊp wɪð/",
            level = "B2",
            topic = "Salud y Bienestar",
            definition = "To deal successfully with a difficult situation.",
            exampleEn = "Mindfulness techniques helped her cope with high workplace pressure.",
            exampleEs = "Las técnicas de atención plena le ayudaron a afrontar la alta presión laboral."
        ),
        VocabWordItem(
            id = "v_b2_15",
            word = "raise awareness",
            translation = "concientizar / crear conciencia pública",
            phonetic = "/reɪz əˈweə.nəs/",
            level = "B2",
            topic = "Relaciones y Sociedad",
            definition = "To make people understand and care about an important issue or problem.",
            exampleEn = "The student association launched a campaign to raise awareness about mental health.",
            exampleEs = "La asociación estudiantil lanzó una campaña para crear conciencia sobre la salud mental."
        )
    )

    /**
     * Combines predefined multi-level Cambridge & B2 vocabulary with the user's saved Room SRS items
     */
    fun getAllVocabulary(savedItems: List<SavedVocabItemEntity>): List<VocabWordItem> {
        val now = System.currentTimeMillis()
        val userItemsMapped = savedItems.map { entity ->
            var exEn = ""
            var exEs = ""
            try {
                val arr = JSONArray(entity.examplesJson)
                if (arr.length() > 0) {
                    val obj = arr.getJSONObject(0)
                    exEn = obj.optString("en", "")
                    exEs = obj.optString("es", "")
                }
            } catch (_: Exception) {}

            val inferredLevel = when {
                entity.sourceModule.contains("b2", ignoreCase = true) -> "B2"
                entity.sourceModule.contains("b1", ignoreCase = true) -> "B1"
                entity.sourceModule.contains("a2", ignoreCase = true) -> "A2"
                entity.sourceModule.contains("a1", ignoreCase = true) -> "A1"
                else -> "Mi Vocabulario"
            }

            val inferredTopic = when {
                entity.linkedTopicId?.contains("work", ignoreCase = true) == true || entity.sourceModule.contains("work", ignoreCase = true) -> "Trabajo y Carrera"
                entity.linkedTopicId?.contains("travel", ignoreCase = true) == true -> "Viajes y Transporte"
                entity.linkedTopicId?.contains("nature", ignoreCase = true) == true -> "Ciencia y Medio Ambiente"
                entity.linkedTopicId?.contains("health", ignoreCase = true) == true -> "Salud y Bienestar"
                entity.linkedTopicId?.contains("food", ignoreCase = true) == true -> "Comida y Restaurantes"
                else -> "⭐ Vocabulario SRS Personal"
            }

            VocabWordItem(
                id = entity.id,
                word = entity.sourceText,
                translation = entity.translation,
                phonetic = entity.phonetic,
                level = inferredLevel,
                topic = inferredTopic,
                definition = entity.definition,
                exampleEn = exEn,
                exampleEs = exEs,
                isFromUserSrs = true,
                srsMastery = entity.masteryLevel,
                srsIntervalDays = entity.intervalDays,
                isSrsDue = entity.nextReviewTimestamp <= now,
                originalSavedItem = entity
            )
        }

        // Map Cambridge Wordlists into VocabWordItems
        val cambridgeItems = CambridgeWordlistsData.fullWordlist.mapIndexed { idx, yleItem ->
            VocabWordItem(
                id = "yle_word_$idx",
                word = yleItem.english,
                translation = yleItem.spanish,
                phonetic = yleItem.phonetic,
                level = when (yleItem.level) {
                    CambridgeYleLevel.STARTERS -> "Starters"
                    CambridgeYleLevel.MOVERS -> "Movers"
                    CambridgeYleLevel.FLYERS -> "Flyers"
                },
                topic = yleItem.theme,
                definition = "Cambridge Official (${yleItem.partOfSpeech}): ${yleItem.spanish}",
                exampleEn = yleItem.exampleEnglish,
                exampleEs = yleItem.exampleSpanish
            )
        }

        // Return combined list, avoiding duplicates by matching word text
        val userWordSet = userItemsMapped.map { it.word.lowercase().trim() }.toSet()
        val combinedStatic = (staticVocabularyList + cambridgeItems).filter { it.word.lowercase().trim() !in userWordSet }
        return userItemsMapped + combinedStatic
    }

    /**
     * Real-time search and multi-criteria filter
     */
    fun filterVocabulary(
        items: List<VocabWordItem>,
        query: String,
        levelFilter: String,
        topicFilter: String,
        statusFilter: String
    ): List<VocabWordItem> {
        val cleanQuery = query.trim().lowercase()

        return items.filter { item ->
            // 1. Text Search matching Word, Translation, Topic, Level, Definition, or Example
            val matchesQuery = cleanQuery.isEmpty() ||
                item.word.lowercase().contains(cleanQuery) ||
                item.translation.lowercase().contains(cleanQuery) ||
                item.topic.lowercase().contains(cleanQuery) ||
                item.level.lowercase().contains(cleanQuery) ||
                item.definition.lowercase().contains(cleanQuery) ||
                item.exampleEn.lowercase().contains(cleanQuery) ||
                item.exampleEs.lowercase().contains(cleanQuery) ||
                item.phonetic.lowercase().contains(cleanQuery)

            // 2. Level Filter
            val matchesLevel = when (levelFilter) {
                "TODOS" -> true
                "SAVED" -> item.isFromUserSrs || item.level == "Mi Vocabulario"
                else -> item.level.equals(levelFilter, ignoreCase = true)
            }

            // 3. Topic Filter
            val matchesTopic = topicFilter == "Todos los Temas" ||
                item.topic.equals(topicFilter, ignoreCase = true) ||
                item.topic.contains(topicFilter, ignoreCase = true)

            // 4. Status Filter
            val matchesStatus = when (statusFilter) {
                "TODOS" -> true
                "DUE" -> item.isSrsDue
                "LEARNING" -> item.srsMastery in 1..4
                "MASTERED" -> item.srsMastery >= 5
                else -> true
            }

            matchesQuery && matchesLevel && matchesTopic && matchesStatus
        }
    }
}
