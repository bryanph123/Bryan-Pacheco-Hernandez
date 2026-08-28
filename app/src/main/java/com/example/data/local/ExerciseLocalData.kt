package com.example.data.local

import com.example.data.local.entities.SavedVocabItemEntity
import com.example.data.local.entities.TopicEntity
import com.example.data.local.model.*
import com.example.data.remote.CambridgeWordlistsData
import com.example.data.srs.SrsAlgorithm
import org.json.JSONArray
import org.json.JSONObject

/**
 * Exercise Repository & Generator with Full Bilingual English/Spanish Support,
 * Pre A1 Starters, A1 Movers, A2 Flyers, and B1/B2 coverage.
 */
object ExerciseLocalData {

    /**
     * Visual illustrations catalog for image-matching questions
     */
    val visualIllustrationsCatalog: List<VisualIllustration> = listOf(
        // Animals
        VisualIllustration("ill_cat", VisualVocabCategory.ANIMALS, "Cat", "Gato", "/kæt/", "🐱", accentColorHex = 0xFFFF7043, visualDescription = "A playful small feline pet.", exampleSentence = "The cat is resting on the mat."),
        VisualIllustration("ill_dog", VisualVocabCategory.ANIMALS, "Dog", "Perro", "/dɒɡ/", "🐶", accentColorHex = 0xFF8D6E63, visualDescription = "Man's best friend, friendly pet.", exampleSentence = "The happy dog wags its tail."),
        VisualIllustration("ill_lion", VisualVocabCategory.ANIMALS, "Lion", "León", "/ˈlaɪ.ən/", "🦁", accentColorHex = 0xFFFFA000, visualDescription = "The king of the jungle with a golden mane.", exampleSentence = "The fierce lion roars loudly."),
        VisualIllustration("ill_elephant", VisualVocabCategory.ANIMALS, "Elephant", "Elefante", "/ˈel.ɪ.fənt/", "🐘", accentColorHex = 0xFF78909C, visualDescription = "A huge grey animal with large ears and a long trunk.", exampleSentence = "An elephant drinks water with its trunk."),
        VisualIllustration("ill_dolphin", VisualVocabCategory.ANIMALS, "Dolphin", "Delfín", "/ˈdɒl.fɪn/", "🐬", accentColorHex = 0xFF0288D1, visualDescription = "An intelligent marine mammal jumping in waves.", exampleSentence = "Dolphins swim gracefully in the sea."),
        VisualIllustration("ill_butterfly", VisualVocabCategory.ANIMALS, "Butterfly", "Mariposa", "/ˈbʌt.ə.flaɪ/", "🦋", accentColorHex = 0xFFAB47BC, visualDescription = "A colourful insect with delicate wings.", exampleSentence = "A bright butterfly landed on the blossom."),
        VisualIllustration("ill_rabbit", VisualVocabCategory.ANIMALS, "Rabbit", "Conejo", "/ˈræb.ɪt/", "🐰", accentColorHex = 0xFFEC407A, visualDescription = "A furry animal with long ears that hops.", exampleSentence = "The rabbit loves crunchy carrots."),
        VisualIllustration("ill_penguin", VisualVocabCategory.ANIMALS, "Penguin", "Pingüino", "/ˈpeŋ.ɡwɪn/", "🐧", accentColorHex = 0xFF263238, visualDescription = "A black and white flightless bird from Antarctica.", exampleSentence = "Penguins slide on the cold ice."),

        // Food & Drink
        VisualIllustration("ill_apple", VisualVocabCategory.FOOD_DRINKS, "Apple", "Manzana", "/ˈæp.əl/", "🍎", accentColorHex = 0xFFE53935, visualDescription = "A crunchy, juicy red fruit.", exampleSentence = "She bites into a sweet red apple."),
        VisualIllustration("ill_banana", VisualVocabCategory.FOOD_DRINKS, "Banana", "Plátano / Banana", "/bəˈnɑː.nə/", "🍌", accentColorHex = 0xFFFDD835, visualDescription = "A long, curved yellow tropical fruit.", exampleSentence = "Peel the banana before you eat it."),
        VisualIllustration("ill_pizza", VisualVocabCategory.FOOD_DRINKS, "Pizza", "Pizza", "/ˈpiːt.sə/", "🍕", accentColorHex = 0xFFFFB300, visualDescription = "Baked dough topped with tomato sauce and melted cheese.", exampleSentence = "We shared a delicious cheese pizza."),
        VisualIllustration("ill_sandwich", VisualVocabCategory.FOOD_DRINKS, "Sandwich", "Sándwich / Emparedado", "/ˈsæn.wɪdʒ/", "🥪", accentColorHex = 0xFF8D6E63, visualDescription = "Two slices of bread with filling inside.", exampleSentence = "He packed a healthy ham and cheese sandwich."),
        VisualIllustration("ill_ice_cream", VisualVocabCategory.FOOD_DRINKS, "Ice Cream", "Helado", "/ˌaɪs ˈkriːm/", "🍦", accentColorHex = 0xFFFF80AB, visualDescription = "A sweet, creamy frozen dessert on a cone.", exampleSentence = "Vanilla ice cream melts fast under the sun."),
        VisualIllustration("ill_strawberry", VisualVocabCategory.FOOD_DRINKS, "Strawberry", "Fresa / Frutilla", "/ˈstrɔː.bər.i/", "🍓", accentColorHex = 0xFFD81B60, visualDescription = "A sweet red berry with tiny seeds on its skin.", exampleSentence = "Strawberries taste amazing with yoghurt."),

        // Transport
        VisualIllustration("ill_car", VisualVocabCategory.TRANSPORT, "Car", "Coche / Carro / Auto", "/kɑːr/", "🚗", accentColorHex = 0xFFE53935, visualDescription = "A motor vehicle with four wheels for passengers.", exampleSentence = "My parents drive a red electric car."),
        VisualIllustration("ill_bus", VisualVocabCategory.TRANSPORT, "Bus", "Autobús / Camión", "/bʌs/", "🚌", accentColorHex = 0xFFFFB300, visualDescription = "A large vehicle carrying many public passengers.", exampleSentence = "The school bus arrives at 8:00 AM."),
        VisualIllustration("ill_airplane", VisualVocabCategory.TRANSPORT, "Airplane", "Avión", "/ˈeə.pleɪn/", "✈️", accentColorHex = 0xFF1E88E5, visualDescription = "A winged aircraft flying across the sky.", exampleSentence = "The airplane took off smoothly into the clouds."),
        VisualIllustration("ill_bicycle", VisualVocabCategory.TRANSPORT, "Bicycle", "Bicicleta", "/ˈbaɪ.sɪ.kəl/", "🚲", accentColorHex = 0xFF43A047, visualDescription = "A two-wheeled pedal-powered vehicle.", exampleSentence = "Riding a bicycle is great exercise."),
        VisualIllustration("ill_rocket", VisualVocabCategory.TRANSPORT, "Rocket", "Cohete espacial", "/ˈrɒk.ɪt/", "🚀", accentColorHex = 0xFF6A1B9A, visualDescription = "A powerful spacecraft traveling into outer space.", exampleSentence = "The rocket blasted off towards Mars."),

        // House & Clothes
        VisualIllustration("ill_house", VisualVocabCategory.HOUSE_OBJECTS, "House", "Casa", "/haʊs/", "🏡", accentColorHex = 0xFF2E7D32, visualDescription = "A building for human habitation with rooms and a garden.", exampleSentence = "They live in a beautiful house near the park."),
        VisualIllustration("ill_tshirt", VisualVocabCategory.CLOTHES, "T-shirt", "Playera / Camiseta", "/ˈtiː.ʃɜːt/", "👕", accentColorHex = 0xFF00ACC1, visualDescription = "A short-sleeved casual cotton top.", exampleSentence = "He wears a blue cotton T-shirt on warm days."),
        VisualIllustration("ill_shoes", VisualVocabCategory.CLOTHES, "Shoes", "Zapatos", "/ʃuːz/", "👟", accentColorHex = 0xFF546E7A, visualDescription = "Footwear with soles for walking and running.", exampleSentence = "Put on your sports shoes before the race."),

        // School & Nature
        VisualIllustration("ill_book", VisualVocabCategory.SCHOOL_STUDY, "Book", "Libro", "/bʊk/", "📖", accentColorHex = 0xFF5E35B1, visualDescription = "Printed or written pages bound together with a cover.", exampleSentence = "She reads an exciting adventure book every night."),
        VisualIllustration("ill_tree", VisualVocabCategory.NATURE_WEATHER, "Tree", "Árbol", "/triː/", "🌳", accentColorHex = 0xFF2E7D32, visualDescription = "A tall woody perennial plant with branches and green leaves.", exampleSentence = "Birds sing high up in the oak tree."),
        VisualIllustration("ill_sun", VisualVocabCategory.NATURE_WEATHER, "Sun", "Sol", "/sʌn/", "☀️", accentColorHex = 0xFFFBC02D, visualDescription = "The star at the centre of the solar system providing light and warmth.", exampleSentence = "The bright sun warms the beach in the morning.")
    )

    /**
     * Rich Bilingual Modular Exercise Catalog
     */
    val localExerciseCatalog: List<ModularExerciseQuestion> = listOf(
        // =====================================================================
        // PRE A1 STARTERS (CAMBRIDGE YLE)
        // =====================================================================
        ModularExerciseQuestion(
            id = "yle_starters_mc_01",
            type = ModularExerciseType.MULTIPLE_CHOICE,
            level = "Starters",
            category = "Cambridge Starters: Animals",
            title = "Animals Identification",
            prompt = "Look at the picture and choose the correct English word for this animal:",
            promptSpanish = "Mira la imagen y elige la palabra correcta en inglés para este animal:",
            contextText = "This large grey animal has big ears and a long trunk.",
            contextTextSpanish = "Este animal gris grande tiene orejas grandes y una trompa larga.",
            baseEnglishSentence = "An elephant has a long nose called a trunk.",
            spanishSentence = "Un elefante tiene una nariz larga llamada trompa.",
            options = listOf("Elephant", "Giraffe", "Crocodile", "Monkey"),
            optionsSpanish = listOf("Elefante", "Jirafa", "Cocodrilo", "Mono"),
            correctAnswer = "Elephant",
            hintSpanish = "Es el mamífero terrestre más grande y tiene colmillos de marfil.",
            explanation = "La palabra correcta es 'Elephant' (elefante). Las otras opciones son 'Giraffe' (jirafa), 'Crocodile' (cocodrilo) y 'Monkey' (mono).",
            audioText = "An elephant has a long nose called a trunk."
        ),
        ModularExerciseQuestion(
            id = "yle_starters_img_01",
            type = ModularExerciseType.IMAGE_VOCAB_MATCHING,
            level = "Starters",
            category = "Cambridge Starters: Food & Drink",
            title = "Fruit Recognition",
            prompt = "What is the English name for this sweet red fruit?",
            promptSpanish = "¿Cuál es el nombre en inglés de esta fruta roja y dulce?",
            contextText = "She eats a fresh red _____ for breakfast.",
            contextTextSpanish = "Ella come una _____ roja fresca para el desayuno.",
            baseEnglishSentence = "An apple a day keeps the doctor away.",
            spanishSentence = "Una manzana al día mantiene lejos al médico.",
            options = listOf("Apple", "Banana", "Tomato", "Carrot"),
            optionsSpanish = listOf("Manzana", "Plátano", "Tomate", "Zanahoria"),
            correctAnswer = "Apple",
            visualIllustration = visualIllustrationsCatalog.firstOrNull { it.id == "ill_apple" },
            hintSpanish = "Empieza con la letra 'A' y es de color rojo.",
            explanation = "'Apple' significa manzana en inglés. 'Banana' es plátano y 'Carrot' es zanahoria.",
            audioText = "Apple. An apple a day keeps the doctor away."
        ),
        ModularExerciseQuestion(
            id = "yle_starters_fill_01",
            type = ModularExerciseType.FILL_IN_THE_BLANK,
            level = "Starters",
            category = "Cambridge Starters: Colours & Clothes",
            title = "Complete the Sentence",
            prompt = "Write the missing colour word in English:",
            promptSpanish = "Escribe la palabra de color faltante en inglés:",
            contextText = "The sky is _____ on a sunny day.",
            contextTextSpanish = "El cielo es _____ en un día soleado.",
            baseEnglishSentence = "The sky is blue on a sunny day.",
            spanishSentence = "El cielo es azul en un día soleado.",
            correctAnswer = "blue",
            acceptedAlternatives = listOf("blue", "Blue", "BLUE"),
            hintSpanish = "El color del cielo o del mar (en español: azul).",
            explanation = "La respuesta correcta es 'blue' (azul).",
            audioText = "The sky is blue on a sunny day."
        ),

        // =====================================================================
        // A1 MOVERS (CAMBRIDGE YLE)
        // =====================================================================
        ModularExerciseQuestion(
            id = "yle_movers_mc_01",
            type = ModularExerciseType.MULTIPLE_CHOICE,
            level = "Movers",
            category = "Cambridge Movers: Past Simple",
            title = "Past Tense Selection",
            prompt = "Choose the correct past simple form to complete the sentence:",
            promptSpanish = "Elige la forma correcta en pasado simple para completar la oración:",
            contextText = "Yesterday, we _____ a funny movie at the cinema.",
            contextTextSpanish = "Ayer, nosotros _____ una película divertida en el cine.",
            baseEnglishSentence = "Yesterday, we watched a funny movie at the cinema.",
            spanishSentence = "Ayer vimos una película divertida en el cine.",
            options = listOf("watched", "watch", "watching", "watches"),
            optionsSpanish = listOf("vimos (pasado)", "ver (presente)", "viendo (gerundio)", "ve (3ra persona)"),
            correctAnswer = "watched",
            hintSpanish = "La palabra 'Yesterday' indica una acción terminada en el pasado (+ ed para verbos regulares).",
            explanation = "En pasado simple con el verbo regular 'watch', agregamos '-ed': 'watched'.",
            audioText = "Yesterday, we watched a funny movie at the cinema."
        ),
        ModularExerciseQuestion(
            id = "yle_movers_img_01",
            type = ModularExerciseType.IMAGE_VOCAB_MATCHING,
            level = "Movers",
            category = "Cambridge Movers: Marine Animals",
            title = "Ocean Creatures",
            prompt = "Which English word matches this friendly sea mammal?",
            promptSpanish = "¿Qué palabra en inglés corresponde a este amigable mamífero marino?",
            contextText = "The _____ jumped high out of the water.",
            contextTextSpanish = "El _____ saltó alto fuera del agua.",
            baseEnglishSentence = "Dolphins are very clever sea animals.",
            spanishSentence = "Los delfines son animales marinos muy inteligentes.",
            options = listOf("Dolphin", "Shark", "Whale", "Penguin"),
            optionsSpanish = listOf("Delfín", "Tiburón", "Ballena", "Pingüino"),
            correctAnswer = "Dolphin",
            visualIllustration = visualIllustrationsCatalog.firstOrNull { it.id == "ill_dolphin" },
            hintSpanish = "Animal acuático muy inteligente conocido por comunicarse con chasquidos y saltar en el agua.",
            explanation = "'Dolphin' es delfín. 'Shark' es tiburón y 'Whale' es ballena.",
            audioText = "Dolphin. Dolphins are very clever sea animals."
        ),
        ModularExerciseQuestion(
            id = "yle_movers_fill_01",
            type = ModularExerciseType.FILL_IN_THE_BLANK,
            level = "Movers",
            category = "Cambridge Movers: Daily Routine",
            title = "Fill in the Time Preposition",
            prompt = "Write the correct preposition of time (in / on / at):",
            promptSpanish = "Escribe la preposición correcta de tiempo (in / on / at):",
            contextText = "I always wake up _____ seven o'clock in the morning.",
            contextTextSpanish = "Siempre me despierto _____ las siete en punto de la mañana.",
            baseEnglishSentence = "I always wake up at seven o'clock in the morning.",
            spanishSentence = "Siempre me despierto a las siete en punto por la mañana.",
            correctAnswer = "at",
            acceptedAlternatives = listOf("at", "At", "AT"),
            hintSpanish = "Para las horas específicas del reloj siempre se usa la preposición 'at'.",
            explanation = "Se utiliza 'at' con horas exactas ('at 7 o'clock'). Se usa 'on' con días y 'in' con meses/años.",
            audioText = "I always wake up at seven o'clock in the morning."
        ),

        // =====================================================================
        // A2 FLYERS (CAMBRIDGE YLE)
        // =====================================================================
        ModularExerciseQuestion(
            id = "yle_flyers_mc_01",
            type = ModularExerciseType.MULTIPLE_CHOICE,
            level = "Flyers",
            category = "Cambridge Flyers: Comparatives",
            title = "Comparative Adjectives",
            prompt = "Choose the correct comparative structure for the sentence:",
            promptSpanish = "Elige la estructura comparativa correcta para la oración:",
            contextText = "A blue whale is much _____ than an elephant.",
            contextTextSpanish = "Una ballena azul es mucho más _____ que un elefante.",
            baseEnglishSentence = "A blue whale is much bigger than an elephant.",
            spanishSentence = "Una ballena azul es mucho más grande que un elefante.",
            options = listOf("bigger", "more big", "biggest", "bigness"),
            optionsSpanish = listOf("más grande (comparativo)", "incorrecto", "el más grande (superlativo)", "sustantivo"),
            correctAnswer = "bigger",
            hintSpanish = "Para adjetivos cortos de 1 sílaba terminados en consonante-vocal-consonante, duplicamos la consonante y añadimos '-er'.",
            explanation = "'Big' es un adjetivo corto. Su comparativo duplica la 'g' y añade 'er' -> 'bigger'.",
            audioText = "A blue whale is much bigger than an elephant."
        ),
        ModularExerciseQuestion(
            id = "yle_flyers_img_01",
            type = ModularExerciseType.IMAGE_VOCAB_MATCHING,
            level = "Flyers",
            category = "Cambridge Flyers: Transport & Space",
            title = "Space Vehicles",
            prompt = "Identify the spacecraft shown in the illustration:",
            promptSpanish = "Identifica el vehículo espacial mostrado en la ilustración:",
            contextText = "Astronauts travel to the space station inside a _____.",
            contextTextSpanish = "Los astronautas viajan a la estación espacial dentro de un _____.",
            baseEnglishSentence = "The rocket blasted off into space with great power.",
            spanishSentence = "El cohete despegó hacia el espacio con gran potencia.",
            options = listOf("Rocket", "Airplane", "Helicopter", "Submarine"),
            optionsSpanish = listOf("Cohete", "Avión", "Helicóptero", "Submarino"),
            correctAnswer = "Rocket",
            visualIllustration = visualIllustrationsCatalog.firstOrNull { it.id == "ill_rocket" },
            hintSpanish = "Vehículo propulsado por motores de reacción utilizado para viajar más allá de la atmósfera terrestre.",
            explanation = "'Rocket' significa cohete espacial en inglés.",
            audioText = "Rocket. The rocket blasted off into space with great power."
        ),
        ModularExerciseQuestion(
            id = "yle_flyers_trans_01",
            type = ModularExerciseType.KEYWORD_TRANSFORMATION,
            level = "Flyers",
            category = "Cambridge Flyers: Sentence Transformation",
            title = "Flyers Sentence Restructuring",
            prompt = "Complete the second sentence so that it means the same as the first, using the keyword:",
            promptSpanish = "Completa la segunda oración para que signifique lo mismo que la primera, usando la palabra clave:",
            contextText = "Sentence 1: Running fast is very easy for Jack.\nSentence 2: Jack can _____ very easily.",
            contextTextSpanish = "Oración 1: Correr rápido es muy fácil para Jack.\nOración 2: Jack puede _____ muy fácilmente.",
            baseEnglishSentence = "Jack can run fast very easily.",
            spanishSentence = "Jack puede correr rápido muy fácilmente.",
            keyWord = "RUN FAST",
            correctAnswer = "run fast",
            acceptedAlternatives = listOf("run fast", "run fast.", "RUN FAST"),
            hintSpanish = "Después del verbo modal 'can' utilizamos el infinitivo sin 'to'.",
            explanation = "Usamos la forma base del verbo después del modal 'can': 'can run fast'.",
            audioText = "Jack can run fast very easily."
        ),

        // =====================================================================
        // LEVEL B1 INTERMEDIATE (CAMBRIDGE PRELIMINARY)
        // =====================================================================
        ModularExerciseQuestion(
            id = "b1_mc_01",
            type = ModularExerciseType.MULTIPLE_CHOICE,
            level = "B1",
            category = "Present Perfect vs Past Simple",
            title = "Present Perfect Experience",
            prompt = "Select the correct option to complete the conversation:",
            promptSpanish = "Selecciona la opción correcta para completar la conversación:",
            contextText = "Have you _____ visited London before?",
            contextTextSpanish = "¿Has visitado alguna vez Londres antes?",
            baseEnglishSentence = "Have you ever visited London before?",
            spanishSentence = "¿Alguna vez has visitado Londres?",
            options = listOf("ever", "never", "yet", "already"),
            optionsSpanish = listOf("alguna vez (en preguntas)", "nunca", "aún / ya", "ya (en afirmativas)"),
            correctAnswer = "ever",
            hintSpanish = "En preguntas sobre experiencias de vida en Present Perfect se usa 'ever'.",
            explanation = "Usamos 'ever' en oraciones interrogativas en Present Perfect para preguntar por experiencias vitales ('alguna vez').",
            audioText = "Have you ever visited London before?"
        ),
        ModularExerciseQuestion(
            id = "b1_trans_01",
            type = ModularExerciseType.KEYWORD_TRANSFORMATION,
            level = "B1",
            category = "Modal Verbs: Obligation",
            title = "B1 Keyword Transformation",
            prompt = "Complete the sentence using between 2 and 4 words, including the word given:",
            promptSpanish = "Completa la oración usando entre 2 y 4 palabras, incluyendo la palabra dada:",
            contextText = "It is not necessary for you to bring your laptop.\nYOU -> You _____ your laptop.",
            contextTextSpanish = "No es necesario que traigas tu computadora portátil.\nYOU -> No _____ tu laptop.",
            baseEnglishSentence = "You do not need to bring your laptop.",
            spanishSentence = "No necesitas traer tu laptop.",
            keyWord = "NEED",
            correctAnswer = "do not need to bring",
            acceptedAlternatives = listOf("do not need to bring", "don't need to bring", "dont need to bring"),
            hintSpanish = "'It is not necessary to do something' equivale a 'don't need to do something'.",
            explanation = "'Not necessary' se transforma en 'don't need to bring' o 'do not need to bring'.",
            audioText = "You do not need to bring your laptop."
        ),

        // =====================================================================
        // LEVEL B2 FIRST (CAMBRIDGE B2)
        // =====================================================================
        ModularExerciseQuestion(
            id = "b2_trans_01",
            type = ModularExerciseType.KEYWORD_TRANSFORMATION,
            level = "B2",
            category = "Cambridge B2 Use of English",
            title = "Part 4: Sentence Transformation",
            prompt = "Complete the second sentence so that it has a similar meaning to the first sentence. Do not change the word given.",
            promptSpanish = "Completa la segunda oración para que tenga un significado similar a la primera. No cambies la palabra dada.",
            contextText = "I regret not studying harder for the final examination.\nWISH -> I _____ harder for the final examination.",
            contextTextSpanish = "Me arrepiento de no haber estudiado más para el examen final.\nWISH -> Desearía _____ más para el examen final.",
            baseEnglishSentence = "I wish I had studied harder for the final examination.",
            spanishSentence = "Desearía haber estudiado más para el examen final.",
            keyWord = "WISH",
            correctAnswer = "wish I had studied",
            acceptedAlternatives = listOf("wish I had studied", "wish i had studied", "WISH I HAD STUDIED"),
            hintSpanish = "Para expresar arrepentimiento sobre una acción en el pasado, usamos 'wish + Past Perfect'.",
            explanation = "Estructura B2 de arrepentimiento pasado: 'wish + Past Perfect' -> 'wish I had studied'.",
            audioText = "I wish I had studied harder for the final examination."
        ),
        ModularExerciseQuestion(
            id = "b2_mc_01",
            type = ModularExerciseType.MULTIPLE_CHOICE,
            level = "B2",
            category = "Connectors & Inversion",
            title = "Concessive Connectors",
            prompt = "Choose the connector that grammatically completes the sentence:",
            promptSpanish = "Elige el conector que completa gramaticalmente la oración:",
            contextText = "_____ the severe storm warning, all flights departed on schedule.",
            contextTextSpanish = "_____ la advertencia de tormenta severa, todos los vuelos salieron según lo programado.",
            baseEnglishSentence = "Despite the severe storm warning, all flights departed on schedule.",
            spanishSentence = "A pesar de la advertencia de tormenta severa, todos los vuelos salieron a tiempo.",
            options = listOf("Despite", "Although", "Even though", "However"),
            optionsSpanish = listOf("A pesar de (+ sustantivo)", "Aunque (+ oración con verbo)", "A pesar de que (+ oración con verbo)", "Sin embargo (adverbio)"),
            correctAnswer = "Despite",
            hintSpanish = "'The severe storm warning' es un sintagma nominal (sustantivo), no una oración con verbo conjugado.",
            explanation = "'Despite' e 'In spite of' van seguidos de un sustantivo o gerundio (-ing). 'Although' requiere una cláusula con sujeto y verbo.",
            audioText = "Despite the severe storm warning, all flights departed on schedule."
        )
    )

    /**
     * Dynamically generates rich bilingual exercise questions from Cambridge Wordlists
     */
    fun generateQuestionsFromCambridgeWordlist(): List<ModularExerciseQuestion> {
        val wordlist = CambridgeWordlistsData.fullWordlist
        val generated = mutableListOf<ModularExerciseQuestion>()

        wordlist.forEachIndexed { index, item ->
            val cleanWord = item.english.trim()
            val blankedExample = item.exampleEnglish.replace(Regex("(?i)\\b${Regex.escape(cleanWord)}\\b"), "_____")

            // Multiple Choice Question
            val otherWords = wordlist.filter { it.theme == item.theme && it.english != item.english }
                .shuffled().take(3).map { it.english }
            val options = (otherWords + cleanWord).shuffled()

            generated.add(
                ModularExerciseQuestion(
                    id = "cambridge_gen_mc_$index",
                    type = ModularExerciseType.MULTIPLE_CHOICE,
                    level = when (item.level) {
                        com.example.data.remote.CambridgeYleLevel.STARTERS -> "Starters"
                        com.example.data.remote.CambridgeYleLevel.MOVERS -> "Movers"
                        com.example.data.remote.CambridgeYleLevel.FLYERS -> "Flyers"
                    },
                    category = item.theme,
                    title = "Vocabulario Cambridge: ${item.english.capitalize()}",
                    prompt = "Choose the English word that completes the sentence correctly:",
                    promptSpanish = "Elige la palabra en inglés que completa correctamente la oración:",
                    contextText = blankedExample,
                    contextTextSpanish = item.exampleSpanish,
                    baseEnglishSentence = item.exampleEnglish,
                    spanishSentence = item.exampleSpanish,
                    options = options,
                    optionsSpanish = options.map { opt ->
                        wordlist.firstOrNull { it.english.equals(opt, ignoreCase = true) }?.spanish ?: opt
                    },
                    correctAnswer = cleanWord,
                    hintSpanish = "Traducción al español del término: '${item.spanish}'. Pronunciación: ${item.phonetic}.",
                    explanation = "La palabra correcta es '${item.english}' (${item.spanish}). Ejemplo: '${item.exampleEnglish}'.",
                    audioText = item.exampleEnglish,
                    vocabTerm = item.english
                )
            )
        }

        return generated
    }

    /**
     * Generate dynamic questions from user's saved SRS vocabulary
     */
    fun generateQuestionsFromSavedVocab(savedItems: List<SavedVocabItemEntity>): List<ModularExerciseQuestion> {
        return savedItems.mapIndexed { idx, entity ->
            val now = System.currentTimeMillis()
            val isDue = entity.nextReviewTimestamp <= now
            val inferredLevel = when {
                entity.sourceModule.contains("b2", ignoreCase = true) -> "B2"
                entity.sourceModule.contains("b1", ignoreCase = true) -> "B1"
                entity.sourceModule.contains("a2", ignoreCase = true) -> "A2"
                entity.sourceModule.contains("flyers", ignoreCase = true) -> "Flyers"
                entity.sourceModule.contains("movers", ignoreCase = true) -> "Movers"
                entity.sourceModule.contains("starters", ignoreCase = true) -> "Starters"
                else -> "A1"
            }

            ModularExerciseQuestion(
                id = "srs_auto_gen_${entity.id}_$idx",
                type = ModularExerciseType.FILL_IN_THE_BLANK,
                level = inferredLevel,
                category = "⭐ Mi Vocabulario SRS",
                title = "Repaso Espaciado: ${entity.sourceText}",
                prompt = "Write the English word for this definition/translation:",
                promptSpanish = "Escribe la palabra en inglés para esta traducción:",
                contextText = "Español: '${entity.translation}'",
                contextTextSpanish = "Definición: ${entity.definition}",
                baseEnglishSentence = entity.sourceText,
                spanishSentence = entity.translation,
                correctAnswer = entity.sourceText.trim(),
                acceptedAlternatives = listOf(entity.sourceText.trim(), entity.sourceText.trim().lowercase()),
                hintSpanish = "Inicia con '${entity.sourceText.take(2)}...' | Fonética: ${entity.phonetic}",
                explanation = "El término en inglés es '${entity.sourceText}' (${entity.translation}).",
                audioText = entity.sourceText,
                vocabTerm = entity.sourceText,
                srsItemId = entity.id,
                srsMasteryLevel = entity.masteryLevel,
                srsIntervalDays = entity.intervalDays,
                srsEaseFactor = entity.easeFactor,
                isSrsDue = isDue
            )
        }
    }

    /**
     * Get filtered questions with bilingual support and SRS prioritization
     */
    fun getFilteredExercises(
        typeFilter: ModularExerciseType?,
        levelFilter: String,
        savedVocabItems: List<SavedVocabItemEntity> = emptyList(),
        currentTopic: TopicEntity? = null,
        onlyDueSrs: Boolean = false,
        sortBySrsPriority: Boolean = true
    ): List<ModularExerciseQuestion> {
        val baseList = mutableListOf<ModularExerciseQuestion>()

        // 1. Add base curated questions
        baseList.addAll(localExerciseCatalog)

        // 2. Add dynamically generated questions from official Cambridge Wordlists
        baseList.addAll(generateQuestionsFromCambridgeWordlist())

        // 3. Add saved SRS user items
        if (savedVocabItems.isNotEmpty()) {
            baseList.addAll(generateQuestionsFromSavedVocab(savedVocabItems))
        }

        val srsMap = savedVocabItems.associateBy { it.sourceText.lowercase().trim() }

        val filtered = baseList.filter { q ->
            val matchType = typeFilter == null || q.type == typeFilter
            val matchLevel = when (levelFilter) {
                "TODOS" -> true
                "Starters" -> q.level.equals("Starters", ignoreCase = true) || q.level.contains("Pre A1", ignoreCase = true)
                "Movers" -> q.level.equals("Movers", ignoreCase = true)
                "Flyers" -> q.level.equals("Flyers", ignoreCase = true)
                "A1" -> q.level.equals("A1", ignoreCase = true) || q.level.equals("Movers", ignoreCase = true)
                "A2" -> q.level.equals("A2", ignoreCase = true) || q.level.equals("Flyers", ignoreCase = true)
                "B1" -> q.level.equals("B1", ignoreCase = true)
                "B2" -> q.level.equals("B2", ignoreCase = true)
                "SAVED" -> q.category.contains("SRS") || q.isSrsDue
                else -> q.level.equals(levelFilter, ignoreCase = true)
            }
            val matchDue = if (onlyDueSrs) q.isSrsDue else true
            matchType && matchLevel && matchDue
        }.distinctBy { it.id }

        return if (sortBySrsPriority) {
            SrsAlgorithm.prioritizeQuestionsBySrs(filtered, srsMap)
        } else {
            filtered
        }
    }
}
